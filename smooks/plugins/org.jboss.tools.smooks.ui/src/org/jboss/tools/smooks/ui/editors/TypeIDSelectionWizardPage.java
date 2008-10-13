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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardNode;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardSelectionPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.INewWizard;
import org.jboss.tools.smooks.analyzer.AnalyzerFactory;
import org.jboss.tools.smooks.analyzer.DataTypeID;
import org.jboss.tools.smooks.ui.IStrucutredDataCreationWizard;
import org.jboss.tools.smooks.ui.IViewerInitor;
import org.jboss.tools.smooks.ui.ViewerInitorStore;
import org.jboss.tools.smooks.ui.wizards.TransformSelectWizardNode;

/**
 * @author Dart Peng<br>
 *         Date : Sep 5, 2008
 */
public class TypeIDSelectionWizardPage extends WizardSelectionPage {
	protected IStrucutredDataCreationWizard sourceWizard;

	protected IStrucutredDataCreationWizard targetWizard;

	protected CheckboxTableViewer source;
	protected CheckboxTableViewer target;
	private List sourceList;
	private String sourceID = null;
	private String targetID = null;
	private IStructuredSelection selection;
	private boolean showDataSelectPage = false;

	public IStructuredSelection getSelection() {
		return selection;
	}

	public void setSelection(IStructuredSelection selection) {
		this.selection = selection;
	}

	public String getSourceID() {
		return sourceID;
	}

	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}

	public String getTargetID() {
		return targetID;
	}

	public void setTargetID(String targetID) {
		this.targetID = targetID;
	}

	public TypeIDSelectionWizardPage(String pageName, boolean showDataSelectPage) {
		super(pageName);
		this.showDataSelectPage = showDataSelectPage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardSelectionPage#getNextPage()
	 */
	public IWizardPage getNextPage() {
		if (this.getSelectedNode() == null) {
			return null;
		}

		boolean isCreated = getSelectedNode().isContentCreated();

		IWizard wizard = getSelectedNode().getWizard();

		if (wizard == null) {
			setSelectedNode(null);
			return null;
		}
		if (!isCreated) {
			if (wizard instanceof IStrucutredDataCreationWizard) {
				String targetID = getDataTypeID(target);
				if (targetID != null) {
					((IStrucutredDataCreationWizard) wizard)
							.setNextDataCreationWizardNode(this
									.getSourceWizard(targetID));
				}
			}
			if (wizard instanceof INewWizard) {
				((INewWizard) wizard).init(null, selection);
			}
			wizard.addPages();
		}
		return wizard.getStartingPage();
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
		sourceList = AnalyzerFactory.getInstance().getRegistrySourceIDList();
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
		target.setInput(sourceList);
		initViewer();

		this.setControl(mainComposite);
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
		if (id == null)
			return false;
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

	private IWizardNode getSourceWizard(String id) {
		Collection<IViewerInitor> viewers = ViewerInitorStore.getInstance()
				.getViewerInitorCollection();
		for (Iterator iterator = viewers.iterator(); iterator.hasNext();) {
			IViewerInitor viewerInitor = (IViewerInitor) iterator.next();
			if (viewerInitor.getTypeID().equals(id)) {
				TransformSelectWizardNode wn = new TransformSelectWizardNode();
				IStrucutredDataCreationWizard wizard = viewerInitor
						.getStructuredDataLoadWizard();
				if (wizard == null)
					return null;
				wn.setWizard(wizard);
				wn.setName(viewerInitor.getName());
				wn.setIconPath(viewerInitor.getWizardIconPath());
				wn.setDescription(viewerInitor.getDescription());
				return wn;
			}
		}
		return null;
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

				if (viewer == source) {
					String sourceID = getDataTypeID(source);
					IWizardNode wn = getSourceWizard(sourceID);
					setSelectedNode(wn);
					setSourceID(sourceID);
					IWizard sw = wn.getWizard();
					if (sw instanceof IStrucutredDataCreationWizard) {
						setSourceWizard((IStrucutredDataCreationWizard) sw);
					}
				}

				if (viewer == target) {
					IWizardNode node = getSelectedNode();
					String targetID = getDataTypeID(target);
					IWizardNode targetNode = getSourceWizard(targetID);
					IWizard tnw = targetNode.getWizard();
					if (tnw instanceof IStrucutredDataCreationWizard) {
						setTargetWizard((IStrucutredDataCreationWizard) tnw);
					}
					if (node != null) {
						IWizard wizard = node.getWizard();
						if (wizard != null
								&& wizard instanceof IStrucutredDataCreationWizard) {
							((IStrucutredDataCreationWizard) wizard)
									.setNextDataCreationWizardNode(targetNode);
						}
					}
					setTargetID(targetID);
				}
			}

		});
		TableColumn nameColumn = new TableColumn(viewer.getTable(), SWT.NONE);
		nameColumn.setWidth(100);
		nameColumn.setText("Name");
		// TableColumn idColumn = new TableColumn(viewer.getTable(), SWT.NONE);
		// idColumn.setWidth(100);
		// idColumn.setText("ID");
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
					// case 1:
					// return ((DataTypeID) element).getId();
				}
			}
			return "";
		}

	}

	public IStrucutredDataCreationWizard getSourceWizard() {
		return sourceWizard;
	}

	public void setSourceWizard(IStrucutredDataCreationWizard sourceWizard) {
		this.sourceWizard = sourceWizard;
	}

	public IStrucutredDataCreationWizard getTargetWizard() {
		return targetWizard;
	}

	public void setTargetWizard(IStrucutredDataCreationWizard targetWizard) {
		this.targetWizard = targetWizard;
	}
}
