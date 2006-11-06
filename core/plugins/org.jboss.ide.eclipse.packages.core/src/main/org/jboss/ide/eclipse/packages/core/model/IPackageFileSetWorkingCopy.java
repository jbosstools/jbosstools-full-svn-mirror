package org.jboss.ide.eclipse.packages.core.model;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

public interface IPackageFileSetWorkingCopy extends IPackageFileSet, IPackageNodeWorkingCopy {
	
	/**
	 * Set this fileset to be a single file
	 * Equivalent to:
	 * <code>setSingleFile(file, null);</code>
	 * 
	 * @param file the single file for this fileset
	 */
	public void setSingleFile(IFile file);
	
	public void setSingleFile(IFile file, String destinationFilename);
	
	public void setSingleFile(IPath path);
	
	public void setSingleFile(IPath path, String destinationFilename);
	
	public void setSourceProject(IProject project);
	
	public void setSourceContainer(IContainer container);
	
	public void setSourceFolder(IPath path);
	
	public void setIncludesPattern(String includes);
	
	public void setExcludesPattern(String excludes);
	
	public void setInWorkspace(boolean isInWorkspace);
	
	public IPackageFileSet getOriginalFileSet();
	
	public IPackageFileSet saveFileSet();
}
