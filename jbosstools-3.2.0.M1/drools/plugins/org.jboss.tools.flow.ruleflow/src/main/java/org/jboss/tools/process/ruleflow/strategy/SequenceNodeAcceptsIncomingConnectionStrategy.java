package org.jboss.tools.process.ruleflow.strategy;

import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.common.strategy.AcceptsIncomingConnectionStrategy;

public class SequenceNodeAcceptsIncomingConnectionStrategy implements AcceptsIncomingConnectionStrategy {

	private Node node;
	
	public boolean acceptsIncomingConnection(Connection connection, Node source) {
		return 
			(source == null
				|| source.getNodeContainer() == node.getNodeContainer())
			&& node.getIncomingConnections().isEmpty();
	}

	public void setNode(Node node) {
		this.node = node;
	}

}
