package org.jboss.tools.process.ruleflow.model.node;

import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.process.ruleflow.model.NodeImpl;

public class StartNode extends NodeImpl {
	
	public StartNode() {
		super(new org.drools.workflow.core.node.StartNode());
	}

	public StartNode(org.drools.workflow.core.node.StartNode node) {
		super(node);
		// TODO XML should contain this meta-data
		setMetaData("configurationElement",  //$NON-NLS-1$
			ElementRegistry.getConfigurationElement("org.jboss.tools.flow.ruleflow.start")); //$NON-NLS-1$
	}

}
