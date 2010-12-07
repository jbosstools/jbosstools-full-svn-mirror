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
package org.jboss.tools.deltacloud.ui.views.cloud;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;
import org.jboss.tools.deltacloud.ui.views.CVMessages;
import org.jboss.tools.deltacloud.ui.views.cloud.property.CVPropertySheetPage;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

public class DeltaCloudView extends ViewPart implements ITabbedPropertySheetPageContributor {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.jboss.tools.deltacloud.ui.views.DeltaCloudView";

	public static final String COLLAPSE_ALL = "CollapseAll.label"; //$NON-NLS-1$
	private static final String HELP_CONTEXTID = "org.jboss.tools.deltacloud.ui.viewer";

	private TreeViewer viewer;

	private Action collapseall;

	public void createPartControl(Composite parent) {
		viewer = createTreeViewer(parent);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), HELP_CONTEXTID);
		makeActions();
		hookContextMenu(viewer.getTree());
		contributeToActionBars();
	}

	private TreeViewer createTreeViewer(Composite parent) {
		TreeViewer viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new CloudViewContentProvider());
		viewer.setLabelProvider(new CloudViewLabelProvider());
		viewer.setUseHashlookup(true);
		viewer.setInput(new CVRootElement(viewer));
		viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		getSite().setSelectionProvider(viewer);
		return viewer;
	}

	private void hookContextMenu(Control control) {
		IMenuManager contextMenu = UIUtils.createContextMenu(control);
		UIUtils.registerContributionManager(UIUtils.getContextMenuId(ID), contextMenu, control);
	}

	private void contributeToActionBars() {
		// TODO: replace by declarative command
		fillLocalToolBar(getViewSite().getActionBars().getToolBarManager());
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(collapseall);
	}

	private void makeActions() {
		collapseall = createCollapseAllAction();
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

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public String getContributorId() {
		return getSite().getId();
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (adapter == IPropertySheetPage.class)
			// If Tabbed view is desired, then change the
			// following to new TabbedPropertySheetPage(this)
			return new CVPropertySheetPage();
		return super.getAdapter(adapter);
	}
}