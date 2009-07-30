package org.jboss.tools.smooks.gef.common;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;

public class RootModel {

	public static final String ADD_CHILDREN = "__pro_add_children";

	public static final String REMOVE_CHILDREN = "__pro_remove_children";

	private PropertyChangeSupport support = new PropertyChangeSupport(this);

	private List<AbstractSmooksGraphicalModel> children;

	public List<AbstractSmooksGraphicalModel> getChildren() {
		if (children == null) {
			children = new ArrayList<AbstractSmooksGraphicalModel>();
		}
		return children;
	}

	public void removeAllTreeNode() {
		getChildren().clear();
		support.firePropertyChange(REMOVE_CHILDREN, new Object(), null);
	}

	public void removeTreeNode(AbstractSmooksGraphicalModel node) {
		this.getChildren().remove(node);
		support.firePropertyChange(REMOVE_CHILDREN, node, null);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

	public void addTreeNode(AbstractSmooksGraphicalModel node) {
		this.getChildren().add(node);
		support.firePropertyChange(ADD_CHILDREN, null, node);
	}
}
