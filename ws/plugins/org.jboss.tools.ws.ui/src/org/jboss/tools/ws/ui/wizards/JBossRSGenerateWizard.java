/******************************************************************************* 
 * Copyright (c) 2010 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.ws.ui.wizards;

import java.io.File;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jst.j2ee.project.JavaEEProjectUtilities;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.ws.creation.core.commands.AddRestEasyJarsCommand;
import org.jboss.tools.ws.creation.core.commands.RSMergeWebXMLCommand;
import org.jboss.tools.ws.creation.core.commands.RSServiceSampleCreationCommand;
import org.jboss.tools.ws.creation.core.data.ServiceModel;
import org.jboss.tools.ws.creation.core.utils.JBossWSCreationUtils;
import org.jboss.tools.ws.ui.JBossWSUIPlugin;
import org.jboss.tools.ws.ui.messages.JBossWSUIMessages;

/**
 * @author Brian Fitzpatrick
 *
 */
public class JBossRSGenerateWizard extends Wizard implements INewWizard {

	String NAMEDEFAULT = "MyRESTApplication"; //$NON-NLS-1$
	String PACKAGEDEFAULT = "org.jboss.samples.rs.webservices"; //$NON-NLS-1$
	String CLASSDEFAULT = "HelloWorldResource"; //$NON-NLS-1$
	String APPCLASSDEFAULT = "MyRESTApplication"; //$NON-NLS-1$

	private String serviceName = NAMEDEFAULT;
	private String packageName = PACKAGEDEFAULT;
	private String className = CLASSDEFAULT;
	private String appClassName = APPCLASSDEFAULT;
	private boolean useDefaultServiceName = true;
	private boolean useDefaultClassName = true;

	private IStructuredSelection selection;
	private IProject project;

	private static String WEB = "web.xml"; //$NON-NLS-1$
	private static String JAVA = ".java"; //$NON-NLS-1$
	private static String WEBINF = "WEB-INF"; //$NON-NLS-1$
	private IFile webFile;

	public JBossRSGenerateWizard() {
		super();
		super.setWindowTitle(JBossWSUIMessages.JBossRSGenerateWizard_RS_Wizard_Window_Title);
		super.setHelpAvailable(false);
	}

	public void addPages() {
		super.addPages();
		JBossRSGenerateWizardPage onePage =
			new JBossRSGenerateWizardPage("onePage"); //$NON-NLS-1$
		addPage(onePage);
	}

	@Override
	public boolean performFinish() {
		if (canFinish()) {
			ServiceModel model = new ServiceModel();
			model.setWebProjectName(project.getName());
			model.addServiceClasses(new StringBuffer().append(getPackageName())
					.append(".").append(getClassName()).toString()); //$NON-NLS-1$
//			model.addServiceClasses(new StringBuffer().append(getPackageName())
//					.append(".").append(getAppClassName()).toString()); //$NON-NLS-1$
			model.setServiceName(getServiceName());
			model.setUpdateWebxml(true);
			model.setCustomPackage(getPackageName());
			model.setApplicationClassName( getAppClassName());

			File file = JBossWSCreationUtils.findFileByPath(getClassName() + JAVA, project
					.getLocation().toOSString());
			if (file != null) {
				MessageDialog
						.openError(
								this.getShell(),
								JBossWSUIMessages.JBossWS_GenerateWizard_MessageDialog_Title,
								JBossWSUIMessages.Error_JBossWS_GenerateWizard_ClassName_Same);
				return false;
			}

			IStatus status = null;
			try {
				RSMergeWebXMLCommand mergeCommand = new RSMergeWebXMLCommand(model);
				status = mergeCommand.execute(null, null);
			} catch (ExecutionException e) {
				JBossWSUIPlugin.log(e);
			}
			if (status != null && status.getSeverity() == Status.ERROR) {
				MessageDialog
						.openError(
								this.getShell(),
								JBossWSUIMessages.JBossWS_GenerateWizard_MessageDialog_Title,
								status.getMessage());
				return false;
			}
			try {
				new AddRestEasyJarsCommand(model).execute(null, null);
				new RSServiceSampleCreationCommand(model).execute(null, null);
				getProject().refreshLocal(IProject.DEPTH_INFINITE, new NullProgressMonitor());
			} catch (ExecutionException e) {
				JBossWSUIPlugin.log(e);
				MessageDialog
					.openError(
						this.getShell(),
						JBossWSUIMessages.JBossWS_GenerateWizard_MessageDialog_Title,
						e.getMessage());
			} catch (CoreException e) {
				JBossWSUIPlugin.log(e);
				MessageDialog
					.openError(
						this.getShell(),
						JBossWSUIMessages.JBossWS_GenerateWizard_MessageDialog_Title,
						e.getMessage());
			}
		}
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		if (this.selection.getFirstElement() instanceof IProject) {
			project = (IProject) this.selection.getFirstElement();
		}
		if (project != null
				&& JavaEEProjectUtilities.isDynamicWebProject(project)) {
			webFile = project.getParent().getFolder(
					JBossWSCreationUtils.getWebContentRootPath(project).append(WEBINF))
					.getFile(WEB);
		}
	}

	@Override
	public boolean canFinish() {
		return super.canFinish();
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getAppClassName() {
		return appClassName;
	}

	public void setAppClassName(String className) {
		this.appClassName = className;
	}

	public boolean isUseDefaultServiceName() {
		return useDefaultServiceName;
	}

	public void setUseDefaultServiceName(boolean useDefaultServiceName) {
		this.useDefaultServiceName = useDefaultServiceName;
	}

	public boolean isUseDefaultClassName() {
		return useDefaultClassName;
	}

	public void setUseDefaultClassName(boolean useDefaultClassName) {
		this.useDefaultClassName = useDefaultClassName;
	}

	public IProject getProject() {
		return project;
	}
	
	public ServiceModel getServiceModel() {
		ServiceModel model = new ServiceModel();
		if (project != null) {
			model.setWebProjectName(project.getName());
		}
		if (getPackageName() != null) {
			model.addServiceClasses(new StringBuffer().append(getPackageName())
				.append(".").append(getClassName()).toString()); //$NON-NLS-1$
		}
		model.setServiceName(getServiceName());
		model.setUpdateWebxml(true);
		model.setCustomPackage(getPackageName());
		model.setCustomClassName(getClassName());
		return model;
	}
	
	public void setProject (String projectName) {
		if (projectName != null && projectName.trim().length() > 0) {
			IProject test =
				ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
			if (test != null) {
				this.project = test;
				if (project != null
						&& JavaEEProjectUtilities.isDynamicWebProject(project)) {
					webFile = project.getParent().getFolder(
							JBossWSCreationUtils.getWebContentRootPath(project).append(WEBINF))
							.getFile(WEB);
				}
			}
		}
	}
	

	public IFile getWebFile() {
		return webFile;
	}
}
