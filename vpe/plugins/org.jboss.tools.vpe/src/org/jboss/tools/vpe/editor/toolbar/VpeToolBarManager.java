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

import java.util.ArrayList;
import java.util.List;
import org.eclipse.compare.Splitter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.messages.VpeUIMessages;

/**
 * @author Erick
 * Created on 14.07.2005
 * @see IVpeToolBarManager 
 */
public class VpeToolBarManager implements IVpeToolBarManager {

	static String TOOLBAR = "VPE_TOOLBAR";
	static String HIDE = "HIDE";

	private Splitter splitter;
	private Composite cmpTlEmpty;
	private List toolbarContainers = new ArrayList();
	private MenuItem hideMenuItem;
	private VpePlugin plugin = VpePlugin.getDefault();

	public VpeToolBarManager() {
	}

	public Composite createToolBarComposite(Composite parent) {
		splitter =  new Splitter(parent, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		splitter.setLayoutData(data);

		/*
		 * The empty composite
		 */
		cmpTlEmpty = new Composite(splitter, SWT.NONE) {
			public Point computeSize (int wHint, int hHint, boolean changed) {
				Point point = super.computeSize(wHint, hHint, changed);
				point.y = 1;
				return point;
			}
		};
		cmpTlEmpty.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		splitter.setVisible(cmpTlEmpty, false);
		return splitter;
	}

	public void addToolBar(IVpeToolBar bar) {
		Composite cmpToolBar = new Composite(splitter, SWT.NONE);
		GridLayout layoutTl = new GridLayout(2, false);
		layoutTl.marginBottom = 0;
		layoutTl.marginHeight = 2;
		layoutTl.marginWidth = 0;
		layoutTl.verticalSpacing = 0;
		layoutTl.horizontalSpacing = 0;
		cmpToolBar.setLayout(layoutTl);
		cmpToolBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		bar.createToolBarControl(cmpToolBar);
		toolbarContainers.add(new ToolbarContainer(cmpToolBar, bar));
	}

	public Composite createMenuComposite(final Composite parent) {
		/*
		 * Menu for the menu bar
		 */
		final Menu menu = new Menu(parent.getShell(), SWT.POP_UP);
		hideMenuItem = new MenuItem(menu, SWT.PUSH);
		String hideMenuText = VpeUIMessages.HIDE_TOOLBAR;
		hideMenuItem.setText(hideMenuText);
		for(int i=0; i<toolbarContainers.size(); i++) {
			final ToolbarContainer toolbarContainer = (ToolbarContainer)toolbarContainers.get(i);
			MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
			toolbarContainer.setMenuItem(menuItem);
			menuItem.setText(toolbarContainer.getToolbar().getName());
			
			menuItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					showToolbar(toolbarContainer);
					parent.getParent().layout(true, true);
				}
			});
		}

		String defaultPreferenceValue = null;
		if(toolbarContainers.size()>0) {
			defaultPreferenceValue = ((ToolbarContainer)toolbarContainers.get(0)).getToolbar().getName();
		} else {
			defaultPreferenceValue = HIDE;
		}
		plugin.getPreferenceStore().setDefault(TOOLBAR, defaultPreferenceValue);

		String value = plugin.getPreferenceStore().getString(TOOLBAR);
		if (HIDE.equals(value)) {
			hideToolbars();
		} else {
//				Sets active toolbar
			for(int i = 0; i < toolbarContainers.size(); i++) {
				if( ((ToolbarContainer)toolbarContainers.get(i)).getToolbar().getName().equals(value)) { 
					showToolbar((ToolbarContainer)toolbarContainers.get(i));
				}
			}
		}

		hideMenuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				hideToolbars();
				parent.getParent().layout(true, true);
			}
		});

		/*
		 * The menu ButtonBar
		 */
		final ToolBar btnBar = new ToolBar(parent, SWT.FLAT);
		GridLayout layout = new GridLayout(1,false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;	
		btnBar.setLayout(layout);
		btnBar.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		btnBar.setSize(23,22);
		btnBar.pack();

		final ToolItem button = new ToolItem(btnBar, SWT.MENU); 
			button.setImage(ImageDescriptor.createFromFile(MozillaEditor.class, "icons/arrow.gif").createImage());		 //$NON-NLS-1$
			button.setToolTipText(VpeUIMessages.MENU);
			button.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				Rectangle bounds = button.getBounds();
				Point point = btnBar.toDisplay( bounds.x + 15, bounds.y + 12);
				menu.setLocation(point);
				menu.setVisible(true);
			}
		});
			
			// add dispose listener 
			button.addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent e) {
					// dispose tollitem's image
					((ToolItem) e.widget).getImage().dispose();

				}
			});

		return btnBar;
	}

	private void showToolbar(ToolbarContainer toolbarContainer) {
		plugin.getPreferenceStore().setValue(TOOLBAR, toolbarContainer.getToolbar().getName());
		plugin.savePluginPreferences();
		hideMenuItem.setEnabled(true);			
		toolbarContainer.getMenuItem().setEnabled(false);
		splitter.setVisible(toolbarContainer.getParent(), true);
		for(int i=0; i<toolbarContainers.size(); i++) {
			ToolbarContainer container = (ToolbarContainer)toolbarContainers.get(i);
			if(container != toolbarContainer) {
				splitter.setVisible(container.getParent(), false);
				container.getMenuItem().setEnabled(true);
			}
		}
		splitter.setVisible(cmpTlEmpty, false);
	}

	private void hideToolbars() {
		plugin.getPreferenceStore().setValue(TOOLBAR, HIDE);
		plugin.savePluginPreferences();
		hideMenuItem.setEnabled(false);
		for(int i=0; i<toolbarContainers.size(); i++) {
			ToolbarContainer container = (ToolbarContainer)toolbarContainers.get(i);
			splitter.setVisible(container.getParent(), false);
			container.getMenuItem().setEnabled(true);
		}
		splitter.setVisible(cmpTlEmpty, true);
	}

	/*
	 * This class describe the container for toolbars 
	 */
	private static class ToolbarContainer {

		private Composite parent;
		private IVpeToolBar toolbar;
		private MenuItem menuItem;

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

		public MenuItem getMenuItem() {
			return menuItem;
		}

		public void setMenuItem(MenuItem menuItem) {
			this.menuItem = menuItem;
		}
	}

	public void dispose() {
		if (splitter != null) {
			splitter.dispose();
			splitter=null;
		}
		hideMenuItem.dispose();
		
		for(int i=0; i<toolbarContainers.size(); i++) {
			ToolbarContainer container = (ToolbarContainer)toolbarContainers.get(i);
			container.getMenuItem().dispose();
		}
		toolbarContainers.clear();
		toolbarContainers=null;
	}
}