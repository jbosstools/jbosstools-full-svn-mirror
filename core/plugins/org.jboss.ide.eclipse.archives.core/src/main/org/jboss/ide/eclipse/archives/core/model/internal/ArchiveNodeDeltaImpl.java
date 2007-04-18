package org.jboss.ide.eclipse.archives.core.model.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeDelta;

public class ArchiveNodeDeltaImpl implements IArchiveNodeDelta {
	
	private ArchiveNodeDeltaImpl parentDelta;
	private ArchiveNodeImpl postNode, preNode;
	private HashMap attributes, properties, children;
	private int kind;
	private IArchiveNodeDelta[] childrenDeltas;
	
	/**
	 * Primary constructor
	 * @param parentDelta
	 * @param impl
	 * @param attributeChanges
	 * @param propertyChanges
	 * @param childChanges
	 */
	public ArchiveNodeDeltaImpl(ArchiveNodeDeltaImpl parentDelta, ArchiveNodeImpl impl, 
			HashMap attributeChanges, HashMap propertyChanges, HashMap childChanges) {
		this.parentDelta = parentDelta;
		postNode = impl;
		kind = 0;
		properties = propertyChanges;
		attributes = attributeChanges; 
		children = childChanges;
		
		// These three lines adjust my "kind" to be accurate
		ensureAccurateKind();
		
		// create *my* pre-node
		// this creates an accurate "old" node but without ANY children at all.
		// The children are expected to be added in the getAffectedChildren
		preNode = ArchiveDeltaPreNodeFactory.createNode(parentDelta, postNode, attributeChanges, propertyChanges);
		
		getAffectedChildren();
	}
	
	/**
	 * Constructor that forces a child to be added or removed, as judged by the parent
	 * @param parentDelta
	 * @param impl
	 * @param forcedKind
	 * @param attributeChanges
	 * @param propertyChanges
	 * @param childChanges
	 */
	public ArchiveNodeDeltaImpl(ArchiveNodeDeltaImpl parentDelta, ArchiveNodeImpl impl, 
			int forcedKind, HashMap attributeChanges, 
			HashMap propertyChanges, HashMap childChanges) {
		this(parentDelta, impl, attributeChanges, propertyChanges, childChanges);
		kind = kind | forcedKind; // pre-gaming 
	}
	
	/** 
	 * Get the parent delta
	 * @return
	 */
	protected ArchiveNodeDeltaImpl getParentDelta() {
		return parentDelta;
	}
	
	protected void ensureAccurateKind() {
		
		// Properties First
		Object key;
		NodeDelta val;
		for( Iterator i = properties.keySet().iterator(); i.hasNext(); ) {
			key = i.next();
			val = (NodeDelta)properties.get(key);
			kind = kind | val.getKind();
		}
		
		// Attributes Second
		if( attributes.keySet().size() > 0 )
			kind = kind | ATTRIBUTE_CHANGED;

		/*
		 * Children third.
		 * 
		 * The changed children are saved in a hashmap
		 * Node -> Integer  (where int is one of 
		 * IPackagesModelDelta.CHILD_ADDED or
		 * IPackagesModelDelta.CHILD_REMOVED 
		 */
		Integer val2;
		for( Iterator i = children.keySet().iterator(); i.hasNext(); ) {
			key = i.next();
			val2 = (Integer)children.get(key);
			if( val2 != null )
				kind = kind | val2.intValue();
		}
	}
	
	
	// Forced during constructor, will set the flag for CHILD_CHANGED if a child has changed at all.
	public IArchiveNodeDelta[] getAffectedChildren() {
		ArrayList priorChildren = new ArrayList();
		if( childrenDeltas == null ) {
			
			// first add the deltas for things that are currently our children
			// this includes items that haven't been changed, and items that were added
			IArchiveNode[] children = postNode.getAllChildren();
			IArchiveNodeDelta delta;
			ArrayList deltas = new ArrayList();
			for( int i = 0; i < children.length; i++ ) {
				// create our child delta before evaluating whether or not to add it
				delta = getDelta(children[i]);
				if( delta.getKind() != IArchiveNodeDelta.NO_CHANGE ) {
					deltas.add(delta);
					kind = kind | DESCENDENT_CHANGED;
				}

				// add ALL current nodes, then later remove the added ones
				priorChildren.add(delta.getPreNode());
			}
			
			// now handle the removed ones
			ArchiveNodeImpl node;
			for(Iterator i = this.children.keySet().iterator(); i.hasNext(); ) {
				node = (ArchiveNodeImpl)i.next();
				int v = ((Integer)this.children.get(node)).intValue();
				delta = getDelta(node);
				if( v == IArchiveNodeDelta.CHILD_REMOVED) {
					deltas.add(delta);
					priorChildren.add(delta.getPreNode());
				} else if( v == IArchiveNodeDelta.CHILD_ADDED) {
					priorChildren.remove(delta.getPreNode());
				}
			}
			
			if( preNode != null ) {
				// now we've got our list of current children... set them. 
				for( Iterator i = priorChildren.iterator(); i.hasNext(); ) {
					preNode.addChild((IArchiveNode)i.next());
				}
				// now clear pre-node's deltas so it looks shiny
				preNode.clearDeltas();
			}
			
			childrenDeltas = (IArchiveNodeDelta[]) deltas.toArray(new IArchiveNodeDelta[deltas.size()]);
		}
		return childrenDeltas;
	}

	/*
	 * Because a node can be ADDED with respect to one node, and
	 * REMOVED with respect to another, that portion of the delta 
	 * kind must be set here, from the parent, rather than in the 
	 * child. 
	 */
	private IArchiveNodeDelta getDelta(IArchiveNode child) {
		if( child instanceof ArchiveNodeImpl ) {
			int addedOrRemoved = 0;
			if( children.containsKey(child)) {
				addedOrRemoved = ((Integer)children.get(child)).intValue() >> 8;
			}
			ArchiveNodeImpl impl = (ArchiveNodeImpl)child;
			
			// Using a different delta constructor here to force 
			// whether this child is added or removed. 
			return new ArchiveNodeDeltaImpl(this, impl, addedOrRemoved,
					(HashMap)impl.attributeChanges.clone(), 
					(HashMap)impl.propertyChanges.clone(), 
					(HashMap)impl.childChanges.clone());

		}
		
		return child.getDelta();
	}
	
	public int getKind() {
		return kind;
	}

	public IArchiveNode getPostNode() {
		return postNode;
	}

	public IArchiveNode getPreNode() {
		return preNode;
	}

	public String[] getAttributesWithDeltas() {
		Collection atts = attributes.keySet();
		return (String[]) atts.toArray(new String[atts.size()]);
	}

	public INodeDelta getAttributeDelta(String key) {
		return (INodeDelta)attributes.get(key);
	}

	public String[] getPropertiesWithDeltas() {
		Collection atts = properties.keySet();
		return (String[]) atts.toArray(new String[atts.size()]);
	}

	public INodeDelta getPropertyDelta(String key) {
		return (INodeDelta)properties.get(key);
	}

	public IArchiveNodeDelta[] getAddedChildrenDeltas() {
		return getChangedChildren(IArchiveNodeDelta.ADDED);
	}
	public IArchiveNodeDelta[] getRemovedChildrenDeltas() {
		return getChangedChildren(IArchiveNodeDelta.REMOVED);
	}

	private IArchiveNodeDelta[] getChangedChildren(int type) {
		ArrayList list = new ArrayList();
		for( int i = 0; i < childrenDeltas.length; i++ ) {
			if( (childrenDeltas[i].getKind() & type) != 0 ) {
				list.add(childrenDeltas[i]);
			}
		}
		return (IArchiveNodeDelta[]) list.toArray(new IArchiveNodeDelta[list.size()]);
	}
	
	/**
	 * A quick and dirty class to keep track of changing
	 * values between saves in a model. 
	 * Used for property changes and attribute changes
	 * @author rstryker
	 *
	 */
	protected static class NodeDelta implements INodeDelta {
		private int kind;
		private Object before, after;
		public NodeDelta(Object before, Object after, int kind) {
			this.before = before;
			this.after = after;
			this.kind = kind;
		}
		public Object getBefore() { return before; }
		public Object getAfter() { return after; }
		public int getKind() {
			return kind;
		}
	}
}
