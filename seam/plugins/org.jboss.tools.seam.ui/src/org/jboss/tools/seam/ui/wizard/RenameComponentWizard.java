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
package org.jboss.tools.seam.ui.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.ui.IValidator;
import org.jboss.tools.common.ui.widget.editor.CompositeEditor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditorFactory;
import org.jboss.tools.seam.core.ISeamComponent;
import org.jboss.tools.seam.core.ISeamProject;
import org.jboss.tools.seam.core.SeamCorePlugin;
import org.jboss.tools.seam.core.SeamCoreMessages;
import org.jboss.tools.seam.internal.core.project.facet.SeamValidatorFactory;
import org.jboss.tools.seam.internal.core.refactoring.RenameComponentProcessor;

/**
 * @author Alexey Kazakov, Daniel Azarov
 */
public class RenameComponentWizard extends RefactoringWizard {

	private ISeamComponent component;
	private String componentName;
	private IFieldEditor editor;
	private ISeamProject seamProject;

	public RenameComponentWizard(Refactoring refactoring, ISeamComponent component) {
		super(refactoring, WIZARD_BASED_USER_INTERFACE);
		this.component = component;
		if(component != null)
			seamProject = SeamCorePlugin.getSeamProject((IProject)component.getResource(), true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.ui.refactoring.RefactoringWizard#addUserInputPages()
	 */
	@Override
	protected void addUserInputPages() {
	    setDefaultPageTitle(getRefactoring().getName());
	    RenameComponentProcessor processor= (RenameComponentProcessor) getRefactoring().getAdapter(RenameComponentProcessor.class);
	    addPage(new RenameComponentWizardPage(processor));
	}
	
	class RenameComponentWizardPage extends UserInputWizardPage{
		private RenameComponentProcessor processor;
		
		public RenameComponentWizardPage(RenameComponentProcessor processor){
			super("");
			this.processor = processor;
		}

		public void createControl(Composite parent) {
			Composite container = new Composite(parent, SWT.NULL);
			container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	        GridLayout layout = new GridLayout();
	        container.setLayout(layout);
	        layout.numColumns = 2;
	        
	        String defaultName = component.getName();
	        editor = IFieldEditorFactory.INSTANCE.createTextEditor(componentName, SeamCoreMessages.SEAM_WIZARD_FACTORY_SEAM_COMPONENT_NAME, defaultName);
	        editor.doFillIntoGrid(container);
	        
	        ((CompositeEditor)editor).addPropertyChangeListener(new PropertyChangeListener(){
	        	public void propertyChange(PropertyChangeEvent evt){
	        		validatePage();
	        	}
	        });
	        setControl(container);
	        setPageComplete(false);
		}
		
		protected final void validatePage() {
			Map<String, IStatus> errors = SeamValidatorFactory.SEAM_COMPONENT_NAME_VALIDATOR.validate(editor.getValueAsString(), seamProject);
			if(!errors.isEmpty()) {
				setErrorMessage(NLS.bind(errors.get(IValidator.DEFAULT_ERROR).getMessage(),SeamCoreMessages.SEAM_BASE_WIZARD_PAGE_SEAM_COMPONENTS));
				setPageComplete(false);
				return;
			}
			RefactoringStatus status= new RefactoringStatus();
			setPageComplete(status);
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.ltk.ui.refactoring.UserInputWizardPage#performFinish()
		 */
		protected boolean performFinish() {
			
			initializeRefactoring();
			return super.performFinish();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ltk.ui.refactoring.UserInputWizardPage#getNextPage()
		 */
		public IWizardPage getNextPage() {
			initializeRefactoring();
			return super.getNextPage();
		}
		
		private void initializeRefactoring() {
			processor.setNewName(editor.getValueAsString());
		}
		
	}
}