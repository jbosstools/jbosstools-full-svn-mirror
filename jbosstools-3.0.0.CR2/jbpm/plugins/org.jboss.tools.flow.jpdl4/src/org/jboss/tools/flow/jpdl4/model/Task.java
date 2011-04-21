package org.jboss.tools.flow.jpdl4.model;

import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.jpdl4.util.Jpdl4Helper;


public class Task extends ProcessNode {
	
	public void removeOutgoingConnection(String type, Connection connection) {
		Jpdl4Helper.mergeLeadingNodes(connection);
		super.removeOutgoingConnection(type, connection);
	}

}
