package org.jboss.ide.eclipse.packages.core.model;

/**
 * Represents a reference to another package within the workspace
 * @author Marshall
 *
 */
public interface IPackageReference extends IPackage {

	/**
	 * The package this is a reference of
	 */
	public IPackage getPackage();
	
	/**
	 * @return The parent node of this reference
	 */
	public IPackageNode getParent();
}
