package org.jboss.ide.eclipse.packages.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

public abstract class ActionWithDelegate extends Action implements IActionDelegate, ISelectionListener {
	
	public ActionWithDelegate () { 
	}
	
	public abstract void run ();
	
	public void run(IAction action) {
		run();
	}
	
	public abstract IStructuredSelection getSelection();
	public void selectionChanged(IAction action, ISelection selection) {
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
	}

}