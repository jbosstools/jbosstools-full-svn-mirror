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
package org.jboss.tools.vpe.editor.toolbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;

/**
 * @author Erick
 * Created on 14.07.2005
 * @see IVpeToolBar
 * This class create a toolBar and store all his item in the array of IItems
 * @see IItems
 */
public abstract class SplitToolBar implements IVpeToolBar {

	private IItems[] items;

	public abstract IItems[] createItems(ToolBar bar);

	public void createToolBarControl(Composite parent) {
		final Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final ToolBar horBar = new ToolBar(comp, SWT.FLAT);
		createItems(horBar);

//		final Button  button = new Button(parent, SWT.FLAT|SWT.PUSH);
//		button.setImage(ImageDescriptor.createFromFile(MozillaEditor.class, "icons/arrow_more.gif").createImage());
//		button.setVisible(false);
//		
//		button.addListener(SWT.Selection, new Listener() {
//			public void handleEvent(Event event) {
//				Point pCm = comp.getSize();
//				Menu menu = setMenu(comp, button);				
//				Point point = comp.toDisplay( pCm.x + 15, pCm.y);
//				menu.setLocation(point);
//				menu.setVisible(true);
//			}
//		});	

//		comp.addControlListener(new ControlListener() {
//		
//			public void controlMoved(ControlEvent e) {
//			}
//
//			public void controlResized(ControlEvent e) {
//				e.display.asyncExec(new Runnable() {
//					public void run() {
//						horBar.redraw();
//					}
//				});
//				Point pTl = horBar.getSize();
//				Point pCm = comp.getSize();
//				if (pTl.x > pCm.x) {
//					button.setVisible(true);
//					button.setEnabled(true);
//					button.redraw();
//				    /*
//					int cmpLength = comp.getSize().x;
//					int itLength = 0;
//					int k = 0;
//					while (cmpLength > itLength){
//						 if (k < items.length) {
//							itLength += items[k].getSize();
//							k++;
//						}
//					}
//					
//					comp.setSize(itLength - items[k-1].getSize(), 
//							comp.getSize().y);
//					comp.redraw();*/
//				}				
//
//				if  (pCm.x > pTl.x) {
//					button.setVisible(false);
//					button.redraw();
//				}								
//			}			
//		});
		horBar.pack(true);		
	}

//	public Menu setMenu(Composite cmp, Button btn){
//		final Menu menu = new Menu(btn.getShell(), SWT.POP_UP);
//		MenuItem[] menuItem = new MenuItem[items.length];
//		
//		int cmpLength = 0;
//		cmpLength = cmp.getSize().x ;
//		int itLength = 0;
//		int k = 0;
//		while (cmpLength > itLength){
//			 if (k < items.length) {
//				itLength += items[k].getSize();
//				k++;
//			}
//		}
//				
//		int j = 0;
//		for(int i = items.length - 1; i >= k - 1; i--){
//			if (items[i].isToolItem()){
//				if (items[i].isInsertable()) {
//					menuItem[j] = new MenuItem(menu, SWT.PUSH);
//					menuItem[j].setImage(items[i].getItemImage());
//					menuItem[j].setText(items[i].getItemToolTip());
//					Listener[] list = items[i].getListeners();
//					for(int c=0; c<list.length; c++) {
//						menuItem[j].addListener(SWT.Selection, list[c]);
//					}
//				} else {
//					menuItem[j] = new MenuItem(menu, SWT.SEPARATOR);
//				}
//			}
//			j++;
//		}
//		return menu;																															
//	}
}