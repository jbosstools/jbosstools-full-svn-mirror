package org.jboss.tools.smooks.ui.wizards;

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
		if (error != null) {
			this.setErrorMessage(error);
		}
		return (error == null);
	}
}