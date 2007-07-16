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

import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.compare.Splitter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.selection.VpeSourceSelection;
import org.jboss.tools.vpe.editor.selection.VpeSourceSelectionBuilder;

/**
 * @author erick
 * This class create and manage the Selection Bar under the VPE. Entry point from the class MozilaEditor
 * This bar can be hiden and shown it uses splitter for this  
 */
 
public class SelectionBar extends Layout implements SelectionListener {
	private Splitter splitter;

	private VpeController vpeController = null;

	private ToolBar selBar = null;

	private int itemCount = 0;

	Composite cmpToolBar = null;

	Composite cmpBar = null;

	Listener selbarListener = null;

	
	public Composite createToolBarComposite(Composite parent, boolean show) {

		splitter = new Splitter(parent, SWT.NONE);
		splitter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		/*
		 * The empty composite
		 */
		Composite cmpTlEmpty = new Composite(splitter, SWT.NONE) {
			public Point computeSize(int wHint, int hHint, boolean changed) {
				Point point = super.computeSize(wHint, hHint, changed);
				point.y = 1;
				return point;
			}
		};

		cmpTlEmpty.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Main composite of the visivle splitter
		cmpToolBar = new Composite(splitter, SWT.NONE);
		cmpToolBar.setLayout(this);
		
		GridLayout layoutTl = new GridLayout(1, false);
		layoutTl.marginBottom = 0;
		layoutTl.marginHeight = 0;
		layoutTl.marginWidth = 0;
		layoutTl.verticalSpacing = 0;
		layoutTl.horizontalSpacing = 0;
		
		// Midle composite, witch contain the selectbar
		cmpBar = new Composite(cmpToolBar, SWT.NONE);
		cmpBar.setLayout(layoutTl);
		cmpBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Create selection bar
		selBar = new ToolBar(cmpBar, SWT.HORIZONTAL | SWT.FLAT);
		if (show == true) {
			splitter.setVisible(cmpTlEmpty, false);
			splitter.setVisible(cmpToolBar, true);
		}
		else {
			splitter.setVisible(cmpTlEmpty, true);
			splitter.setVisible(cmpToolBar, false);
		}
			

		return splitter;
	}

	public void showBar(String show) {

		Control[] children = splitter.getChildren();
		if (children != null && children.length > 0) {
			if (show.equals("show")) {
				splitter.setVisible(children[0], true);
				splitter.setVisible(children[1], false);				
				splitter.getParent().layout(true, true);
			} else {
				splitter.setVisible(children[0], false);
				splitter.setVisible(children[1], true);				
				splitter.getParent().layout(true, true);
			}
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

//		Node node = selection.getFocusNode();
		Node node = selection.getStartNode();
		if (node != null && node.getNodeType() == Node.TEXT_NODE) {
			node = node.getParentNode();
		}

		int elementCounter = 0;
		while (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
			ToolItem item = null;
			if (selBar.getItemCount() > elementCounter) {
				item = selBar.getItem(selBar.getItemCount() - elementCounter - 1);
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
	}

	protected Point computeSize(Composite composite, int wHint, int hHint,
			boolean flushCache) {
		return selBar.computeSize(SWT.DEFAULT, hHint);
	}

	protected void layout(Composite composite, boolean flushCache) {
		Rectangle rect = null;
		try {
			rect = composite.getBounds();
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
		}

		int allItems = selBar.getItems().length;
		if (allItems == 0)
			return;

		Rectangle r = selBar.getItem(allItems - 1).getBounds();
		int width = r.x + r.width;
		int height = r.height;
		

		if (allItems >= itemCount) {
			int x = 0;

			if (itemCount == 0) {
				x = selBar.getBounds().width;
			} else {
				x = selBar.getItem(allItems - itemCount).getBounds().x;
			}
			rect.x -= x;
		}

		if (rect.width < (r.x + r.width - Math.abs(rect.x) + 10)) {
			rect.x -= (r.x + r.width - Math.abs(rect.x)) - rect.width;
		}
		cmpBar.setBounds(new Rectangle(rect.x, 0, width, height));
		selBar.setSize(width, height);
	}

	public void dispose() {
		if (!selBar.isDisposed()) {
			for (int i =0; i < selBar.getItemCount(); i++) {
				if (!selBar.getItem(i).isDisposed()) {
					selBar.getItem(i).removeSelectionListener(this);
				}
			}
		}
	}

	public void widgetSelected(SelectionEvent e) {
		ToolItem toolItem = (ToolItem) e.widget;
		int offset = ((ElementImpl)(Node)toolItem.getData()).getStartOffset(); 
		setSourceFocus(offset);		
	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}
	
	private void setSourceFocus(int offset) {
		vpeController.getPageContext().getSourceBuilder().getStructuredTextViewer().setSelectedRange(offset, 0);
		vpeController.getPageContext().getSourceBuilder().getStructuredTextViewer().revealRange(offset, 0);
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
