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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
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
public class ArchivesModel implements IArchiveModel {
	
	/**
	 * Singleton instance
	 */
	protected static IArchiveModel instance;
	public static IArchiveModel instance() {
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
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveModel#addBuildListener(org.jboss.ide.eclipse.archives.core.model.IArchiveBuildListener)
	 */
	public void addBuildListener(IArchiveBuildListener listener) {
		if( !buildListeners.contains(listener)) 
			buildListeners.add(listener);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveModel#removeBuildListener(org.jboss.ide.eclipse.archives.core.model.IArchiveBuildListener)
	 */
	public void removeBuildListener(IArchiveBuildListener listener) {
		buildListeners.remove(listener);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveModel#getBuildListeners()
	 */
	public IArchiveBuildListener[] getBuildListeners() {
		return buildListeners.toArray(new IArchiveBuildListener[buildListeners.size()]);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveModel#addModelListener(org.jboss.ide.eclipse.archives.core.model.IArchiveModelListener)
	 */
	public void addModelListener(IArchiveModelListener listener) {
		if( !modelListeners.contains(listener)) 
			modelListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveModel#removeModelListener(org.jboss.ide.eclipse.archives.core.model.IArchiveModelListener)
	 */
	public void removeModelListener(IArchiveModelListener listener) {
		if( modelListeners.contains(listener)) 
			modelListeners.remove(listener);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveModel#getModelListeners()
	 */
	public IArchiveModelListener[] getModelListeners() {
		return modelListeners.toArray(new IArchiveModelListener[modelListeners.size()]);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveModel#getModelNodes()
	 */
	public IArchiveModelRootNode[] getModelNodes() {
		Collection<ArchiveModelNode> c = archivesRoot.values();
		return (IArchiveModelRootNode[]) c.toArray(new IArchiveModelRootNode[c.size()]);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveModel#accept(org.jboss.ide.eclipse.archives.core.model.IArchiveNodeVisitor)
	 */
	public boolean accept(IArchiveNodeVisitor visitor) {
		IArchiveModelRootNode[] children = getModelNodes();
		boolean keepGoing = true;
		if (keepGoing)
			for (int i = 0; i < children.length; i++)
				if (keepGoing)
					keepGoing = children[i].accept(visitor);
		return keepGoing;
	}	


	public IArchiveModelRootNode getRoot(IPath project) {
		return (archivesRoot.get(project));
	}
	
	@Deprecated
	public void save(IPath projectPath, IProgressMonitor monitor) throws ArchivesModelException {
		save(getRoot(projectPath), monitor);
	}
	
	@Deprecated
	public void save(IArchiveModelRootNode modelNode, IProgressMonitor monitor) throws ArchivesModelException {
		modelNode.save(monitor);
	}
	
	public boolean isProjectRegistered(IPath projectPath) {
		return archivesRoot.containsKey(projectPath);
	}
	
	public boolean canReregister(IPath projectPath) {
		return canReregister(projectPath, DEFAULT_PACKAGES_FILE);
	}
	
	public boolean canReregister(IPath projectPath, String file) {
		return projectPath.append(file).toFile().exists();
	}
	
	public IArchiveModelRootNode registerProject(IPath projectPath, IProgressMonitor monitor) throws ArchivesModelException {
		return registerProject(projectPath, DEFAULT_PACKAGES_FILE, monitor);
	}
	
	public IArchiveModelRootNode registerProject(IPath projectPath, String file, IProgressMonitor monitor) throws ArchivesModelException {
		XbPackages packages;
		ArchiveModelNode modelNode;

		IPath packagesFile = projectPath.append(file);
		if (packagesFile.toFile().exists()) {
			try {
				packages = XMLBinding.unmarshal(packagesFile.toFile(), monitor);
			} catch( XbException xbe ) {
				// Empty / non-working XML file loaded
				ArchivesCore.getInstance().getLogger().log(IArchivesLogger.MSG_ERR, "Error unmarshalling packages file " + packagesFile, xbe);
				return null;
			}
		} else {
			packages = new XbPackages();
			packages.setVersion(1.2);
		}
		
		// Fill the model
		modelNode = new ArchiveModelNode(projectPath, projectPath.append(file), packages, this);
		ModelUtil.fillArchiveModel(packages, modelNode);
		modelNode.clearDelta();
		
		registerProject(modelNode, monitor);
		return modelNode;
	}
	
	public void registerProject(IArchiveModelRootNode model, IProgressMonitor monitor) {
		ArchivesCore.getInstance().preRegisterProject(model.getProjectPath());
		xbPackages.put(model.getProjectPath(), ((ArchiveModelNode)model).getXbPackages());
		archivesRoot.put(model.getProjectPath(), (ArchiveModelNode)model);
		model.setModel(this);
		fireRegisterProjectEvent((ArchiveModelNode)model);
	}
	
	public void unregisterProject(IPath projectPath, IProgressMonitor monitor) {
		IArchiveModelRootNode root = getRoot(projectPath);
		xbPackages.remove(projectPath);
		archivesRoot.remove(projectPath);
		fireUnregisterProjectEvent(root);
	}

	public void unregisterProject(IArchiveModelRootNode model, IProgressMonitor monitor) {
		xbPackages.remove(model.getProjectPath());
		archivesRoot.remove(model.getProjectPath());
		fireUnregisterProjectEvent((ArchiveModelNode)model);
	}
	
	protected void fireRegisterProjectEvent(final IArchiveModelRootNode newRoot) {
		fireRegistrationEvent(null, newRoot, IArchiveNodeDelta.NODE_REGISTERED);
	}
	
	protected void fireUnregisterProjectEvent(final IArchiveModelRootNode oldRoot) {
		fireRegistrationEvent(oldRoot, null, IArchiveNodeDelta.NODE_UNREGISTERED);
	}
	
	protected void fireRegistrationEvent(final IArchiveModelRootNode oldRoot, final IArchiveModelRootNode newRoot, final int type) {
		IArchiveNodeDelta delta = new IArchiveNodeDelta() {
			public IArchiveNodeDelta[] getAddedChildrenDeltas() {return null;}
			public IArchiveNodeDelta[] getAllAffectedChildren() {return null;}
			public INodeDelta getAttributeDelta(String key) {return null;}
			public String[] getAttributesWithDeltas() {return null;}
			public IArchiveNodeDelta[] getChangedDescendentDeltas() {return null;}
			public int getKind() {return type;}
			public IArchiveNode getPostNode() {return newRoot;}
			public IArchiveNode getPreNode() { return oldRoot; }
			public String[] getPropertiesWithDeltas() {return null;}
			public INodeDelta getPropertyDelta(String key) {return null;}
			public IArchiveNodeDelta[] getRemovedChildrenDeltas() {return null;}
		};
		EventManager.fireDelta(delta);
	}
}
