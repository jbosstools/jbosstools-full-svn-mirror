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

import org.eclipse.core.resources.IProject;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbFolder;

/**
 * A PackageFolderImpl.
 * 
 * @author <a href="marshall@jboss.org">Marshall Culpepper</a>
 * @version $Revision$
 */
public class PackageFolderImpl extends PackageNodeImpl implements
		IPackageFolder {

	private XbFolder folderDelegate;
	
	public PackageFolderImpl(IProject project, XbFolder delegate)
	{
		super(project, delegate);
		
		this.folderDelegate = delegate;
	}

	public String getName() {
		return folderDelegate.getName();
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

	public int getNodeType() {
		return TYPE_PACKAGE_FOLDER;
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

	public void setName(String name) {
		folderDelegate.setName(name);
	}

	protected XbFolder getFolderDelegate ()
	{
		return folderDelegate;
	}
	
	public String toString() {
		return "folder[" + getName() + "]";
	}
}
