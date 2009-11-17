/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.menu;

import java.text.MessageFormat;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.vpe.VpeDebug;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.menu.action.EditAttributesAction;
import org.jboss.tools.vpe.editor.menu.action.SelectThisTagAction;
import org.jboss.tools.vpe.editor.menu.action.StripTagAction;
import org.jboss.tools.vpe.editor.menu.action.ComplexAction;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.template.IZoomEventManager;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.xulrunner.browser.util.DOMTreeDumper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class is used to create context menu for VPE.
 * 
 * @author yradtsevich (based on the implementation of MenuCreationHelper)
 */
public class VpeMenuCreator {
	private final MenuManager menuManager;
	private final VpeMenuUtil vpeMenuUtil;
	private final Node node;

	public VpeMenuCreator(final MenuManager menuManager, final Node node) {
		this.node = node;
		this.menuManager = menuManager;
		this.vpeMenuUtil = new VpeMenuUtil();
	}

	/**
	 * Inserts new menu items into {@link #menuManager}.
	 */
	public void createMenu() {
		createMenu(true);
	}

	/**
	 * Inserts new menu items into {@link #menuManager}.
	 * 
	 * @param topLevelMenu
	 *            if it is {@code true} then the menu will contain elements for
	 *            the top level variant of the VPE menu, otherwise - elements
	 *            for the sub menu variant.
	 * 
	 * @see #addParentTagMenuItem(Element)
	 */
	private void createMenu(boolean topLevelMenu) {
		addIfEnabled(new EditAttributesAction(node));
		menuManager.add(new SetupTemplateContributionItem());

		if (!topLevelMenu) {
			menuManager.add(new SelectThisTagAction(node));
		}

		final Node parent = node == null ? null : node.getParentNode();
		if (parent != null && parent.getNodeType() == Node.ELEMENT_NODE) {
			addParentTagMenuItem((Element) parent);
		}
		addSeparator();

		menuManager.add(new InsertContributionItem(node));
		addIfEnabled(new StripTagAction(node));
		addSeparator();
		if (topLevelMenu) {
			addZoomActions();
		}
		addSeparator();
		if (topLevelMenu) {
			addIfEnabled(new DumpSourceAction());
			addIfEnabled(new DumpSelectedElementAction());
			addIfEnabled(new DumpMappingAction());
			addIfEnabled(new TestAction());
		}
		addSeparator();
		addCutCopyPasteActions(topLevelMenu);
	}

	/**
	 * Creates the Cut, Copy and Paste actions.
	 */
	private void addCutCopyPasteActions(boolean topLevelMenu) {
		final IAction cutAction = getSourceEditorAction(ActionFactory.CUT);
		final IAction copyAction = getSourceEditorAction(ActionFactory.COPY);
		final IAction pasteAction = getSourceEditorAction(ActionFactory.PASTE);
		if (topLevelMenu) {
			if (node != null) {
				menuManager.add(cutAction);
				menuManager.add(copyAction);
			}
			menuManager.add(pasteAction);
		} else {
			final IAction selectAction = new SelectThisTagAction(node);
			if (selectAction.isEnabled()) {
				menuManager.add(new ComplexAction(cutAction.getText(),
						selectAction, cutAction));
				menuManager.add(new ComplexAction(copyAction.getText(),
						selectAction, copyAction));
				menuManager.add(new ComplexAction(pasteAction.getText(),
						selectAction, pasteAction));
			}
		}
	}

	/**
	 * If the {@code action} is enabled, adds it to the {@link #menuManager}.
	 */
	private void addIfEnabled(IAction action) {
		if (action.isEnabled()) {
			menuManager.add(action);
		}
	}

	/**
	 * Adds a menu item for operations on {@code parent} element.
	 * 
	 * @param parent
	 *            the parent element
	 */
	private void addParentTagMenuItem(final Element parent) {
		final String itemName = MessageFormat.format(
				VpeUIMessages.PARENT_TAG_MENU_ITEM, parent.getNodeName());

		final MenuManager parentMenuManager = new MenuManager(itemName);
		parentMenuManager.setParent(menuManager);
		parentMenuManager.setRemoveAllWhenShown(true);
		parentMenuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(final IMenuManager manager) {
				new VpeMenuCreator(parentMenuManager, parent).createMenu(false);
			}
		});

		menuManager.add(parentMenuManager);
	}

	/**
	 * Adds a separator. If {@link #menuManager} is empty or the last item
	 * already is a separator, does nothing.
	 */
	private void addSeparator() {
		final int size = menuManager.getSize();
		if (size > 0) {
			final IContributionItem lastItem = menuManager.getItems()[size - 1];
			if (!lastItem.isSeparator()) {
				menuManager.add(new Separator());
			}
		}
	}

	/**
	 * Returns an action of the source editor.
	 * 
	 * @param actionFactory
	 *            instance of {@link ActionFactory} which identifies the action
	 *            of the item.
	 */
	private IAction getSourceEditorAction(final ActionFactory actionFactory) {
		final AbstractTextEditor sourceEditor = vpeMenuUtil.getSourceEditor();
		final IAction action = sourceEditor.getAction(actionFactory.getId());
		return action;
	}

	/**
	 * Test action. For the debugging purposes only.
	 */
	public static class TestAction extends Action {
		public TestAction() {
			setText("Test Action"); //$NON-NLS-1$
		}

		public void run() {
			// test code
		}

		@Override
		public boolean isEnabled() {
			return VpeDebug.VISUAL_CONTEXTMENU_TEST;
		}
	}

	/**
	 * Action to dump source of VPE. For debugging purposes only.
	 */
	public class DumpSourceAction extends Action {
		public DumpSourceAction() {
			setText("Dump Source"); //$NON-NLS-1$
		}

		@Override
		public void run() {
			final MozillaEditor visualEditor = vpeMenuUtil.getMozillaEditor();
			DOMTreeDumper dumper = new DOMTreeDumper(
					VpeDebug.VISUAL_DUMP_PRINT_HASH);
			dumper
					.setIgnoredAttributes(VpeDebug.VISUAL_DUMP_IGNORED_ATTRIBUTES);
			dumper.dumpToStream(System.out, visualEditor.getDomDocument());
		}

		@Override
		public boolean isEnabled() {
			return VpeDebug.VISUAL_CONTEXTMENU_DUMP_SOURCE;
		}
	}

	/**
	 * Action to dump source of the selected VPE element. For debugging purposes
	 * only.
	 */
	public class DumpSelectedElementAction extends Action {
		public DumpSelectedElementAction() {
			setText("Dump Selected Element"); //$NON-NLS-1$
		}

		@Override
		public void run() {
			final StructuredTextEditor sourceEditor = vpeMenuUtil
					.getSourceEditor();
			final VpeDomMapping domMapping = vpeMenuUtil.getDomMapping();
			final VpeNodeMapping nodeMapping = SelectionUtil
					.getNodeMappingBySourceSelection(sourceEditor, domMapping);
			if (nodeMapping != null) {
				DOMTreeDumper dumper = new DOMTreeDumper(
						VpeDebug.VISUAL_DUMP_PRINT_HASH);
				dumper
						.setIgnoredAttributes(VpeDebug.VISUAL_DUMP_IGNORED_ATTRIBUTES);
				dumper.dumpNode(nodeMapping.getVisualNode());
			}
		}

		@Override
		public boolean isEnabled() {
			return VpeDebug.VISUAL_CONTEXTMENU_DUMP_SELECTED_ELEMENT;
		}
	}

	/**
	 * Action to print the {@link #domMapping}. For debugging purposes only.
	 */
	public class DumpMappingAction extends Action {
		public DumpMappingAction() {
			setText("Dump Mapping"); //$NON-NLS-1$
		}

		@Override
		public void run() {
			final VpeDomMapping domMapping = vpeMenuUtil.getDomMapping();
			domMapping.printMapping();
		}

		@Override
		public boolean isEnabled() {
			return VpeDebug.VISUAL_CONTEXTMENU_DUMP_MAPPING;
		}
	}

	private void addZoomActions() {
		IZoomEventManager zoomEventManager = ((VpeEditorPart) vpeMenuUtil
				.getEditor().getVisualEditor()).getController()
				.getZoomEventManager();
		ZoomActionMenuManager manager = new ZoomActionMenuManager(zoomEventManager);
		menuManager.add(manager);
	}
}
