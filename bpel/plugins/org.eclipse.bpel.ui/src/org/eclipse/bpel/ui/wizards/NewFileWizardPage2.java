/*******************************************************************************
 * Copyright (c) 2006 Oracle Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Oracle Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpel.ui.wizards;

import org.eclipse.bpel.ui.BPELUIPlugin;
import org.eclipse.bpel.ui.IBPELUIConstants;
import org.eclipse.bpel.ui.util.filedialog.FileSelectionGroup;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wst.common.componentcore.ModuleCoreNature;

/**
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date Jul 20, 2006
 *
 */
public class NewFileWizardPage2 extends WizardPage {

  private FileSelectionGroup fResourceComposite;

/**
 * New File Wizard, page 2.
 * 
 * @param pageName
 */
  
public NewFileWizardPage2(String pageName) 
    {
        super(pageName);
        setPageComplete(false);
        
		setTitle(Messages.NewFileWizardPage2_3);
		setDescription(Messages.NewFileWizardPage2_0);
		
        setImageDescriptor( BPELUIPlugin.INSTANCE.getImageDescriptor( IBPELUIConstants.ICON_WIZARD_BANNER ));
    }
  
	/** (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl (Composite parent) {
		
		final NewFileWizard wiz = (NewFileWizard) getWizard();
		Composite composite = new Composite(parent, SWT.NULL);

		initializeDialogUnits(parent);

		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		// Start Resource Variant
		fResourceComposite = new FileSelectionGroup(composite,
			new Listener() {
				public void handleEvent(Event event) {
					IResource resource = fResourceComposite.getSelectedResource();
					setPageComplete(resource != null && resource instanceof IContainer);
					// https://issues.jboss.org/browse/JBIDE-8591
					if (!ModuleCoreNature.isFlexibleProject(resource.getProject()))
						setMessage(Messages.NewFileWizard_Not_A_Faceted_Project, WizardPage.WARNING);
					else
						setMessage(null);
				
					if (resource instanceof IContainer)
						wiz.setBPELContainer((IContainer)resource);
				}
			},
			Messages.NewFileWizardPage2_1,
			Messages.NewFileWizardPage2_2);
		// https://issues.jboss.org/browse/JBIDE-8591
		// update wizard so first page gets the new resource location
		fResourceComposite.setSelectedResource(wiz.getBPELContainer());
		setControl(composite);
	}
	
	
	/**
	 * Return the selected resource container for the BPEL project.
	 * @return the resource container.
	 */
	
	public IContainer getResourceContainer () {
		IResource resource = fResourceComposite.getSelectedResource();
		if (resource != null && resource instanceof IContainer) {
			return (IContainer) resource;
		}
		return null;		
	}	
}
