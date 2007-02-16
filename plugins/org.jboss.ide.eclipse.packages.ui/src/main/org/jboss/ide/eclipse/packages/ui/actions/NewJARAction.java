package org.jboss.ide.eclipse.packages.ui.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.packages.ui.PackagesUIPlugin;
import org.jboss.ide.eclipse.packages.ui.wizards.NewJARWizard;
import org.jboss.ide.eclipse.ui.util.ActionWithDelegate;

public class NewJARAction extends ActionWithDelegate
{
	private IProject project;
	
	public void run() {
		NewJARWizard wizard = new NewJARWizard();
		
		IStructuredSelection selection = getSelection();
		if (selection.isEmpty())
		{
			wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(project));
		} else {
			wizard.init(PlatformUI.getWorkbench(), getSelection());
		}
		
		
		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
		int response = dialog.open();
		if (response == Dialog.OK)
		{
		}
	}
	
	public ImageDescriptor getImageDescriptor() {
		return PackagesUIPlugin.getImageDescriptor(PackagesUIPlugin.IMG_NEW_PACKAGE);
	}
	
	public String getText() {
		return "JAR";
	}
	
	public String getToolTipText() {
		return "Create a new JAR package";
	}
	
	public void setProject (IProject project)
	{
		this.project = project;
	}
}
