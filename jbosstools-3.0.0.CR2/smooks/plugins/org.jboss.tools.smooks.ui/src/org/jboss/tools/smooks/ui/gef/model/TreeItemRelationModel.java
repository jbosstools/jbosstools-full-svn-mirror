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
package org.jboss.tools.smooks.ui.gef.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.TreeItem;

/**
 * @author Dart Peng
 * @Date Jul 31, 2008
 */
public class TreeItemRelationModel extends AbstractStructuredDataModel
		implements IConnectableModel {

	public static final String PRO_TREE_ITEM_SELECTION_STATUS = "__pro_treeitem_selection_status";

	public static final String PRO_TREE_REPAINT = "__pro_tree_repaint";

	TreeItem treeItem = null;
	List<Object> modelSourceConnections = new ArrayList<Object>();
	List<Object> modelTargetConnections = new ArrayList<Object>();

	public TreeItem getTreeItem() {
		return treeItem;
	}

	public boolean isCollapse() {
		TreeItem item = getTreeItem();
		int y = Integer.MAX_VALUE;
		if (item != null && !item.isDisposed()) {
			y = item.getBounds().y;
			// for windows
			TreeItem parentItem = item.getParentItem();
			if(parentItem == null) return false;
		}
		
		return (y == 0);
	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
	}

	public void addSourceConnection(Object connx) {
		this.getModelSourceConnections().add(connx);
		this.firePropertyChange(P_ADD_SOURCE_CONNECTION, null, connx);
	}

	public void addTargetConnection(Object connx) {
		this.getModelTargetConnections().add(connx);
		this.firePropertyChange(P_ADD_TARGET_CONNECTION, null, connx);
	}

	public void removeSourceConnection(Object connx) {
		this.getModelSourceConnections().remove(connx);
		this.firePropertyChange(P_REMOVE_SOURCE_CONNECTION, connx, null);
	}

	public void removeTargetConnection(Object connx) {
		this.getModelTargetConnections().remove(connx);
		this.firePropertyChange(P_REMOVE_TARGET_CONNECTION, connx, null);
	}

	public List<Object> getModelSourceConnections() {
		return modelSourceConnections;
	}

	public void setModelSourceConnections(List<Object> modelSourceConnections) {
		this.modelSourceConnections = modelSourceConnections;
	}

	public List<Object> getModelTargetConnections() {
		return modelTargetConnections;
	}

	public void setModelTargetConnections(List<Object> modelTargetConnections) {
		this.modelTargetConnections = modelTargetConnections;
	}

	public boolean isSourceConnectWith(IConnectableModel target) {
		List list = this.getModelSourceConnections();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			LineConnectionModel connection = (LineConnectionModel) iterator
					.next();
			if (connection.getTarget() == target) {
				return true;
			}
		}
		return false;
	}

	public boolean isTargetConnectWith(IConnectableModel source) {
		List list = this.getModelTargetConnections();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			LineConnectionModel connection = (LineConnectionModel) iterator
					.next();
			if (connection.getSource() == source) {
				return true;
			}
		}
		return false;
	}

}
