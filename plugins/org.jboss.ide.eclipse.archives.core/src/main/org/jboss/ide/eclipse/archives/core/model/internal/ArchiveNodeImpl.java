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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeDelta;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeVisitor;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveNodeDeltaImpl.NodeDelta;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackageNode;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackageNodeWithProperties;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackages;

public abstract class ArchiveNodeImpl implements IArchiveNode {

	protected XbPackageNodeWithProperties nodeDelegate;
	protected IArchiveNode parent;
	protected ArrayList children;
	
	// cached data
	protected HashMap attributeChanges;
	protected HashMap propertyChanges;
	protected HashMap childChanges;
	
	
	public ArchiveNodeImpl (XbPackageNodeWithProperties delegate) {
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
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#getRoot()
	 */
	public IArchiveNode getRoot() {
		return parent == null ? this : parent.getRoot();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#getRootArchive()
	 */
	public IArchive getRootArchive() {
		IArchiveNode parent = this.parent;
		IArchive topArchives = null;

		if( getNodeType() == IArchiveNode.TYPE_ARCHIVE ) topArchives = (IArchive)this;
		while( parent != null ) {
			if( parent.getNodeType() == IArchiveNode.TYPE_ARCHIVE ) 
				topArchives = (IArchive)parent;
			parent = parent.getParent();
		}
		return topArchives;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#getAllChildren()
	 */
	public IArchiveNode[] getAllChildren () {
		return (IArchiveNode[]) children.toArray(new IArchiveNode[children.size()]);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#getChildren(int)
	 */
	public IArchiveNode[] getChildren(int type) {
		ArrayList typedChildren = new ArrayList();
		for (Iterator iter = children.iterator(); iter.hasNext(); ) {
			IArchiveNode child = (IArchiveNode) iter.next();
			if (child.getNodeType() == type) {
				typedChildren.add(child);
			}
		}
		
		return (IArchiveNode[]) typedChildren.toArray(new IArchiveNode[typedChildren.size()]);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#hasChildren()
	 */
	public boolean hasChildren () {
		return nodeDelegate.hasChildren();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#hasChild(org.jboss.ide.eclipse.archives.core.model.IArchiveNode)
	 */
	public boolean hasChild (IArchiveNode child) {
		ArchiveNodeImpl childImpl = (ArchiveNodeImpl)child;
		return nodeDelegate.getAllChildren().contains(childImpl.nodeDelegate);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#getParent()
	 */
	public IArchiveNode getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#setParent(org.jboss.ide.eclipse.archives.core.model.IArchiveNode)
	 */
	public void setParent (IArchiveNode parent) {
		if( getParent() != null && parent != getParent()) {
			getParent().removeChild(this);
		}
		
		if (parent != null && !(parent instanceof ArchiveModelNode)) {
			this.parent = parent;
			nodeDelegate.setParent(((ArchiveNodeImpl)parent).getNodeDelegate());
		} else if (getNodeType() == TYPE_ARCHIVE) {
			this.parent = parent;
			XbPackages packages = ArchivesModel.instance().getXbPackages(getProject());
			nodeDelegate.setParent(packages);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#getProject()
	 */
	public IProject getProject() {
		IArchiveNode root = getRoot();
		if( root.getNodeType() != IArchiveNode.TYPE_MODEL)
			return null;
		return root.getProject();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#getProperty(java.lang.String)
	 */
	public String getProperty(String property) {
		return getProperties().getProperty(property);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#setProperty(java.lang.String, java.lang.String)
	 */
	public void setProperty(String property, String value) {
		if( property == null ) return;
		propertyChanged(property, getProperty(property), value);
		if( value == null ) {
			getProperties().remove(property);
		} else {
			getProperties().setProperty(property, value);
		}
	}

	/**
	 * @return Get the properties for this object
	 */
	protected Properties getProperties() {
		return nodeDelegate.getProperties().getProperties();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#accept(org.jboss.ide.eclipse.archives.core.model.IArchiveNodeVisitor)
	 */
	public boolean accept(IArchiveNodeVisitor visitor) {
		return accept(visitor, false);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#accept(org.jboss.ide.eclipse.archives.core.model.IArchiveNodeVisitor, boolean)
	 */
	public boolean accept(IArchiveNodeVisitor visitor, boolean depthFirst) {
		IArchiveNode children[] = getAllChildren();
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
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#addChild(org.jboss.ide.eclipse.archives.core.model.IArchiveNode)
	 */
	public void addChild(IArchiveNode node) {
		addChild(node, true);
	}

	/**
	 * Add a child with the option to skip adding in the delegate
	 * @param child
	 * @param addInDelegate
	 */
	public void addChild(IArchiveNode child, boolean addInDelegate) {
		Assert.isNotNull(child);
		ArchiveNodeImpl childImpl = (ArchiveNodeImpl) child;
		children.add(childImpl);
		childImpl.setParent(this);
		if( addInDelegate )
			nodeDelegate.addChild(childImpl.nodeDelegate);
		childChanges(child, IArchiveNodeDelta.CHILD_ADDED);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#removeChild(org.jboss.ide.eclipse.archives.core.model.IArchiveNode)
	 */
	public void removeChild(IArchiveNode node) {
		Assert.isNotNull(node);
		ArchiveNodeImpl impl = (ArchiveNodeImpl) node;
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
			childChanges(node, IArchiveNodeDelta.CHILD_REMOVED);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter.equals(IProject.class)) {
			return getProject();
		} else if (adapter.equals(IArchiveNode.class)) {
			return this;
		}
		else return null;
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#connectedToModel()
	 */
	public boolean connectedToModel() {
		IArchiveNode root = getRoot();
		return root instanceof ArchiveModelNode && ArchivesModel.instance().containsRoot((ArchiveModelNode)root);
	}
	
	
	/**
	 * An attribute has changed. Save the change so it can be represented in a delta
	 */
	protected void attributeChanged(String key, Object beforeValue, Object afterValue) {
		int kind = IArchiveNodeDelta.ATTRIBUTE_CHANGED;
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
	
	/**
	 * A property has changed. Save the change so it can be represented in a delta
	 */
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
				changeMap.put(key, new NodeDelta(original, afterValue, IArchiveNodeDelta.PROPERTY_ADDED));
			else if( original.equals(afterValue))
				// value was changed from x to y, then back to x. Therefore, no change
				changeMap.remove(key);
			else if( afterValue == null ) {
				// changed from x to y to null, so removed
				changeMap.put(key, new NodeDelta(original, afterValue, IArchiveNodeDelta.PROPERTY_REMOVED));
			} else {
				// changed from x to y to z, so changed
				changeMap.put(key, new NodeDelta(original, afterValue, IArchiveNodeDelta.PROPERTY_CHANGED));
			}
		} else {
			int kind;
			if( beforeValue == null ) kind = IArchiveNodeDelta.PROPERTY_ADDED;
			else if( afterValue == null ) kind = IArchiveNodeDelta.PROPERTY_REMOVED;
			else kind = IArchiveNodeDelta.PROPERTY_CHANGED;
			changeMap.put(key, new NodeDelta(beforeValue, afterValue, kind));
		}
	}
	
	/**
	 * A child has changed. Save the change
	 * @param node
	 * @param changeType
	 */
	protected void childChanges(IArchiveNode node, int changeType) {
		if( childChanges.containsKey(node)) {
			int lastChange = ((Integer)childChanges.get(node)).intValue();
			if( lastChange == IArchiveNodeDelta.CHILD_ADDED && changeType == IArchiveNodeDelta.CHILD_REMOVED) {
				childChanges.remove(node);
			} else if( lastChange == IArchiveNodeDelta.CHILD_REMOVED && changeType == IArchiveNodeDelta.CHILD_ADDED) {
				childChanges.remove(node);
			}
		} else {
			childChanges.put(node, new Integer(changeType));
		}
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveNode#getDelta()
	 */
	public IArchiveNodeDelta getDelta() {
		return new ArchiveNodeDeltaImpl(null, this, (HashMap)attributeChanges.clone(), 
				(HashMap)propertyChanges.clone(), (HashMap)childChanges.clone());
	}
	
	/**
	 *  Forget all past state
	 */
	public void clearDeltas() {
		attributeChanges.clear();
		propertyChanges.clear();
		childChanges.clear();
		
		// clear children
		IArchiveNode[] children = getAllChildren();
		for( int i = 0; i < children.length; i++ ) 
			((ArchiveNodeImpl)children[i]).clearDeltas();
	}
}
