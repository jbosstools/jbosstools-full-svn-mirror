/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.selbar;

import java.util.ArrayList;

import org.eclipse.compare.Splitter;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.common.meta.XAttribute;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.attribute.adapter.AdapterFactory;
import org.jboss.tools.common.model.ui.attribute.adapter.IModelPropertyEditorAdapter;
import org.jboss.tools.common.model.ui.util.ModelUtilities;
import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.selection.VpeSourceSelection;
import org.jboss.tools.vpe.editor.selection.VpeSourceSelectionBuilder;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.w3c.dom.Node;

/**
 * @author erick This class create and manage the Selection Bar under the VPE.
 *         Entry point from the class MozilaEditor This bar can be hiden and
 *         shown it uses splitter for this
 */

public class SelectionBar extends Layout implements SelectionListener {
    private Splitter splitter;

    private VpeController vpeController = null;

    private ToolBar selBar = null;
    private ToolBar closeSelectionBar = null;

    private int itemCount = 0;

    private Composite cmpToolBar = null;
    private Composite cmpTlEmpty = null;
    private Composite cmpBar = null;
    private Composite closeBar = null;

    //Listener selbarListener = null;

    final static String PREFERENCE_STATUS_BAR_ENABLE = "yes";
    final static String PREFERENCE_STATUS_BAR_DISABLE = "no";

	public Composite createToolBarComposite(Composite parent, boolean show) {
		splitter = new Splitter(parent, SWT.NONE);
		splitter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		/*
		 * The empty composite
		 */
		cmpTlEmpty = new Composite(splitter, SWT.NONE) {
			public Point computeSize(int wHint, int hHint, boolean changed) {
				Point point = super.computeSize(wHint, hHint, changed);
				point.y = 1;
				return point;
			}
		};

		cmpTlEmpty.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Main composite of the visible splitter
		cmpToolBar = new Composite(splitter, SWT.NONE);
		cmpToolBar.setLayout(this);

		GridLayout layoutTl = new GridLayout(1, false);
		layoutTl.marginBottom = 0;
		layoutTl.marginHeight = 0;
		layoutTl.marginWidth = 0;
		layoutTl.verticalSpacing = 0;
		layoutTl.horizontalSpacing = 0;

		// Middle composite, witch contain the selectbar
		cmpBar = new Composite(cmpToolBar, SWT.NONE);
		cmpBar.setLayout(layoutTl);
		cmpBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		closeBar = new Composite(cmpToolBar, SWT.NONE);
		closeBar.setLayout(layoutTl);
		closeBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		closeSelectionBar = new ToolBar(closeBar, SWT.HORIZONTAL | SWT.FLAT);
		ToolItem closeItem = new ToolItem(closeSelectionBar, SWT.FLAT);
		closeItem.setImage(PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_TOOL_DELETE));
		closeItem.setToolTipText(VpeUIMessages.HIDE_SELECTIONBAR);
		closeItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				boolean toggleState = VpePreference.ALWAYS_HIDE_SELECTION_BAR_WITHOUT_PROMT
						.getValue().equals(PREFERENCE_STATUS_BAR_ENABLE);
				XModelObject optionsObject = ModelUtilities
						.getPreferenceModel().getByPath(
								VpePreference.VPE_EDITOR_PATH);
				if (!toggleState) {
					MessageDialogWithToggle dialog = MessageDialogWithToggle
							.openOkCancelConfirm(
									PlatformUI.getWorkbench()
											.getActiveWorkbenchWindow()
											.getShell(),
									VpeUIMessages.CONFIRM_SELECTION_BAR_DIALOG_TITLE,
									VpeUIMessages.CONFIRM_SELECTION_BAR_DIALOG_MESSAGE,
									VpeUIMessages.CONFIRM_SELECTION_BAR_DIALOG_TOGGLE_MESSAGE,
									false, null, null);
					if (dialog.getReturnCode() != IDialogConstants.OK_ID) {
						return;
					}
					if (dialog.getToggleState()) {
						optionsObject
								.setAttributeValue(
										VpePreference.ATT_ALWAYS_HIDE_SELECTION_BAR_WITHOUT_PROMT,
										PREFERENCE_STATUS_BAR_ENABLE);
					}
				}
				optionsObject.setAttributeValue(
						VpePreference.ATT_SHOW_SELECTION_TAG_BAR,
						PREFERENCE_STATUS_BAR_DISABLE);
						
				/*
				 * Fixes http://jira.jboss.com/jira/browse/JBIDE-2298
				 * To get stored in xml XModelObject 
				 * should be marked as modified.
				 */
				optionsObject.setModified(true);
				performStore(optionsObject);
				
				showBar(PREFERENCE_STATUS_BAR_DISABLE);
			}
		});
		// Create selection bar
		selBar = new ToolBar(cmpBar, SWT.HORIZONTAL | SWT.FLAT);
		if (show == true) {
			splitter.setVisible(cmpTlEmpty, false);
			splitter.setVisible(cmpToolBar, true);
		} else {
			splitter.setVisible(cmpTlEmpty, true);
			splitter.setVisible(cmpToolBar, false);
		}

		return splitter;
	}

    public void showBar(String show) {
		if (PREFERENCE_STATUS_BAR_ENABLE.equals(show)) {
			splitter.setVisible(cmpToolBar, true);
			splitter.setVisible(cmpTlEmpty, false);
			splitter.getParent().layout(true, true);
		} else {
			splitter.setVisible(cmpToolBar, false);
			splitter.setVisible(cmpTlEmpty, true);
			splitter.getParent().layout(true, true);
		}
	}

    public void setVpeController(VpeController vpeController) {
		this.vpeController = vpeController;
	}

    public void selectionChanged() {
		VpeSourceSelectionBuilder sourceSelectionBuilder = new VpeSourceSelectionBuilder(
				vpeController.getSourceEditor());
		VpeSourceSelection selection = sourceSelectionBuilder.getSelection();
		if (selection == null) {
			return;
		}

		// Node node = selection.getFocusNode();
		Node node = selection.getStartNode();
		if (node != null && node.getNodeType() == Node.TEXT_NODE) {
			node = node.getParentNode();
		}

		int elementCounter = 0;
		while (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
			ToolItem item = null;
			if (selBar.getItemCount() > elementCounter) {
				item = selBar.getItem(selBar.getItemCount() - elementCounter
						- 1);
				item.setData(node);
			} else {
				item = new ToolItem(selBar, SWT.FLAT, 0);
				item.addSelectionListener(this);
				item.setData(node);
			}

			item.setText(node.getNodeName());
			elementCounter++;
			node = node.getParentNode();
		}

		itemCount = elementCounter;
		cmpToolBar.layout();
		// bug was fixed when toolbar are not shown for resizeble components
		cmpToolBar.layout();
		splitter.getParent().layout(true,true);
	}

    protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
		Point point = selBar.computeSize(SWT.DEFAULT, hHint);
		point.y = closeBar.getSize().y;
		return point;
	}

    protected void layout(Composite composite, boolean flushCache) {
		Rectangle rect = null;
		try {
			rect = composite.getBounds();
		} catch (SWTException e) {
			VpePlugin.getPluginLog().logError(e);
		}

		Rectangle closeBarRect = closeSelectionBar.getItem(0).getBounds();
		rect.width -= closeBarRect.width;

		int allItems = selBar.getItems().length;
		if (allItems == 0) {
			cmpBar.setBounds(new Rectangle(rect.x, rect.y, rect.width,
					closeBarRect.height));
			closeBar.setBounds(new Rectangle(rect.width, rect.y, rect.width
					+ closeBarRect.width, closeBarRect.height));
			return;
		}
		Rectangle r = selBar.getItem(allItems - 1).getBounds();
		int width = r.x + r.width +1;
		int height = r.height;

		if (allItems >= itemCount) {
			int x = 0;

			if (itemCount == 0) {
				x = selBar.getBounds().width;
			} else {
				x = selBar.getItem(allItems - itemCount).getBounds().x;
			}
			
			rect.x -= x;

			if (rect.width < (r.x + r.width - Math.abs(rect.x) + 10)) {
				rect.x -= (r.x + r.width - Math.abs(rect.x)) - rect.width;
			}
			
			cmpBar.setBounds(new Rectangle(rect.x, 0, width, height));
			selBar.setSize(width, closeBarRect.height);
			closeBar.setBounds(new Rectangle(rect.width, rect.y, rect.width
					+ closeBarRect.width, closeBarRect.height));
		}
	}

    public void dispose() {
		if (!selBar.isDisposed()) {
			for (int i = 0; i < selBar.getItemCount(); i++) {
				if (!selBar.getItem(i).isDisposed()) {
					selBar.getItem(i).removeSelectionListener(this);
				}
			}
			selBar.dispose();
			selBar = null;
		}
		if (!closeSelectionBar.isDisposed()) {
			for (int i = 0; i < closeSelectionBar.getItemCount(); i++) {
				if (!closeSelectionBar.getItem(i).isDisposed()) {
					closeSelectionBar.getItem(i).removeSelectionListener(this);
				}
			}
			closeSelectionBar.dispose();
			closeSelectionBar = null;
		}
		if (splitter != null) {
			splitter.dispose();
			splitter = null;
		}
	}

    public void widgetSelected(SelectionEvent e) {
		ToolItem toolItem = (ToolItem) e.widget;

		SelectionUtil.setSourceSelection(vpeController.getPageContext(),
				(Node) toolItem.getData());
	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}

	/**
	 * Performs storing model object in the model and xml file.
	 * 
	 * @param xmo the model object to store
	 */
	private void performStore(XModelObject xmo) {
		if (null == xmo || null == xmo.getModel()
				|| null == xmo.getModelEntity()) {
			return;
		}
		
		ArrayList<IModelPropertyEditorAdapter> adapters = new ArrayList<IModelPropertyEditorAdapter>();
		XAttribute[] attribute = xmo.getModelEntity().getAttributes();
		for (int i = 0; i < attribute.length; i++) {
			if(!attribute[i].isVisible()) {
				continue;
			}
			IModelPropertyEditorAdapter adapter = AdapterFactory.getAdapter(attribute[i], xmo, xmo.getModel());
			adapters.add(adapter);
		}
		/*
		 * Stores model object by its adaptors. 
		 */
		for (IModelPropertyEditorAdapter adapter : adapters) {
			adapter.store();
		}

		/*
		 * Saves model options
		 */
		xmo.getModel().saveOptions();
	}
	

    public String toString() {
		StringBuffer st = new StringBuffer("CountItem: ");
		st.append(itemCount);
		st.append(" Parent Composite: " + cmpToolBar.getBounds().width);
		st.append(" Midle composite: " + cmpBar.getBounds().width);
		st.append(" Bar : " + selBar.getBounds().width);
		return st.toString();
	}
}
