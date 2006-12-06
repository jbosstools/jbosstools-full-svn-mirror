package org.jboss.ide.eclipse.packages.core.model;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

/**
 * A working copy of an IPackageFileSet, used to set the fileset's attributes
 * @author Marshall
 */
public interface IPackageFileSetWorkingCopy extends IPackageFileSet, IPackageNodeWorkingCopy {
	
	/**
	 * Set this fileset to be a single file in the workspace
	 * Equivalent to:
	 * <code>setSingleFile(file, null);</code>
	 * 
	 * @param file the single file for this fileset
	 */
	public void setSingleFile(IFile file);
	
	/**
	 * Set this fileset to be a single file in the workspace, using the specified destination filename
	 * @param file The single file to be included in this fileset
	 * @param destinationFilename The filename that the file will be called in the package
	 */
	public void setSingleFile(IFile file, String destinationFilename);
	
	/**
	 * Set this fileset to be a single file in the filesystem
	 * Equivalent to:
	 * <code>setSingleFile(path, null);</code>
	 * @param path An absolute path to a file on the filesystem 
	 */
	public void setSingleFile(IPath path);
	
	/**
	 * Set this fileset to be a single file in the filesystem, using the specified destination filename
	 * @param path An absolute path to a file on the filesystem
	 * @param destinationFilename The filename that the file will be called in the package
	 */
	public void setSingleFile(IPath path, String destinationFilename);
	
	/**
	 * Sets the "root" or "source" of this fileset to be the passed-in project
	 * @param project The project that is the source of this fileset
	 */
	public void setSourceProject(IProject project);
	
	/**
	 * Sets the "root" or "source" of this fileset to the be the passed-in container
	 * @param container The container that is the source of this fileset
	 */
	public void setSourceContainer(IContainer container);
	
	/**
	 * Sets the "root" or "source" of this fileset to be an absolute path on the filesystem
	 * @param path The absolute path that is the source of this fileset
	 */
	public void setSourceFolder(IPath path);
	
	/**
	 * Set the includes pattern for this fileset. This pattern uses the same syntax as Ant's include pattern.
	 * @param includes The includes pattern for this fileset
	 */
	public void setIncludesPattern(String includes);
	
	/**
	 * Set the excludes pattern for this fileset. This pattern uses the same syntax as Ant's exclude pattern.
	 * @param excludes The excludes pattern for this fileset
	 */
	public void setExcludesPattern(String excludes);
	
	/**
	 * Set whether or not this fileset's source is in the workspace. This will automatically be handled if you
	 * use setSingleFile, setSourceProject, setSourceContainer, or setSourceFolder.
	 * @param isInWorkspace Whether or not this fileset's source is in the workspace
	 */
	public void setInWorkspace(boolean isInWorkspace);
	
	/**
	 * @return The original fileset this working copy is derived from
	 */
	public IPackageFileSet getOriginalFileSet();
	
	/**
	 * Save this working copy's data into the original fileset
	 * @return A reference to the original fileset
	 */
	public IPackageFileSet saveFileSet();
}
