package org.jboss.ide.eclipse.archives.ui.actions;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.archives.ui.PackagesSharedImages;
import org.jboss.ide.eclipse.archives.ui.views.ProjectArchivesView;
import org.jboss.ide.eclipse.archives.ui.wizards.NewJARWizard;

public class NewJARAction extends ActionWithDelegate {
	public void run() {
		try {
		NewJARWizard wizard = new NewJARWizard();
		
		wizard.init(PlatformUI.getWorkbench(), getSelection());
		
		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
		int response = dialog.open();
		if (response == Dialog.OK) {
		}
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	public IStructuredSelection getSelection() {
		return ProjectArchivesView.getInstance().getSelection();
	}
	public ImageDescriptor getImageDescriptor() {
		return PackagesSharedImages.getImageDescriptor(PackagesSharedImages.IMG_NEW_PACKAGE);
	}
	
	public String getText() {
		return "JAR";
	}
	
	public String getToolTipText() {
		return "Create a new JAR package";
	}
}
