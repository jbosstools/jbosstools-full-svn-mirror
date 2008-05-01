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
package org.jboss.ide.eclipse.archives.core.model.internal;

import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModelException;
import org.jboss.ide.eclipse.archives.core.model.EventManager;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModel;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelRootNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeDelta;
import org.jboss.ide.eclipse.archives.core.model.INamedContainerArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XMLBinding;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackages;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XMLBinding.XbException;

/**
 * 
 * @author rob.stryker <rob.stryker@redhat.com>
 *
 */
public class ArchiveModelNode extends ArchiveNodeImpl implements IArchiveModelRootNode {
	private IPath project;
	private IPath descriptor;
	private IArchiveModel model;
	
	public ArchiveModelNode(IPath project, XbPackages node) {
		this(project, null, node);
	}
	
	public ArchiveModelNode(IPath project, IPath descriptor, XbPackages node) {
		this(project, descriptor, node, null);
	}
	
	public ArchiveModelNode(IPath project, XbPackages node, IArchiveModel model) {
		this(project, null, node, model);
	}
	
	public ArchiveModelNode(IPath project, IPath descriptor,
			XbPackages node, IArchiveModel model) {
		super(node);
		this.project = project;
		this.descriptor = descriptor != null ? descriptor : 
				project.append(IArchiveModel.DEFAULT_PACKAGES_FILE);
		this.model = model;
	}
	
	public IPath getDescriptor() {
		return descriptor;
	}
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveModelNode#getManager()
	 */
	public IArchiveModel getModel() {
		return model;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.internal.ArchiveNodeImpl#getProject()
	 */
	public IPath getProjectPath() {
		return project;
	}
	
	public XbPackages getXbPackages() {
		return (XbPackages)nodeDelegate;
	}
	
	/**
	 * The model root can only accept IArchive's as children
	 * @see IArchiveNode#addChild(IArchiveNode)
	 */
	protected boolean validateChild(IArchiveNode child) {
		if( child.getNodeType() != IArchiveNode.TYPE_ARCHIVE)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#getNodeType()
	 */
	public int getNodeType() {
		return IArchiveNode.TYPE_MODEL_ROOT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.internal.ArchiveNodeImpl#getRoot()
	 */
	public IArchiveNode getRoot() {
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.internal.ArchiveNodeImpl#connectedToModel()
	 */
	public boolean connectedToModel() {
		return getModel() != null;
	}

	/**
	 * No parent allowed for a model node
	 * @see IArchiveNode#setParent(IArchiveNode)
	 */
	public IArchiveNode getParent() {
		return null;
	}

	/**
	 * No parent allowed for a model node
	 * @see IArchiveNode#setParent(IArchiveNode)
	 */
	public void setParent(IArchiveNode parent) {
	}

	public void setModel(IArchiveModel model) {
		this.model = model;
	}
	
	/**
	 * I have no relative path. I'm above the root archive
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#getRootArchiveRelativePath()
	 */
	public IPath getRootArchiveRelativePath() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveModelNode#save(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void save(IProgressMonitor monitor) throws ArchivesModelException {
		XbPackages packs = (XbPackages)getNodeDelegate();
		try {
			XMLBinding.marshallToFile(packs, getDescriptor(), monitor);
		} catch( XbException xbe ) {
			throw new ArchivesModelException(xbe);
		}
		IArchiveNodeDelta delta = getDelta();
		clearDelta();
		EventManager.fireDelta(delta);
	}
	
	public boolean validateModel() {
		if( getChildren(IArchiveNode.TYPE_ARCHIVE).length < getAllChildren().length)
			return false;
		ArrayList<IPath> list = new ArrayList<IPath>();
		IArchiveNode[] children = getChildren(IArchiveNode.TYPE_ARCHIVE);
		IArchive child;
		IPath p;
		for( int i = 0; i < children.length; i++ ) {
			child = (IArchive)children[i];
			
			if( child.getGlobalDestinationPath() != null ) 
				p = child.getGlobalDestinationPath().append(child.getName());
			else 
				p = child.getDestinationPath().append(child.getName());
			
			if( list.contains(p))
				return false;
			else
				list.add(p);
		}

		return super.validateModel();
	}
}
