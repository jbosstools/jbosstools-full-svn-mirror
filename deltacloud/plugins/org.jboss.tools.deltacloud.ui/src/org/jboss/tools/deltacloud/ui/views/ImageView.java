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

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.core.ICloudManagerListener;
import org.jboss.tools.deltacloud.core.IImageFilter;
import org.jboss.tools.deltacloud.core.IImageListListener;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.ErrorUtils;
import org.jboss.tools.deltacloud.ui.IDeltaCloudPreferenceConstants;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;
import org.osgi.service.prefs.Preferences;

public class ImageView extends ViewPart implements ICloudManagerListener, IImageListListener {

	public static final String ID = "org.jboss.tools.deltacloud.ui.views.ImageView";

	private final static String CLOUD_SELECTOR_LABEL = "CloudSelector.label"; //$NON-NLS-1$
	private static final String FILTERED_LABEL = "Filtered.label"; //$NON-NLS-1$
	private static final String FILTERED_TOOLTIP = "FilteredImages.tooltip"; //$NON-NLS-1$	
	private TableViewer viewer;
	private Composite container;
	private Combo cloudSelector;
	private Label filterLabel;
	private DeltaCloud[] clouds;
	private DeltaCloud currCloud;

	private ModifyListener cloudModifyListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			if (currCloud != null) {
				currCloud.removeImageListListener(ImageView.this);
			}
			int index = cloudSelector.getSelectionIndex();
			if (index >= 0) {
				currCloud = clouds[index];
				storeSelectedCloud();

				Display.getCurrent().asyncExec(new Runnable() {

					@Override
					public void run() {
						viewer.setInput(currCloud);
						currCloud.addImageListListener(ImageView.this);
						viewer.refresh();
					}

				});
			}
		}

		private void storeSelectedCloud() {
			Preferences prefs = new InstanceScope().getNode(Activator.PLUGIN_ID);
			try {
				prefs.put(IDeltaCloudPreferenceConstants.LAST_CLOUD_IMAGE_VIEW, currCloud.getName());
			} catch (Exception exc) {
				// do nothing
			}
		}
	};

	private class ColumnListener extends SelectionAdapter {

		private int column;

		public ColumnListener(int column, TableViewer viewer) {
			this.column = column;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			ImageComparator comparator = (ImageComparator) viewer.getComparator();
			Table t = viewer.getTable();
			if (comparator.getColumn() == column) {
				comparator.reverseDirection();
			}
			comparator.setColumn(column);
			TableColumn tc = (TableColumn) e.getSource();
			t.setSortColumn(tc);
			t.setSortDirection(SWT.NONE);
			viewer.refresh();
		}

	};

	@Override
	public void dispose() {
		for (DeltaCloud cloud : clouds) {
			cloud.removeImageListListener(this);
		}
		DeltaCloudManager.getDefault().removeCloudManagerListener(this);
		super.dispose();
	}

	@Override
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		container.setLayout(layout);

		Label cloudSelectorLabel = new Label(container, SWT.NULL);
		cloudSelectorLabel.setText(CVMessages.getString(CLOUD_SELECTOR_LABEL));

		cloudSelector = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
		clouds = getClouds();
		initializeCloudSelector(clouds);
		cloudSelector.addModifyListener(cloudModifyListener);
		// Following is a kludge so that on Linux the Combo is read-only but
		// has a white background.
		cloudSelector.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				e.doit = false;
			}
		});

		filterLabel = new Label(container, SWT.NULL);
		filterLabel.setText(CVMessages.getString(FILTERED_LABEL));
		filterLabel.setToolTipText(CVMessages.getString(FILTERED_TOOLTIP));

		Composite tableArea = createTableViewer();

		ImageComparator comparator = new ImageComparator(0);
		viewer.setComparator(comparator);
		getSite().setSelectionProvider(viewer);

		if (currCloud != null) {
			currCloud.removeImageListListener(this);
			viewer.setInput(currCloud);
			currCloud.addImageListListener(this);
			IImageFilter filter = currCloud.getImageFilter();
			filterLabel.setVisible(!filter.toString().equals(IImageFilter.ALL_STRING));
		}

		Point p1 = cloudSelectorLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point p2 = cloudSelector.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int centering = (p2.y - p1.y + 1) / 2;

		FormData f = new FormData();
		f.top = new FormAttachment(0, 5 + centering);
		f.left = new FormAttachment(0, 30);
		cloudSelectorLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(0, 5);
		f.left = new FormAttachment(cloudSelectorLabel, 5);
		cloudSelector.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(0, 5 + centering);
		f.right = new FormAttachment(100, -10);
		filterLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(cloudSelector, 8);
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(100, 0);
		f.bottom = new FormAttachment(100, 0);
		tableArea.setLayoutData(f);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "org.jboss.tools.deltacloud.ui.viewer");
		hookContextMenu();
		
		DeltaCloudManager.getDefault().addCloudManagerListener(this);
	}

	private Composite createTableViewer() {
		Composite tableArea = new Composite(container, SWT.NULL);
		TableColumnLayout tableLayout = new TableColumnLayout();
		tableArea.setLayout(tableLayout);

		viewer = new TableViewer(tableArea, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		createColumns(tableLayout, table);
		ImageViewLabelAndContentProvider provider = new ImageViewLabelAndContentProvider();
		viewer.setContentProvider(provider);
		viewer.setLabelProvider(provider);
		return tableArea;
	}

	private void createColumns(TableColumnLayout tableLayout, Table table) {
		for (int i = 0; i < ImageViewLabelAndContentProvider.Column.getSize(); ++i) {
			ImageViewLabelAndContentProvider.Column c =
					ImageViewLabelAndContentProvider.Column.getColumn(i);
			TableColumn tc = new TableColumn(table, SWT.NONE);
			if (i == 0)
				table.setSortColumn(tc);
			tc.setText(CVMessages.getString(c.name()));
			tableLayout.setColumnData(tc, new ColumnWeightData(c.getWeight(), true));
			tc.addSelectionListener(new ColumnListener(i, viewer));
		}
		table.setSortDirection(SWT.NONE);
	}

	private void hookContextMenu() {
		IMenuManager contextMenu = UIUtils.createContextMenu(viewer.getTable());
		UIUtils.registerContributionManager(UIUtils.getContextMenuId(ID), contextMenu, viewer.getTable());
	}

	@Override
	public void setFocus() {
		viewer.getTable().setFocus();
	}

	private void initializeCloudSelector(DeltaCloud[] clouds) {
		int defaultIndex = 0;
		String[] cloudNames = new String[clouds.length];
		// If we have saved the last cloud used from a previous session,
		// default to using that cloud to start unless it no longer exists
		Preferences prefs = new InstanceScope().getNode(Activator.PLUGIN_ID);
		String lastCloudUsed = prefs.get(IDeltaCloudPreferenceConstants.LAST_CLOUD_IMAGE_VIEW, "");
		for (int i = 0; i < clouds.length; ++i) {
			cloudNames[i] = clouds[i].getName();
			if (cloudNames[i].equals(lastCloudUsed))
				defaultIndex = i;
		}
		cloudSelector.setItems(cloudNames);
		if (clouds.length > 0) {
			cloudSelector.setText(cloudNames[defaultIndex]);
			currCloud = clouds[defaultIndex];
		}
	}

	public void cloudsChanged(int type) {
		String currName = null;
		int currIndex = 0;
		clouds = getClouds();
		if (currCloud != null) {
			currName = currCloud.getName();
			currIndex = cloudSelector.getSelectionIndex();
		}
		String[] cloudNames = new String[clouds.length];
		int index = 0;
		for (int i = 0; i < clouds.length; ++i) {
			cloudNames[i] = clouds[i].getName();
			if (cloudNames[i].equals(currName))
				index = i;
		}
		if (type == ICloudManagerListener.RENAME_EVENT) {
			index = currIndex; // no change in cloud displayed
		}
		cloudSelector.removeModifyListener(cloudModifyListener);
		cloudSelector.setItems(cloudNames);
		if (cloudNames.length > 0) {
			cloudSelector.setText(cloudNames[index]);
			currCloud = clouds[index];
			viewer.setInput(currCloud);
		} else {
			currCloud = null;
			cloudSelector.setText("");
			viewer.setInput(new DeltaCloudImage[0]);
		}
		cloudSelector.addModifyListener(cloudModifyListener);
	}

	public void listChanged(DeltaCloud cloud, DeltaCloudImage[] list) {
		final DeltaCloudImage[] finalList = list;
		if (cloud.getName().equals(currCloud.getName())) {
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					currCloud.removeImageListListener(ImageView.this);
					viewer.setInput(finalList);
					currCloud.addImageListListener(ImageView.this);
					viewer.refresh();
				}

			});
		}
	}
	
	private DeltaCloud[] getClouds() {
		DeltaCloud[] clouds = new DeltaCloud[]{};
		try {
			clouds = DeltaCloudManager.getDefault().getClouds();
		} catch (DeltaCloudException e) {
			// TODO: internationalize strings
			ErrorUtils.handleError(
					"Error",
					"Could not get all clouds",
					e, Display.getDefault().getActiveShell());
		}
		return clouds;
	}


}
