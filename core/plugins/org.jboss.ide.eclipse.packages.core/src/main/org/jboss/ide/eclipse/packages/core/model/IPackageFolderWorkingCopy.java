package org.jboss.ide.eclipse.packages.core.model;

/**
 * A working copy of an IPackageFolder, used to set the folder's attributes
 * @author Marshall
 */
public interface IPackageFolderWorkingCopy extends IPackageFolder, IPackageNodeWorkingCopy {
	
	/**
	 * Set the name of this folder
	 * @param name The name of this folder
	 */
	public void setName(String name);
	
	/**
	 * @return The original folder this working copy is derived from
	 */
	public IPackageFolder getOriginalFolder();
	
	/**
	 * Save this working copy's data into the original folder
	 * @return A reference to the original folder
	 */
	public IPackageFolder saveFolder();
}
