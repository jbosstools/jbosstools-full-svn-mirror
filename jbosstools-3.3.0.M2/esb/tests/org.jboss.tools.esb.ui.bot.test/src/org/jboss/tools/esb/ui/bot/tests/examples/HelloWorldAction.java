package org.jboss.tools.esb.ui.bot.tests.examples;

import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@SWTBotTestRequires(server=@Server(type=ServerType.SOA,state=ServerState.Running))
public class HelloWorldAction extends ESBExampleTest {
	@Override
	public String getExampleName() {
		return "JBoss ESB HelloWorld Action Example - ESB";
	}
	@Override
	public String getExampleProjectName() {
		return "helloworld_action";
	}
	@Override
	public String getExampleClientProjectName() {
		return "helloworld_action_client";
	}

	@Override
	protected void executeExample() {
		super.executeExample();
		String text = executeClientGetServerOutput(getExampleClientProjectName(),"src","org.jboss.soa.esb.samples.quickstart.helloworldaction.test","SendJMSMessage.java");
		assertNotNull("Calling JMS Send message failed, nothing appened to server log",text);	
		assertTrue("Calling JMS Send message failed, unexpected server output :"+text,text.contains("Hello World Action ESB invoked!"));				
	}
}
