package org.jboss.tools.smooks.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

/**
 * 
 * @author dart
 * 
 */
public class SmooksConfigFileNewWizardPage extends WizardNewFileCreationPage {
	public SmooksConfigFileNewWizardPage(String pageName,
			IStructuredSelection selection) {
		super(pageName, selection);
		setFileExtension("smooks"); //$NON-NLS-1$
		super.setTitle(Messages.getString("SmooksConfigFileNewWizardPage.NewConfigFileWizardPageTitle")); //$NON-NLS-1$
		super.setDescription(Messages.getString("SmooksConfigFileNewWizardPage.NewConfigFileWizardPageDescription")); //$NON-NLS-1$
		this.setFileName(Messages.getString("SmooksConfigFileNewWizardPage.NewConfigFileWizardPageDefaultFileName")); //$NON-NLS-1$
	}

	@Override
	protected boolean validatePage() {
		boolean flag = super.validatePage();
		String name = this.getFileName();
		if (name.indexOf(".") == -1) //$NON-NLS-1$
			return flag;
		String extensionName = name.substring(name.indexOf(".") + 1, name //$NON-NLS-1$
				.length());
		String error = null;
		if (extensionName.equalsIgnoreCase(this.getFileExtension())) {

		} else {
			error = Messages.getString("SmooksConfigFileNewWizardPage.NewConfigFileWizardPageErrorMessage1"); //$NON-NLS-1$
		}
		IPath containerPath = this.getContainerFullPath();
		IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(containerPath);
		IProject project = container.getProject();
		boolean isJavaProject = false;
		if(project != null){
			try{
				if(project.hasNature(JavaCore.NATURE_ID)){
					isJavaProject = true;
				}
			}catch(Exception e){
				
			}
		}
		if(!isJavaProject){
			error = "Please select a folder of Java projects.";
		}
		this.setErrorMessage(error);
		return (error == null);
	}
}