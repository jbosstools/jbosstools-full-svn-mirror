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

import java.text.MessageFormat;

import org.eclipse.compare.Splitter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.messages.VpeUIMessages;

/**
 * @author Erick Created on 14.07.2005
 * @see IVpeToolBarManager
 */
public class VpeToolBarManager implements IVpeToolBarManager {

	private Splitter splitter;

	private Menu dropDownMenu;

	public static final String SHOW = "show"; //$NON-NLS-1$
	public static final String HIDE = "hide"; //$NON-NLS-1$

	public VpeToolBarManager(Menu dropDownMenu) {

		this.dropDownMenu = dropDownMenu;
	}

	public Composite createToolBarComposite(Composite parent) {
		splitter = new Splitter(parent, SWT.NONE) {

			// if there are no visual children then return Point(0,0)
			public Point computeSize(int hint, int hint2, boolean changed) {
                
				int countVisibleChild = 0;
				for (Control child : getChildren()) {
					if (child.getVisible())
						countVisibleChild++;
				}

				if (countVisibleChild == 0)
					return new Point(0, 0);
				else
					return super.computeSize(hint, hint2, changed);
			}
		};
		splitter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return splitter;
	}

	public void addToolBar(IVpeToolBar bar) {

		Composite cmpToolBar = new Composite(splitter, SWT.NONE);
		splitter.getParent().setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		splitter.getParent().setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		FormData data = new FormData();
		data.left = new FormAttachment(0);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(0);

		cmpToolBar.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		GridLayout layoutTl = new GridLayout(2, false);
		layoutTl.marginBottom = 0;
		layoutTl.marginHeight = 0;
		layoutTl.marginWidth = 0;
		layoutTl.verticalSpacing = 0;
		layoutTl.horizontalSpacing = 0;
		cmpToolBar.setLayout(layoutTl);
		cmpToolBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// create toolbar control
		bar.createToolBarControl(cmpToolBar);

		// create tolbar container
		ToolbarContainer toolbarContainer = new ToolbarContainer(cmpToolBar, bar);

		// show or hide toolbar
		setStateToolbar(cmpToolBar, isShowedToolbar(bar));

		// create item to manage show/hide toolbar
		attachToMenu(dropDownMenu, toolbarContainer);
	}

	/**
	 * create item to manage show/hide toolbar
	 * 
	 * @param menu
	 * @param toolbarContainer
	 */
	public void attachToMenu(Menu menu, ToolbarContainer toolbarContainer) {
		MenuItem menuItem = new MenuItem(dropDownMenu, SWT.PUSH);

		boolean showToolbar = isShowedToolbar(toolbarContainer.getToolbar());

		// set text to menu item
		menuItem.setText(
				MessageFormat.format((showToolbar ? VpeUIMessages.HIDE_TOOLBAR : VpeUIMessages.SHOW_TOOLBAR),
						toolbarContainer.getToolbar().getName()));

		// add listener
		menuItem.addSelectionListener(
				new ToolbarManagerSelectionListener(toolbarContainer, showToolbar));
	}

	/**
	 * 
	 * @param bar
	 * @return
	 */
	protected boolean isShowedToolbar(IVpeToolBar bar) {
		return !HIDE.equalsIgnoreCase(getPreference(bar.getId()));
	}

	/**
	 * show/hide toolbar
	 * 
	 * @param toolBar
	 * @param show
	 */
	protected void setStateToolbar(Control toolBar, boolean show) {
		splitter.setVisible(toolBar, show);
		splitter.getParent().layout(true, true);
	}

	public void dispose() {
		if (splitter != null) {
			splitter.dispose();
			splitter = null;
		}

		for (MenuItem menuItem : dropDownMenu.getItems()) {
			menuItem.dispose();
		}

	}

	/**
	 * get preference by key
	 * 
	 * @param key
	 * @return
	 */
	private String getPreference(String key) {

		return VpePlugin.getDefault().getPreferenceStore().getString(key);
	}

	/**
	 * set preference
	 * 
	 * @param key
	 * @param value
	 */
	private void setPreference(String key, String value) {

		VpePlugin.getDefault().getPreferenceStore().setValue(key, value);
	}

	/**
	 * selection listener to manage toolbars
	 * 
	 * @author Sergey Dzmitrovich
	 * 
	 */
	private class ToolbarManagerSelectionListener extends SelectionAdapter {

		private ToolbarContainer toolbarContainer;
		private boolean showBar;

		public ToolbarManagerSelectionListener(
				ToolbarContainer toolbarContainer, boolean showBar) {
			this.toolbarContainer = toolbarContainer;
			this.showBar = showBar;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			MenuItem selectedItem = (MenuItem) e.widget;

			// change flag
			showBar = !showBar;

			// set new value of preference 
			setPreference(toolbarContainer.getToolbar().getId(), 
					showBar ? SHOW : HIDE);
			// change text
			selectedItem.setText(
					MessageFormat.format(showBar ? VpeUIMessages.HIDE_TOOLBAR 
												 : VpeUIMessages.SHOW_TOOLBAR,
										 toolbarContainer.getToolbar().getName())
								 );
			// show or hide toolbar
			setStateToolbar(toolbarContainer.getParent(), showBar);
		}
	}

	/*
	 * This class describe the container for toolbars
	 */
	private static class ToolbarContainer {

		private Composite parent;
		private IVpeToolBar toolbar;

		public ToolbarContainer(Composite parent, IVpeToolBar toolbar) {
			this.parent = parent;
			this.toolbar = toolbar;
		}

		public Composite getParent() {
			return parent;
		}

		public IVpeToolBar getToolbar() {
			return toolbar;
		}

	}
}