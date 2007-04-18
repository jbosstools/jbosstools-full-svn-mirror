package org.jboss.ide.eclipse.archives.core.model.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackageNodeWithProperties;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackages;

public class ArchiveModelNode extends ArchiveNodeImpl {
	private IProject project;

	public ArchiveModelNode(IProject project, XbPackageNodeWithProperties node) {
		super(node);
		this.project = project;
	}
	
	public IProject getProject() {
		return project;
	}
	
	public XbPackages getXbPackages() {
		return (XbPackages)nodeDelegate;
	}
	
	// Can only add packages types here
	public void addChild(IArchiveNode child) {
		if( child instanceof IArchive ) {
			super.addChild(child);
		}
	}

	public int getNodeType() {
		return IArchiveNode.TYPE_MODEL;
	}

	public IArchiveNode getRoot() {
		return this;
	}
	
	public boolean connectedToModel() {
		return ArchivesModel.instance().containsRoot(this);
	}

	public IArchiveNode getParent() {
		return null;
	}

	public void setParent(IArchiveNode parent) {
	}

	public IPath getRootArchiveRelativePath() {
		return null;
	}

	public IPath[] getPackagePaths() {
		return null;
	}
}
