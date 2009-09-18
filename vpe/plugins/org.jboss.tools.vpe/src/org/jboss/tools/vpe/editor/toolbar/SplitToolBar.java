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

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Erick
 * Created on 14.07.2005
 * @author yradtsevich
 * 
 * @see IVpeToolBar
 * This class create a toolBar and store all his item in the array of IItems
 * @see IItems
 */
public abstract class SplitToolBar implements IVpeToolBar {

	protected CoolBar coolBar;
	public abstract void createItems(ToolBar bar);

	public void createToolBarControl(Composite parent) {
		final Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		comp.setLayout(new GridLayout());
		coolBar = new CoolBar(comp, SWT.FLAT | SWT.WRAP);
		coolBar.setLayoutData(new GridData(GridData.FILL_BOTH));

		final ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT | SWT.WRAP);
		createItems(toolBar);
		CoolItem coolItem = new CoolItem(coolBar, SWT.DROP_DOWN);
		coolItem.setControl(toolBar);
		Point size = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point coolSize = coolItem.computeSize(size.x, size.y);

		coolItem.setSize(coolSize);
		coolItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == SWT.ARROW) {
					CoolItem item = (CoolItem) event.widget;
					Rectangle itemBounds = item.getBounds();
					Point pt = coolBar.toDisplay(new Point(itemBounds.x,
							itemBounds.y));
					itemBounds.x = pt.x;
					itemBounds.y = pt.y;
					ToolBar bar = (ToolBar) item.getControl();
					ToolItem[] tools = bar.getItems();

					int i = 0;
					while (i < tools.length) {
						Rectangle toolBounds = tools[i].getBounds();
						pt = bar.toDisplay(new Point(toolBounds.x, toolBounds.y));
						toolBounds.x = pt.x;
						toolBounds.y = pt.y;

						/*
						 * Figure out the visible portion of the tool by looking
						 * at the intersection of the tool bounds with the cool
						 * item bounds.
						 */
						Rectangle intersection = itemBounds
								.intersection(toolBounds);

						/*
						 * If the tool is not completely within the cool item
						 * bounds, then it is partially hidden, and all
						 * remaining tools are completely hidden.
						 */
						if (!intersection.equals(toolBounds)) {
							break;
						}
						i++;
					}

					/*
					 * Create a menu with items for each of the completely
					 * hidden buttons.
					 */
					final Shell floatingShell = new Shell(coolBar.getShell(),
							SWT.ON_TOP);
					FillLayout shellLayout = new FillLayout(SWT.VERTICAL);
					shellLayout.marginHeight = 5;
					shellLayout.marginWidth = 5;
					shellLayout.spacing = 5;
					
					floatingShell.setLayout(shellLayout);
					SelectionListener selectionListener = new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							floatingShell.dispose();
						}
					};
					
					ToolBar floatingBar = null;
					for (int j = i; j < tools.length; j++) {
						ToolItem tool = tools[j];
						int style = tool.getStyle();

						if ((style & SWT.SEPARATOR) != 0) {
							Control control = tool.getControl();
							if (control instanceof Combo) {
								cloneCombo(floatingShell, selectionListener,
										(Combo) control);
							}
						} else {
							if (floatingBar == null) {
								floatingBar = new ToolBar(floatingShell, 
										SWT.FLAT | SWT.WRAP);
							}
							cloneItem(floatingBar, tool, selectionListener);
						}
					}
//					Point size = floatingBar.computeSize (300, SWT.DEFAULT);
//					System.out.println(size);
//					floatingBar.setSize (size);
//					floatingBar.pack();
					floatingShell.pack();

					/*
					 * Drop down the menu below the chevron, with the left edges
					 * aligned.
					 */
					pt = coolBar.toDisplay(new Point(event.x, event.y));
					arrange(floatingShell, pt);
					floatingShell.setVisible(true);
					floatingShell.setFocus();
					floatingShell.addShellListener(new ShellAdapter() {
						public void shellDeactivated(ShellEvent e) {
							e.widget.dispose();
						}
					});
				}
			}
		});
		coolBar.pack();
//		coolBar.layout();
		
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
		toolBar.pack(true);		
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

	/**
	 * Creates a copy of {@code item} in the {@code destBar} and
	 * adds given {@code selectionListener} to the created control.
	 */
	protected void cloneItem(ToolBar destBar, ToolItem item,
			SelectionListener selectionListener) {
		ToolItem copiedItem = createToolItem(destBar, item.getStyle(),
				item.getImage(), item.getToolTipText());
		copiedItem.setEnabled(item.getEnabled());
		copiedItem.setSelection(item.getSelection());
		copySelectionListeners(item, copiedItem);
		copiedItem.addSelectionListener(selectionListener);
	}

	/**
	 * Creates a copy of {@code combo} in the {@code parent} and
	 * adds given {@code selectionListener} to the created control.
	 */
	protected void cloneCombo(Composite parent,
			SelectionListener selectionListener, Combo combo) {
		Combo copiedCombo = createCombo(parent, combo.getStyle(),
				combo.getToolTipText(),
				Arrays.asList(combo.getItems()),
				combo.getSelectionIndex());
		copiedCombo.setEnabled(combo.getEnabled());
		copiedCombo.setVisible(combo.getVisible());
		copySelectionListeners(combo, copiedCombo);
		copiedCombo.addSelectionListener(selectionListener);
	}

	/**
	 * Copies all selection listeners from {@code src} to {@code dest}.
	 */
	protected void copySelectionListeners(Widget src, Widget dest) {
		Listener[] listeners = src.getListeners(SWT.Selection);
		for (Listener listener : listeners) {
			dest.addListener(SWT.Selection, listener);
		}
	}
	
	/**
	 * Creates and returns new {@code ToolItem} in the 
	 * {@code bar}. 
	 */
	protected ToolItem createToolItem(ToolBar bar, int style,
			Image image, String toolTipText) {
		ToolItem item = null;
		item = new ToolItem(bar, style);
		item.setImage(image);		
		item.setToolTipText(toolTipText);

		return item;
	}

	/**
	 * Creates and returns new {@code Combo} in the 
	 * {@code bar}.
	 */
	protected Combo createComboToolItem(ToolBar bar, int style,
			String toolTipText,	List<String> comboItems, int selectionIndex) {
		Combo combo = createCombo(bar, style,
				toolTipText, comboItems, selectionIndex);
		ToolItem sep = new ToolItem(bar, SWT.SEPARATOR);
		sep.setWidth(combo.getSize().x);
		sep.setControl(combo);

		return combo;
	}
	
	protected Combo createCombo(Composite parent, int style,
			String toolTipText,	List<String> comboItems, int selectionIndex) {
		Combo combo = new Combo(parent, style);
//		combo.setLayoutData(new RowData());
		combo.setItems(comboItems.toArray(new String[comboItems.size()]));
		combo.setToolTipText(toolTipText);
		combo.select(selectionIndex);
		combo.pack();
		return combo;
	}
	
	/**
	 * Arranges {@code control} on the display near the {@code point}.
	 */
	private void arrange(Control control, Point point) {
		Point size = control.getSize();
		Rectangle bounds = new Rectangle(point.x, point.y, size.x, size.y);
		control.setBounds(arrange(bounds, control.getDisplay().getBounds()));
	}

	/**
	 * Returns the nearest {@code Rectangle} to the given {@code bounds},
	 * which is fully placed in the {@code clientArea}.
	 */
	private Rectangle arrange(Rectangle bounds, Rectangle clientArea) {
		Rectangle result = new Rectangle(bounds.x, bounds.y,
				bounds.width, bounds.height);
		if (result.x + result.width > clientArea.x + clientArea.width) {
			result.x = clientArea.x + clientArea.width - result.width;
		}
		if (result.x < clientArea.x) {
			result.x = clientArea.x;
		}
		if (result.y + result.height > clientArea.y + clientArea.height) {
			result.y = clientArea.y + clientArea.height - result.height;
		}
		if (result.y < clientArea.y) {
			result.y = clientArea.y;
		}

		return result;
	}
}

