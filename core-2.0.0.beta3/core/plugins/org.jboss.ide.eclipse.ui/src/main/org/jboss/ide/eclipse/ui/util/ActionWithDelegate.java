package org.jboss.ide.eclipse.ui.util;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * This is a utility action base class that wraps itself as both a JFace Action (used by views/toolbars/etc)
 * and an IActionDelegate (contributed by plugin extension). The action registers itself with the active
 * workbench window's selection service, and the active page's part listener to automatically keep tabs on the current selection.
 * Subclasses should use the "getSelection()" method to retrieve the current selection.
 * 
 * @author Marshall
 */
public abstract class ActionWithDelegate extends Action implements IActionDelegate, ISelectionListener
{
	private IStructuredSelection selection;
	
	public ActionWithDelegate ()
	{
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(this);
	}
	
	public abstract void run ();
	
	public void run(IAction action) {
		run();
	}
	
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			setSelection((IStructuredSelection)selection);
		}
	}
	
	protected void setSelection(IStructuredSelection selection)
	{
		if (!selection.isEmpty())
			this.selection = selection;
	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			setSelection((IStructuredSelection)selection);
		}
	}
	
	public IStructuredSelection getSelection() {
		return selection;
	}
}
