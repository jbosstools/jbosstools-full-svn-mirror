package org.jboss.ide.eclipse.packages.ui.actions;

import org.eclipse.ui.IViewPart;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;

public abstract class AbstractNodeActionDelegate implements INodeActionDelegate {

	protected IViewPart view;
	
	public boolean isEnabledFor(IPackageNode node) {
		return true;
	}
	
	public void run(IPackageNode node) {
		
	}
}
