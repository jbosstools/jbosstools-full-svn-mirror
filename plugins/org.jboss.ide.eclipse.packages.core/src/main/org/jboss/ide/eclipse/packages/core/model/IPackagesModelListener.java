package org.jboss.ide.eclipse.packages.core.model;

public interface IPackagesModelListener {

	public void packageNodeAdded (IPackageNode added);
	
	public void packageNodeRemoved (IPackageNode removed);
	
	public void packageNodeChanged (IPackageNode changed);
}
