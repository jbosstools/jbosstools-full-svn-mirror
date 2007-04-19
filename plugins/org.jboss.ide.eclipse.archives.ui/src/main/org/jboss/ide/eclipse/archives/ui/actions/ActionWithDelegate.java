package org.jboss.ide.eclipse.archives.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

public abstract class ActionWithDelegate extends Action implements IActionDelegate, ISelectionListener {
	
/*
 * Constructors
 */
	public ActionWithDelegate () { 
	}
	
	public ActionWithDelegate(String text) {
		super(text);
	}

	public ActionWithDelegate(String text, ImageDescriptor image) {
		super(text, image);
	}

	public ActionWithDelegate(String text, int style) {
		super(text, style);
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