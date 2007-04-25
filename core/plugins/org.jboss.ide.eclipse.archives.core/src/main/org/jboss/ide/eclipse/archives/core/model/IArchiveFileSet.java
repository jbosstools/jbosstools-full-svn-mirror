/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.archives.core.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;

/**
 * <p>
 * This interface represents a file set inside of a package definition or folder.
 * </p>
 * 
 * @author <a href="marshall@jboss.org">Marshall Culpepper</a>
 * @author <a href="rob.stryker@redhat.com">Rob Stryker</a>
 * @version $Revision$
 */
public interface IArchiveFileSet extends IArchiveNode {
	public static final String ATTRIBUTE_PREFIX = "org.jboss.ide.eclipse.archives.core.model.IPackageFileSet.";
	public static final String INCLUDES_ATTRIBUTE = ATTRIBUTE_PREFIX + "includes";
	public static final String EXCLUDES_ATTRIBUTE = ATTRIBUTE_PREFIX + "excludes";
	public static final String IN_WORKSPACE_ATTRIBUTE = ATTRIBUTE_PREFIX + "inWorkspace";
	public static final String SOURCE_PATH_ATTRIBUTE = ATTRIBUTE_PREFIX + "sourcePath";


	
	/**
	 * @return Whether or not this fileset's basedir is inside the workspace
	 */
	public boolean isInWorkspace();
	
	/**
	 * Returns the absolute file-system relative source path
	 * @return The path to the source folder ("basedir" in ant terminology) for this fileset. 
	 */
	public IPath getGlobalSourcePath();
	
	/**
	 * @return the source path from the delegate (file-system or workspace-relative)
	 */
	public IPath getSourcePath();
	
	/**
	 * Force the scanner to check for matched files again
	 */
	public void resetScanner();
	
	/**
	 * @return The includes pattern for this fileset
	 */
	public String getIncludesPattern();
	
	/**
	 * @return The excludes pattern for this fileset
	 */
	public String getExcludesPattern();
	
	/**
	 * @return An array of matching IPath's in the filesystem (for external filesystem filesets)
	 */
	public IPath[] findMatchingPaths();
	
	/**
	 * @param file The file to check
	 * @return Whether or not this fileset matches the passed-in file
	 */
	public boolean matchesFile(IFile file);
	
	/**
	 * @param path The absolute path on the filesystem to check
	 * @return Whether or not this fileset matches the passed-in path
	 */
	public boolean matchesPath(IPath path);
	
	/**
	 * Sets the "root" or "source" of this fileset (file-system or workspace relative)
	 * @param path The absolute path that is the source of this fileset
	 */
	public void setSourcePath(IPath path);
	
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
	 * Get the relative path of the input file to the root archive
	 * @param inputFile
	 * @return
	 */
	public IPath getRootArchiveRelativePath(IPath inputFile);
}
