/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.toolbar;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class VpeDropDownMenu {

	private Menu dropDownMenu;

	public VpeDropDownMenu(ToolBar bar, String toolTipText) {
		ToolItem item = new ToolItem(bar, SWT.MENU);
		item.setToolTipText(toolTipText);
		item.setImage(ImageDescriptor.createFromFile(MozillaEditor.class,
				"icons/arrow.gif").createImage()); //$NON-NLS-1$

		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
					ToolItem item = (ToolItem) event.widget;
					Rectangle rect = item.getBounds();
					Point pt = item.getParent().toDisplay(
							new Point(rect.x, rect.y));
					dropDownMenu.setLocation(pt.x, pt.y + rect.height);
					dropDownMenu.setVisible(true);
			}
		});
		
		item.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				((ToolItem) e.widget).getImage().dispose();
			}

		});

		this.dropDownMenu = new Menu(item.getParent().getShell());

	}

	public Menu getDropDownMenu() {
		return dropDownMenu;
	}
	

}
