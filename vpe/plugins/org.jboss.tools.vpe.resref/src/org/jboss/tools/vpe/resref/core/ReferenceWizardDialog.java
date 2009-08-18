package org.jboss.tools.vpe.resref.core;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.resref.core.ResourceReference;

public class ReferenceWizardDialog extends WizardDialog {

	protected ResourceReference resref = null;
	
	public ReferenceWizardDialog(Shell parentShell, ReferenceWizard newWizard, ResourceReference resref) {
		super(parentShell, newWizard);
		this.resref = resref;
	}

	@Override
	protected void constrainShellSize() {
		super.constrainShellSize();
	}
	
	@Override
	public int open() {
		/*
		 * Get Resouce Reference dialog.
		 */
		ReferenceWizard wizard = (ReferenceWizard) getWizard();
		/*
		 * Read values from resref when editing.
		 */
		wizard.setResref(resref);
		
		/*
		 * Open the dialog
		 */
		int returnCode = super.open();
		/*
		 * If Finish pressed - store new values in the resref.
		 */
		if (Dialog.OK == returnCode) {
			resref = wizard.getResref();
		}
		return returnCode;
	}
	
}
