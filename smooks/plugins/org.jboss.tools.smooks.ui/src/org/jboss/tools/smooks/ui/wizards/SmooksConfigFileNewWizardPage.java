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
		setFileExtension("smooks");
	}

	@Override
	protected boolean validatePage() {
		boolean flag = super.validatePage();
		String name = this.getFileName();
		if (name.indexOf(".") == -1)
			return flag;
		String extensionName = name.substring(name.indexOf(".") + 1, name
				.length());
		String error = null;
		if (extensionName.equalsIgnoreCase(this.getFileExtension())) {

		} else {
			error = "file extension must be \"smooks\"";
		}
		if (error != null) {
			this.setErrorMessage(error);
		}
		return (error == null);
	}
}