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
package org.jboss.ide.eclipse.archives.core.build;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.ide.eclipse.archives.core.CorePreferenceManager;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFolder;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelListener;
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
		if( !CorePreferenceManager.isBuilderEnabled(delta.getPostNode().getProject())) 
			return;

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
	private void handle(IArchiveNodeDelta delta) {
		if( (delta.getKind() & IArchiveNodeDelta.REMOVED) != 0 ) {
			nodeRemoved(delta.getPreNode());
			return;
		} else if( (delta.getKind() & IArchiveNodeDelta.ADDED) != 0 ) {
			nodeAdded(delta.getPostNode());
			return;
		} else  if( (delta.getKind() & IArchiveNodeDelta.ATTRIBUTE_CHANGED) != 0) {
			boolean shouldHandleChildren = handleAttributeChange(delta);
			if( shouldHandleChildren ) {
				IArchiveNodeDelta[] children = delta.getAffectedChildren();
				for( int i = 0; i < children.length; i++ ) {
					handle(children[i]);
				}
			}
		} else if( descendentChanged(delta.getKind()) ) { 
			IArchiveNodeDelta[] children = delta.getAffectedChildren();
			for( int i = 0; i < children.length; i++ ) {
				handle(children[i]);
			}
		}
	}
	protected boolean descendentChanged(int kind) {
		 return (kind & IArchiveNodeDelta.DESCENDENT_CHANGED) != 0 || 
		 		(kind & IArchiveNodeDelta.CHILD_ADDED) != 0 ||
		 		(kind & IArchiveNodeDelta.CHILD_REMOVED) != 0;
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
		if( added.getNodeType() == IArchiveNode.TYPE_ARCHIVE) {
			// create the package
			ModelTruezipBridge.createFile(added);
		} else if( added.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FOLDER ) {
			// create hte folder
			ModelTruezipBridge.createFile(added);
		}
		IArchiveFileSet[] filesets = ModelUtil.findAllDescendentFilesets(added);
		for( int i = 0; i < filesets.length; i++ ) {
			ModelTruezipBridge.fullFilesetBuild(filesets[i]);
			IPath[] paths = filesets[i].findMatchingPaths();
			EventManager.filesUpdated(filesets[i].getRootArchive(), filesets[i], paths);
		}
		refreshLocal(added);
	}
	
	
	private void nodeRemoved(IArchiveNode removed) {
		if( removed.getNodeType() == IArchiveNode.TYPE_ARCHIVE) {
			ModelTruezipBridge.deleteArchive((IArchive)removed);
			refreshLocal(removed);
			return;
		} else if( removed.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FOLDER ){
			IArchiveFileSet[] filesets = ModelUtil.findAllDescendentFilesets(((IArchiveFolder)removed));
			for( int i = 0; i < filesets.length; i++ ) {
				IPath[] removedPaths = ModelTruezipBridge.fullFilesetRemove(((IArchiveFileSet)removed), false);
				EventManager.filesRemoved(removedPaths, ((IArchiveFileSet)removed));
			}
			refreshLocal(removed);
			return;
		}

		IArchiveFileSet[] filesets = ModelUtil.findAllDescendentFilesets(removed);
		for( int i = 0; i < filesets.length; i++ ) {
			IPath[] removedPaths = ModelTruezipBridge.fullFilesetRemove(((IArchiveFileSet)removed), false);
			EventManager.filesRemoved(removedPaths, ((IArchiveFileSet)removed));
		}
		refreshLocal(removed);
	}
	
	
	// refresh the file tree structure
	private void refreshLocal(IArchiveNode node) {
		IArchive pack = node.getRootArchive();
		if( pack != null && pack.isDestinationInWorkspace() ) {
			// refresh the root package node
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IResource res = root.getContainerForLocation(pack.getDestinationPath());
			if( res != null ) {
				try {
					res.getParent().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
				} catch( CoreException ce ) {
				}
			}
		}

	}
}
