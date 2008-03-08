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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveModelNode;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XMLBinding;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackages;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XMLBinding.XbException;
import org.jboss.ide.eclipse.archives.core.util.ModelUtil;

/**
 * The root model which keeps track of registered projects
 * and what archives / model nodes they contain.
 * 
 * @author <a href="rob.stryker@redhat.com">Rob Stryker</a>
 */
public class ArchivesModel implements IArchiveModelListenerManager {
	
	/**
	 * The packages file name
	 */
	public static final String PROJECT_PACKAGES_FILE = ".packages";
	
	/**
	 * Singleton instance
	 */
	protected static ArchivesModel instance;
	public static ArchivesModel instance() {
		if( instance == null ) 
			instance = new ArchivesModel();
		return instance;
	}
	
	private HashMap<IPath, XbPackages> xbPackages; // maps an IPath (of a project) to XbPackages
	private HashMap<IPath, ArchiveModelNode> archivesRoot; // maps an IPath (of a project) to PackageModelNode, aka root
	private ArrayList<IArchiveBuildListener> buildListeners;
	private ArrayList<IArchiveModelListener> modelListeners;
	public ArchivesModel() {
		xbPackages = new HashMap<IPath, XbPackages>();
		archivesRoot = new HashMap<IPath, ArchiveModelNode>();
		buildListeners = new ArrayList<IArchiveBuildListener>();
		modelListeners = new ArrayList<IArchiveModelListener>();
	}
	
	
	public static final int LIST_FRONT = 0;
	public static final int LIST_BACK = -1;
	
	/*
	 * Listeners
	 */
	public void addBuildListener(IArchiveBuildListener listener) {
		if( !buildListeners.contains(listener)) 
			buildListeners.add(listener);
	}
	public void addBuildListener(IArchiveBuildListener listener, int loc) {
		if( !buildListeners.contains(listener)) {
			if( loc == LIST_FRONT )
				buildListeners.add(0, listener);
			else
				buildListeners.add(listener);				
		}
	}
	
	public void removeBuildListener(IArchiveBuildListener listener) {
		buildListeners.remove(listener);
	}
	public IArchiveBuildListener[] getBuildListeners() {
		return buildListeners.toArray(new IArchiveBuildListener[buildListeners.size()]);
	}
	
	public void addModelListener(IArchiveModelListener listener) {
		if( !modelListeners.contains(listener)) 
			modelListeners.add(listener);
	}
	public void addModelListener(IArchiveModelListener listener, int loc) {
		if( !modelListeners.contains(listener)) {
			if( loc == LIST_FRONT )
				modelListeners.add(0, listener);
			else
				modelListeners.add(listener);				
		}
	}
	
	public void removeModelListener(IArchiveModelListener listener) {
		if( modelListeners.contains(listener)) 
			modelListeners.remove(listener);
	}
	public IArchiveModelListener[] getModelListeners() {
		return modelListeners.toArray(new IArchiveModelListener[modelListeners.size()]);
	}

	
	
	
	
	
	public XbPackages getXbPackages(IPath project) {
		return (xbPackages.get(project));
	}
	
	/**
	 * If the project hasn't been registered, register it
	 * @param project
	 * @param monitor
	 * @return
	 */
	public XbPackages getXbPackages(IPath project, IProgressMonitor monitor) {
		if( !xbPackages.containsKey(project)) 
			registerProject(project, monitor);
		return (xbPackages.get(project));
	}
	
	/**
	 * Accept a visitor
	 */
	public boolean accept(IArchiveNodeVisitor visitor) {
		IArchiveNode children[] = getAllArchives();
		boolean keepGoing = true;

		if (keepGoing) {
			for (int i = 0; i < children.length; i++) {
				if (keepGoing) {
					keepGoing = children[i].accept(visitor);
				}
			}
		}
		
		return keepGoing;
	}	
	
	/**
	 * Gets every single *registered* model
	 * @return
	 */
	protected ArchiveModelNode[] getAllArchives() {
		ArchiveModelNode[] ret = new ArchiveModelNode[archivesRoot.keySet().size()];
		Iterator<IPath> i = archivesRoot.keySet().iterator();
		int x = 0;
		while(i.hasNext()) {
			ret[x++] = archivesRoot.get(i.next());
		}
		return ret;
	}
	
	/**
	 * Get the root node for this object
	 * @param project
	 * @return
	 */
	public IArchiveModelNode getRoot(IPath project) {
		return getRoot(project, false, new NullProgressMonitor());
	}
	
	public IArchiveModelNode getRoot(IPath project, boolean register, IProgressMonitor monitor) {
		if( archivesRoot.get(project) == null && register ) {
			registerProject(project, monitor);
		}
		return (archivesRoot.get(project));
	}
	
	public IArchive[] getProjectArchives(IPath project) {
		return getProjectArchives(project, false, new NullProgressMonitor());
	}
	public IArchive[] getProjectArchives(IPath project, boolean register, IProgressMonitor monitor) {
		IArchiveModelNode root = getRoot(project, register, monitor);
		if( root != null ) {
			List<IArchiveNode> list = Arrays.asList( getRoot(project, register, monitor).getAllChildren());
			return list.toArray(new IArchive[list.size()]);
		} else if( register) {
			registerProject(project, monitor);
			List<IArchiveNode> list = Arrays.asList( getRoot(project, register, monitor).getAllChildren());
			return list.toArray(new IArchive[list.size()]);
		} else {
			return new IArchive[] {};
		}
	}
	
	// to make sure the node root is actually in the model
	public boolean containsRoot(ArchiveModelNode node) {
		return archivesRoot.containsValue(node);
	}
	
	public void registerProject(IPath project, IProgressMonitor monitor) {
		// if the file exists, read it in
		if( monitor == null ) monitor = new NullProgressMonitor();
		monitor.beginTask("Loading configuration...", XMLBinding.NUM_UNMARSHAL_MONITOR_STEPS + 2);
		
		ArchivesCore.getInstance().preRegisterProject(project);
		
		ArchiveModelNode root;
		Exception e = null;
		IPath packagesFile = project.append(PROJECT_PACKAGES_FILE);
		if (packagesFile.toFile().exists()) {
			XbPackages packages = null;
			try {
				FileInputStream is = new FileInputStream(packagesFile.toFile());
				packages = XMLBinding.unmarshal(is, monitor);
				monitor.worked(1);
			} catch (FileNotFoundException f) {
				e = f;
			} catch( XbException xbe) {
				e = xbe;
			}
				
			if (packages == null) {
				// Empty / non-working XML file loaded
				ArchivesCore.getInstance().getLogger().log(IArchivesLogger.MSG_ERR, "Could not unmarshall packages file", e);
				return;
			}
			
			root = new ArchiveModelNode(project, packages, this);
			ArchiveModelNode oldRoot = archivesRoot.get(project);
			xbPackages.put(project, packages);
			archivesRoot.put(project, root);
			ModelUtil.fillArchiveModel(packages, (ArchiveModelNode)getRoot(project));
			root.clearDeltas();
			fireRegisterProjectEvent(oldRoot, root);
			monitor.worked(1);
		} else {
			// file not found, just create some default xbpackages and insert them
			XbPackages packages = new XbPackages();
			ArchiveModelNode root2 = new ArchiveModelNode(project, packages, this);
			xbPackages.put(project, packages);
			archivesRoot.put(project, root2);
			fireRegisterProjectEvent(null, root2);
		}
	}
	
	protected void fireRegisterProjectEvent(final ArchiveModelNode oldRoot, final ArchiveModelNode newRoot) {
		IArchiveNodeDelta delta = new IArchiveNodeDelta() {
			public IArchiveNodeDelta[] getAddedChildrenDeltas() {return null;}
			public IArchiveNodeDelta[] getAllAffectedChildren() {return null;}
			public INodeDelta getAttributeDelta(String key) {return null;}
			public String[] getAttributesWithDeltas() {return null;}
			public IArchiveNodeDelta[] getChangedDescendentDeltas() {return null;}
			public int getKind() {return IArchiveNodeDelta.UNKNOWN_CHANGE;}
			public IArchiveNode getPostNode() {return newRoot;}
			public IArchiveNode getPreNode() { return oldRoot; }
			public String[] getPropertiesWithDeltas() {return null;}
			public INodeDelta getPropertyDelta(String key) {return null;}
			public IArchiveNodeDelta[] getRemovedChildrenDeltas() {return null;}
		};
		EventManager.fireDelta(delta);
	}
	

	public void saveModel (IPath project, IProgressMonitor monitor) {
		// get a list of dirty nodes
		
		if (monitor == null)
			monitor = new NullProgressMonitor();
		
		IPath packagesFile = project.append(ArchivesModel.PROJECT_PACKAGES_FILE);
		XbPackages packs = getXbPackages(project);
		try {
			XMLBinding.marshallToFile(packs, packagesFile, monitor);
		} catch( IOException ioe ) {
			ArchivesCore.getInstance().getLogger().log(IArchivesLogger.MSG_ERR, "Could not marshall packages file", ioe);
			return;
		} catch( XbException xbe ) {
			ArchivesCore.getInstance().getLogger().log(IArchivesLogger.MSG_ERR, "Could not marshall packages file", xbe);
			return;
		}
		
		// get deltas
		ArchiveModelNode root = (ArchiveModelNode)getRoot(project);
		IArchiveNodeDelta delta = root.getDelta();
		
		// clear deltas
		root.clearDeltas();
		
		// fire delta events
		EventManager.fireDelta(delta);
	}

	// TODO: This requires massive help and new API's as well. 
	public void attach(IArchiveNode parent, IArchiveNode child, IProgressMonitor monitor) {
		parent.addChild(child);
		if( parent.connectedToModel() && parent.getProjectPath() != null) {
			// save
			saveModel(parent.getProjectPath(), monitor);
		}
	}
}
