package org.jboss.tools.esb.ui.bot.tests.examples;

import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@SWTBotTestRequires(server=@Server(type=ServerType.SOA,state=ServerState.Running))
public class HelloWorldFileAction extends ESBExampleTest {
	@Override
	public String getExampleName() {
		return "JBoss ESB HelloWorld File Action Example - ESB";
	}
	@Override
	public String getExampleProjectName() {
		return "helloworld_file_action";
	}
	@Override
	public String getExampleClientProjectName() {
		return "helloworld_file_action_client";
	}
	@Override
	protected void executeExample() {
		fail("Example execution not yet implemented");
	}
}
