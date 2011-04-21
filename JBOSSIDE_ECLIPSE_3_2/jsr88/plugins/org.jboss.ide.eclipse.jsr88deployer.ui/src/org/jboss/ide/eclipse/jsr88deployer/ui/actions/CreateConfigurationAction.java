/**
 * 
 */
package org.jboss.ide.eclipse.jsr88deployer.ui.actions;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jboss.ide.eclipse.jsr88deployer.ui.dialogs.CreateConfigurationDialog;

/**
 * @author Rob Stryker
 */
public class CreateConfigurationAction implements IObjectActionDelegate, IWorkbenchWindowActionDelegate {

	private IResource resource;
	
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	public void run(IAction action) {
		CreateConfigurationDialog dialog = 
			new CreateConfigurationDialog(new Shell(), resource);
		int response = dialog.open();
		// do nothing regardless... all that does is save
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if( selection instanceof IStructuredSelection ) {
			Object first = ((IStructuredSelection)selection).getFirstElement();
			if( first instanceof IResource ) {
				this.resource = ((IResource)first);
			}
		}
	}

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
	}

}
