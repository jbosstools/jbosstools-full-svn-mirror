package org.jboss.ide.eclipse.packages.core.model;

import org.eclipse.core.resources.IProject;

/**
 * Implementations of this interface should register themselves to listen with:
 * <code>
 * 	PackagesCore.registerPackagesModelListener(listener)
 * </code>
 * 
 * @author Marshall
 */
public interface IPackagesModelListener {

	/**
	 * A project was registered with the model
	 * @param project The registered project
	 */
	public void projectRegistered (IProject project);
	
	/**
	 * A node was added to the model
	 * @param added The added node
	 */
	public void packageNodeAdded (IPackageNode added);
	
	/**
	 * A node was removed from the model
	 * @param removed The removed node
	 */
	public void packageNodeRemoved (IPackageNode removed);
	
	/**
	 * A node's data/attirbutes were changed
	 * @param changed The changed node
	 */
	public void packageNodeChanged (IPackageNode changed);
	
	/**
	 * A node has been attached to the model. This will only be called once
	 * for the top level attached node, any processing of sub-nodes will need to happen using IPackageNode.accept.
	 * 
	 * @param attached The node that was attached
	 */
	public void packageNodeAttached (IPackageNode attached);
}
