/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial implementation as prop page heirarchy
 * rfrost@bea.com - conversion to single property page impl
 *******************************************************************************/

package org.jboss.ide.eclipse.as.wtp.override.ui.propertypage;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.jboss.ide.eclipse.as.wtp.override.ui.Messages;
import org.jboss.ide.eclipse.as.wtp.override.ui.propertypage.impl.EarModuleDependenciesPropertyPage;


/*
 * The only change in this file between here and upstream is 
 * the method createEARContent
 * 
 * We'd obviously prefer to remove the extension of the superclass here
 * but elements in the web ui demand we be a part of that tree for right now
 * Also we'd switch to depending on IModuleDependenciesControl (local)
 */

/**
 * Primary project property page for J2EE dependencies; content is dynamically 
 * generated based on the project facets and will be comprised by a
 * set of IJ2EEDependenciesControl implementations.
 * 
 */
public class J2EEDependenciesPage extends PropertyPage {
	
	public String DESCRIPTION = Messages.J2EEDependenciesPage_Description;

	private IProject project;
	private IModuleDependenciesControl[] controls = new IModuleDependenciesControl[0];
	
	public J2EEDependenciesPage() {
		super();
	}
	
	private Composite getFacetErrorComposite(final Composite parent) {
		final String errorCheckingFacet = Messages.J2EEDependenciesPage_ErrorCheckingFacets;
		setErrorMessage(errorCheckingFacet);
		setValid(false);
		return getErrorComposite(parent, errorCheckingFacet);		
	}
	
	private Composite getErrorComposite(final Composite parent, final String error) {
		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginWidth = 0;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		final Label label= new Label(composite, SWT.NONE);
		label.setText(error);
		return composite;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		for (int i = 0; i < controls.length; i++) {
			if (controls[i] != null) {
				if (!controls[i].performOk()) {
					return false;
				}
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	public void performDefaults() {
		for (int i = 0; i < controls.length; i++) {
			if (controls[i] != null) {
				controls[i].performDefaults();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePage#performCancel()
	 */
	public boolean performCancel() {
		for (int i = 0; i < controls.length; i++) {
			if (controls[i] != null) {
				if (!controls[i].performCancel()) {
					return false;
				}
			}
		}
		return super.performCancel();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		for (int i = 0; i < controls.length; i++) {
			if (controls[i] != null) {
				controls[i].setVisible(visible);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#dispose()
	 */
	public void dispose() {
		super.dispose();
		for (int i = 0; i < controls.length; i++) {
			if(controls[i] != null){
				controls[i].dispose();
			}
		}
	}

	protected static void createDescriptionComposite(final Composite parent, final String description) {
		Composite descriptionComp = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		descriptionComp.setLayout(layout);
		descriptionComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fillDescription(descriptionComp, description);
	}
	
	private static void fillDescription(Composite c, String s) {
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 250;
		Text text = new Text(c, SWT.READ_ONLY | SWT.WRAP);
		text.setLayoutData(data);
		text.setText(s);
	}
	
	protected Control createContents(Composite parent) {
		
		// Need to find out what type of project we are handling
		project = (IProject) getElement().getAdapter(IResource.class);
		if( project != null ) {
			try {
				IFacetedProject facetedProject = ProjectFacetsManager.create(project); 
				IDependencyPageProvider provider = null;
				if( facetedProject == null )
					return getFacetErrorComposite(parent);
				
				provider = DependencyPageExtensionManager.getManager().getProvider(facetedProject);
				if( provider != null ) {
					controls = provider.createPages(facetedProject, this);
					return provider.createRootControl(controls, parent);
				}
				AddModuleDependenciesPropertiesPage page = new AddModuleDependenciesPropertiesPage(project, this);
				controls = new IModuleDependenciesControl[1];
				controls[0] = page;
				return page.createContents(parent);
			} catch( CoreException ce )	{
			}
		}
		return getFacetErrorComposite(parent);
	}

	/*
	 * This can maybe be replaced with an extension point in the future
	 * This is the area that needs to be extended. 
	 */
	protected Control createContents2(Composite parent, IFacetedProject facetedProject) {
//		boolean isEAR = facetedProject.hasProjectFacet(ProjectFacetsManager.getProjectFacet(IModuleConstants.JST_EAR_MODULE)); 
//		boolean isWEB = facetedProject.hasProjectFacet(ProjectFacetsManager.getProjectFacet(IModuleConstants.JST_WEB_MODULE));
//		if (isEAR) {
//			return createEARContent(parent);
//		} else if (isWEB) {
//			return createWebContent(parent);
//		}
		return null;
	}
	
	
	/*
	 * This method is different from upstream
	 */
	private Composite createEARContent(final Composite parent) {
		//AddModuleDependenciesPropertiesPage page = new AddModuleDependenciesPropertiesPage(project, this);
		EarModuleDependenciesPropertyPage page = new EarModuleDependenciesPropertyPage(project, this);
		controls = new IModuleDependenciesControl[1];
		controls[0] = page;
		return page.createContents(parent);
	}
	
//	private Composite createWebContent(final Composite parent) {
//		final boolean standalone = J2EEProjectUtilities.isStandaloneProject(project);
//		
//		if (standalone) {
//			// only need to create the Web Libraries page
//			controls = new IJ2EEDependenciesControl[1];
//			controls[0] = new WebLibDependencyPropertiesPage(project, this);
//			return controls[0].createContents(parent);
//		} else {
//			// Create a tabbed folder with both "J2EE Modules" and "Web Libraries"
//			final TabFolder folder = new TabFolder(parent, SWT.LEFT);
//			folder.setLayoutData(new GridData(GridData.FILL_BOTH));
//			folder.setFont(parent.getFont());
//
//			// Create the two tabs 
//			controls = new IJ2EEDependenciesControl[2];
//		
//			controls[0] = new JARDependencyPropertiesPage(project, this);
//			TabItem tab = new TabItem(folder, SWT.NONE);
//			tab.setControl(controls[0].createContents(folder));
//			tab.setText(ManifestUIResourceHandler.J2EE_Modules);
//			controls[1] = new WebLibDependencyPropertiesPage(project, this);		
//			tab = new TabItem(folder, SWT.NONE);
//			tab.setControl(controls[1].createContents(folder));
//			tab.setText(ManifestUIResourceHandler.Web_Libraries);
//		
//			folder.setSelection(0);
//			return folder;
//		}
//	}
//	
//	private Composite createNonEARContent(final Composite parent) {
//		controls = new IJ2EEDependenciesControl[1];
//		final boolean standalone = J2EEProjectUtilities.isStandaloneProject(project);
//		if (standalone) {
//			// if not referenced by an EAR, check if referenced by a dynamic web project
//			if (J2EEProjectUtilities.getReferencingWebProjects(project).length > 0) {
//				controls[0] = new WebRefDependencyPropertiesPage(project, this);
//			} else {
//				return getUnreferencedErrorComposite(parent);
//			}
//		} else { 
//			controls[0] = new JARDependencyPropertiesPage(project, this);			
//		}
//
//		return controls[0].createContents(parent);
//	}
//	
}
