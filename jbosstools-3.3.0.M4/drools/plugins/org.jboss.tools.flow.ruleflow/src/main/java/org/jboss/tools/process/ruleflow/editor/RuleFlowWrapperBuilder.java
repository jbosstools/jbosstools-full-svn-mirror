package org.jboss.tools.process.ruleflow.editor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.common.model.Flow;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.common.wrapper.DefaultConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.DefaultFlowWrapper;
import org.jboss.tools.flow.common.wrapper.DefaultNodeWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.process.ruleflow.model.RuleFlowProcess;

public class RuleFlowWrapperBuilder {

	public DefaultFlowWrapper getProcessWrapper(Flow process) {
        if (process instanceof RuleFlowProcess) {
            RuleFlowProcess ruleFlowProcess = (RuleFlowProcess) process;
            DefaultFlowWrapper processWrapper = new DefaultFlowWrapper();
            processWrapper.setElement(process);
            Set<Node> nodes = new HashSet<Node>();
            nodes.addAll(ruleFlowProcess.getNodes());
            Set<Connection> connections = new HashSet<Connection>();
            processNodes(nodes, connections, processWrapper);
            return processWrapper;
        }
        return null;
    }
    
    public static void processNodes(Set<Node> nodes, Set<Connection> connections, DefaultFlowWrapper container) {
        Map<Node, NodeWrapper> nodeWrappers = new HashMap<Node, NodeWrapper>();
        for (Node node: nodes) {
            NodeWrapper nodeWrapper = new DefaultNodeWrapper();
            nodeWrapper.setElement(node);
            nodeWrapper.setParent(container);
            container.localAddElement(nodeWrapper);
            nodeWrappers.put(node, nodeWrapper);
            for (List<Connection> inConnections: node.getIncomingConnections().values()) {
                for (Connection connection: inConnections) {
                    connections.add(connection);
                }
            }
            for (List<Connection> outConnections: node.getOutgoingConnections().values()) {
                for (Connection connection: outConnections) {
                    connections.add(connection);
                }
            }
        }
        for (Connection connection: connections) {
            DefaultConnectionWrapper connectionWrapper = new DefaultConnectionWrapper();
            connectionWrapper.setElement(connection);
            connectionWrapper.localSetBendpoints(null);
            NodeWrapper from = nodeWrappers.get(connection.getFrom());
            NodeWrapper to = nodeWrappers.get(connection.getTo());
            if (from != null && to != null) {
	            connectionWrapper.localSetSource(from);
	            from.localAddOutgoingConnection(connectionWrapper);
	            connectionWrapper.localSetTarget(to);
	            to.localAddIncomingConnection(connectionWrapper);
            }
        }
    }
    
}
