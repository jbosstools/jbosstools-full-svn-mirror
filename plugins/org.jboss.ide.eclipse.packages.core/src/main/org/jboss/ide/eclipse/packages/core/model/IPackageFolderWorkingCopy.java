package org.jboss.ide.eclipse.packages.core.model;

public interface IPackageFolderWorkingCopy extends IPackageFolder, IPackageNodeWorkingCopy {
	public void setName(String name);
	
	public void addPackage(IPackage pkg);
	public void addFolder(IPackageFolder folder);
	public void addFileSet(IPackageFileSet fileset);
	
	public IPackageFolder getOriginalFolder();
	public IPackageFolder saveFolder();
}
