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

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Dart Peng<br>
 * Date : Sep 18, 2008
 */
public class NewResourceConfigWizardPage extends WizardPage implements ISelectionChangedListener{

	private NewResourceConfigKey selectedKey = null;
	
	
	public NewResourceConfigWizardPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, Messages.getString("NewResourceConfigWizardPage.NewConfigWizardPageTitle"), titleImage); //$NON-NLS-1$
		setDescription(Messages.getString("NewResourceConfigWizardPage.NewConfigWizardPageDescription")); //$NON-NLS-1$
	}

	public NewResourceConfigWizardPage(String pageName) {
		super(pageName);
		setTitle(Messages.getString("NewResourceConfigWizardPage.NewConfigWizardPageTitle")); //$NON-NLS-1$
		setDescription(Messages.getString("NewResourceConfigWizardPage.NewConfigWizardPageDescription")); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite mainComposite = new Composite(parent,SWT.NONE);
		FillLayout layout = new FillLayout();
//		layout.marginHeight = 350;
		mainComposite.setLayout(layout);
		TableViewer viewer = new TableViewer(mainComposite);
		viewer.setContentProvider(new NewResourceConfigKeyContentProvider());
		viewer.setLabelProvider(new NewResourceConfigKeyLabelProvider());
		viewer.setInput(NewResourceConfigFactory.getInstance().getAllIDs());
		viewer.addSelectionChangedListener(this);
		setControl(mainComposite);
		
		validatePageFinish();
	}
	
	protected void validatePageFinish(){
		String error = null;
		if(selectedKey == null){
			error = Messages.getString("NewResourceConfigWizardPage.NewConfigWizardPageErrorMessage1"); //$NON-NLS-1$
		}
		
		setErrorMessage(error);
		setPageComplete(error == null);
	}
	
	private class NewResourceConfigKeyContentProvider implements IStructuredContentProvider{

		public Object[] getElements(Object inputElement) {
			if(inputElement instanceof List){
				return ((List)inputElement).toArray();
			}
			if(inputElement.getClass().isArray()){
				return (Object[])inputElement;
			}
			return new Object[]{};
		}

		public void dispose() {
			
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			
		}
		
	}
	
	private class NewResourceConfigKeyLabelProvider extends LabelProvider{

		public Image getImage(Object element) {
			if(element instanceof NewResourceConfigKey){
				return ((NewResourceConfigKey)element).getImage();
			}
			return super.getImage(element);
		}

		public String getText(Object element) {
			if(element instanceof NewResourceConfigKey){
				return ((NewResourceConfigKey)element).getName();
			}
			return super.getText(element);
		}
		
	}

	public void selectionChanged(SelectionChangedEvent event) {
		Object obj = ((IStructuredSelection)event.getSelection()).getFirstElement();
		if(obj != null && obj instanceof NewResourceConfigKey){
			selectedKey = (NewResourceConfigKey)obj;
		}
		
		validatePageFinish();
	}

	public NewResourceConfigKey getSelectedKey() {
		return selectedKey;
	}

}
