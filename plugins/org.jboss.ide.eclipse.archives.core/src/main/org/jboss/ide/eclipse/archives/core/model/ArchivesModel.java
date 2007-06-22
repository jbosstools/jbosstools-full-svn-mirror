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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.Trace;
import org.jboss.ide.eclipse.archives.core.model.events.EventManager;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveFileSetImpl;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveFolderImpl;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveImpl;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveModelNode;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveNodeImpl;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XMLBinding;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbFileSet;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbFolder;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackage;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackageNode;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackages;
import org.jboss.ide.eclipse.archives.core.model.other.IArchiveBuildListener;
import org.jboss.ide.eclipse.archives.core.model.other.IArchiveModelListener;

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
	
	private HashMap xbPackages; // maps an IPath (of a project) to XbPackages
	private HashMap archivesRoot; // maps an IPath (of a project) to PackageModelNode, aka root
	private ArrayList buildListeners, modelListeners;
	public ArchivesModel() {
		xbPackages = new HashMap();
		archivesRoot = new HashMap();
		buildListeners = new ArrayList();
		modelListeners = new ArrayList();
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
		return (IArchiveBuildListener[]) buildListeners.toArray(new IArchiveBuildListener[buildListeners.size()]);
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
		return (IArchiveModelListener[]) modelListeners.toArray(new IArchiveModelListener[modelListeners.size()]);
	}

	
	
	
	
	
	public XbPackages getXbPackages(IPath project) {
		return (XbPackages)(xbPackages.get(project));
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
		return (XbPackages)(xbPackages.get(project));
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
		Iterator i = archivesRoot.keySet().iterator();
		int x = 0;
		while(i.hasNext()) {
			ret[x++] = (ArchiveModelNode)archivesRoot.get(i.next());
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
		return (IArchiveModelNode)(archivesRoot.get(project));
	}
	
	public IArchive[] getProjectArchives(IPath project) {
		return getProjectArchives(project, false, new NullProgressMonitor());
	}
	public IArchive[] getProjectArchives(IPath project, boolean register, IProgressMonitor monitor) {
		IArchiveModelNode root = getRoot(project, register, monitor);
		if( root != null ) {
			List list = Arrays.asList( getRoot(project, register, monitor).getAllChildren());
			return (IArchive[]) list.toArray(new IArchive[list.size()]);
		} else {
			registerProject(project, monitor);
			List list = Arrays.asList( getRoot(project, register, monitor).getAllChildren());
			return (IArchive[]) list.toArray(new IArchive[list.size()]);
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
		
		ArchivesCore.getInstance().preRegister(project);
		
		ArchiveModelNode root;
		IPath packagesFile = project.append(PROJECT_PACKAGES_FILE);
		if (packagesFile.toFile().exists())
		{
			try {
				FileInputStream is = new FileInputStream(packagesFile.toFile());
				XbPackages packages = XMLBinding.unmarshal(is, monitor);
				monitor.worked(1);
				
				if (packages == null) {
					// Empty / non-working XML file loaded
					Trace.trace(getClass(), "WARNING: .packages file for project " + project.lastSegment() + " is empty or contains the wrong content");
					return;
				}
				root = new ArchiveModelNode(project, packages, this);
				xbPackages.put(project, packages);
				archivesRoot.put(project, root);
				createPackageNodeImpl(project, packages, null);
				root.clearDeltas();
				monitor.worked(1);
			} catch (FileNotFoundException e) {
				Trace.trace(getClass(), e);
			}
		} else {
			// file not found, just create some default xbpackages and insert them
			XbPackages packages = new XbPackages();
			xbPackages.put(project, packages);
			archivesRoot.put(project, new ArchiveModelNode(project, packages, this));
		}
	}
	
	public ArchiveNodeImpl createPackageNodeImpl (IPath project, XbPackageNode node, IArchiveNode parent) {
		
		if( node instanceof XbPackages ) {
			ArchiveModelNode impl = (ArchiveModelNode)getRoot(project);
			for (Iterator iter = node.getAllChildren().iterator(); iter.hasNext(); ) {
				XbPackageNode child = (XbPackageNode) iter.next();
				ArchiveNodeImpl childImpl = createPackageNodeImpl(project, child, impl);
				if (impl != null && childImpl != null) {
					impl.addChild(childImpl, false);
				}
			}
			return null;
		}
		
		ArchiveNodeImpl nodeImpl = null;
		if (node instanceof XbPackage) {
			nodeImpl = new ArchiveImpl((XbPackage)node);
		} else if (node instanceof XbFolder) {
			nodeImpl = new ArchiveFolderImpl((XbFolder)node);
		} else if (node instanceof XbFileSet) {
			nodeImpl = new ArchiveFileSetImpl((XbFileSet)node);
		}
		
		for (Iterator iter = node.getAllChildren().iterator(); iter.hasNext(); ) {
			XbPackageNode child = (XbPackageNode) iter.next();
			ArchiveNodeImpl childImpl = createPackageNodeImpl(project, child, nodeImpl);
			if (nodeImpl != null && childImpl != null) {
				nodeImpl.addChild(childImpl, false);
			}
		}
		
		return nodeImpl;
	}
	
	public void saveModel (IPath project, IProgressMonitor monitor) {
		// get a list of dirty nodes
		
		if (monitor == null)
			monitor = new NullProgressMonitor();
		
		IPath packagesFile = project.append(ArchivesModel.PROJECT_PACKAGES_FILE);
		XbPackages packs = getXbPackages(project);

		XMLBinding.savePackagesToFile(packs, packagesFile, monitor);
		
		// get deltas
		try {
			ArchiveModelNode root = (ArchiveModelNode)getRoot(project);
			IArchiveNodeDelta delta = root.getDelta();
			
			// clear deltas
			root.clearDeltas();
			
			// fire delta events
			EventManager.fireDelta(delta);
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	public void attach(IArchiveNode parent, IArchiveNode child, IProgressMonitor monitor) {
		parent.addChild(child);
		if( parent.connectedToModel() && parent.getProjectPath() != null) {
			// save
			saveModel(parent.getProjectPath(), monitor);
		}
	}
}
