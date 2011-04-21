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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardNode;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.jboss.tools.smooks.analyzer.DataTypeID;
import org.jboss.tools.smooks.javabean.analyzer.JavaBeanAnalyzer;
import org.jboss.tools.smooks.ui.IStructuredDataCreationWizard;
import org.jboss.tools.smooks.ui.IViewerInitor;
import org.jboss.tools.smooks.ui.StructuredDataCreationWizardDailog;
import org.jboss.tools.smooks.ui.ViewerInitorStore;
import org.jboss.tools.smooks.ui.wizards.ISmooksDataCreationAddtionWizard;
import org.jboss.tools.smooks.ui.wizards.TransformSelectWizardNode;
import org.jboss.tools.smooks.xml2java.analyzer.XML2JavaAnalyzer;

/**
 * @author Dart Peng<br>
 *         Date : Sep 5, 2008
 */
public class TypeIDSelectionWizardPage extends WizardPage {
	protected IWizard sourceDataCreationWizard;

	protected IWizard targetDataCreationWizard;

	private Object sourceTreeViewerInputContents;

	private Object targetTreeViewerInputContents;

	protected CheckboxTableViewer source;
	
	protected CheckboxTableViewer target;
	
	private List sourceList;
	
	private String sourceID = null;
	
	private String targetID = null;
	
	private String oldSourceID = null;
	
	private String oldTargetID = null;
	
	private IStructuredSelection selection;
	
	private boolean showDataSelectPage = false;

	private Hyperlink sourceDataLink;

	private Hyperlink targetDataLink;

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
		setTitle(Messages
				.getString("TypeIDSelectionWizardPage.TypeIDSelectionWizardPageTitle")); //$NON-NLS-1$
		setDescription(Messages
				.getString("TypeIDSelectionWizardPage.TypeIDSelectionWizardPageDescription")); //$NON-NLS-1$
	}

	@Override
	public boolean canFlipToNextPage() {
		if (this.getSourceID() != null && getTargetID() != null) {
			return true;
		}
		return false;
	}

	@Override
	public IWizardPage getNextPage() {
		String sourceID = this.getSourceID();
		String targetID = this.getTargetID();
		IStructuredDataCreationWizard sourceWizard = null;
		IStructuredDataCreationWizard targetWizard = null;
		IWizard rootWizard = getWizard();
		ISmooksDataCreationAddtionWizard wizard = (ISmooksDataCreationAddtionWizard) rootWizard;
		if (sourceID == null) {
			wizard.clearSourceWizardPages();
		}
		if (targetID == null) {
			wizard.clearTargetWizardPages();
		}
		if (sourceID != null && (!sourceID.equals(oldSourceID))) {
			sourceWizard = ViewerInitorStore.getInstance()
					.getStructuredDataCreationWizard(sourceID);
			oldSourceID = sourceID;
		}
		if (targetID != null && (!targetID.equals(oldTargetID))) {
			targetWizard = ViewerInitorStore.getInstance()
					.getStructuredDataCreationWizard(targetID);
			oldTargetID = targetID;
		}
		if (!wizardIsCreated(sourceWizard) && sourceWizard != null) {
			if (sourceWizard instanceof INewWizard) {
				((INewWizard) sourceWizard).init(null, selection);
			}
			sourceWizard.addPages();
		}
		if (!wizardIsCreated(targetWizard) && targetWizard != null) {
			if (targetWizard instanceof INewWizard) {
				((INewWizard) targetWizard).init(null, selection);
			}
			targetWizard.addPages();
		}
		addDataCreationPages(sourceWizard, targetWizard);
		return super.getNextPage();
	}

	private void addDataCreationPages(IWizard sourceWizard, IWizard targetWizard) {
		IWizard rootWizard = getWizard();
		if (rootWizard instanceof ISmooksDataCreationAddtionWizard) {
			ISmooksDataCreationAddtionWizard wizard = (ISmooksDataCreationAddtionWizard) rootWizard;
			if (sourceWizard != null) {
				wizard.clearSourceWizardPages();
				IWizardPage[] pages = sourceWizard.getPages();
				for (int i = 0; i < pages.length; i++) {
					IWizardPage p = pages[i];
					wizard.addSourceWizardPage(p);
					p.setWizard(rootWizard);
				}
				sourceDataCreationWizard = sourceWizard;
			}

			if (targetWizard != null) {
				wizard.clearTargetWizardPages();
				IWizardPage[] pages = targetWizard.getPages();
				for (int i = 0; i < pages.length; i++) {
					IWizardPage p = pages[i];
					wizard.addTargetWizardPage(p);
					p.setWizard(rootWizard);
				}
				targetDataCreationWizard = targetWizard;
			}
		}
	}

	private boolean wizardIsCreated(IWizard wizard) {
		if (wizard == null)
			return false;
		if (wizard.getPageCount() <= 0) {
			return false;
		}
		return true;
	}
	
	private void updateWizardPageStates(){
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);
		
		// MODIFY by Dart 2008.12.19
		mainComposite.setLayout(new GridLayout());
		
		Button j2jButton = new Button(mainComposite,SWT.RADIO);
		j2jButton.setText("Java-to-Java");
		j2jButton.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				setPageComplete(true);
				setSourceID("org.jboss.tools.smooks.ui.viewerInitor.javabean");
				setTargetID("org.jboss.tools.smooks.ui.viewerInitor.javabean");
				getContainer().updateButtons();
			}
			
		});
		
		Button x2jButton = new Button(mainComposite,SWT.RADIO);
		x2jButton.setText("XML-to-Java");
		x2jButton.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				setPageComplete(true);
				setSourceID("org.jboss.tools.smooks.xml.viewerInitor.xml");
				setTargetID("org.jboss.tools.smooks.ui.viewerInitor.javabean");
				getContainer().updateButtons();
			}
			
		});
		setPageComplete(false);
		
//		GridLayout gl = new GridLayout();
//		gl.numColumns = 2;
//		gl.makeColumnsEqualWidth = true;
//		mainComposite.setLayout(gl);
//		sourceList = AnalyzerFactory.getInstance().getRegistrySourceIDList();
//		Label sl = new Label(mainComposite, SWT.NONE);
//		sl.setText(Messages
//				.getString("TypeIDSelectionWizardPage.SourceTypeIDTitle")); //$NON-NLS-1$
//
//		Label tl = new Label(mainComposite, SWT.NONE);
//		tl.setText(Messages
//				.getString("TypeIDSelectionWizardPage.TargetTypeIDTitle")); //$NON-NLS-1$
//
//		Composite sourceBorder = new Composite(mainComposite, SWT.NONE);
//		sourceBorder.setBackground(ColorConstants.black);
//		FillLayout sbLayout = new FillLayout();
//		sbLayout.marginHeight = 1;
//		sbLayout.marginWidth = 1;
//		sourceBorder.setLayout(sbLayout);
//		source = createTableViewer(sourceBorder);
//		GridData gd = new GridData(GridData.FILL_BOTH);
//		sourceBorder.setLayoutData(gd);
//
//		Composite targetBorder = new Composite(mainComposite, SWT.NONE);
//		targetBorder.setBackground(ColorConstants.black);
//		FillLayout tbLayout = new FillLayout();
//		tbLayout.marginHeight = 1;
//		tbLayout.marginWidth = 1;
//		targetBorder.setLayout(tbLayout);
//		target = createTableViewer(targetBorder);
//		targetBorder.setLayoutData(gd);
//
//		source.setInput(sourceList);
//		target.setInput(sourceList);
//		initViewer();
//
//		sourceDataLink = new Hyperlink(mainComposite, SWT.NONE);
//		sourceDataLink.setText("Source Model Select:Empty"); //$NON-NLS-1$
//		sourceDataLink.addHyperlinkListener(new IHyperlinkListener() {
//
//			public void linkActivated(HyperlinkEvent e) {
//				openSourceWizard();
//			}
//
//			public void linkEntered(HyperlinkEvent e) {
//
//			}
//
//			public void linkExited(HyperlinkEvent e) {
//
//			}
//
//		});
//		// TODO don't show this
//		sourceDataLink.setVisible(false);
//		targetDataLink = new Hyperlink(mainComposite, SWT.NONE);
//		targetDataLink.setText("Target Model Select:Empty"); //$NON-NLS-1$
//		targetDataLink.addHyperlinkListener(new IHyperlinkListener() {
//
//			public void linkActivated(HyperlinkEvent e) {
//				openTargetWizard();
//			}
//
//			public void linkEntered(HyperlinkEvent e) {
//
//			}
//
//			public void linkExited(HyperlinkEvent e) {
//
//			}
//
//		});
//		// TODO don't show this
//		targetDataLink.setVisible(false);

		this.setControl(mainComposite);
	}

	protected void openTargetWizard() {
		targetTreeViewerInputContents = getReturnObjectFromWizard(getTargetID());
		resetLinkText();
	}

	protected IWizard getWizardViaDataID(String dataID) {
		if (dataID == null)
			return null;
		IWizardNode wn = getSourceWizard(dataID);
		// setSelectedNode(wn);
		IWizard sw = wn.getWizard();
		if (sw instanceof IStructuredDataCreationWizard) {
			// ((IStrucutredDataCreationWizard)sw).i
		}
		if (sw instanceof INewWizard) {
			((INewWizard) sw).init(null, this.getSelection());
		}
		return sw;
	}

	protected Object getReturnObjectFromWizard(String dataID) {
		IWizard wizard = getWizardViaDataID(dataID);
		if (wizard != null) {
			StructuredDataCreationWizardDailog dialog = new StructuredDataCreationWizardDailog(
					getShell(), wizard);
			if (dialog.open() == Dialog.OK) {
				return dialog.getCurrentCreationWizard()
						.getTreeViewerInputContents();
			}
		} else {
			MessageDialog
					.openInformation(getShell(),
							"Info", //$NON-NLS-1$
							Messages
									.getString("TypeIDSelectionWizardPage.WarningMessage")); //$NON-NLS-1$
		}

		return null;
	}

	protected void openSourceWizard() {
		sourceTreeViewerInputContents = getReturnObjectFromWizard(getSourceID());
		// resetLinkText();
	}

	/**
	 * @deprecated
	 */
	private void resetLinkText() {
		// if (sourceTreeViewerInputContents != null) {
		// sourceDataLink.setText("Source Model Select");
		// }
		//
		// if (targetTreeViewerInputContents != null) {
		// targetDataLink.setText("Target Model Select");
		// }
	}

	protected String getDataTypeID(CheckboxTableViewer viewer) {
		Object[] objs = viewer.getCheckedElements();
		if (objs == null)
			return null;
		if (objs.length <= 0)
			return null;
		DataTypeID d = (DataTypeID) objs[0];
		return d.getId();
	}

	protected void initViewer() {
		IWizard wizard = this.getWizard();
		if (wizard instanceof TypeIDSelectionWizard) {
			String s = ((TypeIDSelectionWizard) wizard).getSourceDataTypeID();
			String t = ((TypeIDSelectionWizard) wizard).getTargetDataTypeID();
			this.setSourceID(s);
			this.setTargetID(t);
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
				IStructuredDataCreationWizard wizard = viewerInitor
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
					setSourceID(sourceID);
				}

				if (viewer == target) {
					String targetID = getDataTypeID(target);
					setTargetID(targetID);
				}
				IWizardContainer container = getContainer();
				if (container != null) {
					container.updateButtons();
				}
			}

		});
		TableColumn nameColumn = new TableColumn(viewer.getTable(), SWT.NONE);
		nameColumn.setWidth(250);
		nameColumn.setText(Messages
				.getString("TypeIDSelectionWizardPage.NameColumn")); //$NON-NLS-1$
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
			return ""; //$NON-NLS-1$
		}

	}

	public Object getSourceTreeViewerInputContents() {
		return sourceTreeViewerInputContents;
	}

	public void setSourceTreeViewerInputContents(
			Object sourceTreeViewerInputContents) {
		this.sourceTreeViewerInputContents = sourceTreeViewerInputContents;
	}

	public Object getTargetTreeViewerInputContents() {
		return targetTreeViewerInputContents;
	}

	public void setTargetTreeViewerInputContents(
			Object targetTreeViewerInputContents) {
		this.targetTreeViewerInputContents = targetTreeViewerInputContents;
	}

	public IWizard getSourceDataCreationWizard() {
		return sourceDataCreationWizard;
	}

	public IWizard getTargetDataCreationWizard() {
		return targetDataCreationWizard;
	}
}
