package org.jboss.tools.bpel.ui.bot.test.examples;

import org.jboss.tools.bpel.ui.bot.test.BPELTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

/**
 * 
 * @author apodhrad
 * 
 */
@Require(server = @Server(type = ServerType.ALL, state = ServerState.Running), perspective = "BPEL")
public class CorrelationExampleTest extends BPELExampleTest {

	private static final String PROJECT_NAME = "Simple_Correlation";
	private static final String WSDL_URL = "http://localhost:8080/Quickstart_bpel_simple_correlationWS?wsdl";

	@Override
	public String[] getProjectNames() {
		return new String[] { PROJECT_NAME };
	}

	@Override
	public String getExampleName() {
		return "A correlation BPEL example";
	}

	@Override
	protected void executeExample() {
		deployExamples(PROJECT_NAME);
		assertTrue(BPELTest.isProjectDeployed(PROJECT_NAME));

		testResponses(WSDL_URL, PROJECT_NAME);

		servers.removeAllProjectsFromServer();
	}


}
