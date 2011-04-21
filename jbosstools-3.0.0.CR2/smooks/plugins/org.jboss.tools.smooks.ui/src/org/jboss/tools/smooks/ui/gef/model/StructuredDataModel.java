package org.jboss.tools.smooks.ui.gef.model;

import java.util.ArrayList;
import java.util.List;

public class StructuredDataModel extends AbstractStructuredDataModel implements
		IConnectableModel {

	public static final int KIND_INPUT = 1;
	public static final int KIND_OUTPUT = 2;

	private List modelSourceConnections = new ArrayList();

	private List modelTargetConnections = new ArrayList();

	public StructuredDataModel() {
		super();
		this.setHeight(200);
		this.setWidht(100);
	}

	/**
	 * @return the modelSourceConnections
	 */
	public List getModelSourceConnections() {
		return modelSourceConnections;
	}

	/**
	 * @param modelSourceConnections
	 *            the modelSourceConnections to set
	 */
	public void setModelSourceConnections(List modelSourceConnections) {
		this.modelSourceConnections = modelSourceConnections;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.ui.gef.model.IConnectableModel#addSourceConnection
	 * (java.lang.Object)
	 */
	public void addSourceConnection(Object connx) {
		modelSourceConnections.add(connx);
		firePropertyChange(IConnectableModel.P_ADD_SOURCE_CONNECTION, null, connx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.ui.gef.model.IConnectableModel#addTargetConnection
	 * (java.lang.Object)
	 */
	public void addTargetConnection(Object connx) {
		modelTargetConnections.add(connx);
		firePropertyChange(IConnectableModel.P_ADD_TARGET_CONNECTION, null, connx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.ui.gef.model.IConnectableModel#removeSourceConnection
	 * (java.lang.Object)
	 */
	public void removeSourceConnection(Object connx) {
		modelSourceConnections.remove(connx);
		firePropertyChange(IConnectableModel.P_ADD_SOURCE_CONNECTION, connx, null);
	}
	
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.ui.gef.model.IConnectableModel#removeTargetConnection
	 * (java.lang.Object)
	 */
	public void removeTargetConnection(Object connx) {
		modelTargetConnections.remove(connx);
		firePropertyChange(IConnectableModel.P_ADD_TARGET_CONNECTION, connx, null);
	}

	/**
	 * @return the modelTargetConnections
	 */
	public List getModelTargetConnections() {
		return modelTargetConnections;
	}

	/**
	 * @param modelTargetConnections
	 *            the modelTargetConnections to set
	 */
	public void setModelTargetConnections(List modelTargetConnections) {
		this.modelTargetConnections = modelTargetConnections;
	}

	public boolean isSourceConnectWith(IConnectableModel target) {
		throw new RuntimeException("Don't support this method currently");
	}

	public boolean isTargetConnectWith(IConnectableModel source) {
		throw new RuntimeException("Don't support this method currently");
	}
}