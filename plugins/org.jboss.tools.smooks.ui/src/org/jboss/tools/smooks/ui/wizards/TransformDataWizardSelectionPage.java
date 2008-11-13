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
package org.jboss.tools.smooks.ui.wizards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
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
import org.jboss.tools.smooks.ui.IStructuredDataCreationWizard;
import org.jboss.tools.smooks.ui.IViewerInitor;
import org.jboss.tools.smooks.ui.ViewerInitorStore;

/**
 * @author Dart Peng
 * @Date Jul 30, 2008
 */
public class TransformDataWizardSelectionPage extends WizardSelectionPage {

	List registedWizard = new ArrayList();
	TreeViewer viewer = null;
	private Label desLabel;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		// parent.setLayout(new FillLayout());
		Composite main = new Composite(parent, SWT.NONE);

		GridLayout gridLayout = new GridLayout();
		main.setLayout(gridLayout);

		viewer = new TreeViewer(main, SWT.NONE);
		viewer.setContentProvider(new WizardNodeContentProvider());
		viewer.setLabelProvider(new WizardNodeLabelProvider());
		GridData gd = new GridData(GridData.FILL_BOTH);
		viewer.getTree().setLayoutData(gd);
		createAllExtentionWizard();

		desLabel = new Label(main, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		desLabel.setLayoutData(gd);

		viewer.setInput(registedWizard);
		if (!registedWizard.isEmpty()) {
			this.setSelectedNode((IWizardNode) registedWizard.get(0));
		}
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				IWizardNode node = (IWizardNode) selection.getFirstElement();
				desLabel.setText(""); //$NON-NLS-1$
				if (node != null) {
					setSelectedNode(node);
					if(node instanceof TransformSelectWizardNode){
						String des = ((TransformSelectWizardNode)node).getDescription();
						desLabel.setText(des);
					}
					IStructuredDataCreationWizard wizard = (IStructuredDataCreationWizard) node
							.getWizard();
					TransformDataSelectionWizard pw = (TransformDataSelectionWizard) getWizard();
					wizard.init(pw.getSite(), pw.getInput());
				}

			}

		});
		viewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				getContainer().showPage(getNextPage());
			}

		});

		// for (Iterator iterator = registedWizard.iterator();
		// iterator.hasNext();) {
		// IWizardNode node = (IWizardNode) iterator.next();
		// node.
		// }
		this.setControl(main);
	}

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
			wizard.addPages();
		}
		return wizard.getStartingPage();
	}

	protected void createAllExtentionWizard() {
		Collection<IViewerInitor> viewers = ViewerInitorStore.getInstance()
				.getViewerInitorCollection();
		if (viewer == null)
			return;
		for (Iterator iterator = viewers.iterator(); iterator.hasNext();) {
			IViewerInitor viewerInitor = (IViewerInitor) iterator.next();
			TransformSelectWizardNode wn = new TransformSelectWizardNode();
			IStructuredDataCreationWizard wizard = viewerInitor
					.getStructuredDataLoadWizard();
			if (wizard == null)
				continue;
			wn.setWizard(wizard);
			wn.setName(viewerInitor.getName());
			wn.setIconPath(viewerInitor.getWizardIconPath());
			wn.setDescription(viewerInitor.getDescription());
			this.registedWizard.add(wn);
		}
	}

	public TransformDataWizardSelectionPage(String pageName) {
		super(pageName);

		setDescription(Messages.getString("TransformDataWizardSelectionPage.TransformDataWizardDescription")); //$NON-NLS-1$
		setTitle(Messages.getString("TransformDataWizardSelectionPage.TransformDataWizardTitle")); //$NON-NLS-1$

	}

	public void activeSelectionWizard() {

	}

	private class WizardNodeLabelProvider extends LabelProvider {

		private HashMap map = new HashMap();

		@Override
		public Image getImage(Object element) {
			if (element instanceof TransformSelectWizardNode) {
				String path = ((TransformSelectWizardNode) element)
						.getIconPath();
				if (path != null) {
					Image icon = (Image) map.get(path);
					if (icon == null) {

					}
				}
			}
			return super.getImage(element);
		}

		@Override
		public String getText(Object element) {
			if (element instanceof TransformSelectWizardNode) {
				return ((TransformSelectWizardNode) element).getName();
			}
			return super.getText(element);
		}

		@Override
		public void dispose() {
			super.dispose();
		}
	}

	private class WizardNodeContentProvider implements ITreeContentProvider {

		public Object[] getChildren(Object parentElement) {
			// TODO Auto-generated method stub
			return new Object[] {};
		}

		public Object getParent(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean hasChildren(Object element) {
			// TODO Auto-generated method stub
			return false;
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			}
			return new Object[] {};
		}

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

	}

}
