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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageNodeVisitor;
import org.jboss.ide.eclipse.packages.core.model.IPackagesModelDelta;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModelDeltaImpl.NodeDelta;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackageNode;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackageNodeWithProperties;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackages;

public abstract class PackageNodeImpl implements IPackageNode {

	protected XbPackageNodeWithProperties nodeDelegate;
	protected IPackageNode parent;
	protected ArrayList children;
	
	// cached data
	protected HashMap attributeChanges;
	protected HashMap propertyChanges;
	protected HashMap childChanges;
	
	
	public PackageNodeImpl (XbPackageNodeWithProperties delegate) {
		nodeDelegate = delegate;
		children = new ArrayList();

		// for deltas
		attributeChanges = new HashMap();
		propertyChanges = new HashMap();
		childChanges = new HashMap();
	}
		
	public XbPackageNode getNodeDelegate() {
		return nodeDelegate;
	}
	
	public IPackageNode getRoot() {
		return parent == null ? this : parent.getRoot();
	}

	
	public IPackageNode[] getAllChildren () {
		return (IPackageNode[]) children.toArray(new IPackageNode[children.size()]);
	}
	
	public IPackageNode[] getChildren(int type) {
		ArrayList typedChildren = new ArrayList();
		for (Iterator iter = children.iterator(); iter.hasNext(); ) {
			IPackageNode child = (IPackageNode) iter.next();
			if (child.getNodeType() == type) {
				typedChildren.add(child);
			}
		}
		
		return (IPackageNode[]) typedChildren.toArray(new IPackageNode[typedChildren.size()]);
	}
	
	public boolean hasChildren () {
		return nodeDelegate.hasChildren();
	}
	
	public boolean hasChild (IPackageNode child) {
		PackageNodeImpl childImpl = (PackageNodeImpl)child;
		return nodeDelegate.getAllChildren().contains(childImpl.nodeDelegate);
	}

	public IPackageNode getParent() {
		return parent;
	}

	public void setParent (IPackageNode parent) {
		if( getParent() != null && parent != getParent()) {
			getParent().removeChild(this);
		}
		
		if (parent != null && !(parent instanceof PackageModelNode)) {
			this.parent = parent;
			nodeDelegate.setParent(((PackageNodeImpl)parent).getNodeDelegate());
		} else if (getNodeType() == TYPE_PACKAGE) {
			this.parent = parent;
			XbPackages packages = PackagesModel.instance().getXbPackages(getProject());
			nodeDelegate.setParent(packages);
		}
	}
	
	public IProject getProject() {
		IPackageNode root = getRoot();
		if( root.getNodeType() != IPackageNode.TYPE_MODEL)
			return null;
		return root.getProject();
	}
	
	public String getProperty(String property) {
		return getProperties().getProperty(property);
	}
	
	public void setProperty(String property, String value) {
		if( property == null ) return;
		propertyChanged(property, getProperty(property), value);
		if( value == null ) {
			getProperties().remove(property);
		} else {
			getProperties().setProperty(property, value);
		}
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
		
		if (keepGoing) {
			for (int i = 0; i < children.length; i++) {
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
		addChild(node, true);
	}

	public void addChild(IPackageNode child, boolean addInDelegate) {
		Assert.isNotNull(child);
		PackageNodeImpl childImpl = (PackageNodeImpl) child;
		children.add(childImpl);
		childImpl.setParent(this);
		if( addInDelegate )
			nodeDelegate.addChild(childImpl.nodeDelegate);
		childChanges(child, IPackagesModelDelta.CHILD_ADDED);
	}

	public void removeChild(IPackageNode node) {
		Assert.isNotNull(node);
		PackageNodeImpl impl = (PackageNodeImpl) node;
		boolean removed = false;
		if (nodeDelegate.getAllChildren().contains(impl.nodeDelegate)) {
			nodeDelegate.removeChild(impl.nodeDelegate);
			removed = true;
		}

		if (children.contains(node)) {
			children.remove(node);
			removed = true;
		}
		if( removed )
			childChanges(node, IPackagesModelDelta.CHILD_REMOVED);
	}
	
	public Object getAdapter(Class adapter) {
		if (adapter.equals(IProject.class)) {
			return getProject();
		} else if (adapter.equals(IPackageNode.class)) {
			return this;
		}
		else return null;
	}
	
	/**
	 * Is this method necessary? Some of the interfaces have a similar method
	 * This is only used in destination composite in the UI so far. 
	 * TODO: Determine which is necessary, this method or getRootArchiveRelativePath
	 * @return
	 */
//	public IPath getPackageRelativePath() {
//		String path = "";
//		if (getNodeType() == IPackageNode.TYPE_PACKAGE ) {
//			if (getParent() == null) return null;
//			path = ((IPackage)this).getName();
//		} else if (getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER) {
//			path = ((IPackageFolder)this).getName();
//		}
//		
//		IPackageNode parent = getParent(), save = null;
//		while (true) {
//			if (parent.getNodeType() == IPackageNode.TYPE_PACKAGE)
//				path = ((IPackage)parent).getName() + "/" + path;
//			else
//				path = ((IPackageFolder)parent).getName() + "/" + path;
//			
//			save = parent;
//			parent = parent.getParent();
//			if (parent instanceof PackageModelNode ) { 
//				parent = save;
//				break;
//			}
//		}
//		if (path.charAt(path.length()-1) == '/') {
//			path = path.substring(0, path.length() - 1);
//		}
//		return new Path(path);
//	}

	public boolean connectedToModel() {
		IPackageNode root = getRoot();
		return root instanceof PackageModelNode && PackagesModel.instance().containsRoot((PackageModelNode)root);
	}
	
	
	/*
	 * The following are for deltas. It keeps track of recent changes
	 * and makes sure all changes are accoutned for properly between saves
	 */
	protected void attributeChanged(String key, Object beforeValue, Object afterValue) {
		int kind = IPackagesModelDelta.ATTRIBUTE_CHANGED;
		HashMap map = attributeChanges;
		
		// short circuit if no change has REALLY occurred
		if( beforeValue != null && beforeValue.equals(afterValue)) return;
		
		if( map.containsKey(key)) {
			Object original = ((NodeDelta)map.get(key)).getBefore();
			if( original == null && afterValue == null ) 
				map.remove(key);
			else if( original == null ) 
				map.put(key, new NodeDelta(original, afterValue, kind));
			else if( original.equals(afterValue))
				// value was changed from x to y, then back to x. Therefore, no change
				map.remove(key);
			else
				// value was changed from x to y to z. 
				// Before should remain x, after should become z
				map.put(key, new NodeDelta(original, afterValue, kind));
		} else {
			// added
			map.put(key, new NodeDelta(beforeValue, afterValue, kind));
		}
	}
	
	protected void propertyChanged(String key, Object beforeValue, Object afterValue) {
		HashMap changeMap = propertyChanges;
		// short circuit if no change has REALLY occurred
		if( beforeValue != null && beforeValue.equals(afterValue)) return;
		
		
		if( changeMap.containsKey(key)) {
			// element has already been added, removed, or changed since last save
			Object original = ((NodeDelta)changeMap.get(key)).getBefore();
			if( original == null && afterValue == null ) 
				changeMap.remove(key);
			else if( original == null ) 
				changeMap.put(key, new NodeDelta(original, afterValue, IPackagesModelDelta.PROPERTY_ADDED));
			else if( original.equals(afterValue))
				// value was changed from x to y, then back to x. Therefore, no change
				changeMap.remove(key);
			else if( afterValue == null ) {
				// changed from x to y to null, so removed
				changeMap.put(key, new NodeDelta(original, afterValue, IPackagesModelDelta.PROPERTY_REMOVED));
			} else {
				// changed from x to y to z, so changed
				changeMap.put(key, new NodeDelta(original, afterValue, IPackagesModelDelta.PROPERTY_CHANGED));
			}
		} else {
			int kind;
			if( beforeValue == null ) kind = IPackagesModelDelta.PROPERTY_ADDED;
			else if( afterValue == null ) kind = IPackagesModelDelta.PROPERTY_REMOVED;
			else kind = IPackagesModelDelta.PROPERTY_CHANGED;
			changeMap.put(key, new NodeDelta(beforeValue, afterValue, kind));
		}
	}
	
	// children are either added or removed here.  
	// changed children are discovered through the delta
	protected void childChanges(IPackageNode node, int changeType) {
		if( childChanges.containsKey(node)) {
			int lastChange = ((Integer)childChanges.get(node)).intValue();
			if( lastChange == IPackagesModelDelta.CHILD_ADDED && changeType == IPackagesModelDelta.CHILD_REMOVED) {
				childChanges.remove(node);
			} else if( lastChange == IPackagesModelDelta.CHILD_REMOVED && changeType == IPackagesModelDelta.CHILD_ADDED) {
				childChanges.remove(node);
			}
		} else {
			childChanges.put(node, new Integer(changeType));
		}
	}	
	
	public IPackagesModelDelta getDelta() {
		return new PackagesModelDeltaImpl(null, this, (HashMap)attributeChanges.clone(), 
				(HashMap)propertyChanges.clone(), (HashMap)childChanges.clone());
	}
	
	protected void clearDeltas() {
		attributeChanges.clear();
		propertyChanges.clear();
		childChanges.clear();
		
		// clear children
		IPackageNode[] children = getAllChildren();
		for( int i = 0; i < children.length; i++ ) 
			((PackageNodeImpl)children[i]).clearDeltas();
	}
}
