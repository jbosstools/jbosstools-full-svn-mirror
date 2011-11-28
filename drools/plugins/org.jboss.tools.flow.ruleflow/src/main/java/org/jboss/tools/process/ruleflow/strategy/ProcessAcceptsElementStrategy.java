package org.jboss.tools.process.ruleflow.strategy;

import org.jboss.tools.flow.common.model.Container;
import org.jboss.tools.flow.common.strategy.AcceptsElementStrategy;
import org.jboss.tools.process.ruleflow.model.RuleFlowProcess;
import org.jboss.tools.process.ruleflow.model.node.StartNode;

public class ProcessAcceptsElementStrategy implements AcceptsElementStrategy {
	
	private RuleFlowProcess process;

	public boolean acceptsElement(Object element) {
		if (process == null) {
			return false;
		} else if (element instanceof StartNode) {
			return process.getStart() == null;
		} else {
			return true;
		}
	}
	
	public void setContainer(Container container) {
		if (container instanceof RuleFlowProcess) {
			this.process = (RuleFlowProcess) container;
		}
	}

}
