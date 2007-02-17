package org.jboss.ide.eclipse.packages.ui.actions;

import org.eclipse.ui.IViewPart;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.ui.util.ActionWithDelegate;

public abstract class AbstractNodeActionDelegate extends ActionWithDelegate
		implements INodeActionDelegate {

	protected IViewPart view;
	
	public boolean isEnabledFor(IPackageNode node) {
		return true;
	}

	public void init(IViewPart view) {
		this.view = view;
	}

}
