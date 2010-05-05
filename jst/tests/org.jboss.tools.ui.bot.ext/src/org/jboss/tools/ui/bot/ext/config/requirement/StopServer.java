package org.jboss.tools.ui.bot.ext.config.requirement;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
/**
 * Stops server (as dependent requirement has {@link AddServer}
 * @author Vladimir Pakan
 *
 */
public class StopServer extends RequirementBase {

	@Override
	public boolean checkFulfilled() {
		return !SWTTestExt.configuredState.getServer().isRunning;
	}

	@Override
	public void handle(){
	  if (SWTTestExt.configuredState.getServer().isRunning){
	    SWTTestExt.servers.stopServer(SWTTestExt.configuredState.getServer().name);
	    SWTTestExt.configuredState.getServer().isRunning = false;
	  }
	}
}
