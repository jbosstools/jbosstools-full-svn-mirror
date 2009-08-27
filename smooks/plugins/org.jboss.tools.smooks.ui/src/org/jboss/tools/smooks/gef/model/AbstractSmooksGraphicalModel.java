/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.gef.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.jboss.tools.smooks.gef.tree.model.IConnectableNode;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;

/**
 * @author Dart
 * 
 */
public class AbstractSmooksGraphicalModel implements IConnectableNode {

	public static final String PRO_ADD_CHILD = "_pro_add_child";

	public static final String PRO_REMOVE_CHILD = "_pro_remove_child";

	public static final String PRO_ADD_SOURCE_CONNECTION = "_pro_add_source_connected";

	public static final String PRO_ADD_TARGET_CONNECTION = "_pro_add_target_connected";

	public static final String PRO_REMOVE_SOURCE_CONNECTION = "_pro_remove_source_connected";

	public static final String PRO_REMOVE_TARGET_CONNECTION = "_pro_remove_target_connected";

	public static final String PRO_TEXT_CHANGED = "_pro_text_changed";

	public static final String PRO_FORCE_VISUAL_CHANGED = "_pro_force_text_changed";

	public static final String PRO_FORCE_CHIDLREN_CHANGED = "_pro_force_children_changed";

	public static final String PRO_FORCE_CONNECTION_CHANGED = "_pro_force_connection_changed";

	protected Object data = null;

	private boolean linkable = true;

	private AbstractSmooksGraphicalModel parent = null;

	private HashMap<String, Object> userDataMap = new HashMap<String, Object>();

	protected PropertyChangeSupport support = new PropertyChangeSupport(this);

	protected List<AbstractSmooksGraphicalModel> children = null;

	private List<TreeNodeConnection> sourceConnections = new ArrayList<TreeNodeConnection>();

	private List<TreeNodeConnection> targetConnections = new ArrayList<TreeNodeConnection>();

	public AbstractSmooksGraphicalModel(Object data) {
		setData(data);
	}

	public boolean hasChildren() {
		return true;
	}

	public AbstractSmooksGraphicalModel getParent() {
		return parent;
	}

	public void setParent(AbstractSmooksGraphicalModel parent) {
		this.parent = parent;
	}

	//
	// protected AbstractSmooksGraphicalModel createChildModel(Object model,
	// ITreeContentProvider contentProvider,
	// ILabelProvider labelProvider) {
	// return new TreeNodeModel(model, contentProvider, labelProvider);
	// }

	public List<AbstractSmooksGraphicalModel> getChildren() {
		if (children == null) {
			children = new ArrayList<AbstractSmooksGraphicalModel>();
		}
		return children;
	}
	
	public List<AbstractSmooksGraphicalModel> getChildrenWithoutDynamic() {
		if (children == null) {
			children = new ArrayList<AbstractSmooksGraphicalModel>();
		}
		return children;
	}

	public static void disconnectAllConnections(AbstractSmooksGraphicalModel node) {
		List<TreeNodeConnection> sourceConnections = node.getSourceConnections();
		List<TreeNodeConnection> targetConnections = node.getTargetConnections();
		List<TreeNodeConnection> tempSourceConnections = new ArrayList<TreeNodeConnection>(sourceConnections);
		List<TreeNodeConnection> tempTargetConnections = new ArrayList<TreeNodeConnection>(targetConnections);

		for (Iterator<?> iterator2 = tempTargetConnections.iterator(); iterator2.hasNext();) {
			TreeNodeConnection treeNodeConnection = (TreeNodeConnection) iterator2.next();
			AbstractSmooksGraphicalModel sourceNode = treeNodeConnection.getSourceNode();
			sourceNode.getSourceConnections().remove(treeNodeConnection);
			sourceNode.fireConnectionChanged();
		}

		for (Iterator<?> iterator2 = tempSourceConnections.iterator(); iterator2.hasNext();) {
			TreeNodeConnection treeNodeConnection = (TreeNodeConnection) iterator2.next();
//			treeNodeConnection.disconnectTarget();
			AbstractSmooksGraphicalModel targetNode = treeNodeConnection.getTargetNode();
			targetNode.getTargetConnections().remove(treeNodeConnection);
			targetNode.fireConnectionChanged();
		}

		tempSourceConnections.clear();
		tempTargetConnections.clear();
		tempSourceConnections = null;
		tempTargetConnections = null;

		List<AbstractSmooksGraphicalModel> children = node.getChildren();
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			TreeNodeModel treeNodeModel = (TreeNodeModel) iterator.next();
			disconnectAllConnections(treeNodeModel);
		}
	}

	protected boolean graphicalChildExist(Object model, Object[] models) {
		for (int i = 0; i < models.length; i++) {
			if (model == models[i]) {
				return true;
			}
		}
		return false;
	}

	protected boolean childExsit(Object model) {
		if (children != null) {
			for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
				TreeNodeModel node = (TreeNodeModel) iterator.next();
				if (node.getData() == model) {
					return true;
				}
			}
		}
		return false;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

	public void addSourceConnection(TreeNodeConnection connection) {
		if (this.sourceConnections.indexOf(connection) == -1) {
			this.sourceConnections.add(connection);
			support.firePropertyChange(PRO_ADD_SOURCE_CONNECTION, null, connection);
		}
	}

	public void removeSourceConnection(TreeNodeConnection connection) {
		if (this.sourceConnections.indexOf(connection) != -1) {
			this.sourceConnections.remove(connection);
			support.firePropertyChange(PRO_REMOVE_SOURCE_CONNECTION, connection, null);
		}
	}

	public void addTargetConnection(TreeNodeConnection connection) {
		if (this.targetConnections.indexOf(connection) == -1) {
			this.targetConnections.add(connection);
			support.firePropertyChange(PRO_ADD_TARGET_CONNECTION, null, connection);
		}
	}

	public void removeTargetConnection(TreeNodeConnection connection) {
		if (this.targetConnections.indexOf(connection) != -1) {
			this.targetConnections.remove(connection);
			support.firePropertyChange(PRO_REMOVE_TARGET_CONNECTION, connection, null);
		}
	}

	public void addChild(AbstractSmooksGraphicalModel node) {
		if (getChildrenWithoutDynamic().indexOf(node) == -1) {
			getChildrenWithoutDynamic().add(node);
			node.setParent(this);
			support.firePropertyChange(PRO_ADD_CHILD, null, node);
		}
	}

	public void removeChild(AbstractSmooksGraphicalModel node) {
		if (getChildrenWithoutDynamic().indexOf(node) != -1) {
			getChildrenWithoutDynamic().remove(node);
			node.setParent(null);
			support.firePropertyChange(PRO_REMOVE_CHILD, node, null);
		}
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setUserData(String key, Object data) {
		userDataMap.put(key, data);
	}

	public Object getUserData(String key) {
		return userDataMap.get(key);
	}

	public String getText() {
		// if (data != null && labelProvider != null) {
		// return labelProvider.getText(data);
		// }
		return null;
	}

	public Image getImage() {
		// if (data != null && labelProvider != null) {
		// return labelProvider.getImage(data);
		// }
		return null;
	}

	public boolean isLinkable() {
		return linkable;
	}

	public void setLinkable(boolean linkable) {
		this.linkable = linkable;
	}

	public boolean canLinkWithSource(Object model) {
		return true;
	}

	public boolean canLinkWithTarget(Object model) {
		return true;
	}

	public void setText(String text) {
	}

	public List<TreeNodeConnection> getSourceConnections() {
		return sourceConnections;
	}

	public List<TreeNodeConnection> getTargetConnections() {
		return targetConnections;
	}

	public void fireChildrenChanged() {
		support.firePropertyChange(PRO_FORCE_CHIDLREN_CHANGED, new Object(), null);
	}

	public void fireConnectionChanged() {
		support.firePropertyChange(PRO_FORCE_CONNECTION_CHANGED, new Object(), null);
	}

	public void fireVisualChanged() {
		support.firePropertyChange(PRO_FORCE_VISUAL_CHANGED, new Object(), null);
	}
}
