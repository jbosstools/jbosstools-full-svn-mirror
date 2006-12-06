package org.jboss.ide.eclipse.packages.core.model;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.packages.core.model.types.IPackageType;

/**
 * A working copy of an IPackage, used to set the package's attributes
 * @author Marshall
 */
public interface IPackageWorkingCopy extends IPackage, IPackageNodeWorkingCopy {
	/**
	 * Set the package type of this package
	 * @param type The package type
	 */
	public void setPackageType(IPackageType type);

	/**
	 * Set the name of this package
	 * @param name This package's name
	 */
	public void setName(String name);
	
	/**
	 * Set whether or not this package is generated as a folder
	 * @param exploded
	 */
	public void setExploded(boolean exploded);
	
	/**
	 * Set a path to a custom manifest for this package. This is usefulf or runnable JARs etc.
	 * @param manifestFile The manifest file in the workspace
	 */
	public void setManifest(IFile manifestFile);
	
	/**
	 * Sets the destination folder in the filesystem for this package
	 * @param path The absolute path where this package will be built
	 */
	public void setDestinationFolder (IPath path);
	
	/**
	 * Sets the destination container in the workspace for this package
	 * @param container The container where this package will be built
	 */
	public void setDestinationContainer(IContainer container);
	
	/**
	 * @return The package that this working copy was derived from
	 */
	public IPackage getOriginalPackage();
	
	/**
	 * Save this working copy's data into the original package
	 * @return A reference to the original package
	 */
	public IPackage savePackage();
}
