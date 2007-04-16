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

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.packages.core.model.types.IPackageType;

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
	public static final String ATTRIBUTE_PREFIX = "org.jboss.ide.eclipse.packages.core.model.IPackage.";
	public static final String PACKAGE_TYPE_ATTRIBUTE = ATTRIBUTE_PREFIX + "packageType";
	public static final String EXPLODED_ATTRIBUTE = ATTRIBUTE_PREFIX + "exploded";
	public static final String DESTINATION_ATTRIBUTE = ATTRIBUTE_PREFIX + "destination";
	public static final String NAME_ATTRIBUTE = ATTRIBUTE_PREFIX + "name";
	public static final String IN_WORKSPACE_ATTRIBUTE = ATTRIBUTE_PREFIX + "inWorkspace";
	

	/**
	 * @return The package type of this package.
	 */
	public IPackageType getPackageType();
	
	/**
	 * return the raw string from the delegate even if the type is not found
	 * @return
	 */
	public String getPackageTypeId();

	/**
	 * @return The name (with extension) of this package.
	 */
	public String getName();
	
//	/**
//	 * @return Whether or not this package is a reference to another package.
//	 */
//	public boolean isReference();
	
//	/**
//	 * @return An array of references to this package.
//	 */
//	public IPackageReference[] getReferences ();
	
	/**
	 * @return Whether or not this package will be build exploded, or as a directory instead of a ZIP/JAR
	 */
	public boolean isExploded();
	
	/**
	 * @return Whether or not this package is a "top-level" package aka, not a child of another folder or package
	 */
	public boolean isTopLevel();
		
	/**
	 * If this package is top-level, there are two types of destinations it can have. "Inside" the workspace, and "outside" the workspace.
	 * If the destination is inside the workspace, you will need to call getDestinationContainer()
	 * Otherwise you will need to call getDestinationFolder()
	 * @return Wheter or not the destination of this package is in the workspace
	 * @see IPackage.getDestinationFolder()
	 * @see IPackage.getDestinationContainer()
	 */
	public boolean isDestinationInWorkspace();
	
	/**
	 * @return An IPath to this package's destination. 
	 * Destination will always be file-system based
	 */
	public IPath getDestinationPath();
	
	/**
	 * @return A list of sub-packages contained in this package
	 */
	public IPackage[] getPackages();
	
	/**
	 * @return A list of folders contained in this package
	 */
	public IPackageFolder[] getFolders();
	
	/**
	 * @return A list of filesets contained in this package
	 */
	public IPackageFileSet[] getFileSets();

	/**
	 * Get The path to this package's output file.
	 * This path should be GLOBAL
	 * @return  the path
	 */
	public IPath getPackageFilePath();
	
	/**
	 * If this package is not top-level, this will return a relative path to this package from within it's parent, i.e.
	 * my.ear/web/my.war/WEB-INF/lib. Otherwise, this will return null
	 * @return a relative IPath to this package's top level parent
	 */
	//public IPath getPackageRelativePath();
	
	/**
	 * Set the package type of this package
	 * @param type The package type
	 */
	public void setPackageType(IPackageType type);

	/**
	 * Set the package type via ID. 
	 * @param type
	 */
	public void setPackageType(String type);
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
	 * Sets the destination path for this package.
	 * @param path The absolute path where this package will be built
	 */
	public void setDestinationPath (IPath path, boolean inWorkspace);
		
}
