package org.jboss.ide.eclipse.packages.core.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;

/**
 * The base the entire package node class hierarchy.
 * 
 * Each node in a package may have arbitrary properties that can be reflected upon by other plug-ins
 * 
 * @author <a href="marshall@jboss.org">Marshall Culpepper</a>
 * @version $Revision$
 */
public interface IPackageNodeBase extends IAdaptable {

	/**
	 * The node type that represents an IPackage
	 */
	public static final int TYPE_PACKAGE = 0;
	
	/**
	 * The node type that represents an IPackageFileSet
	 */
	public static final int TYPE_PACKAGE_FILESET = 1;
	
	/**
	 * The node type that represents an IPackageFolder
	 */
	public static final int TYPE_PACKAGE_FOLDER = 2;
	
	/**
	 * @return The parent of this package node, or null if this node is top level
	 */
	public IPackageNodeBase getParent();
	
	/**
	 * @param type TYPE_PACKAGE, TYPE_PACKAGE_FILESET, or TYPE_PACKAGE_FOLDER
	 * @return An array of child nodes of the passed in type
	 */
	public IPackageNodeBase[] getChildren(int type);
	
	/**
	 * @return An array of all children nodes
	 */
	public IPackageNodeBase[] getAllChildren();
	
	/**
	 * @return Whether or not this node has children
	 */
	public boolean hasChildren();
	
	/**
	 * @param child A possible child node
	 * @return Whether or not the passed-in node is a child of this node
	 */
	public boolean hasChild(IPackageNodeBase child);
	
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
	 * @return The project that this node is defined in (not necessarily the project where this is based if this is a fileset)
	 */
	public IProject getProject();
	
	/**
	 * Recursively visit the package node tree below this node with the passed-in package node visitor.
	 * @param visitor A package node visitor
	 * @return Whether or not the entire sub-tree was visited
	 */
	public boolean accept(IPackageNodeVisitor visitor);
	
	/**
	 * Recursively visit the package node tree below this node with the passed-in package node visitor, using depth-first ordering
	 * @param visitor A package node visitor
	 * @return Whether or not the entire sub-tree was visited
	 */
	public boolean accept(IPackageNodeVisitor visitor, boolean depthFirst);
	
	/**
	 * @return A working copy of this package node
	 */
	public IPackageNodeWorkingCopy createWorkingCopy();

	/**
	 * @return Whether or not this node has an outstanding working copy
	 */
	public boolean hasWorkingCopy();
}
