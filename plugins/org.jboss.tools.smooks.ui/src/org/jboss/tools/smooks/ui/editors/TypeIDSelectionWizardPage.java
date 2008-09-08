/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.ui.editors;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.jboss.tools.smooks.analyzer.AnalyzerFactory;
import org.jboss.tools.smooks.analyzer.DataTypeID;

/**
 * @author Dart Peng<br>
 *         Date : Sep 5, 2008
 */
public class TypeIDSelectionWizardPage extends WizardPage {

	protected CheckboxTableViewer source;
	protected CheckboxTableViewer target;

	public TypeIDSelectionWizardPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public TypeIDSelectionWizardPage(String pageName) {
		super(pageName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.makeColumnsEqualWidth = true;
		mainComposite.setLayout(gl);
		List<DataTypeID> sourceList = AnalyzerFactory.getInstance()
				.getRegistrySourceIDList();
		List<DataTypeID> targetList = AnalyzerFactory.getInstance()
				.getRegistryTargetIDList();
		Label sl = new Label(mainComposite, SWT.NONE);
		sl.setText("Source Data Type ID List : ");

		Label tl = new Label(mainComposite, SWT.NONE);
		tl.setText("Target Data Type ID List : ");

		source = createTableViewer(mainComposite);
		GridData gd = new GridData(GridData.FILL_BOTH);
		source.getTable().setLayoutData(gd);
		target = createTableViewer(mainComposite);
		target.getTable().setLayoutData(gd);

		source.setInput(sourceList);
		target.setInput(targetList);
		initViewer();
		
		this.setControl(mainComposite);
	}

	public String getSourceDataTypeID() {
		return getDataTypeID(source);
	}

	public String getTargetDataTypeID() {
		return getDataTypeID(target);
	}

	protected String getDataTypeID(CheckboxTableViewer viewer) {
		Object[] objs = viewer.getCheckedElements();
		if (objs == null)
			return null;
		if (objs.length > 1)
			return null;
		DataTypeID d = (DataTypeID) objs[0];
		return d.getId();
	}

	protected void initViewer() {
		IWizard wizard = this.getWizard();
		if (wizard instanceof TypeIDSelectionWizard) {
			String s = ((TypeIDSelectionWizard) wizard).getSourceDataTypeID();
			String t = ((TypeIDSelectionWizard) wizard).getTargetDataTypeID();
			if (initViewerCheckState(s, source)) {
				source.getTable().setEnabled(false);
			}
			if (initViewerCheckState(t, target)) {
				target.getTable().setEnabled(false);
			}
		}
	}

	protected boolean initViewerCheckState(String id, CheckboxTableViewer viewer) {
		if(id == null) return false;
		List l = (List) viewer.getInput();
		for (Iterator iterator = l.iterator(); iterator.hasNext();) {
			DataTypeID dti = (DataTypeID) iterator.next();
			if (id.equals(dti.getId())) {
				viewer.setChecked(dti, true);
				return true;
			}
		}
		return false;
	}

	protected CheckboxTableViewer createTableViewer(Composite parent) {
		final CheckboxTableViewer viewer = CheckboxTableViewer.newCheckList(
				parent, SWT.FULL_SELECTION);
		viewer.addCheckStateListener(new ICheckStateListener() {
			private boolean fireEvent = true;

			public void checkStateChanged(CheckStateChangedEvent event) {
				if (!fireEvent)
					return;
				boolean check = event.getChecked();
				if (check) {
					fireEvent = false;
					viewer.setAllChecked(false);
					viewer.setChecked(event.getElement(), true);
					fireEvent = true;
				}
			}

		});
		TableColumn nameColumn = new TableColumn(viewer.getTable(), SWT.NONE);
		nameColumn.setWidth(100);
		nameColumn.setText("Name");
		TableColumn idColumn = new TableColumn(viewer.getTable(), SWT.NONE);
		idColumn.setWidth(100);
		idColumn.setText("ID");
		viewer.setContentProvider(new TypeIDContentProvider());
		viewer.setLabelProvider(new TypeIDLabelProvider());
		return viewer;
	}

	private class TypeIDContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			}
			return new Object[] {};
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private class TypeIDLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {

			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof DataTypeID) {
				switch (columnIndex) {
				case 0:
					return ((DataTypeID) element).getName();
				case 1:
					return ((DataTypeID) element).getId();
				}
			}
			return "";
		}

	}
}
