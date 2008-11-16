package org.jboss.tools.flow.jpdl4.model;

import java.util.Iterator;

import org.jboss.tools.flow.common.model.DefaultFlow;
import org.jboss.tools.flow.common.model.Node;

public class Process extends DefaultFlow {
	
	private Node initial = null;
	
	public StartEvent getStartState() {
		for (Iterator<Node> iterator = getNodes().iterator(); iterator.hasNext(); ) {
			Node node = iterator.next();
			if (node instanceof StartEvent) {
				return (StartEvent)node;
			}
		}
		return null;
	}
	
	public Node getInitial() {
		return initial;
	}
	
	public void setInitial(Node node) {
		initial = node;
	}
	
}
