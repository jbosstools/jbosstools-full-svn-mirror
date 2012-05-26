package org.jboss.tools.process.ruleflow.model;

import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.common.registry.ElementRegistry;

public class ConnectionImpl implements Connection {
	
	private org.drools.workflow.core.impl.ConnectionImpl connection;
	
	private Node from;
	private Node to;
	private Map<String, Object> metaData = new HashMap<String, Object>();
	
	public ConnectionImpl() {
	}
	
	public ConnectionImpl(Node from, String fromType, Node to, String toType) {
		this.from = from;
		this.to = to;
		this.connection = new org.drools.workflow.core.impl.ConnectionImpl(
			((NodeImpl) from).getNode(), fromType,
			((NodeImpl) to).getNode(), toType);
	}
	
	public ConnectionImpl(org.drools.workflow.core.Connection connection, Node from, Node to) {
		this.connection = (org.drools.workflow.core.impl.ConnectionImpl) connection;
		// TODO XML should contain this meta-data
		setMetaData("configurationElement",  //$NON-NLS-1$
			ElementRegistry.getConfigurationElement("org.jboss.tools.flow.ruleflow.connection")); //$NON-NLS-1$
		this.from = from;
		this.to = to;
	}
	
	public org.drools.workflow.core.Connection getConnection() {
		return connection;
	}

	public Node getFrom() {
		return from;
	}

	public void setFrom(Node from) {
		if (connection != null) {
			connection.terminate();
			connection = null;
		}
		this.from = from;
		if (from != null && to != null) {
			connection = new org.drools.workflow.core.impl.ConnectionImpl(
				((NodeImpl) from).getNode(), org.drools.workflow.core.Node.CONNECTION_DEFAULT_TYPE,
				((NodeImpl) to).getNode(), org.drools.workflow.core.Node.CONNECTION_DEFAULT_TYPE);
			for (Map.Entry<String, Object> entry: metaData.entrySet()) {
				connection.setMetaData(entry.getKey(), entry.getValue());
			}
		}
	}

	public String getFromType() {
		return connection == null ? null : connection.getFromType();
	}

	public Node getTo() {
		return to;
	}

	public void setTo(Node to) {
		if (connection != null) {
			connection.terminate();
			connection = null;
		}
		this.to = to;
		if (from != null && to != null) {
			connection = new org.drools.workflow.core.impl.ConnectionImpl(
				((NodeImpl) from).getNode(), org.drools.workflow.core.Node.CONNECTION_DEFAULT_TYPE,
				((NodeImpl) to).getNode(), org.drools.workflow.core.Node.CONNECTION_DEFAULT_TYPE);
		}
	}

	public String getToType() {
		return connection == null ? null : connection.getToType();
	}

	public Object getMetaData(String name) {
		return metaData.get(name);
	}

	public void setMetaData(String name, Object value) {
		metaData.put(name, value);
		if (connection != null) {
			connection.setMetaData(name, value);
		}
	}

}
