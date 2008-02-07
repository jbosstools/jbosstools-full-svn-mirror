package org.jboss.ide.eclipse.archives.core.build;


import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFolder;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelListener;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeDelta;
import org.jboss.ide.eclipse.archives.core.model.events.EventManager;
import org.jboss.ide.eclipse.archives.core.util.ModelUtil;
import org.jboss.ide.eclipse.archives.core.util.internal.ModelTruezipBridge;

/**
 * This class responds to model change events. 
 * It is given a delta as to what nodes are added, removed, or changed. 
 * It then keeps the output file for the top level archive in sync with
 * the changes to the model.
 * 
 * If the automatic builder is not enabled for this project, the listener
 * does nothing. 
 * 
 * @author Rob Stryker (rob.stryker@redhat.com)
 *
 */
public class ModelChangeListener implements IArchiveModelListener {

	/**
	 * This is the entry point for model change events.
	 * It immediately passes the delta to be handled.
	 */
	public void modelChanged(IArchiveNodeDelta delta) {
		// if we're not building, get out
		if( !ArchivesCore.getInstance().getPreferenceManager().isBuilderEnabled(delta.getPostNode().getProjectPath())) 
			return;

		try {
			handle(delta);
		} catch( Exception e ) {
			e.printStackTrace();
		}
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
	private void handle(IArchiveNodeDelta delta) {
		if( isTopLevelArchive(delta.getPostNode())) 
			EventManager.startedBuildingArchive((IArchive)delta.getPostNode());

		if( (delta.getKind() & IArchiveNodeDelta.UNKNOWN_CHANGE) != 0 ) {
			nodeRemoved(delta.getPreNode());
			nodeAdded(delta.getPostNode());
		} if( (delta.getKind() & IArchiveNodeDelta.REMOVED) != 0 ) {
			nodeRemoved(delta.getPreNode());
		} else if( (delta.getKind() & IArchiveNodeDelta.ADDED) != 0 ) {
			nodeAdded(delta.getPostNode());
		} else  if( (delta.getKind() & IArchiveNodeDelta.ATTRIBUTE_CHANGED) != 0) {
			boolean shouldHandleChildren = handleAttributeChange(delta);
			if( shouldHandleChildren ) {
				IArchiveNodeDelta[] children = delta.getAllAffectedChildren();
				for( int i = 0; i < children.length; i++ ) {
					handle(children[i]);
				}
			}
		} else if( descendentChanged(delta.getKind()) ) { 
			IArchiveNodeDelta[] children = delta.getAllAffectedChildren();
			for( int i = 0; i < children.length; i++ ) {
				handle(children[i]);
			}
		}
		
		if( isTopLevelArchive(delta.getPostNode())) 
			EventManager.finishedBuildingArchive((IArchive)delta.getPostNode());

	}
	protected boolean descendentChanged(int kind) {
		 return (kind & IArchiveNodeDelta.DESCENDENT_CHANGED) != 0 || 
		 		(kind & IArchiveNodeDelta.CHILD_ADDED) != 0 ||
		 		(kind & IArchiveNodeDelta.CHILD_REMOVED) != 0;
	}
	protected boolean isTopLevelArchive(IArchiveNode node) {
		if( node != null && node instanceof IArchive && ((IArchive)node).isTopLevel())
			return true;
		return false;
	}
	/**
	 * Handle changes in this node
	 * @param delta
	 * @return Whether or not the caller should also handle the children
	 */
	private boolean handleAttributeChange(IArchiveNodeDelta delta) {
		switch( delta.getPostNode().getNodeType()) {
		case IArchiveNode.TYPE_ARCHIVE_FOLDER: 
			return handleFolderAttributeChanged(delta);
		case IArchiveNode.TYPE_ARCHIVE_FILESET:
			return handleFilesetAttributeChanged(delta);
		case IArchiveNode.TYPE_ARCHIVE:
			return handlePackageAttributeChanged(delta);
		}
		return false;
	}

	
	/*
	 * These three methods will need to be optimized eventually. Because right now they suck
	 */
	private boolean handleFolderAttributeChanged(IArchiveNodeDelta delta) {
		nodeRemoved(delta.getPreNode());
		nodeAdded(delta.getPostNode());
		return false;
	}
	
	private boolean handleFilesetAttributeChanged(IArchiveNodeDelta delta) {
		nodeRemoved(delta.getPreNode());
		nodeAdded(delta.getPostNode());
		return false;
	}
	
	private boolean handlePackageAttributeChanged(IArchiveNodeDelta delta) {
		nodeRemoved(delta.getPreNode());
		nodeAdded(delta.getPostNode());
		return false;
	}
	
	
	
	
	private void nodeAdded(IArchiveNode added) {
		if( added == null ) return;
		
		if( added.getNodeType() == IArchiveNode.TYPE_MODEL) {
			IArchiveNode[] archives = ((IArchiveModelNode)added).getChildren(IArchiveNode.TYPE_ARCHIVE);
			for( int i = 0; i < archives.length; i++ ) {
				nodeAdded(archives[i]);
			}
		} else if( added.getNodeType() == IArchiveNode.TYPE_ARCHIVE) {
			// create the package
			ModelTruezipBridge.createFile(added);
		} else if( added.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FOLDER ) {
			// create the folder
			ModelTruezipBridge.createFile(added);
		}
		IArchiveFileSet[] filesets = ModelUtil.findAllDescendentFilesets(added);
		for( int i = 0; i < filesets.length; i++ ) {
			ModelTruezipBridge.fullFilesetBuild(filesets[i]);
			IPath[] paths = filesets[i].findMatchingPaths();
			EventManager.filesUpdated(filesets[i].getRootArchive(), filesets[i], paths);
		}
		postChange(added);
	}
	
	
	private void nodeRemoved(IArchiveNode removed) {
		if( removed == null ) return;
		if( removed.getNodeType() == IArchiveNode.TYPE_MODEL ) {
			// remove all top level items
			IArchiveNode[] kids = removed.getChildren(IArchiveNode.TYPE_ARCHIVE);
			for( int i = 0; i < kids.length; i++ ) {
				nodeRemoved(kids[i]);
			}
			postChange(removed);
			return;
		} else if( removed.getNodeType() == IArchiveNode.TYPE_ARCHIVE) {
			ModelTruezipBridge.deleteArchive((IArchive)removed);
			postChange(removed);
			return;
		} else if( removed.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FOLDER ){
			IArchiveFileSet[] filesets = ModelUtil.findAllDescendentFilesets(((IArchiveFolder)removed));
			for( int i = 0; i < filesets.length; i++ ) {
				IPath[] removedPaths = ModelTruezipBridge.fullFilesetRemove(filesets[i], false);
				EventManager.filesRemoved(removedPaths, ((IArchiveFileSet)filesets[i]));
			}
			postChange(removed);
			return;
		} 

		IArchiveFileSet[] filesets = ModelUtil.findAllDescendentFilesets(removed);
		for( int i = 0; i < filesets.length; i++ ) {
			IPath[] removedPaths = ModelTruezipBridge.fullFilesetRemove(((IArchiveFileSet)removed), false);
			EventManager.filesRemoved(removedPaths, ((IArchiveFileSet)removed));
		}
		postChange(removed);
	}
	
	
	protected void postChange(IArchiveNode node) {
	}
}
