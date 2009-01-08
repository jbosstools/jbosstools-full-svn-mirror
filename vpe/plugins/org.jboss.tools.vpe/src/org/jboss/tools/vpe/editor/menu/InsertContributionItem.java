/*******************************************************************************
 * Copyright (c) 2007-2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.vpe.editor.menu;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.util.ModelUtilities;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.menu.action.InsertAction;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.editor.util.NodesManagingUtil;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.w3c.dom.Node;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class InsertContributionItem extends ContributionItem {

	private StructuredTextEditor sourceEditor;

	private VpePageContext pageContext;

	private final static String NAME_PROPERTY = "name"; //$NON-NLS-1$

	private final static String HIDDEN_PROPERTY = "hidden"; //$NON-NLS-1$

	private final static String ELEMENT_TYPE_PROPERTY = "element type"; //$NON-NLS-1$

	private final static String END_TEXT_PROPERTY = "end text";//$NON-NLS-1$

	private final static String TAG_ELEMENT_TYPE = "macro"; //$NON-NLS-1$

	private static List<String> INSERT_ITEMS;
	static {
		INSERT_ITEMS = new ArrayList<String>();
		INSERT_ITEMS.add(VpeUIMessages.INSERT_AROUND); // id = 0
		INSERT_ITEMS.add(VpeUIMessages.INSERT_BEFORE); // id = 1
		INSERT_ITEMS.add(VpeUIMessages.INSERT_AFTER); // id = 2
		INSERT_ITEMS.add(VpeUIMessages.REPLACE_WITH); // id = 3
	}

	// ids correspond to order of items in INSERT_ITEMS
	private int INSERT_AROUND = 0;
	private int INSERT_BEFORE = 1;
	private int INSERT_AFTER = 2;
	private int REPLACE_WITH = 3;

	public InsertContributionItem() {
		JSPMultiPageEditor editor = (JSPMultiPageEditor) PlatformUI
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

		for (int i = 0; i < INSERT_ITEMS.size(); i++) {

			String itemName = INSERT_ITEMS.get(i);

			MenuItem item = new MenuItem(menu, SWT.CASCADE, index + i);
			item.setText(itemName);

			Menu paletteManu = new Menu(menu);

			item.setMenu(paletteManu);

			MenuManager paletteManuManager = new MenuManager(
					VpeUIMessages.FROM_PALETTE);

			XModelObject model = ModelUtilities.getPreferenceModel().getByPath(
					"%Palette%"); //$NON-NLS-1$

			paletteManuManager
					.addMenuListener(new InsertMenuListener(model, i));

			paletteManuManager.fill(paletteManu, -1);

		}

	}

	/**
	 * 
	 * @param sourceEditor
	 * @return
	 */
	private Point getSelectionRange(StructuredTextEditor sourceEditor) {

		// IStructuredSelection selection = (IStructuredSelection) sourceEditor
		// .getSelectionProvider().getSelection();

		// Object[] selectedObjects = selection.toArray();

		// Node firstElement = (Node) selectedObjects[0];
		// Node endElement = (Node) selectedObjects[selectedObjects.length - 1];

		Point selectionRange = SelectionUtil
				.getSourceSelectionRange(sourceEditor);

		Node firstElement = SelectionUtil.getNodeBySourcePosition(sourceEditor,
				selectionRange.x);
		Node endElement = SelectionUtil.getNodeBySourcePosition(sourceEditor,
				selectionRange.x + selectionRange.y);

		int start;
		int length;

		if (firstElement.getNodeType() == Node.TEXT_NODE)
			start = selectionRange.x;
		else
			start = NodesManagingUtil.getStartOffsetNode(firstElement);

		if (endElement.getNodeType() == Node.TEXT_NODE)
			length = selectionRange.x + selectionRange.y - start;
		else
			length = NodesManagingUtil.getEndOffsetNode(endElement) - start;

		return new Point(start, length);

	}

	/**
	 * @author Sergey Dzmitrovich
	 * 
	 *         fill contextMenu run-time
	 * 
	 */
	public class InsertMenuListener implements IMenuListener {

		private XModelObject modelObject;
		private int typeAction;

		public InsertMenuListener(XModelObject modelObject, int typeAction) {
			this.modelObject = modelObject;
			this.typeAction = typeAction;
		}

		public void menuAboutToShow(IMenuManager manager) {

			XModelObject[] modelObjects = modelObject.getChildren();
			for (int i = 0; i < modelObjects.length; i++) {
				if (Constants.YES_STRING.equals(modelObjects[i]
						.getAttributeValue(HIDDEN_PROPERTY))) {
					continue;
				}

				if (TAG_ELEMENT_TYPE.equals(modelObjects[i]
						.getAttributeValue(ELEMENT_TYPE_PROPERTY))) {

					String endText = modelObjects[i]
							.getAttributeValue(END_TEXT_PROPERTY);

					if (!((typeAction == INSERT_AROUND) && ((endText == null) || (endText
							.length() == 0)))) {

						Point selectionRange = getSelectionRange(sourceEditor);

						if (typeAction == INSERT_BEFORE) {
							selectionRange.y = 0;
						} else if (typeAction == INSERT_AFTER) {
							selectionRange.x += selectionRange.y;
							selectionRange.y = 0;
						}

						manager.add(new InsertAction(modelObjects[i]
								.getAttributeValue(NAME_PROPERTY),
								selectionRange, modelObjects[i], pageContext,
								sourceEditor, REPLACE_WITH == this.typeAction));
					}
				}

				else {
					MenuManager subMenu = new InsertSubMenuManager(
							modelObjects[i].getAttributeValue(NAME_PROPERTY));

					subMenu.addMenuListener(new InsertMenuListener(
							modelObjects[i], typeAction));

					manager.add(subMenu);

					subMenu.fill(((MenuManager) manager).getMenu(), -1);

				}

			}

		}

	}

	/**
	 * @author Sergey Dzmitrovich
	 * 
	 *         class was created to override method isVisible ( because of it
	 *         there is possibility to fill context menu run-time )
	 */
	public class InsertSubMenuManager extends MenuManager {

		@Override
		public boolean isVisible() {
			return true;
		}

		public InsertSubMenuManager(String text) {
			super(text);
		}

	}

}
