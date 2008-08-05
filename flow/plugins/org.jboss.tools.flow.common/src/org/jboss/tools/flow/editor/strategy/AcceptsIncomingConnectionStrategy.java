package org.jboss.tools.flow.editor.strategy;

import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.common.model.Node;

public interface AcceptsIncomingConnectionStrategy {
	
	void setNode(Node node);
	
	boolean acceptsIncomingConnection(Connection connection, Node source);
	

}
