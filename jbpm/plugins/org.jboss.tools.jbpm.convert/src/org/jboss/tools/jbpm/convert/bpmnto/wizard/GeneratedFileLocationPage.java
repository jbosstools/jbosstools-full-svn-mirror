/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.jbpm.convert.bpmnto.wizard;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.jbpm.convert.b2j.messages.B2JMessages;

/**
 * @author Grid Qian
 * 
 * the wizardpage for the generated file location
 */
public class GeneratedFileLocationPage extends WizardPage {

	private TreeViewer viewer;
	private ISelection currentSelection;
	private Button button;
	private IWizard wizard;
	private boolean isOverWrite = true;


	protected GeneratedFileLocationPage(String pageName, String title,
			String description) {
		super(pageName);
		this.setDescription(description);
		this.setTitle(title);
	}

	public void createControl(Composite parent) {
		Composite composite = createDialogArea(parent);

		createListTitleArea(composite);
		createListViewer(composite);
		createCheckbox(composite);
		super.setControl(composite);

		initializePage();

	}

	private Label createListTitleArea(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label
				.setText(B2JMessages.Bpmn_GeneratedFile_Location_WizardPage_ViewerTitle);
		label.setFont(parent.getFont());
		return label;
	}

	private void createListViewer(Composite composite) {
		viewer = new TreeViewer(composite, SWT.BORDER | SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.setLabelProvider(new WorkbenchLabelProvider());
		WorkbenchContentProvider cp = new WorkbenchContentProvider();
		viewer.setContentProvider(cp);
		viewer.setFilters(new ViewerFilter[] { new ProFilter() });
		viewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				updateControls();
				currentSelection = viewer.getSelection();
				((BpmnToWizard) wizard)
						.setTargetLocationSelection((IStructuredSelection) currentSelection);

			}
		});
	}

	private Button createCheckbox(Composite parent) {
		button = new Button(parent, SWT.CHECK | SWT.NONE);
		button
				.setText(B2JMessages.Bpmn_GeneratedFile_Location_WizardPage_CheckBox);
		button.setFont(parent.getFont());
		button.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent arg0) {		
			}
			public void widgetSelected(SelectionEvent arg0) {
				isOverWrite = button.getSelection();
			}});
		return button;
	}

	private void initializePage() {
		wizard = this.getWizard();
		viewer.setInput(ResourcesPlugin.getWorkspace());
		if (this.currentSelection != null) {
			if (currentSelection != null
					&& currentSelection instanceof ITreeSelection) {
				// Select the parent project of this first bpmn file chosen
				ITreeSelection node = (ITreeSelection) currentSelection;
				TreePath[] paths = node.getPaths();
				if(paths.length == 0) {
					return;
				}
				TreePath projPath = new TreePath(new Object[] { paths[0]
						.getFirstSegment() });
				TreeSelection projSel = new TreeSelection(projPath);
				viewer.setSelection(projSel, true);
			}
		}
		button.setSelection(true);
	}

	private void updateControls() {
		super.getWizard().getContainer().updateButtons();
	}

	private Composite createDialogArea(Composite parent) {
		// create a composite with standard margins and spacing
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 7;
		layout.marginWidth = 7;
		layout.verticalSpacing = 4;
		layout.horizontalSpacing = 4;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		return composite;
	}

	public boolean isPageComplete() {
		if (viewer != null && viewer.getSelection() == null) {
			return false;
		}
		return true;
	}

	public ISelection getSelection() {
		return currentSelection;
	}

	public void setSelection(ISelection currentSelection) {
		this.currentSelection = currentSelection;
	}
	

	public boolean isOverWrite() {
		return isOverWrite;
	}

	public void setOverWrite(boolean isOverWrite) {
		this.isOverWrite = isOverWrite;
	}

}

class ProFilter extends ViewerFilter {
	@Override
	public boolean select(Viewer viewer, Object parent, Object element) {
		if (element instanceof IContainer) {
			return ((IContainer)element).getProject().isAccessible();
		} else {
			return false;
		}
	}
}
