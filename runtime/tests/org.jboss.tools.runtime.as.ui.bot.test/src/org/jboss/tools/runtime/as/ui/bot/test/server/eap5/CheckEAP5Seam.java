package org.jboss.tools.runtime.as.ui.bot.test.server.eap5;

import org.jboss.tools.runtime.as.ui.bot.test.RuntimeProperties;
import org.jboss.tools.runtime.as.ui.bot.test.entity.Runtime;
import org.jboss.tools.runtime.as.ui.bot.test.template.CheckSeamRuntimeTemplate;

public class CheckEAP5Seam extends CheckSeamRuntimeTemplate {

	@Override
	protected Runtime getExpectedRuntime() {
		Runtime server = new Runtime();
		server.setName("Seam seam 2.2");
		server.setVersion("2.2");
		server.setLocation(RuntimeProperties.getInstance().getRuntimePath(DetectEAP5.SERVER_ID) + "/seam");
		return server;
	}
}
