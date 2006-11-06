package org.jboss.ide.eclipse.packages.core.model;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;

public interface IPackageWorkingCopy extends IPackage, IPackageNodeWorkingCopy {
	public void setPackageType(String type);

	public void setName(String name);
	
	public void setExploded(boolean exploded);
	
	public void setManifest(IFile manifestFile);
	
	public void setDestinationFolder (IPath path);
	
	public void setDestinationContainer(IContainer container);
	
	public void addPackage(IPackage pkb);
	public void addFolder(IPackageFolder folder);
	public void addFileSet(IPackageFileSet fileset);
	
	public IPackage getOriginalPackage();
	
	public IPackage savePackage();
}
