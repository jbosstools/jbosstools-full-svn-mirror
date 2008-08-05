package org.jboss.tools.flow.editor.strategy;

import org.jboss.tools.flow.common.core.Connection;
import org.jboss.tools.flow.common.core.Node;

public interface AcceptsOutgoingConnectionStrategy {
	
	void setNode(Node node);
	
	boolean acceptsOutgoingConnection(Connection connection, Node target);
	

}
