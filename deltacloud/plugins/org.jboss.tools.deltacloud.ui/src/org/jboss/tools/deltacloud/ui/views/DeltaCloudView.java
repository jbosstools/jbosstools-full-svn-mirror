/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.ui.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.core.ICloudManagerListener;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

public class DeltaCloudView extends ViewPart implements ICloudManagerListener,
		ITabbedPropertySheetPageContributor {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.jboss.tools.deltacloud.ui.views.DeltaCloudView";

	private static final String CONTEXT_MENU_ID = "popup:" + ID;
	private static final String VIEW_MENU_ID = "menu:" + ID;

	public static final String COLLAPSE_ALL = "CollapseAll.label"; //$NON-NLS-1$

	private static final String HELP_CONTEXTID = "org.jboss.tools.deltacloud.ui.viewer";

	private TreeViewer viewer;

	private Action collapseall;
	private Action doubleClickAction;

	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new CloudViewContentProvider());
		viewer.setLabelProvider(new CloudViewLabelProvider());
		viewer.setInput(new CVRootElement(viewer));
		viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		getSite().setSelectionProvider(viewer); // for tabbed properties

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), HELP_CONTEXTID);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		DeltaCloudManager.getDefault().addCloudManagerListener(this);
	}

	@Override
	public void dispose() {
		DeltaCloudManager.getDefault().removeCloudManagerListener(this);
		super.dispose();
	}

	private void hookContextMenu() {
		IMenuManager contextMenu = UIUtils.createContextMenu(viewer.getControl());
		UIUtils.registerContributionManager(CONTEXT_MENU_ID, contextMenu, viewer.getControl());
	}

	private void contributeToActionBars() {
		// TODO: replace by declarative command
		fillLocalToolBar(getViewSite().getActionBars().getToolBarManager());
		IActionBars actionBars = getViewSite().getActionBars();
		IMenuManager menuManager = actionBars.getMenuManager();
		UIUtils.registerContributionManager(VIEW_MENU_ID, menuManager, viewer.getControl());
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(collapseall);
	}

	private void makeActions() {
		collapseall = createCollapseAllAction();

		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				@SuppressWarnings("unused")
				Object obj = ((IStructuredSelection) selection).getFirstElement();
			}
		};

	}

	private Action createCollapseAllAction() {
		Action collapseAll = new Action() {
			public void run() {
				viewer.collapseAll();
			}
		};
		collapseAll.setText(CVMessages.getString(COLLAPSE_ALL));
		collapseAll.setToolTipText(CVMessages.getString(COLLAPSE_ALL));
		collapseAll.setImageDescriptor(SWTImagesFactory.DESC_COLLAPSE_ALL);
		return collapseAll;
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void changeEvent(int type) {
		viewer.setInput(new CVRootElement(viewer));
	}

	@Override
	public String getContributorId() {
		return getSite().getId();
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySheetPage.class)
			// If Tabbed view is desired, then change the
			// following to new TabbedPropertySheetPage(this)
			return new CVPropertySheetPage();
		return super.getAdapter(adapter);
	}
}