package org.jboss.tools.internal.deltacloud.ui.wizards;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class CustomWizardDialog extends WizardDialog {

	private String finishText;
	
	public CustomWizardDialog(Shell parentShell, IWizard newWizard, String finishText) {
		super(parentShell, newWizard);
		this.finishText = finishText;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		// All we want to do is override the text for the finish button
		Button finishButton = getButton(IDialogConstants.FINISH_ID);
		finishButton.setText(finishText);
	}

}
