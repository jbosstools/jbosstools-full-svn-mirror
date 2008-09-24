package org.jboss.tools.process.ruleflow.strategy;

import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.common.strategy.AcceptsOutgoingConnectionStrategy;

public class SequenceNodeAcceptsOutgoingConnectionStrategy implements AcceptsOutgoingConnectionStrategy {

	private Node node;
	
	public boolean acceptsOutgoingConnection(Connection connection, Node target) {
		return 
			(target == null
	    		|| target.getNodeContainer() == node.getNodeContainer())
			&& node.getOutgoingConnections().isEmpty();
	}

	public void setNode(Node node) {
		this.node = node;
	}

}
