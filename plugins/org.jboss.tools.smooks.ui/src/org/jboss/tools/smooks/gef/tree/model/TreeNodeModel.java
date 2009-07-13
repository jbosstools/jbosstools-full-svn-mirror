package org.jboss.tools.smooks.gef.tree.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.graphics.Image;

public class TreeNodeModel implements IConnectableNode {

	protected ITreeContentProvider contentProvider;

	protected ILabelProvider labelProvider;

	public static final String PRO_ADD_CHILD = "_pro_add_child";

	public static final String PRO_REMOVE_CHILD = "_pro_remove_child";

	public static final String PRO_ADD_SOURCE_CONNECTION = "_pro_add_source_connected";

	public static final String PRO_ADD_TARGET_CONNECTION = "_pro_add_target_connected";

	public static final String PRO_REMOVE_SOURCE_CONNECTION = "_pro_remove_source_connected";

	public static final String PRO_REMOVE_TARGET_CONNECTION = "_pro_remove_target_connected";

	public static final String PRO_TEXT_CHANGED = "_pro_text_changed";

	protected Object data = null;

	private boolean linkable = true;

	private TreeNodeModel parent = null;

	private HashMap<String, Object> userDataMap = new HashMap<String, Object>();

	protected PropertyChangeSupport support = new PropertyChangeSupport(this);

	protected List<TreeNodeModel> children = null;

	private List<TreeNodeConnection> sourceConnections = new ArrayList<TreeNodeConnection>();

	private List<TreeNodeConnection> targetConnections = new ArrayList<TreeNodeConnection>();

	public TreeNodeModel(Object data, ITreeContentProvider contentProvider, ILabelProvider labelProvider) {
		setContentProvider(contentProvider);
		setLabelProvider(labelProvider);
		setData(data);
	}

	public boolean hasChildren() {
		if (contentProvider != null && data != null) {
			return contentProvider.hasChildren(data);
		}
		return true;
	}

	public TreeNodeModel getParent() {
		return parent;
	}

	public void setParent(TreeNodeModel parent) {
		this.parent = parent;
	}

	public ITreeContentProvider getContentProvider() {
		return contentProvider;
	}

	public void setContentProvider(ITreeContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}

	public ILabelProvider getLabelProvider() {
		return labelProvider;
	}

	public void setLabelProvider(ILabelProvider labelProvider) {
		this.labelProvider = labelProvider;
	}

	protected TreeNodeModel createChildModel(Object model, ITreeContentProvider contentProvider,
			ILabelProvider labelProvider) {
		return new TreeNodeModel(model, contentProvider, labelProvider);
	}

	public List<TreeNodeModel> getChildren() {
		if (children == null) {
			if (this.contentProvider != null && data != null) {
				Object[] models = contentProvider.getChildren(data);
				if (models != null) {
					children = new ArrayList<TreeNodeModel>();
					for (int i = 0; i < models.length; i++) {
						Object model = models[i];
						TreeNodeModel n = createChildModel(model, contentProvider, labelProvider);
						children.add(n);
						n.setParent(this);
					}
					return children;
				}
			}
			return Collections.emptyList();
		} else {
			if (this.contentProvider != null && data != null) {
				Object[] models = contentProvider.getChildren(data);
				if (models != null) {
					for (int i = 0; i < models.length; i++) {
						Object model = models[i];
						if (!childExsit(model)) {
							TreeNodeModel n = createChildModel(model, contentProvider, labelProvider);
							children.add(n);
							n.setParent(this);
						}
					}
					List<TreeNodeModel> temp = new ArrayList<TreeNodeModel>(children);
					for (Iterator<?> iterator = temp.iterator(); iterator.hasNext();) {
						TreeNodeModel node = (TreeNodeModel) iterator.next();
						Object data = node.getData();
						if (!graphicalChildExist(data, models)) {
							children.remove(node);
							node.setParent(null);
						}
					}
					temp.clear();
					temp = null;
				}
			}
		}
		return children;
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

	public void addChild(TreeNodeModel node) {
		if (getChildren().indexOf(node) == -1) {
			getChildren().add(node);
			node.setParent(this);
			support.firePropertyChange(PRO_ADD_CHILD, null, node);
		}
	}

	public void removeChild(TreeNodeModel node) {
		if (getChildren().indexOf(node) != -1) {
			getChildren().remove(node);
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
		if (data != null && labelProvider != null) {
			return labelProvider.getText(data);
		}
		return "";
	}

	public Image getImage() {
		if (data != null && labelProvider != null) {
			return labelProvider.getImage(data);
		}
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
		// if (text != null && !text.equals(this.text)) {
		// String oldtext = this.text;
		// this.text = text;
		// support.firePropertyChange(PRO_TEXT_CHANGED, oldtext, this.text);
		// }
	}

	public List<TreeNodeConnection> getSourceConnections() {
		return sourceConnections;
	}

	public List<TreeNodeConnection> getTargetConnections() {
		return targetConnections;
	}

}
