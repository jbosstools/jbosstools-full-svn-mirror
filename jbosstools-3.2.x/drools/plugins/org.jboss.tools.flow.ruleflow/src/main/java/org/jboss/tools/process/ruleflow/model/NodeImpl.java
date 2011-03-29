package org.jboss.tools.process.ruleflow.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.common.model.Container;
import org.jboss.tools.flow.common.model.Node;

public class NodeImpl implements Node {

	private org.drools.workflow.core.Node node;
	
	private Container nodeContainer;
	private Map<String, List<Connection>> incomingConnections = new HashMap<String, List<Connection>>();
	private Map<String, List<Connection>> outgoingConnections = new HashMap<String, List<Connection>>();
	
	public NodeImpl(org.drools.workflow.core.Node node) {
		this.node = node;
	}
	
	public org.drools.workflow.core.Node getNode() {
		return node;
	}

	public void addIncomingConnection(String type, Connection connection) {
		internalAddIncomingConnection(type, connection);
		node.addIncomingConnection(type, ((ConnectionImpl) connection).getConnection());
	}
	
	public void internalAddIncomingConnection(String type, Connection connection) {
		List<Connection> list = incomingConnections.get(type);
		if (list == null) {
			list = new ArrayList<Connection>();
			incomingConnections.put(type, list);
		}
		list.add(connection);
	}

	public void addOutgoingConnection(String type, Connection connection) {
		internalAddOutgoingConnection(type, connection);
		node.addOutgoingConnection(type, ((ConnectionImpl) connection).getConnection());
	}
	
	public void internalAddOutgoingConnection(String type, Connection connection) {
		List<Connection> list = outgoingConnections.get(type);
		if (list == null) {
			list = new ArrayList<Connection>();
			outgoingConnections.put(type, list);
		}
		list.add(connection);
	}

	public long getId() {
		return node.getId();
	}

	public Map<String, List<Connection>> getIncomingConnections() {
		return incomingConnections;
	}

	public List<Connection> getIncomingConnections(String type) {
		return incomingConnections.get(type);
	}

	public Object getMetaData(String name) {
		return node.getMetaData(name);
	}

	public String getName() {
		return node.getName();
	}

	public Container getNodeContainer() {
		return nodeContainer;
	}

	public Map<String, List<Connection>> getOutgoingConnections() {
		return outgoingConnections;
	}

	public List<Connection> getOutgoingConnections(String type) {
		return outgoingConnections.get(type);
	}

	public void removeIncomingConnection(String type, Connection connection) {
		List<Connection> list = incomingConnections.get(type);
		if (list != null) {
			list.remove(connection);
			if (list.isEmpty()) {
				incomingConnections.remove(type);
			}
		}
		node.removeIncomingConnection(type, ((ConnectionImpl) connection).getConnection());
	}

	public void removeOutgoingConnection(String type, Connection connection) {
		List<Connection> list = outgoingConnections.get(type);
		if (list != null) {
			list.remove(connection);
			if (list.isEmpty()) {
				outgoingConnections.remove(type);
			}
		}
		node.removeOutgoingConnection(type, ((ConnectionImpl) connection).getConnection());
	}

	public void setId(long id) {
		node.setId(id);
	}

	public void setMetaData(String name, Object value) {
		node.setMetaData(name, value);
	}

	public void setName(String name) {
		node.setName(name);
	}

	public void setNodeContainer(Container nodeContainer) {
		this.nodeContainer = nodeContainer;
		node.setNodeContainer(((RuleFlowProcess) nodeContainer).getRuleFlowProcess());
	}
}
