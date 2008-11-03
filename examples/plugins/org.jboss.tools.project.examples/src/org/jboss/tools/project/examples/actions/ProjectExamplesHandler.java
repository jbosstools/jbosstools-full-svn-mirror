package org.jboss.tools.project.examples.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.project.examples.wizard.NewProjectExamplesWizard;

public class ProjectExamplesHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		Wizard wizard = new NewProjectExamplesWizard();
		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
		dialog.open();
		return null;
	}

}
