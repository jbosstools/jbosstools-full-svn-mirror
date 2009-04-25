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
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.util.ModelUtilities;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.jst.web.tld.URIConstants;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.menu.action.InsertAction2;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.editor.util.NodesManagingUtil;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.jboss.tools.vpe.editor.util.XmlUtil;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.w3c.dom.Node;

/**
 * @author Sergey Dzmitrovich
 *
 */
public class InsertContributionItem extends ContributionItem {

	private final StructuredTextEditor sourceEditor;
	private final VpePageContext pageContext;
	private final static String NAME_PROPERTY = "name";			//$NON-NLS-1$
	private final static String HIDDEN_PROPERTY = "hidden"; 	//$NON-NLS-1$
	private final static String ELEMENT_TYPE_PROPERTY 
			= "element type"; //$NON-NLS-1$
	private final static String END_TEXT_PROPERTY = "end text";	//$NON-NLS-1$
	private final static String TAG_ELEMENT_TYPE = "macro";		//$NON-NLS-1$
	private final static String TAGLIB_ELEMENT_TYPE = "sub-group"; //$NON-NLS-1$
	private final static String LEFT_ANGLE_BRACKET = "<";		//$NON-NLS-1$
	private final static String RIGHT_ANGLE_BRACKET = ">";		//$NON-NLS-1$

	public InsertContributionItem() {
		final JSPMultiPageEditor editor = (JSPMultiPageEditor) PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		this.sourceEditor = editor.getSourceEditor();
		this.pageContext = ((VpeEditorPart) editor.getVisualEditor())
				.getController().getPageContext();
	}

	public InsertContributionItem(StructuredTextEditor sourceEditor,
			VpePageContext pageContext) {
		this.sourceEditor = sourceEditor;
		this.pageContext = pageContext;
	}

	@Override
	public void fill(Menu menu, int index) {
		for (final InsertType insertItem : InsertType.values()) {
			final String itemName = insertItem.getMessage();
			final MenuItem item = new MenuItem(menu,
					SWT.CASCADE, index + insertItem.ordinal());
			item.setText(itemName);

			final Menu paletteManu = new Menu(menu);
			item.setMenu(paletteManu);

			final MenuManager paletteManuManager = new MenuManager(
					VpeUIMessages.FROM_PALETTE);
			final XModelObject model = ModelUtilities.getPreferenceModel()
					.getByPath("%Palette%"); //$NON-NLS-1$

			paletteManuManager.addMenuListener(
					new InsertMenuListener(model, insertItem));
			paletteManuManager.setRemoveAllWhenShown(true);
			paletteManuManager.fill(paletteManu, -1);
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

						manager.add(new InsertAction2(name, modelObjectChild,
								pageContext, sourceEditor, insertionType));
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
	public class InsertSubMenuManager extends MenuManager {

		public InsertSubMenuManager(String text) {
			super(text);
		}

		@Override
		public boolean isVisible() {
			return true;
		}
	}
}
