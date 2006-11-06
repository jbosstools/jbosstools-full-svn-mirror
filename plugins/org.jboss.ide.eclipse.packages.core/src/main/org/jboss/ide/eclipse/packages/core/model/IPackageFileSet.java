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
package org.jboss.ide.eclipse.packages.core.model;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

/**
 * <p>
 * This interface represents a file set inside of a package definition or folder.
 * </p>
 * 
 * @author <a href="marshall@jboss.org">Marshall Culpepper</a>
 * @version $Revision$
 */
public interface IPackageFileSet extends IPackageNode {

	public boolean isSingleFile();
	
	public boolean isInWorkspace();
	
	public IFile getFile();
	
	public IPath getFilePath();
	
	public String getDestinationFilename();
	
	public IProject getSourceProject();
	
	public IContainer getSourceContainer();
	
	public IPath getSourceFolder();
	
	public String getIncludesPattern();
	
	public String getExcludesPattern();
	
	public IFile[] findMatchingFiles();
	
	public IPath[] findMatchingPaths();
	
	public boolean matchesFile(IFile file);
	
	public boolean matchesPath(IPath path);
	
	public IPackageFileSetWorkingCopy createFileSetWorkingCopy();
}
