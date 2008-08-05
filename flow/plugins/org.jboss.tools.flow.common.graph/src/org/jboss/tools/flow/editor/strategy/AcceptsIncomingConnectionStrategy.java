package org.jboss.tools.flow.editor.strategy;

import org.jboss.tools.flow.common.core.Connection;
import org.jboss.tools.flow.common.core.Node;

public interface AcceptsIncomingConnectionStrategy {
	
	void setNode(Node node);
	
	boolean acceptsIncomingConnection(Connection connection, Node source);
	

}
