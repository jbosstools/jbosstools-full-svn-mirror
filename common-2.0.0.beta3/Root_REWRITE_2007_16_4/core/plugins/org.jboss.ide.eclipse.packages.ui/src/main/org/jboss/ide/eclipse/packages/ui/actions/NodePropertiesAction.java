package org.jboss.ide.eclipse.packages.ui.actions;

import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.packages.core.Trace;

public class NodePropertiesAction extends AbstractNodeActionDelegate {

	public void run() {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.eclipse.ui.views.PropertySheet");
		} catch (PartInitException e) {
			Trace.trace(getClass(), e);
		}
	}
}
