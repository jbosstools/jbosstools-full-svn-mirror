package org.jboss.tools.process.ruleflow.model.node;

import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.process.ruleflow.model.NodeImpl;

public class SubProcessNode extends NodeImpl {
	
	public SubProcessNode() {
		super(new org.drools.workflow.core.node.SubProcessNode());
	}
	
	public SubProcessNode(org.drools.workflow.core.node.SubProcessNode node) {
		super(node);
		// TODO XML should contain this meta-data
		setMetaData("configurationElement",  //$NON-NLS-1$
			ElementRegistry.getConfigurationElement("org.jboss.tools.flow.ruleflow.subProcess")); //$NON-NLS-1$
	}

}
