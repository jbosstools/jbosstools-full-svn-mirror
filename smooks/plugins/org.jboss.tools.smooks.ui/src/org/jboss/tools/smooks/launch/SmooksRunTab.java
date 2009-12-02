/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and others contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2005-2006, JBoss Inc.
 */
 package org.jboss.tools.smooks.launch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.FileEditorInput;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;

import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.jboss.tools.smooks.configuration.RuntimeMetadata;
import org.jboss.tools.smooks.editor.AbstractSmooksFormEditor;

/**
 * Smooks Run Tab.
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class SmooksRunTab extends AbstractLaunchConfigurationTab {
	
	// Project UI widgets
	private Text fProjText;
	private Button fProjButton;
	private Text fConfigurationText;
	private Button fSearchButton;
	private RuntimeMetadata launchMetaData = new RuntimeMetadata();
	
	private ILaunchConfiguration fLaunchConfiguration;

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {		
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);

		GridLayout topLayout = new GridLayout();
		topLayout.numColumns= 3;
		comp.setLayout(topLayout);		
		
		createFormElements(comp);
		
		Dialog.applyDialogFont(comp);
		validatePage();
	}

	private void createSpacer(Composite comp) {
		Label label= new Label(comp, SWT.NONE);
		GridData gd= new GridData();
		gd.horizontalSpan= 3;
		label.setLayoutData(gd);
	}
	
	private void createFormElements(Composite comp) {
		GridData gd = new GridData();
		
		Label fProjLabel = new Label(comp, SWT.NONE);
		gd.horizontalIndent = 10;
		fProjLabel.setText("Project:"); 
		fProjLabel.setLayoutData(gd);
		
		fProjText= new Text(comp, SWT.SINGLE | SWT.BORDER);
		fProjText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fProjText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				validatePage();
				updateLaunchConfigurationDialog();				
				fSearchButton.setEnabled(fProjText.getText().length() > 0);
			}
		});
			
		fProjButton = new Button(comp, SWT.PUSH);
		fProjButton.setText("Browse"); 
		fProjButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handleProjectButtonSelected();
			}
		});
		setButtonGridData(fProjButton);
		
		Label fConfigLabel = new Label(comp, SWT.NONE);
		gd = new GridData();
		gd.horizontalIndent = 10;
		fConfigLabel.setLayoutData(gd);
		fConfigLabel.setText("Configuration:"); 
		
	
		fConfigurationText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		fConfigurationText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fConfigurationText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				handleSmooksConfigChange();
				validatePage();
				updateLaunchConfigurationDialog();
			}
		});
		
		fSearchButton = new Button(comp, SWT.PUSH);
		fSearchButton.setEnabled(fProjText.getText().length() > 0);
		fSearchButton.setText("Search"); 
		fSearchButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handleSearchButtonSelected();
			}
		});
		setButtonGridData(fSearchButton);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy launchConfigWC) {
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();		
		
		launchConfigWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""); //$NON-NLS-1$
		launchConfigWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, ""); //$NON-NLS-1$

		if(activePage != null) {
			IEditorPart editor = activePage.getActiveEditor();
			
			if(editor instanceof AbstractSmooksFormEditor) {
				IEditorInput editorInput = editor.getEditorInput();

				if(editorInput instanceof FileEditorInput) {
					FileEditorInput fileEI = (FileEditorInput) editorInput;					
					IFile file = fileEI.getFile();
					
					launchConfigWC.rename("[" + file.getProject().getName() + "]" + editor.getTitle());
					launchConfigWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, file.getProject().getName()); //$NON-NLS-1$
					launchConfigWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, file.getProjectRelativePath().toString()); //$NON-NLS-1$
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration config) {
		fLaunchConfiguration = config;
		try {
			fProjText.setText(config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""));  //$NON-NLS-1$
			fConfigurationText.setText(config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, ""));  //$NON-NLS-1$
		} catch (CoreException ce) {
		}
		IResource smooksConfig = getSmooksConfig();
		if(smooksConfig != null) {
			launchMetaData.setSmooksConfig(smooksConfig);
		}
		validatePage();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy launchConfigWC) {
		launchConfigWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, fProjText.getText());
		launchConfigWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, fConfigurationText.getText());
	}

	/*
	 * Show a dialog that lists all main types
	 */
	private void handleSearchButtonSelected() {
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(fProjText.getText());

		if(assertProjectOK(project)) {			
			dialog.setInput(project);
		} else {
			dialog.setInput(root);			
		}

		dialog.setTitle("Select Smooks Configuration File...");
		if (dialog.open() == Window.CANCEL) {
			return;
		}
		
		Object[] results = dialog.getResult();
		if ((results == null) || (results.length < 1)) {
			return;
		}		

		IResource resource = (IResource)results[0];
		fConfigurationText.setText(resource.getProjectRelativePath().toString());
	}
	
	private void handleSmooksConfigChange() {
		launchMetaData.setSmooksConfig(getSmooksConfig());
	}
		
	/*
	 * Show a dialog that lets the user select a project.  This in turn provides
	 * context for the main type, allowing the user to key a main type name, or
	 * constraining the search for main types to the specified project.
	 */
	private void handleProjectButtonSelected() {
		IJavaProject project = chooseJavaProject();
		if (project == null) {
			return;
		}
		
		String projectName = project.getElementName();
		fProjText.setText(projectName);		
	}
	
	/*
	 * Realize a Java Project selection dialog and return the first selected project,
	 * or null if there was none.
	 */
	private IJavaProject chooseJavaProject() {
		IJavaProject[] projects;
		try {
			projects= JavaCore.create(getWorkspaceRoot()).getJavaProjects();
		} catch (JavaModelException e) {
			projects= new IJavaProject[0];
		}
		
		ILabelProvider labelProvider= new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT);
		ElementListSelectionDialog dialog= new ElementListSelectionDialog(getShell(), labelProvider);
		dialog.setTitle("Select Smooks Configuration"); 
		dialog.setMessage("Type Project name to narrow selection:"); 
		dialog.setElements(projects);
		
		IJavaProject javaProject = getJavaProject();
		if (javaProject != null) {
			dialog.setInitialSelections(new Object[] { javaProject });
		}
		if (dialog.open() == Window.OK) {			
			return (IJavaProject) dialog.getFirstResult();
		}			
		return null;		
	}
	
	/*
	 * Return the IJavaProject corresponding to the project name in the project name
	 * text field, or null if the text does not match a project name.
	 */
	private IJavaProject getJavaProject() {
		String projectName = fProjText.getText().trim();
		if (projectName.length() < 1) {
			return null;
		}
		return getJavaModel().getJavaProject(projectName);		
	}
	
	/*
	 * Convenience method to get the workspace root.
	 */
	private static IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	
	/*
	 * Convenience method to get access to the java model.
	 */
	private IJavaModel getJavaModel() {
		return JavaCore.create(getWorkspaceRoot());
	}


	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#isValid(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public boolean isValid(ILaunchConfiguration config) {
		validatePage();
		return getErrorMessage() == null;
	}

	private void validatePage() {
		
		setErrorMessage(null);
		setMessage(null);

		String projectName= fProjText.getText().trim();
		if (projectName.length() == 0) {
			setErrorMessage("Project name not configured.");
			return;
		}

		IStatus status= ResourcesPlugin.getWorkspace().validatePath(IPath.SEPARATOR + projectName, IResource.PROJECT);
		if (!status.isOK() || !Path.ROOT.isValidSegment(projectName)) {
			setErrorMessage("Invalid project name '" + projectName + "'.");
			return;
		}

		IProject project= getWorkspaceRoot().getProject(projectName);
		if (!project.exists()) {
			setErrorMessage("Unknown project name '" + projectName + "'.");
			return;
		}
		IJavaProject javaProject= JavaCore.create(project);

		String configName= fConfigurationText.getText().trim();
		try {
			if (!project.hasNature(JavaCore.NATURE_ID)) {
				setErrorMessage("Project '" + projectName + "' is not a Java Project.");
				return;
			}
			if (configName.length() == 0) {
				setErrorMessage("Smooks Configuration not specified.");
				return;
			}
		} catch (CoreException e) {
			setErrorMessage("Unexpected Validation Error: " + e.getMessage());
		}
		
		if(!launchMetaData.isValidSmooksConfig()) {
			setErrorMessage(launchMetaData.getErrorMessage());
		}
	}
	
	private IResource getSmooksConfig() {
		String projectName = fProjText.getText();
		String configName = fConfigurationText.getText();
		
		return getSmooksConfig(projectName, configName);
	}
	
	public static IResource getSmooksConfig(ILaunchConfiguration launchConfig) {		
		try {
			String projectName = launchConfig.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""); //$NON-NLS-1$;
			String configName = launchConfig.getAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, ""); //$NON-NLS-1$;

			return getSmooksConfig(projectName, configName);
		} catch (CoreException e) {
			return null;
		} 
	}

	private static IResource getSmooksConfig(String projectName, String configName) {
		if(projectName != null && !projectName.trim().equals("") && configName != null && !configName.trim().equals("")) {
			IProject project = getWorkspaceRoot().getProject(projectName);
			if(assertProjectOK(project)) {
				return project.findMember(configName);
			}
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return "Run"; 
	}

	private void setButtonGridData(Button button) {
		GridData gridData= new GridData();
		button.setLayoutData(gridData);
	}

	private static boolean assertProjectOK(IProject project) {
		return (project != null && project.exists() && project.isAccessible() && !project.isHidden());
	}
} 

 
