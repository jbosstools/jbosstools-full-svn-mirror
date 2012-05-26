/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.internal.deltacloud.ui.wizards;

import java.text.MessageFormat;
import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.common.ui.WizardUtils;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.IImageFilter;
import org.jboss.tools.deltacloud.core.ImageFilter;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob.CLOUDELEMENT;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;
import org.jboss.tools.deltacloud.ui.views.Columns;
import org.jboss.tools.deltacloud.ui.views.Columns.Column;
import org.jboss.tools.deltacloud.ui.views.cloudelements.ITableContentAndLabelProvider;
import org.jboss.tools.deltacloud.ui.views.cloudelements.TableViewerColumnComparator;

/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class FindImagePage extends WizardPage {

	private final static String NAME = "FindImage.name"; //$NON-NLS-1$
	private final static String TITLE = "FindImage.title"; //$NON-NLS-1$
	private final static String DESC = "FindImage.desc"; //$NON-NLS-1$
	private final static String NAME_LABEL = "Name.label"; //$NON-NLS-1$
	private final static String ID_LABEL = "Id.label"; //$NON-NLS-1$
	private final static String ARCH_LABEL = "Arch.label"; //$NON-NLS-1$
	private final static String DESC_LABEL = "Desc.label"; //$NON-NLS-1$
	private final static String INVALID_SEMICOLON = "ErrorFilterSemicolon.msg"; //$NON-NLS-1$

	private DeltaCloud cloud;
	private TableViewer viewer;

	private Text nameText;
	private Text idText;
	private Text archText;
	private Text descText;

	private IImageFilter filter;
	private DeltaCloudImage selectedElement;
	private String oldRules;

	private ModifyListener textListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			validate();
		}

	};

	private class ColumnListener extends SelectionAdapter {

		private int column;
		private TableViewer viewer;

		public ColumnListener(int column, TableViewer viewer) {
			this.column = column;
			this.viewer = viewer;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			TableViewerColumnComparator comparator = (TableViewerColumnComparator) viewer.getComparator();
			Table t = viewer.getTable();
			if (comparator.getColumn() == column) {
				comparator.switchDirection();
			}
			comparator.setColumn(column);
			comparator.setSortIndicator(viewer);
			TableColumn tc = (TableColumn) e.getSource();
			t.setSortColumn(tc);
			viewer.refresh();
		}
	};

	public FindImagePage(DeltaCloud cloud) {
		super(WizardMessages.getString(NAME));
		this.cloud = cloud;
		filter = new ImageFilter(cloud);
		setDescription(WizardMessages.getString(DESC));
		setTitle(WizardMessages.getString(TITLE));
		setImageDescriptor(SWTImagesFactory.DESC_DELTA_LARGE);
		setPageComplete(false);
	}

	public String getImageId() {
		if (selectedElement == null) {
			return null;
		}
		return selectedElement.getId();
	}

	private void validate() {
		boolean hasError = false;
		boolean isComplete = true;

		String name = nameText.getText();
		String id = idText.getText();
		String arch = archText.getText();
		String desc = descText.getText();

		// TODO remove this filter related functionality. bad encapsulation!
		hasError = validateFilters(hasError, name, id, arch, desc);

		if (selectedElement == null)
			isComplete = false;

		if (!hasError) {
			setErrorMessage(null);
			// TODO remove this filter related functionality. bad encapsulation!
			String newRules = name + "*;" //$NON-NLS-1$ 
					+ id + "*;" //$NON-NLS-1$
					+ arch + "*;" //$NON-NLS-1$ 
					+ desc + "*"; //$NON-NLS-1$

			if (!newRules.equals(oldRules)) {
				filter = new ImageFilter(newRules, cloud);
				oldRules = newRules;
				asyncSetImagesToViewer(filter);
			}
		}
		setPageComplete(isComplete && !hasError);
	}

	private boolean validateFilters(boolean hasError, String name, String id, String arch, String desc) {
		if (name.contains(";") || //$NON-NLS-1$
				id.contains(";") || //$NON-NLS-1$
				arch.contains(";") || //$NON-NLS-1$
				desc.contains(";")) { //$NON-NLS-1$
			setErrorMessage(WizardMessages.getString(INVALID_SEMICOLON));
			hasError = true;
		}
		return hasError;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		container.setLayout(layout);

		Label nameLabel = new Label(container, SWT.NULL);
		nameLabel.setText(WizardMessages.getString(NAME_LABEL));

		nameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		nameText.setText(filter.getNameRule().toString());
		nameText.addModifyListener(textListener);

		Label idLabel = new Label(container, SWT.NULL);
		idLabel.setText(WizardMessages.getString(ID_LABEL));

		idText = new Text(container, SWT.BORDER | SWT.SINGLE);
		idText.setText(filter.getIdRule().toString());
		idText.addModifyListener(textListener);

		Label archLabel = new Label(container, SWT.NULL);
		archLabel.setText(WizardMessages.getString(ARCH_LABEL));

		archText = new Text(container, SWT.BORDER | SWT.SINGLE);
		archText.setText(filter.getArchRule().toString());
		archText.addModifyListener(textListener);

		Label descLabel = new Label(container, SWT.NULL);
		descLabel.setText(WizardMessages.getString(DESC_LABEL));

		descText = new Text(container, SWT.BORDER | SWT.SINGLE);
		descText.setText(filter.getDescRule().toString());
		descText.addModifyListener(textListener);

		Composite tableArea = new Composite(container, SWT.NULL);
		TableColumnLayout tableLayout = new TableColumnLayout();
		tableArea.setLayout(tableLayout);

		viewer = new TableViewer(tableArea, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		FindImagePageLabelAndContentProvider provider = new FindImagePageLabelAndContentProvider();
		viewer.setContentProvider(provider);
		viewer.setLabelProvider(provider);
		TableViewerColumnComparator comparator = new TableViewerColumnComparator();
		viewer.setComparator(comparator);
		createColumns(provider, tableLayout, table);
		table.setSortDirection(SWT.NONE);
		viewer.getTable().addListener(SWT.MouseDoubleClick, onImageDoubleclick());
		
		Point p1 = nameLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point p2 = nameText.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int centering = (p2.y - p1.y + 1) / 2;

		FormData f = new FormData();
		// f.left = new FormAttachment(0, 5);
		// f.top = new FormAttachment(0, 5 + centering);
		// filterLabel.setLayoutData(f);

		f = new FormData();
		f.left = new FormAttachment(0, 10);
		f.top = new FormAttachment(0, 5 + centering);
		nameLabel.setLayoutData(f);

		f = new FormData();
		f.left = new FormAttachment(nameLabel, 5);
		f.top = new FormAttachment(0, 5);
		f.right = new FormAttachment(22, -10);
		nameText.setLayoutData(f);

		f = new FormData();
		f.left = new FormAttachment(22);
		f.top = new FormAttachment(0, 5 + centering);
		idLabel.setLayoutData(f);

		f = new FormData();
		f.left = new FormAttachment(idLabel, 5);
		f.top = new FormAttachment(0, 5);
		f.right = new FormAttachment(40, -10);
		idText.setLayoutData(f);

		f = new FormData();
		f.left = new FormAttachment(40);
		f.top = new FormAttachment(0, 5 + centering);
		archLabel.setLayoutData(f);

		f = new FormData();
		f.left = new FormAttachment(archLabel, 5);
		f.top = new FormAttachment(0, 5);
		f.right = new FormAttachment(67, -10);
		archText.setLayoutData(f);

		f = new FormData();
		f.left = new FormAttachment(67);
		f.top = new FormAttachment(0, 5 + centering);
		descLabel.setLayoutData(f);

		f = new FormData();
		f.left = new FormAttachment(descLabel, 5);
		f.top = new FormAttachment(0, 5);
		f.right = new FormAttachment(100, -10);
		descText.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(nameText, 13);
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(100, 0);
		f.bottom = new FormAttachment(100, 0);
		tableArea.setLayoutData(f);

		setControl(container);

		hookSelection();
		validate();
	}

	private void createColumns(ITableContentAndLabelProvider<DeltaCloudImage> provider, TableColumnLayout tableLayout,
			Table table) {
		Columns<DeltaCloudImage> columns = provider.getColumns();
		for (int i = 0; i < columns.getSize(); ++i) {
			Column<DeltaCloudImage> c = columns.getColumn(i);
			TableColumn tc = new TableColumn(table, SWT.NONE);
			if (i == 0) {
				table.setSortColumn(tc);
			}
			tc.setText(c.getName());
			tableLayout.setColumnData(tc, new ColumnWeightData(c.getWeight(), true));
			tc.addSelectionListener(new ColumnListener(i, viewer));
		}
	}

	private void hookSelection() {
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				handleSelection();
			}
		});
	}

	private void handleSelection() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		selectedElement = (DeltaCloudImage) selection.getFirstElement();
		validate();
	}

	private void asyncSetImagesToViewer(final IImageFilter filter) {
		new AbstractCloudElementJob(
				MessageFormat.format("Get images from cloud {0}", cloud.getName()), cloud, CLOUDELEMENT.IMAGES) {

			@Override
			protected IStatus doRun(IProgressMonitor monitor) throws DeltaCloudException {
				try {
					Collection<DeltaCloudImage> filteredImages = filter.filter(cloud.getImages());
					setViewerInput(filteredImages.toArray(new DeltaCloudImage[] {}));
					return Status.OK_STATUS;
				} catch (DeltaCloudException e) {
					setViewerInput(new DeltaCloudImage[] {});
					throw e;
				}
			}
		}.schedule();
	}

	private void setViewerInput(final DeltaCloudImage[] images) {
		viewer.getControl().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				viewer.setInput(images);
			}
		});
	}

	private Listener onImageDoubleclick() {
		return new Listener() {

			@Override
			public void handleEvent(Event event) {
				WizardUtils.nextPageOrFinish(FindImagePage.this);
			}
		};
	}

}
