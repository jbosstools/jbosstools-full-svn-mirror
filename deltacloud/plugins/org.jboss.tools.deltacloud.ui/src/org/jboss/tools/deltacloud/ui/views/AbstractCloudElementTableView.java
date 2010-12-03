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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.core.ICloudManagerListener;
import org.jboss.tools.deltacloud.core.IDeltaCloudElement;
import org.jboss.tools.deltacloud.core.IInstanceFilter;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.ErrorUtils;
import org.jboss.tools.internal.deltacloud.ui.preferences.TextPreferenceValue;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

/**
 * A common superclass for viewers that operate on IDeltaCloudElements
 * (currently DeltaCloudImage and DeltaCloudInstance)
 * 
 * @see InstanceView
 * @see ImageView
 * 
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public abstract class AbstractCloudElementTableView<CLOUDELEMENT extends IDeltaCloudElement> extends ViewPart implements
		ICloudManagerListener {

	private final static String CLOUD_SELECTOR_LABEL = "CloudSelector.label"; //$NON-NLS-1$

	private static final String FILTERED_LABEL = "Filtered.label"; //$NON-NLS-1$
	private static final String FILTERED_TOOLTIP = "FilteredImages.tooltip"; //$NON-NLS-1$	

	private Combo currentCloudSelector;
	private Label currentCloudSelectorLabel;
	private TableViewer viewer;

	private DeltaCloud currentCloud;

	private TextPreferenceValue lastSelectedCloudPref;

	private ModifyListener cloudModifyListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			int index = currentCloudSelector.getSelectionIndex();
			if (index < 0) {
				return;
			}

			removeListener(currentCloud);

			AbstractCloudElementTableView.this.currentCloud = getCurrentCloud(index, getClouds());
			if (currentCloud != null) {
				lastSelectedCloudPref.store(currentCloud.getName());
				Display.getCurrent().asyncExec(new Runnable() {

					@Override
					public void run() {
						setViewerInput(currentCloud);
						addListener(currentCloud);
					}
				});
			}
		}
	};

	private ISelectionListener workbenchSelectionListener = new ISelectionListener() {

		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			DeltaCloud cloud = UIUtils.getFirstAdaptedElement(selection, DeltaCloud.class);
			if (cloud != null) {
				currentCloudSelector.select(getCloudIndex(cloud, getClouds()));
			}
		}
	};

	private Job viewerInputJob = new Job("") {

		@Override
		protected IStatus run(IProgressMonitor monitor) {

			return Status.OK_STATUS;
		}
	};

	public AbstractCloudElementTableView() {
		lastSelectedCloudPref = new TextPreferenceValue(getSelectedCloudPrefsKey(), Activator.getDefault());
	}

	private class ColumnListener extends SelectionAdapter {

		private int column;

		public ColumnListener(int column) {
			this.column = column;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			TableViewerColumnComparator comparator = (TableViewerColumnComparator) viewer.getComparator();
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

	protected abstract String getSelectedCloudPrefsKey();

	protected abstract void addListener(DeltaCloud cloud);

	protected abstract void removeListener(DeltaCloud cloud);

	@Override
	public void dispose() {
		for (DeltaCloud cloud : getClouds()) {
			removeListener(cloud);
		}
		DeltaCloudManager.getDefault().removeCloudManagerListener(this);
		super.dispose();
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		container.setLayout(layout);

		DeltaCloud[] clouds = getClouds();

		createCloudSelector(container);
		initCloudSelector(lastSelectedCloudPref.get(), currentCloudSelector, clouds);

		Label filterLabel = new Label(container, SWT.NULL);
		filterLabel.setText(CVMessages.getString(FILTERED_LABEL));
		filterLabel.setToolTipText(CVMessages.getString(FILTERED_TOOLTIP));

		Composite tableArea = new Composite(container, SWT.NULL);
		viewer = createTableViewer(tableArea);

		currentCloud = getCurrentCloud(currentCloudSelector.getSelectionIndex(), clouds);

		addListener(currentCloud);
		setViewerInput(currentCloud);
		setFilterLabelVisible(currentCloud, filterLabel);

		Point p1 = currentCloudSelectorLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point p2 = currentCloudSelector.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int centering = (p2.y - p1.y + 1) / 2;

		FormData f = new FormData();
		f.top = new FormAttachment(0, 5 + centering);
		f.left = new FormAttachment(0, 30);
		currentCloudSelectorLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(0, 5);
		f.left = new FormAttachment(currentCloudSelectorLabel, 5);
		currentCloudSelector.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(0, 5 + centering);
		f.right = new FormAttachment(100, -10);
		filterLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(currentCloudSelector, 8);
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(100, 0);
		f.bottom = new FormAttachment(100, 0);
		tableArea.setLayoutData(f);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "org.jboss.tools.deltacloud.ui.viewer");
		hookContextMenu(viewer.getControl());
		getSite().setSelectionProvider(viewer);
		getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(workbenchSelectionListener);

		DeltaCloudManager.getDefault().addCloudManagerListener(this);
	}

	private TableViewer createTableViewer(Composite tableArea) {
		TableColumnLayout tableLayout = new TableColumnLayout();
		tableArea.setLayout(tableLayout);

		TableViewer viewer = new TableViewer(tableArea,
				SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.MULTI);
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		ITableContentAndLabelProvider provider = getContentAndLabelProvider();
		viewer.setContentProvider(provider);
		viewer.setLabelProvider(provider);
		createColumns(tableLayout, table);

		viewer.setComparator(new TableViewerColumnComparator());
		table.setSortDirection(SWT.NONE);

		return viewer;
	}

	protected abstract ITableContentAndLabelProvider getContentAndLabelProvider();

	private void setViewerInput(DeltaCloud cloud) {
		if (currentCloud != null) {
			viewer.setInput(currentCloud);
		}
	}

	/**
	 * Gets the clouds that are available in the model.
	 * 
	 * @return the clouds
	 */
	private DeltaCloud[] getClouds() {
		DeltaCloud[] clouds = new DeltaCloud[] {};
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

	private int getCloudIndex(DeltaCloud cloud, DeltaCloud[] clouds) {
		if (cloud == null) {
			return 0;
		}
		return getCloudIndex(cloud.getName(), clouds);
	}

	private int getCloudIndex(String cloudName, DeltaCloud[] clouds) {
		int index = 0;
		if (cloudName != null && clouds.length > 0) {
			for (int i = 0; i < clouds.length; i++) {
				DeltaCloud cloud = clouds[i];
				if (cloudName != null && cloudName.equals(cloud.getName())) {
					index = i;
					break;
				}
			}
		}
		return index;
	}

	private void setFilterLabelVisible(DeltaCloud currentCloud, Label filterLabel) {
		if (currentCloud == null) {
			filterLabel.setVisible(false);
			return;
		}

		IInstanceFilter filter = currentCloud.getInstanceFilter();
		filterLabel.setVisible(!filter.toString().equals(IInstanceFilter.ALL_STRING));
	}

	private DeltaCloud getCurrentCloud(int cloudIndex, DeltaCloud[] clouds) {
		if (cloudIndex < 0 || cloudIndex >= clouds.length) {
			return null;
		}

		return clouds[cloudIndex];
	}

	private void createColumns(TableColumnLayout tableLayout, Table table) {
		for (int i = 0; i < InstanceViewLabelAndContentProvider.Column.getSize(); ++i) {
			InstanceViewLabelAndContentProvider.Column c =
					InstanceViewLabelAndContentProvider.Column.getColumn(i);
			TableColumn tc = new TableColumn(table, SWT.NONE);
			if (i == 0) {
				table.setSortColumn(tc);
			}
			tc.setText(CVMessages.getString(c.name()));
			tableLayout.setColumnData(tc, new ColumnWeightData(c.getWeight(), true));
			tc.addSelectionListener(new ColumnListener(i));
		}
	}

	private void createCloudSelector(Composite parent) {
		this.currentCloudSelectorLabel = new Label(parent, SWT.NULL);
		currentCloudSelectorLabel.setText(CVMessages.getString(CLOUD_SELECTOR_LABEL));

		this.currentCloudSelector = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
		currentCloudSelector.addModifyListener(cloudModifyListener);
		// Following is a kludge so that on Linux the Combo is read-only but
		// has a white background.
		currentCloudSelector.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				e.doit = false;
			}
		});
	}

	private void hookContextMenu(Control control) {
		IMenuManager contextMenu = UIUtils.createContextMenu(control);
		UIUtils.registerContributionManager(UIUtils.getContextMenuId(getViewID()), contextMenu, control);
	}

	protected abstract String getViewID();

	private void initCloudSelector(String cloudNameToSelect, Combo cloudSelector, DeltaCloud[] clouds) {
		if (clouds.length > 0
				&& cloudNameToSelect != null && cloudNameToSelect.length() > 0) {
			cloudSelector.setItems(toCloudNames(clouds));
			cloudSelector.select(getCloudIndex(cloudNameToSelect, clouds));
		}
	}

	public void cloudsChanged(int type) {
		int currIndex = 0;
		if (currentCloud != null) {
			currIndex = currentCloudSelector.getSelectionIndex();
		}
		DeltaCloud[] clouds = getClouds();
		int index = getCloudIndex(currentCloud, getClouds());
		if (type == ICloudManagerListener.RENAME_EVENT) {
			index = currIndex; // no change in cloud displayed
		}

		String[] cloudNames = toCloudNames(clouds);
		setCloudSelectorItems(cloudNames);
		this.currentCloud = getCurrentCloud(index, clouds);

		if (cloudNames.length > 0) {
			currentCloudSelector.setText(cloudNames[index]);
			setViewerInput(currentCloud);
		} else {
			currentCloudSelector.setText("");
			setViewerInput(null);
		}
	}

	private String[] toCloudNames(DeltaCloud[] clouds) {
		List<String> cloudNames = new ArrayList<String>();
		for (DeltaCloud cloud : clouds) {
			if (cloud != null) {
				cloudNames.add(cloud.getName());
			}
		}
		return (String[]) cloudNames.toArray(new String[cloudNames.size()]);
	}

	private void setCloudSelectorItems(String[] cloudNames) {
		currentCloudSelector.removeModifyListener(cloudModifyListener);
		currentCloudSelector.setItems(cloudNames);
		currentCloudSelector.addModifyListener(cloudModifyListener);
	}

	/**
	 * Refresh the states of the commands in the toolbar.
	 */
	protected abstract void refreshToolbarCommandStates();

	public void listChanged(final DeltaCloud cloud, final CLOUDELEMENT[] cloudChildren) {
		// Run following under Display thread since this can be
		// triggered by a non-display thread notifying listeners.
		if (cloud != null
				&& currentCloud != null
				&& cloud.getName().equals(currentCloud.getName())) {
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					// does not add identical instance twice
					addListener(cloud);
					setViewerInput(cloud);
					refreshToolbarCommandStates();
				}
			});
		}
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

}
