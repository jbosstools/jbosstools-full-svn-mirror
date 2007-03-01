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

import java.io.IOException;
import java.util.ArrayList;
import java.util.jar.Manifest;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.core.util.ResourceUtil;
import org.jboss.ide.eclipse.packages.core.ExtensionManager;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageReference;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackage;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackages;
import org.jboss.ide.eclipse.packages.core.model.types.IPackageType;

/**
 * A Package.
 * 
 * @author <a href="marshall@jboss.org">Marshall Culpepper</a>
 * @version $Revision$
 */
public class PackageImpl extends PackageNodeImpl implements IPackage {

	private XbPackage packageDelegate;
	private boolean parentShouldBeNull;
	private ArrayList references;
	
	public PackageImpl(IProject project, XbPackage delegate)
	{
		super(project, delegate);
		
		this.packageDelegate = delegate;
		this.hasWorkingCopy = false;
		this.parentShouldBeNull = false;
		this.references = new ArrayList();
	}
	
	public int getNodeType() {
		return TYPE_PACKAGE;
	}

	public boolean isDestinationInWorkspace() {
		return packageDelegate.isInWorkspace();
	}
	
	public IContainer getDestinationContainer() {
		if (!isDestinationInWorkspace()) return null;
		
		if (packageDelegate.getToDir() == null || packageDelegate.getToDir().equals("."))
			return project;
		
		return ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(packageDelegate.getToDir()));
	}
	
	public IPath getDestinationPath () {
		if (packageDelegate.getToDir() == null || packageDelegate.getToDir().equals("."))
			return ProjectUtil.getProjectLocation(project);
		
		else if (isDestinationInWorkspace())
		{	
			return ResourceUtil.makeAbsolute(ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(packageDelegate.getToDir())));
		}
		else return new Path(packageDelegate.getToDir());
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

	public String getName() {
		return packageDelegate.getName();
	}

	public IPackage[] getPackages() {
		IPackageNode nodes[] = getChildren(TYPE_PACKAGE);
		IPackage pkgs[] = new IPackage[nodes.length];
		System.arraycopy(nodes, 0, pkgs, 0, nodes.length);
		return pkgs;
	}

	public IPackageType  getPackageType() {
		return ExtensionManager.getPackageType(packageDelegate.getPackageType());
	}
	
	public IFile getPackageFile() {
		if (isDestinationInWorkspace()) {
			return getDestinationContainer().getFile(new Path(getName()));
		} else return null;
	}

	public IPath getPackageFilePath() {
		return getDestinationPath().append(new Path(getName()));
	}
	
	public boolean isExploded() {
		return packageDelegate.isExploded();
	}

	public boolean isReference() {
		return (packageDelegate.getRef() != null
			&& packageDelegate.getRef().length() > 0);
	}
	
	public IPackageReference[] getReferences() {
		return (IPackageReference[]) references.toArray(new IPackageReference[references.size()]);
	}
	
	public void addReference(PackageReferenceImpl impl) {
		references.add(impl);
	}
	
	public boolean isTopLevel() {
		return (packageDelegate.getParent() instanceof XbPackages || packageDelegate.getParent() == null);
	}
	
	public Manifest getManifest() {
		if (!hasManifest()) return null;
		
		IFile manifestFile = getManifestFile();
		
		try {
			return new Manifest(manifestFile.getContents());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public IFile getManifestFile () {
		if (!hasManifest()) return null;
		
		return getProject().getFile(packageDelegate.getManifest());
	}
	
	public boolean hasManifest() {
		return packageDelegate.getManifest() != null;
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

	public void setDestinationPath(IPath path) {
		packageDelegate.setInWorkspace(ResourceUtil.isResourceInWorkspace(path));
		packageDelegate.setToDir(path.toString());
	}
	
	public void setDestinationContainer(IContainer container) {
		packageDelegate.setInWorkspace(true);
		if (!container.equals(getProject()))
		{
			packageDelegate.setToDir(container.getFullPath().toString());
		}
	}

	public void setExploded(boolean exploded) {
		packageDelegate.setExploded(exploded);
	}

	public void setManifest(IFile manifestFile) {
		packageDelegate.setManifest(manifestFile.getProjectRelativePath().toString());
	}

	public void setName(String name) {
		packageDelegate.setName(name);
	}

	public void setPackageType(IPackageType type) {
		packageDelegate.setPackageType(type.getId());
	}
	
	public IPackageReference createReference (boolean topLevel) {
		PackageReferenceImpl ref = new PackageReferenceImpl(this, new XbPackage());
		
		if (topLevel)
		{
			XbPackages packages = PackagesModel.instance().getXbPackages(getProject());
			packages.addChild(ref.xbPackage);
		}

		return ref;
	}
	
	protected XbPackage getPackageDelegate ()
	{
		return packageDelegate;
	}
	
	public String toString() {
		return getName();
	}

	protected boolean shouldParentBeNull ()
	{
		return parentShouldBeNull;
	}
	
	public void setParentShouldBeNull (boolean parentShouldBeNull)
	{
		this.parentShouldBeNull = parentShouldBeNull;
	}
}
