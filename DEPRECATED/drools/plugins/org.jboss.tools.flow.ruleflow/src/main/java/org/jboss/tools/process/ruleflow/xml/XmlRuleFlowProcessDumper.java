package org.jboss.tools.process.ruleflow.xml;

import org.jboss.tools.process.ruleflow.model.RuleFlowProcess;

public class XmlRuleFlowProcessDumper {
	
	public static String dump(RuleFlowProcess process) {
		return org.drools.xml.XmlRuleFlowProcessDumper.INSTANCE.dump(process.getRuleFlowProcess(), true);
	}

}
