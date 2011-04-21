package org.jboss.ide.eclipse.packages.ui.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Display;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackagesModelListener;

public class PackagesListenerProxy implements IPackagesModelListener
{
	private IPackagesModelListener listener;
	private Listener internalListener = new Listener();
	
	private class Listener implements Runnable {
		public static final int ADDED = 0;
		public static final int REMOVED = 1;
		public static final int CHANGED = 2;
		public static final int ATTACHED = 3;
		public static final int PROJECT_REGISTERED = 4;
		
		public IPackageNode node;
		public IProject project;
		public int event;
		public void run ()
		{
			if (event > -1)
			{
				switch (event) {
				case ADDED: listener.packageNodeAdded(node); break;
				case REMOVED: listener.packageNodeRemoved(node); break;
				case CHANGED: listener.packageNodeChanged(node); break;
				case ATTACHED: listener.packageNodeAttached(node); break;
				case PROJECT_REGISTERED: listener.projectRegistered(project); break;
				}
			}
			
			node = null;
			project = null;
			event = -1;
		}
	}
	
	public PackagesListenerProxy (IPackagesModelListener listener)
	{
		this.listener = listener;
	}
	
	public void projectRegistered(IProject project) {
		internalListener.project = project;
		internalListener.event = Listener.PROJECT_REGISTERED;
		Display.getDefault().syncExec(internalListener);
	}
	
	public void packageNodeAdded(IPackageNode added) {
		internalListener.node = added;
		internalListener.event = Listener.ADDED;
		Display.getDefault().syncExec(internalListener);
	}

	public void packageNodeChanged(IPackageNode changed) {
		internalListener.node = changed;
		internalListener.event = Listener.CHANGED;
		Display.getDefault().syncExec(internalListener);
	}

	public void packageNodeRemoved(IPackageNode removed) {
		internalListener.node = removed;
		internalListener.event = Listener.REMOVED;
		Display.getDefault().syncExec(internalListener);
	}

	public void packageNodeAttached(IPackageNode attached) {
		internalListener.node = attached;
		internalListener.event = Listener.ATTACHED;
		Display.getDefault().syncExec(internalListener);
	}
}
