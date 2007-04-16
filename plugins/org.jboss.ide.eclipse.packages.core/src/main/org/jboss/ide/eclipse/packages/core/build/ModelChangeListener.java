package org.jboss.ide.eclipse.packages.core.build;

import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackagesModelDelta;
import org.jboss.ide.eclipse.packages.core.model.IPackagesModelListener;
import org.jboss.ide.eclipse.packages.core.util.ModelTruezipBridge;
import org.jboss.ide.eclipse.packages.core.util.ModelUtil;

public class ModelChangeListener implements IPackagesModelListener {

	/**
	 * This is the entry point for model change events.
	 * It immediately passes the delta to be handled.
	 */
	public void modelChanged(IPackagesModelDelta delta) {
		handle(delta);
	}

	/**
	 * This can handle any type of node / delta, not just
	 * root elements. If the node is added or removed, it 
	 * will handle those segments and return without checking
	 * the children at all. IT is the responsibility of the add
	 * and remove methods to go through the children.
	 * 
	 *  Otherwise, it will simply handle attribute children and then
	 *  move on to the children.
	 * 
	 * @param delta
	 */
	private void handle(IPackagesModelDelta delta) {
		if( (delta.getKind() & IPackagesModelDelta.REMOVED) != 0 ) {
			nodeRemoved(delta.getPreNode());
			return;
		} else if( (delta.getKind() & IPackagesModelDelta.ADDED) != 0 ) {
			nodeAdded(delta.getPostNode());
			return;
		} else  if( (delta.getKind() & IPackagesModelDelta.ATTRIBUTE_CHANGED) != 0) {
			boolean shouldHandleChildren = handleAttributeChange(delta);
			if( shouldHandleChildren ) {
				IPackagesModelDelta[] children = delta.getAffectedChildren();
				for( int i = 0; i < children.length; i++ ) {
					handle(children[i]);
				}
			}
		} else if( descendentChanged(delta.getKind()) ) { 
			IPackagesModelDelta[] children = delta.getAffectedChildren();
			for( int i = 0; i < children.length; i++ ) {
				handle(children[i]);
			}
		}
	}
	protected boolean descendentChanged(int kind) {
		 return (kind & IPackagesModelDelta.DESCENDENT_CHANGED) != 0 || 
		 		(kind & IPackagesModelDelta.CHILD_ADDED) != 0 ||
		 		(kind & IPackagesModelDelta.CHILD_REMOVED) != 0;
	}

	/**
	 * Handle changes in this node
	 * @param delta
	 * @return Whether or not the caller should also handle the children
	 */
	private boolean handleAttributeChange(IPackagesModelDelta delta) {
		switch( delta.getPostNode().getNodeType()) {
		case IPackageNode.TYPE_PACKAGE_FOLDER: 
			return handleFolderAttributeChanged(delta);
		case IPackageNode.TYPE_PACKAGE_FILESET:
			return handleFilesetAttributeChanged(delta);
		case IPackageNode.TYPE_PACKAGE:
			return handlePackageAttributeChanged(delta);
		}
		return false;
	}

	private boolean handleFolderAttributeChanged(IPackagesModelDelta delta) {
		nodeRemoved(delta.getPreNode());
		nodeAdded(delta.getPostNode());
		return false;
	}
	
	private boolean handleFilesetAttributeChanged(IPackagesModelDelta delta) {
		nodeRemoved(delta.getPreNode());
		nodeAdded(delta.getPostNode());
		return false;
	}
	
	private boolean handlePackageAttributeChanged(IPackagesModelDelta delta) {
		nodeRemoved(delta.getPreNode());
		nodeAdded(delta.getPostNode());
		return false;
	}
	
	
	
	
	private void nodeAdded(IPackageNode added) {
		if( added.getNodeType() == IPackageNode.TYPE_PACKAGE) {
			// create the package
			ModelTruezipBridge.createContainer(added);
		} else if( added.getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER ) {
			// create hte folder
			ModelTruezipBridge.createContainer(added);
		}
		IPackageFileSet[] filesets = ModelUtil.findAllDescendentFilesets(added);
		for( int i = 0; i < filesets.length; i++ ) {
			ModelTruezipBridge.fullFilesetBuild(filesets[i]);
		}

	}
	
	
	private void nodeRemoved(IPackageNode removed) {
		if( removed.getNodeType() == IPackageNode.TYPE_PACKAGE) {
			ModelTruezipBridge.deletePackage((IPackage)removed);
			return;
		} else if( removed.getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER ){
			ModelTruezipBridge.deleteFolder((IPackageFolder)removed);
			return;
		}

		IPackageFileSet[] filesets = ModelUtil.findAllDescendentFilesets(removed);
		for( int i = 0; i < filesets.length; i++ ) {
			ModelTruezipBridge.fullFilesetRemove(filesets[i]);
		}
	}
}
