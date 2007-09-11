/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.wizards.project;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.common.model.ui.attribute.XAttributeSupport;
import org.jboss.tools.common.model.ui.attribute.adapter.IModelPropertyEditorAdapter;
import org.jboss.tools.common.model.ui.attribute.editor.IPropertyEditor;
import org.jboss.tools.common.model.ui.util.ModelUtilities;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

import org.jboss.tools.common.meta.action.XEntityData;
import org.jboss.tools.common.meta.action.impl.XEntityDataImpl;
import org.jboss.tools.common.meta.key.WizardKeys;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.util.XMLUtil;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.jst.web.context.ImportWebDirProjectContext;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

public class ImportWebProjectWizardPage extends WizardPage {
	private ImportWebDirProjectContext context;
	private XAttributeSupport support;
	private IModelPropertyEditorAdapter projectNameAdapter;
	private IModelPropertyEditorAdapter webXmlLocationAdapter;
	private PropertyChangeListener updateDataListener;
	private IPropertyEditor projectNameEditor;
	
//	private static final int DIALOG_WIDTH = 540;
//	private static final int DIALOG_HEIGHT = 600;
	private static String REDHAT_NAME = "RedHat"; //$NON-NLS-1$

	public ImportWebProjectWizardPage(ImportWebDirProjectContext context) {
		super("Wizard Page"); //$NON-NLS-1$
		
		this.context = context;
		XEntityData entityData = XEntityDataImpl.create(
			new String[][] {
				{ImportWebDirProjectContext.PAGE_NAME, ""}, //$NON-NLS-1$
				{ImportWebDirProjectContext.ATTR_LOCATON, "yes"}, //$NON-NLS-1$
				{ImportWebDirProjectContext.ATTR_NAME, "yes"} //$NON-NLS-1$
			}
		);
		
		support = new XAttributeSupport(ModelUtilities.getPreferenceModel().getRoot(), entityData);
		support.setLayout(getLayoutForSupport());
		projectNameAdapter = support.getPropertyEditorAdapterByName(ImportWebDirProjectContext.ATTR_NAME);
		projectNameAdapter.setValue(""); //$NON-NLS-1$
		webXmlLocationAdapter = support.getPropertyEditorAdapterByName(ImportWebDirProjectContext.ATTR_LOCATON);
		webXmlLocationAdapter.setValue(""); //$NON-NLS-1$
//		IPropertyEditor editor = support.getPropertyEditorByName(ImportWebDirProjectContext.ATTR_LOCATON);
		//editor.setLabelText("Location*");
		if(context.isInitialized()) {
			projectNameAdapter.setValue("" + context.getProjectName()); //$NON-NLS-1$
			if(getWebXmlFile(context.getInitialLocation()) != null)
				webXmlLocationAdapter.setValue("" + context.getInitialLocation()); //$NON-NLS-1$
		} 
	}
	
	public void dispose() {
		super.dispose();
		if (support!=null) support.dispose();
		support = null;
		if (projectNameAdapter!=null) projectNameAdapter.dispose();
		projectNameAdapter = null;
		if (webXmlLocationAdapter!=null) webXmlLocationAdapter.dispose();
		webXmlLocationAdapter = null;
		if (projectNameEditor!=null) projectNameEditor.dispose();
		projectNameEditor = null;
		updateDataListener = null;
	}

	public void createControl(Composite parent)	{
		createControlImpl(parent);
	}

	private void createControlImpl(Composite parent)	{
		initializeDialogUnits(parent);
		Control control = support.createControl(parent);
		//setDialogSize(DIALOG_WIDTH,control.getSize().y); // BUGFIX:6348
		setControl(control);
		projectNameEditor = (IPropertyEditor)support.getPropertyEditorByName(ImportWebDirProjectContext.ATTR_NAME);
		projectNameEditor.getFieldEditor((Composite)getControl()).setEnabled(false, (Composite)getControl());
		initListeners();
		setErrorMessage(null);
		setMessage(null);
		setPageComplete(validatePage());
	}
	
	public void setVisible(boolean visible) {
		context.rollbackSupportDelta();
		super.setVisible(visible);
		// fix for JSFSTUD-112
//		if(visible) setShellSize(SWT.DEFAULT, SWT.DEFAULT); 
	}
	
	public void setDialogSize(int width, int height) {
		Shell shell = getShell();
		shell.pack();
		
		setShellSize(width, height);
		
		// locate shell to center
		Rectangle displayArea = shell.getDisplay().getClientArea();
		int x = (displayArea.width - getShell().getSize().x)/2; 
		int y = (displayArea.height - getShell().getSize().y)/2; 
		shell.setLocation(x,y);		
	}
	
	private void setShellSize(int width, int height) {
		Shell shell = getShell();
		if (width==SWT.DEFAULT) width = shell.getSize().x;
		if (height==SWT.DEFAULT) height = shell.getSize().y;
		shell.setSize(width, height);
	}
	
	private Layout getLayoutForSupport() {
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 4;
		gridLayout.marginWidth = 4;
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 10;
		return gridLayout;
	}
	
	private void initListeners() {
		updateDataListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				updateContext(true, false);
				setPageComplete(validatePage());
				getContainer().updateButtons();
			}
		};
		projectNameAdapter.addValueChangeListener(updateDataListener);
		webXmlLocationAdapter.addValueChangeListener(
			new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					updateContext(false, true);
					setPageComplete(validatePage());
					getContainer().updateButtons();
				}
			}
		);
	}

	private String getProjectNameValue() {
		return projectNameAdapter.getStringValue(true).trim(); 
	}

	private String getWebXmlLocationValue()	{
		return webXmlLocationAdapter.getStringValue(true).trim();
	}
	
	private void setProjectNameValue(String value) {
		projectNameAdapter.removeValueChangeListener(updateDataListener);
		projectNameAdapter.setValue(value);
		projectNameAdapter.addValueChangeListener(updateDataListener);
	}

	private void updateProjectNameValue(boolean onProjectNameEdit, boolean onProjectLocationEdit) {
		if(context.isInitialized()) return;
		File projectFile = null;
		projectNameEditor.getFieldEditor((Composite)getControl()).setEnabled(projectFile == null, (Composite)getControl());

		if (onProjectNameEdit) {
			String an = context.getApplicationName();
			String pn = context.getProjectName();
			String npn = getProjectNameValue();
			if(an == null || an.equals(pn)) {
				context.setApplicationName(npn);
			}
		}
		if(!onProjectLocationEdit) return;
		IProject project = findExistingProject(); 
		IPath webXmlPath = new Path(getWebXmlLocationValue());
		String sgName = getSuggestedProjectName(webXmlPath);
		String appName = sgName;
		if(sgName != null && context.getInitialLocation() == null && project != null && !project.getName().equals(sgName)) {
			project = null;
		}
		if(project == null /* && appName.equals("WebContent")*/) {
///			appName = sgName;
		}
		if(project != null) {
			appName = project.getName();
			setProjectNameValue(appName);
			projectNameEditor.getFieldEditor((Composite)getControl()).setEnabled(false, (Composite)getControl());
			context.setApplicationName(appName);					
		} else {
			setProjectNameValue(appName);
			context.setApplicationName(appName);					
		}
		
		if(projectFile != null) {
			appName = XMLUtil.getElement(projectFile).getAttribute("APPLICATION_NAME"); //$NON-NLS-1$
			context.setApplicationName(appName);
		}
	}
	
	private String getSuggestedProjectName(IPath webXmlPath) {
		IPath p = webXmlPath.removeLastSegments(3);
		IPath q = ModelPlugin.getWorkspace().getRoot().getLocation();
		if(!q.equals(p) && !p.isPrefixOf(q)) {
			return webXmlPath.segment(webXmlPath.segmentCount() - 4);
		}
		return null;
	}
	
	private IProject findExistingProject() {
		IPath webXmlPath = new Path(getWebXmlLocationValue());
		IProject[] ps = ModelUIPlugin.getWorkspace().getRoot().getProjects();
		for (int i = 0; i < ps.length; i++) {
			IPath location = ps[i].getLocation();
			if(location != null && location.isPrefixOf(webXmlPath)) {
				return ps[i];
			}
		}
		return null;
	}

	private File getWebXmlFile() {
		return getWebXmlFile(getWebXmlLocationValue());
	}

	private File getWebXmlFile(String path) {
		File tmp = (path == null) ? null : new File(path);
		return (tmp != null && tmp.exists() && tmp.isFile() && "web.xml".equals(tmp.getName())) ? tmp : null; //$NON-NLS-1$
	}

	private boolean validatePage() {
		setErrorMessage(null);
		setMessage(null);

		String webXmlLocation = getWebXmlLocationValue();
		if ("".equals(webXmlLocation))  //$NON-NLS-1$
			return false;
		if (!(new Path("")).isValidPath(webXmlLocation)) { //$NON-NLS-1$
			setErrorMessage(WizardKeys.getString(ImportWebDirProjectContext.PAGE_NAME+"_locationError")); //$NON-NLS-1$
			return false;
		}

		if (getWebXmlFile() == null){
			String message = context.getWebXMLErrorMessage();
			if(message == null) {
				message = WizardKeys.getString(ImportWebDirProjectContext.PAGE_NAME+"_notAProject"); //$NON-NLS-1$
			}
			setErrorMessage(message);
			return false;
		}
		if(context.getInitialLocationErrorMessage() != null) {
			setErrorMessage(context.getInitialLocationErrorMessage());
			return false;
		}
		if(context.getWebXMLErrorMessage() != null) {
			setErrorMessage(context.getWebXMLErrorMessage());
			return false;
		}
		
		String projectName = getProjectNameValue();
		if ("".equals(projectName)) //$NON-NLS-1$
			return false;

		IWorkspace workspace = ModelUIPlugin.getWorkspace();
		IStatus nameStatus = workspace.validateName(projectName, IResource.PROJECT);
		if (!nameStatus.isOK()) {
			setErrorMessage(nameStatus.getMessage());
			return false;
		}		
		IProject project = null;
		try {
			project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		} catch (Exception e) {
			setErrorMessage(e.getMessage());
			return false;
		}
		
		if(project != null && project.exists() && !project.isOpen()) {
			setErrorMessage(WebUIMessages.PROJECT_EXISTS_IN_WORKSPACE + getProjectNameValue() + " exists in workspace and is closed. To use the project, please open it."); //$NON-NLS-2$
			return false;
		}
		if(context.isInitialized()) return true;
		boolean hasNature = false;
		boolean hasJavaNature = false;
		try {
			project.refreshLocal(IProject.DEPTH_INFINITE, null);
			hasJavaNature = project.exists() && project.hasNature(JavaCore.NATURE_ID);
			hasNature = project.exists() && project.hasNature(context.getNatureID());
		} catch (CoreException e) {
			hasNature = project.exists();
		}
		if(project.exists() && (!hasNature) && hasJavaNature) {
			setMessage(NLS.bind(WebUIMessages.JAVA_PROJECT_EXISTS, getProjectNameValue(), REDHAT_NAME)); 
		} else if(project.exists() && (!hasNature) && !hasJavaNature) {
			setMessage(NLS.bind(WebUIMessages.PROJECT_EXISTS, getProjectNameValue(), REDHAT_NAME));
		} else {
			setMessage(null);
		}
		
		if (project.exists() && (hasNature || !hasJavaNature)) {
			setErrorMessage(WizardKeys.getString(ImportWebDirProjectContext.PAGE_NAME+"_existsInWorkspace")); //$NON-NLS-1$
			return false;
		}
		
		return true;
	}
	
	private void updateContext(boolean onProjectNameEdit, boolean onProjectLocationEdit) {
 		try	{
			if (getWebXmlFile() != null) updateProjectNameValue(onProjectNameEdit, onProjectLocationEdit);
			context.setProjectName(getProjectNameValue());
			context.setWebXmlLocation(getWebXmlLocationValue());
		} catch (Exception ex) {
			WebUiPlugin.getPluginLog().logError(ex);
		}
	}
	
}
