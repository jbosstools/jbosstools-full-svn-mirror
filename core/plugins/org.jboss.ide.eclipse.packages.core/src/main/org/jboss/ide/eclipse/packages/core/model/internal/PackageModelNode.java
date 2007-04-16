package org.jboss.ide.eclipse.packages.core.model.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackageNodeWithProperties;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackages;

public class PackageModelNode extends PackageNodeImpl {
	private IProject project;

	public PackageModelNode(IProject project, XbPackageNodeWithProperties node) {
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
	public void addChild(IPackageNode child) {
		if( child instanceof IPackage ) {
			super.addChild(child);
		}
	}

	public int getNodeType() {
		return IPackageNode.TYPE_MODEL;
	}

	public IPackageNode getRoot() {
		return this;
	}
	
	public boolean connectedToModel() {
		return PackagesModel.instance().containsRoot(this);
	}

	public IPackageNode getParent() {
		return null;
	}

	public void setParent(IPackageNode parent) {
	}

	public IPath getRootArchiveRelativePath() {
		return null;
	}

	public IPath[] getPackagePaths() {
		return null;
	}
}
