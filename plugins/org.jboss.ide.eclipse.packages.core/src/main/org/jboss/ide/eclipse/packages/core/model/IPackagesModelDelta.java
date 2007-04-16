package org.jboss.ide.eclipse.packages.core.model;



public interface IPackagesModelDelta {
	
	/**
	 * There is no change to this node or any of it's children
	 */
	public static final int NO_CHANGE = 0;
		
	/**
	 * I have been added
	 */
	public static final int ADDED = 0x1;
	
	/**
	 * I have been removed
	 */
	public static final int REMOVED	= 0x2;
	
	
	
	/**
	 * Used to designate that a sub-property within 
	 * a <property> tag has been added.
	 */
	public static final int PROPERTY_ADDED = 0x10;
	
	/**
	 * Used to designate that a sub-property within 
	 * a <property> tag has been removed.
	 */
	public static final int PROPERTY_REMOVED = 0x20;
	
	/**
	 * Used to designate that a sub-property within 
	 * a <property> tag has been changed.
	 */
	public static final int PROPERTY_CHANGED = 0x40;
	
	/**
	 * Used to designate that an primary property of the node, 
	 * such as inWorkspace or exploded, has changed. 
	 */
	public static final int ATTRIBUTE_CHANGED = 0x80;
	
	/**
	 * A child has been added directly to me
	 */
	public static final int CHILD_ADDED		= 0x100;
	
	/**
	 * A child has been removed directly from me
	 */
	public static final int CHILD_REMOVED	= 0x200;
	
	/**
	 * Some other change has occurred, most likely a 
	 * grand-child added or a child's property changed.
	 */
	public static final int DESCENDENT_CHANGED 	= 0x400;
	
	/**
	 * Return the delta kind
	 * @return
	 */
	public int getKind();

	/**
	 * Return the affected node after changes
	 * @return
	 */
	public IPackageNode getPostNode();
	
	/**
	 * Return the affected node before changes
	 * @return
	 */
	public IPackageNode getPreNode();
	
	/**
	 * Get the children that have also been changed
	 * @return
	 */
	public IPackagesModelDelta[] getAffectedChildren();
	
	public String[] getPropertiesWithDeltas();
	public INodeDelta getPropertyDelta(String key);
	
	public String[] getAttributesWithDeltas();
	public INodeDelta getAttributeDelta(String key);
	
	public IPackagesModelDelta[] getAddedChildrenDeltas();
	public IPackagesModelDelta[] getRemovedChildrenDeltas();
	
	
	
	public interface INodeDelta {
		public Object getBefore();
		public Object getAfter();
		public int getKind();
	}
	
}
