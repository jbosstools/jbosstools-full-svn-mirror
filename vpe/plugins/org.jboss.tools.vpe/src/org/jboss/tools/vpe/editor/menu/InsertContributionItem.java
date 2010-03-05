/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc. Distributed under license by
 * Red Hat, Inc. All rights reserved. This program is made available
 * under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributor: Red Hat,
 * Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.menu;

import java.util.List;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.util.ModelUtilities;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.jst.web.tld.URIConstants;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.menu.action.ComplexAction;
import org.jboss.tools.vpe.editor.menu.action.InsertAction2;
import org.jboss.tools.vpe.editor.menu.action.SelectThisTagAction;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.editor.util.XmlUtil;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.w3c.dom.Node;

/**
 * @author Sergey Dzmitrovich
 * @author yradtsevich
 */
public class InsertContributionItem extends ContributionItem {

	private final static String NAME_PROPERTY = "name";			//$NON-NLS-1$
	private final static String HIDDEN_PROPERTY = "hidden"; 	//$NON-NLS-1$
	private final static String ELEMENT_TYPE_PROPERTY 
	= "element type"; //$NON-NLS-1$
	private final static String END_TEXT_PROPERTY = "end text";	//$NON-NLS-1$
	private final static String TAG_ELEMENT_TYPE = "macro";		//$NON-NLS-1$
	private final static String TAGLIB_ELEMENT_TYPE = "sub-group"; //$NON-NLS-1$
	private final static String LEFT_ANGLE_BRACKET = "<";		//$NON-NLS-1$
	private final static String RIGHT_ANGLE_BRACKET = ">";		//$NON-NLS-1$
	
	private final Node node;
	private final StructuredTextEditor sourceEditor;
	private final VpePageContext pageContext;
	private final JSPMultiPageEditor editor;
	private final VpeController vpeController;
	
	/**
	 * Creates an {@code InsertContributionItem}
	 * to make insert actions on the currently selected node.
	 */
	public InsertContributionItem() {
		this(null);
	}

	/**
	 * Creates an {@code InsertContributionItem}
	 * to make insert actions on the given {@code node}.
	 */
	public InsertContributionItem(final Node node) {
		this.node = node;

		editor = (JSPMultiPageEditor)
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActiveEditor();
		this.sourceEditor = editor.getSourceEditor();
		/*
		 * Fixes https://jira.jboss.org/jira/browse/JBIDE-5996
		 * When VisualEditor is not initialized it is impossible
		 * to fill this menu item.
		 */
		this.vpeController = ((VpeEditorPart) editor.getVisualEditor()).getController();
		if (null != vpeController) {
			this.pageContext = vpeController.getPageContext();
		} else {
			this.pageContext = null;
		}
	}

	@Override
	public void fill(Menu menu, int index) {
		if (null != vpeController) {
			/*
			 * Setting each InsertType to correct position in the menu
			 */
			for (final InsertType insertItem : InsertType.values()) {
				/*
				 * Use MenuManager to create submenu.
				 */
				final MenuManager paletteManuManager = new MenuManager(
						insertItem.getMessage());
				final XModelObject model = ModelUtilities.getPreferenceModel()
				.getByPath("%Palette%"); //$NON-NLS-1$
				paletteManuManager.addMenuListener(new InsertMenuListener(
						model, insertItem));
				paletteManuManager.setRemoveAllWhenShown(true);
				paletteManuManager.fill(menu, index);
				index++;
			}
		}
	}

	/**
	 * Fills contextMenu at run-time.
	 *
	 * @author Sergey Dzmitrovich
	 */
	public class InsertMenuListener implements IMenuListener {

		private final XModelObject modelObject;
		private final InsertType insertionType;

		public InsertMenuListener(XModelObject modelObject,
				InsertType insertionType) {
			this.modelObject = modelObject;
			this.insertionType = insertionType;
		}

		public void menuAboutToShow(IMenuManager manager) {
			final XModelObject[] modelObjectChildren
					= modelObject.getChildren();

			String prefix = null;
			if (TAGLIB_ELEMENT_TYPE.equals(
					modelObject.getAttributeValue(ELEMENT_TYPE_PROPERTY))) {
				prefix = getPrefix(modelObject);
			}

			final IAction selectNodeAction =
					node == null ? null : new SelectThisTagAction(node);					

			for (final XModelObject modelObjectChild : modelObjectChildren) {
				if (Constants.YES_STRING.equals(
						modelObjectChild.getAttributeValue(HIDDEN_PROPERTY))) {
					continue;
				}

				if (TAG_ELEMENT_TYPE.equals(modelObjectChild
						.getAttributeValue(ELEMENT_TYPE_PROPERTY))) {

					final String endText = modelObjectChild
							.getAttributeValue(END_TEXT_PROPERTY);

					if (insertionType != InsertType.INSERT_AROUND
							|| (endText != null && endText.length() > 0)) {

						final String name = LEFT_ANGLE_BRACKET
								+ (prefix == null || prefix.length() == 0 
										? Constants.EMPTY
										: prefix + Constants.COLON)
								+ modelObjectChild
										.getAttributeValue(NAME_PROPERTY)
								+ RIGHT_ANGLE_BRACKET;

						final InsertAction2 insertAction = new InsertAction2(
								name, modelObjectChild,
								sourceEditor, insertionType);

						final IAction action;
						if (selectNodeAction == null) {
							action = insertAction;
						} else {
							action = new ComplexAction(insertAction.getText(),
									selectNodeAction, insertAction);
						}
						manager.add(action);
					}
				} else {
					final MenuManager subMenu = new InsertSubMenuManager(
							modelObjectChild.getAttributeValue(NAME_PROPERTY));
					subMenu.setRemoveAllWhenShown(true);

					subMenu.addMenuListener(new InsertMenuListener(
							modelObjectChild, insertionType));

					manager.add(subMenu);

					subMenu.fill(( (MenuManager) manager).getMenu(), -1);
				}
			}
		}

		private String getPrefix(XModelObject modelObject) {
			final List<TaglibData> taglibs = XmlUtil.getTaglibsForNode(
					(Node) ((IStructuredSelection) sourceEditor
							.getSelectionProvider().getSelection())
							.getFirstElement(), pageContext);

			final String uri = modelObject
					.getAttributeValue(URIConstants.LIBRARY_URI);
			String prefix = null;
			final TaglibData sourceNodeTaglib = XmlUtil
					.getTaglibForURI(uri, taglibs);

			if (sourceNodeTaglib == null) {
				prefix = modelObject
						.getAttributeValue(URIConstants.DEFAULT_PREFIX);
			} else {
				prefix = sourceNodeTaglib.getPrefix();
			}

			return prefix;
		}
	}

	/**
	 * This class was created to override method isVisible. 
	 * Because of it there is a possibility to fill context
	 * menu at run-time).
	 * 
	 * @author Sergey Dzmitrovich
	 */
	public static class InsertSubMenuManager extends MenuManager {

		public InsertSubMenuManager(String text) {
			super(text);
		}

		@Override
		public boolean isVisible() {
			return true;
		}
	}
}
