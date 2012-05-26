package org.jboss.tools.process.ruleflow.strategy;

import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.common.strategy.AcceptsIncomingConnectionStrategy;

public class StartNodeAcceptsIncomingConnectionStrategy implements AcceptsIncomingConnectionStrategy {

	public boolean acceptsIncomingConnection(Connection connection, Node source) {
		return false;
	}

	public void setNode(Node node) {
	}

}
