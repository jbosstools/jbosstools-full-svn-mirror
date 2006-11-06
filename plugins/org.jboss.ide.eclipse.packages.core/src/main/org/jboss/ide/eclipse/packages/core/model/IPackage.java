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

import java.util.jar.Manifest;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;

/** 
 * <p>
 * This interface represents a package definition.
 * A package definition consists of a list of folders, filesets, and sub-packages
 * </p>
 * 
 * @author <a href="marshall@jboss.org">Marshall Culpepper</a>
 * @version $Revision$
 */
public interface IPackage extends IPackageNode {

	/**
	 * @return The package type of this package.
	 */
	public String getPackageType();

	public String getName();
	
	public boolean isReference();
	
	public boolean isExploded();
	
	public boolean isTopLevel();
	
	public boolean hasManifest();
	
	public Manifest getManifest();
	
	public boolean isDestinationInWorkspace();
	
	public IPath getDestinationFolder();
	
	public IContainer getDestinationContainer();
	
	public IPackage[] getPackages();
	
	public IPackageFolder[] getFolders();
	
	public IPackageFileSet[] getFileSets();
	
	public IPackageWorkingCopy createPackageWorkingCopy();

	/**
	 * Get the IFile that corresponds with this package.
	 * @return The corresponding IFile in the workspace (note that this file may not exist)
	 */
	public IFile getPackageFile();

	public IPath getPackageRelativePath();
}
