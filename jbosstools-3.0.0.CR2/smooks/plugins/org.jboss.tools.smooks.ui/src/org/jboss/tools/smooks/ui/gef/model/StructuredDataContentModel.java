package org.jboss.tools.smooks.ui.gef.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;

public class StructuredDataContentModel extends AbstractStructuredDataModel
		implements IConnectableModel {

	public static final int DATA_OBJECT_NODE_CATEGORY_SOURCE = 1;
	public static final int DATA_OBJECT_NODE_CATEGORY_TARGET = 2;

	private String name;

	private String javaType;

	private boolean isComplexType;

	private boolean isRootNode;

	private boolean isListType;

	private boolean isObject;

	private Rectangle constraint;

	private int nodeCategory = -1;

	private List sourceConnections = new ArrayList();

	private List targetConnections = new ArrayList();

	public Rectangle getConstraint() {
		return constraint;
	}

	public void setConstraint(Rectangle constraint) {
		this.constraint = constraint;
	}

	public boolean isComplexType() {
		return isComplexType;
	}

	public void setComplexType(boolean complexType) {
		this.isComplexType = complexType;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRootNode() {
		return isRootNode;
	}

	public void setRootNode(boolean root) {
		this.isRootNode = root;
	}

	public boolean isListType() {
		return isListType;
	}

	public void setListType(boolean array) {
		this.isListType = array;
	}

	/**
	 */
	public boolean isObject() {
		return isObject;
	}

	/**
	 */
	public void setObject(boolean isObject) {
		this.isObject = isObject;
	}

	/**
	 */
	public int getNodeCategory() {
		return nodeCategory;
	}

	/**
	 */
	public void setNodeCategory(int nodeCategory) {
		this.nodeCategory = nodeCategory;
	}

	/**
	 * @param connx
	 */
	public void addSourceConnection(Object connx) {
		sourceConnections.add(connx);

		firePropertyChange(P_ADD_SOURCE_CONNECTION, null, connx);
	}

	/**
	 * @param connx
	 */
	public void addTargetConnection(Object connx) {
		targetConnections.add(connx);

		firePropertyChange(P_ADD_TARGET_CONNECTION, null, connx);
	}

	public List getModelSourceConnections() {
		return sourceConnections;
	}

	public List getModelTargetConnections() {
		return targetConnections;
	}

	public void removeSourceConnection(Object connx) {
		sourceConnections.remove(connx);

		firePropertyChange(P_ADD_SOURCE_CONNECTION, connx, null);
	}

	public void removeTargetConnection(Object connx) {
		targetConnections.remove(connx);

		firePropertyChange(P_ADD_TARGET_CONNECTION, connx, null);
	}

	public boolean equals(Object node) {
		return super.equals(node);
	}

	public boolean isSourceConnectWith(IConnectableModel target) {
		throw new RuntimeException("Don't support this method currently");
	}

	public boolean isTargetConnectWith(IConnectableModel source) {
		throw new RuntimeException("Don't support this method currently");
	}
}