package org.jboss.tools.process.ruleflow.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.tools.flow.common.model.Flow;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.process.ruleflow.model.node.StartNode;
import org.jboss.tools.process.ruleflow.model.node.SubProcessNode;

public class RuleFlowProcess implements Flow {
	
	private org.drools.ruleflow.core.RuleFlowProcess process;
	
	private Map<Long, Node> nodes = new HashMap<Long, Node>();
	
	public RuleFlowProcess() {
		this.process = new org.drools.ruleflow.core.RuleFlowProcess();
	}

	public RuleFlowProcess(org.drools.ruleflow.core.RuleFlowProcess process) {
		this.process = process;
		// TODO XML should contain this meta-data
		setMetaData("configurationElement", 
			ElementRegistry.getConfigurationElement("org.jboss.tools.flow.ruleflow.process"));
		List<org.drools.workflow.core.Connection> connections = new ArrayList<org.drools.workflow.core.Connection>();
		for (org.drools.workflow.core.Node node: process.getNodes()) {
			Node newNode = null;
			if (node instanceof org.drools.workflow.core.node.StartNode) {
				newNode = new StartNode((org.drools.workflow.core.node.StartNode) node);
			} else if (node instanceof org.drools.workflow.core.node.SubProcessNode) {
				newNode = new SubProcessNode((org.drools.workflow.core.node.SubProcessNode) node);
			}
			if (newNode == null) {
				throw new IllegalArgumentException("Could not find NodeImpl for node " + newNode);
			}
			this.nodes.put(node.getId(), newNode);
			for (List<org.drools.workflow.core.Connection> inConnections: node.getIncomingConnections().values()) {
                for (org.drools.workflow.core.Connection connection: inConnections) {
                    connections.add(connection);
                }
            }
		}
		for (org.drools.workflow.core.Connection connection: connections) {
			NodeImpl from = (NodeImpl) nodes.get(connection.getFrom().getId());
			NodeImpl to = (NodeImpl) nodes.get(connection.getTo().getId());
            ConnectionImpl newConnection = new ConnectionImpl(connection, from, to);
            from.internalAddOutgoingConnection(connection.getFromType(), newConnection);
            to.internalAddIncomingConnection(connection.getToType(), newConnection);
        }
	}
	
	public org.drools.ruleflow.core.RuleFlowProcess getRuleFlowProcess() {
		return process;
	}
	
	public String getId() {
		return process.getId();
	}

	public String getName() {
		return process.getName();
	}

	public String getPackageName() {
		return process.getPackageName();
	}

	public String getType() {
		return process.getType();
	}

	public String getVersion() {
		return process.getVersion();
	}

	public void setId(String id) {
		process.setId(id);
	}

	public void setName(String name) {
		process.setName(name);
	}

	public void setPackageName(String packageName) {
		process.setPackageName(packageName);
	}

	public void setType(String type) {
		process.setType(type);
	}

	public void setVersion(String version) {
		process.setVersion(version);
	}

	public void addNode(Node node) {
		nodes.put(node.getId(), node);
		process.addNode(((NodeImpl) node).getNode());
	}

	public Node getNode(long id) {
		return nodes.get(id);
	}

	public List<Node> getNodes() {
		return new ArrayList<Node>(nodes.values());
	}

	public void removeNode(Node node) {
		nodes.remove(node.getId());
		process.removeNode(((NodeImpl) node).getNode());
	}

	public Object getMetaData(String name) {
		return process.getMetaData(name);
	}

	public void setMetaData(String name, Object value) {
		process.setMetaData(name, value);
	}
	
	public StartNode getStart() {
		org.drools.workflow.core.node.StartNode start = process.getStart();
		if (start == null) {
			return null;
		}
		return (StartNode) nodes.get(start.getId());
	}

}
