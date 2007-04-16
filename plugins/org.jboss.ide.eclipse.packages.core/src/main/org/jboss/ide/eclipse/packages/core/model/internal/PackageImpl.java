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
package org.jboss.ide.eclipse.packages.core.model.internal;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.packages.core.ExtensionManager;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackage;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackages;
import org.jboss.ide.eclipse.packages.core.model.types.IPackageType;

/**
 * A Package.
 * 
 * @author Rob stryker
 */
public class PackageImpl extends PackageNodeImpl implements IPackage {

	private XbPackage packageDelegate;
	
	public PackageImpl() {
		this(new XbPackage());
	}
	public PackageImpl(XbPackage delegate) {
		super(delegate);
		this.packageDelegate = delegate;
	}
	
	public int getNodeType() {
		return TYPE_PACKAGE;
	}

	public boolean isDestinationInWorkspace() {
		return packageDelegate.isInWorkspace();
	}
	
	public IPath getDestinationPath () {
		if (packageDelegate.getToDir() == null || packageDelegate.getToDir().equals("."))
			return getProject() == null ? null : getProject().getLocation();
		
		if (isDestinationInWorkspace()) {	
			return ResourcesPlugin.getWorkspace().getRoot().getLocation().append(new Path(packageDelegate.getToDir()));
		} else 
			return new Path(packageDelegate.getToDir());
	}

	public IPath getPackageFilePath() {
		return getDestinationPath().append(getName());
	}
	
	public IPackageFileSet[] getFileSets() {
		IPackageNode nodes[] = getChildren(TYPE_PACKAGE_FILESET);
		IPackageFileSet filesets[] = new IPackageFileSet[nodes.length];
		System.arraycopy(nodes, 0, filesets, 0, nodes.length);
		return filesets;
	}

	public IPackageFolder[] getFolders() {
		IPackageNode nodes[] = getChildren(TYPE_PACKAGE_FOLDER);
		IPackageFolder folders[] = new IPackageFolder[nodes.length];
		System.arraycopy(nodes, 0, folders, 0, nodes.length);
		return folders;
	}

	public IPackage[] getPackages() {
		IPackageNode nodes[] = getChildren(TYPE_PACKAGE);
		IPackage pkgs[] = new IPackage[nodes.length];
		System.arraycopy(nodes, 0, pkgs, 0, nodes.length);
		return pkgs;
	}

	public String getName() {
		return packageDelegate.getName();
	}

	public IPackageType  getPackageType() {
		return ExtensionManager.getPackageType(packageDelegate.getPackageType());
	}
		
	public boolean isExploded() {
		return packageDelegate.isExploded();
	}
	
	public boolean isTopLevel() {
		return (packageDelegate.getParent() instanceof XbPackages);
	}
	
	public void addFileSet(IPackageFileSet fileset) {
		addChild(fileset);
	}

	public void addFolder(IPackageFolder folder) {
		addChild(folder);
	}

	public void addPackage(IPackage pkg) {
		addChild(pkg);
	}

	public void setDestinationPath(IPath path, boolean inWorkspace) {
		IPath destPath = getDestinationPath();
		attributeChanged(IN_WORKSPACE_ATTRIBUTE, new Boolean(isDestinationInWorkspace()), new Boolean(inWorkspace));
		attributeChanged(DESTINATION_ATTRIBUTE, destPath == null ? null : destPath.toString(), path == null ? null : path.toString());
		packageDelegate.setInWorkspace(inWorkspace);
		packageDelegate.setToDir(path.toString());
	}

	public void setExploded(boolean exploded) {
		attributeChanged(EXPLODED_ATTRIBUTE, new Boolean(isExploded()), new Boolean(exploded));
		packageDelegate.setExploded(exploded);
	}

	public void setName(String name) {
		attributeChanged(NAME_ATTRIBUTE, getName(), name);
		packageDelegate.setName(name);
	}

	public void setPackageType(IPackageType type) {
		attributeChanged(PACKAGE_TYPE_ATTRIBUTE, getPackageTypeId(), type == null ? null : type.getId());
		packageDelegate.setPackageType(type.getId());
	}
	
	protected XbPackage getPackageDelegate () {
		return packageDelegate;
	}

	public void setPackageType(String type) {
		attributeChanged(PACKAGE_TYPE_ATTRIBUTE, getPackageTypeId(), type);
		packageDelegate.setPackageType(type);
	}
	
	public String toString() {
		return getName();
	}
	public String getPackageTypeId() {
		return packageDelegate.getPackageType();
	}
	public IPath getRootArchiveRelativePath() {
		if( getParent() == null || getParent().getRootArchiveRelativePath() == null )
			return new Path(getName());
		return getParent().getRootArchiveRelativePath().append(getName());
	}

}
