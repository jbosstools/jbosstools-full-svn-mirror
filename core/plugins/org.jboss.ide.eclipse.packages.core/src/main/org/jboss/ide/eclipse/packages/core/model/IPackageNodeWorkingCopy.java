package org.jboss.ide.eclipse.packages.core.model;

public interface IPackageNodeWorkingCopy extends IPackageNode {
	
	public void setProperty(String property, String value);
	
	public IPackageNode save();
	
	public IPackageNode getOriginal();
}
