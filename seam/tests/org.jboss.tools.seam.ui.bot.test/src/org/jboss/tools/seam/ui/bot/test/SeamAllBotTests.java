package org.jboss.tools.seam.ui.bot.test;

import org.jboss.tools.seam.ui.bot.test.create.CreateSeamRuntimes;
import org.jboss.tools.seam.ui.bot.test.create.CreateSeamProjects;
import org.jboss.tools.seam.ui.bot.test.create.CreateForms;
import org.jboss.tools.seam.ui.bot.test.create.CreateActions;
import org.jboss.tools.seam.ui.bot.test.create.CreateConversations;
import org.jboss.tools.seam.ui.bot.test.create.CreateEntities;
import org.jboss.tools.seam.ui.bot.test.create.CreateServerRuntimes;

import junit.framework.Test;
import junit.framework.TestSuite;



/**
 * 
 * This is a swtbot testcase for an eclipse application.
 * 
 */
public class SeamAllBotTests {
	public static Test suite(){
		TestSuite suite = new TestSuite("Seam tests");
		suite.addTestSuite(CreateServerRuntimes.class);
		suite.addTestSuite(CreateSeamRuntimes.class);
		suite.addTestSuite(CreateSeamProjects.class);
		suite.addTestSuite(CreateForms.class);
		suite.addTestSuite(CreateActions.class);
		suite.addTestSuite(CreateConversations.class);
		suite.addTestSuite(CreateEntities.class);
		return suite;
	}
}