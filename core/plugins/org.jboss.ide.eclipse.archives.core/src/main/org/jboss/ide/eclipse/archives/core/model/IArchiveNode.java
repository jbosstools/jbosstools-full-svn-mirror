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

import org.eclipse.core.runtime.IPath;

/**
 * The super type of all package nodes (IPackage, IPackageFileSet, IPackageFolder)
 * 
 * Each node in a package may have arbitrary properties that can be reflected upon by other plug-ins
 * 
 * @author <a href="marshall@jboss.org">Marshall Culpepper</a>
 * @author <a href="rob.stryker@redhat.com">Rob Stryker</a>
 * @version $Revision: 1929 $
 */
public interface IArchiveNode {
	/**
	 * The node type that represents the model
	 */
	public static final int TYPE_MODEL = -1;

	/**
	 * The node type that represents an IPackage
	 */
	public static final int TYPE_ARCHIVE = 0;
	
	/**
	 * The node type that represents an IPackageReference
	 */
	public static final int TYPE_ARCHIVE_REFERENCE = 1;
	
	/**
	 * The node type that represents an IPackageFileSet
	 */
	public static final int TYPE_ARCHIVE_FILESET = 2;
	
	/**
	 * The node type that represents an IPackageFolder
	 */
	public static final int TYPE_ARCHIVE_FOLDER = 3;
	
	/**
	 * @return The parent of this package node, or null if this node is top level
	 */
	public IArchiveNode getParent();
	
	/**
	 * Set the parent of this package node 
	 * @param parent The new parent of this node
	 */
	public void setParent(IArchiveNode parent);
	
	/**
	 * @param type TYPE_PACKAGE, TYPE_PACKAGE_FILESET, or TYPE_PACKAGE_FOLDER
	 * @return An array of child nodes of the passed in type
	 */
	public IArchiveNode[] getChildren(int type);
	
	/**
	 * @return An array of all children nodes
	 */
	public IArchiveNode[] getAllChildren();
	
	/**
	 * @return Whether or not this node has children
	 */
	public boolean hasChildren();
	
	/**
	 * @param child A possible child node
	 * @return Whether or not the passed-in node is a child of this node
	 */
	public boolean hasChild(IArchiveNode child);
	
	/**
	 * @return The type of this package node
	 */
	public int getNodeType();
	
	/**
	 * @param property The name of the property to fetch
	 * @return The value of the specified property
	 */
	public String getProperty(String property);
	
	/**
	 * Set a property on this package node
	 * @param property The name of the property to set
	 * @param value The new value of the property
	 */
	public void setProperty(String property, String value);
	
	/**
	 * @return The project that this node is defined in (not necessarily the project where this is based if this is a fileset)
	 */
	public IPath getProjectPath();
	
	/**
	 * Recursively visit the package node tree below this node with the passed-in package node visitor.
	 * @param visitor A package node visitor
	 * @return Whether or not the entire sub-tree was visited
	 */
	public boolean accept(IArchiveNodeVisitor visitor);
	
	/**
	 * Recursively visit the package node tree below this node with the passed-in package node visitor, using depth-first ordering
	 * @param visitor A package node visitor
	 * @return Whether or not the entire sub-tree was visited
	 */
	public boolean accept(IArchiveNodeVisitor visitor, boolean depthFirst);
	
	/**
	 * Add a child node to this node
	 * @param child The child to add
	 */
	public void addChild(IArchiveNode child);
	
	/**
	 * Remove a child node from this node
	 * @param child The child to remove
	 */
	public void removeChild(IArchiveNode child);
	
	/**
	 * Get the highest parent that is not null. 
	 * @return
	 */
	public IArchiveNode getRoot();
	
	/**
	 * Get the model this node is attached to, or null if none
	 * @return
	 */
	public IArchiveModelNode getModel();
	
	/**
	 * Is the root of this node a PackageModelNode and registered in ArchivesModel?
	 * @return
	 */
	public boolean connectedToModel();
	
	/**
	 * Get the path relative to the root archive
	 * @return
	 */
	public IPath getRootArchiveRelativePath();
	
	/**
	 * Get the root top-level package for this node
	 * @return
	 */
	public IArchive getRootArchive();
	/**
	 * Get the current delta
	 * @return
	 */
	public IArchiveNodeDelta getDelta();
}
