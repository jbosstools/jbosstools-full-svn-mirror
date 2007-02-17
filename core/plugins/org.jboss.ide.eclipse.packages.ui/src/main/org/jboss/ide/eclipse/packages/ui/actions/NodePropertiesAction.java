package org.jboss.ide.eclipse.packages.ui.actions;

import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.jboss.ide.eclipse.packages.ui.PackagesUIPlugin;
import org.jboss.ide.eclipse.ui.util.ActionWithDelegate;

public class NodePropertiesAction extends ActionWithDelegate implements
		IViewActionDelegate {

	private IViewPart view;
	
	public void run() {
		PackagesUIPlugin.alert("Placeholder");
	}

	public void init(IViewPart view) {
		this.view = view;
	}

}
