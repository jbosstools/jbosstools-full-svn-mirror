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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackageNodeWithProperties;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackages;

public class ArchiveModelNode extends ArchiveNodeImpl implements IArchiveModelNode {
	private IProject project;

	public ArchiveModelNode(IProject project, XbPackageNodeWithProperties node) {
		super(node);
		this.project = project;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.internal.ArchiveNodeImpl#getProject()
	 */
	public IProject getProject() {
		return project;
	}
	
	public XbPackages getXbPackages() {
		return (XbPackages)nodeDelegate;
	}
	
	/**
	 * The model root can only accept IArchive's as children
	 * @see IArchiveNode#addChild(IArchiveNode)
	 */
	public void addChild(IArchiveNode child) {
		if( child instanceof IArchive ) {
			super.addChild(child);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#getNodeType()
	 */
	public int getNodeType() {
		return IArchiveNode.TYPE_MODEL;
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
		return ArchivesModel.instance().containsRoot(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.internal.ArchiveNodeImpl#getParent()
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

	/**
	 * I have no relative path. I'm above the root archive
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#getRootArchiveRelativePath()
	 */
	public IPath getRootArchiveRelativePath() {
		return null;
	}
}
