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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageNodeVisitor;
import org.jboss.ide.eclipse.packages.core.model.IPackageReference;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbFileSet;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbFolder;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackage;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackageNode;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackageNodeWithProperties;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackages;

public abstract class PackageNodeImpl implements IPackageNode {

	protected XbPackageNodeWithProperties nodeDelegate;
	protected IProject project;
	protected IPackageNode parent;
	protected ArrayList children;
	protected boolean detached, hasWorkingCopy;
	
	protected static int nodeTypeToIntType (Class type)
	{
		if (XbPackage.class.equals(type)) return TYPE_PACKAGE;
		else if (XbFileSet.class.equals(type)) return TYPE_PACKAGE_FILESET;
		else if (XbFolder.class.equals(type)) return TYPE_PACKAGE_FOLDER;
		return -1;
	}
	
	protected static Class intTypeToNodeType (int type)
	{
		switch (type) {
		case TYPE_PACKAGE:
			return XbPackage.class;
		case TYPE_PACKAGE_FILESET:
			return XbFileSet.class;
		case TYPE_PACKAGE_FOLDER:
			return XbFolder.class;
		}
		return null;
	}
	
	public PackageNodeImpl (IProject project, XbPackageNodeWithProperties delegate)
	{
		nodeDelegate = delegate;
		this.project = project;
		hasWorkingCopy = false;
		detached = false;
		
		children = new ArrayList();
	}
	
	public PackageNodeImpl ()
	{
		this(null, null);
	}
	
	public XbPackageNode getNodeDelegate()
	{
		return nodeDelegate;
	}
	
	public IPackageNode[] getAllChildren ()
	{
		return (IPackageNode[]) children.toArray(new IPackageNode[children.size()]);
	}
	
	public IPackageNode[] getChildren(int type) {
		ArrayList typedChildren = new ArrayList();
		for (Iterator iter = children.iterator(); iter.hasNext(); )
		{
			IPackageNode child = (IPackageNode) iter.next();
			if (child.getNodeType() == type)
			{
				typedChildren.add(child);
			}
		}
		
		return (IPackageNode[]) typedChildren.toArray(new IPackageNode[typedChildren.size()]);
	}
	
	public boolean hasChildren () {
		return nodeDelegate.hasChildren();
	}
	
	public boolean hasChild (IPackageNode child)
	{
		PackageNodeImpl childImpl = (PackageNodeImpl)child;
		return nodeDelegate.getAllChildren().contains(childImpl.nodeDelegate);
	}

	public IPackageNode getParent() {
		return parent;
	}

	public void setParent (IPackageNode parent)
	{
		if (parent != null)
		{
			this.parent = parent;
			nodeDelegate.setParent(((PackageNodeImpl)parent).getNodeDelegate());
		}
		else if (getNodeType() == TYPE_PACKAGE) {
			this.parent = null;
			XbPackages packages = PackagesModel.instance().getXbPackages(getProject());
			nodeDelegate.setParent(packages);
		}
	}

	public IPath getPackageRelativePath() {
		return PackagesCore.getPackageRelativePath(this);
	}
	
	public IProject getProject() {
		return project;
	}
	
	public String getProperty(String property) {
		return getProperties().getProperty(property);
	}
	
	public void setProperty(String property, String value) {
		getProperties().setProperty(property, value);
		
		PackagesModel.instance().saveModel(getProject(), new NullProgressMonitor());
	}

	public Properties getProperties() {
		return nodeDelegate.getProperties().getProperties();
	}

	public boolean accept(IPackageNodeVisitor visitor) {
		return accept(visitor, false);
	}
	
	public boolean accept(IPackageNodeVisitor visitor, boolean depthFirst) {
		IPackageNode children[] = getAllChildren();
		boolean keepGoing = true;
		
		if (!depthFirst)
			keepGoing = visitor.visit(this);
		
		if (keepGoing)
		{
			for (int i = 0; i < children.length; i++)
			{
				if (keepGoing) {
					keepGoing = children[i].accept(visitor, depthFirst);
				}
			}
		}
		
		if (depthFirst && keepGoing)
			keepGoing = visitor.visit(this);
		
		return keepGoing;
	}
	
	
	public void addChild(IPackageNode node) {
		Assert.isNotNull(node);
		
		if (node.getNodeType() == TYPE_PACKAGE_REFERENCE)
		{
			addRef((IPackageReference)node);
			return;
		}
		
		PackageNodeImpl impl = (PackageNodeImpl) node;
		
		nodeDelegate.addChild(impl.nodeDelegate);
		
		addChildImpl(impl);
		
		if (!detached && !areAnyParentsDetached())
		{
			PackagesModel.instance().saveModel(node.getProject(), null);
			PackagesModel.instance().fireNodeAdded(node);
		}
	}
	
	private void addRef (IPackageReference pkgRef)
	{		
		PackageReferenceImpl refImpl = (PackageReferenceImpl) pkgRef;
		
		children.add(refImpl);
		refImpl.setParent(this);
		
		if (!detached && !areAnyParentsDetached())
		{
			PackagesModel.instance().saveModel(getProject(), null);
		}
	}
	
	// convenience so we can skip adding in the delegate
	protected void addChildImpl (PackageNodeImpl childImpl)
	{
		children.add(childImpl);
		childImpl.setParent(this);
	}
	
	public void removeChild(IPackageNode node) {
		Assert.isNotNull(node);
		
		PackageNodeImpl impl = (PackageNodeImpl) node;
		
		if (nodeDelegate.getAllChildren().contains(impl.nodeDelegate)) {
			nodeDelegate.removeChild(impl.nodeDelegate);
		}
		
		if (children.contains(node))
			children.remove(node);
		
		if (!detached && !areAnyParentsDetached())
		{
			PackagesModel.instance().saveModel(node.getProject(), null);
			PackagesModel.instance().fireNodeRemoved(node);
		}
	}
	
	public Object getAdapter(Class adapter) {
		if (adapter.equals(IProject.class))
		{
			return getProject();
		} else if (adapter.equals(IPackageNode.class)) {
			return this;
		}
		else return null;
	}
	
	public boolean isDetached() {
		return detached;
	}
	
	public boolean areAnyParentsDetached ()
	{
		for (IPackageNode parent = getParent(); parent != null; parent = parent.getParent() )
		{
			if (((PackageNodeImpl)parent).isDetached()) return true;
		}
		return false;
	}

	public void setDetached(boolean detached) {
		this.detached = detached;
	}
	
	public void flagAsChanged ()
	{
		PackagesModel.instance().fireNodeChanged(this);
	}
}
