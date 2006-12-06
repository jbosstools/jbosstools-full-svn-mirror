package org.jboss.ide.eclipse.packages.core.model;

/**
 * A working copy of a package node. Working copies are used to set attributes on a package node.
 * @author Marshall
 */
public interface IPackageNodeWorkingCopy extends IPackageNodeBase {
	
	/**
	 * Set a property on this working copy
	 * @param property The property to set
	 * @param value The value of the property
	 */
	public void setProperty(String property, String value);
	
	/**
	 * Save this working copy (will overwrite the values in the original node)
	 * @return The original node
	 */
	public IPackageNode save();
	
	/**
	 * @return The original node this working copy is derived from
	 */
	public IPackageNode getOriginal();
}
