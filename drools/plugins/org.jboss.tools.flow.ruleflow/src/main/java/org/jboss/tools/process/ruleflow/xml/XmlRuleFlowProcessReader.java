package org.jboss.tools.process.ruleflow.xml;

import java.io.Reader;

import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.xml.XmlProcessReader;
import org.jboss.tools.process.ruleflow.model.RuleFlowProcess;

public class XmlRuleFlowProcessReader {
	
	private XmlProcessReader processReader;
	
	public XmlRuleFlowProcessReader() {
		PackageBuilderConfiguration configuration = new PackageBuilderConfiguration();
        processReader = new XmlProcessReader(configuration.getSemanticModules());
	}
	
	public RuleFlowProcess read(Reader reader) throws Exception {
		org.drools.ruleflow.core.RuleFlowProcess process =
			(org.drools.ruleflow.core.RuleFlowProcess) processReader.read(reader);
		return new RuleFlowProcess(process);
	}

}
