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
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageNodeBase;
import org.jboss.ide.eclipse.packages.core.model.IPackageNodeVisitor;
import org.jboss.ide.eclipse.packages.core.model.IPackageNodeWorkingCopy;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbFileSet;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbFolder;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackage;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackageNode;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackageNodeWithProperties;

public abstract class PackageNodeImpl implements IPackageNode, IPackageNodeWorkingCopy {

	protected XbPackageNodeWithProperties nodeDelegate;
	protected IProject project;
	protected ArrayList childrenToRegister, childrenToUnregister;
	protected boolean hasWorkingCopy;
	
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
		childrenToRegister = new ArrayList();
		childrenToUnregister = new ArrayList();
		hasWorkingCopy = false;
	}
	
	public PackageNodeImpl ()
	{
		this(null, null);
	}
	
	public XbPackageNode getNodeDelegate()
	{
		return nodeDelegate;
	}
	
	private IPackageNode[] nodesToChildren (List nodes)
	{
		IPackageNode children[] = new IPackageNode[nodes.size()];
		int i = 0;
		
		for (Iterator iter = nodes.iterator(); iter.hasNext();)
		{
			XbPackageNode node = (XbPackageNode) iter.next();
			children[i] = PackagesModel.instance().getPackageNodeImpl(node);
			i++;
		}
		
		return children;
	}
	
	public IPackageNodeBase[] getAllChildren ()
	{
		return nodesToChildren(nodeDelegate.getAllChildren());
	}
	
	public IPackageNodeBase[] getChildren(int type) {
		return nodesToChildren(nodeDelegate.getChildren(intTypeToNodeType(type)));
	}
	
	public boolean hasChildren () {
		return nodeDelegate.hasChildren();
	}
	
	public boolean hasChild (IPackageNodeBase child)
	{
		PackageNodeImpl childImpl = (PackageNodeImpl)child;
		return nodeDelegate.getAllChildren().contains(childImpl.nodeDelegate);
	}

	public IPackageNodeBase getParent() {
		XbPackageNode parent = nodeDelegate.getParent();
		if (parent != null)
			return PackagesModel.instance().getPackageNodeImpl(parent);
		
		return null;
	}

	public IProject getProject() {
		return project;
	}
	
	public String getProperty(String property) {
		return getProperties().getProperty(property);
	}
	
	public void setProperty(String property, String value) {
		getProperties().setProperty(property, value);
	}

	public Properties getProperties() {
		return nodeDelegate.getProperties().getProperties();
	}

	public boolean accept(IPackageNodeVisitor visitor) {
		return accept(visitor, false);
	}
	
	public boolean accept(IPackageNodeVisitor visitor, boolean depthFirst) {
		IPackageNodeBase children[] = getAllChildren();
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
		
		PackageNodeImpl impl = (PackageNodeImpl) node;
		
		nodeDelegate.addChild(impl.nodeDelegate);
		
		PackagesModel.instance().saveModel(node.getProject());
		PackagesModel.instance().fireNodeAdded(node);
	}
	
	public void removeChild(IPackageNode node) {
		Assert.isNotNull(node);
		
		PackageNodeImpl impl = (PackageNodeImpl) node;
		
		if (nodeDelegate.getAllChildren().contains(impl.nodeDelegate)) {
			nodeDelegate.removeChild(impl.nodeDelegate);
		}
		
		PackagesModel.instance().saveModel(node.getProject());
		PackagesModel.instance().fireNodeRemoved(node);
	}
	
	public Object getAdapter(Class adapter) {
		if (adapter.equals(IProject.class))
		{
			return getProject();
		}
		else return null;
	}
	
	public boolean isWorkingCopy ()
	{
		return getOriginal() != null;
	}
	
	protected void finalize() throws Throwable {
		if (getOriginal() != null) ((PackageNodeImpl)getOriginal()).hasWorkingCopy = false;
	}
}
