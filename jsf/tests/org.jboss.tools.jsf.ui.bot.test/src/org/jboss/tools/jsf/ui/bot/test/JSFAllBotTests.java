package org.jboss.tools.jsf.ui.bot.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.jsf.ui.bot.test.cssdialog.jbide.CSSSelectorJBIDE3288;
import org.jboss.tools.jsf.ui.bot.test.cssdialog.jbide.JBIDE3148and4441Test;
import org.jboss.tools.jsf.ui.bot.test.cssdialog.jbide.JBIDE3577Test;
import org.jboss.tools.jsf.ui.bot.test.cssdialog.jbide.JBIDE3579Test;
import org.jboss.tools.jsf.ui.bot.test.cssdialog.jbide.JBIDE3920Test;
import org.jboss.tools.jsf.ui.bot.test.cssdialog.jbide.JBIDE4391Test;
import org.jboss.tools.jsf.ui.bot.test.jsf2.refactor.JSF2AttributeRenameTest;
import org.jboss.tools.jsf.ui.bot.test.jsf2.refactor.JSF2MoveParticipantTest;
import org.jboss.tools.jsf.ui.bot.test.jsf2.refactor.JSF2RenameParticipantTest;
import org.jboss.tools.jsf.ui.bot.test.smoke.AddRemoveJSFCapabilitiesTest;
import org.jboss.tools.jsf.ui.bot.test.smoke.CodeCompletionTest;
import org.jboss.tools.jsf.ui.bot.test.smoke.CreateNewJSFProjectTest;
import org.jboss.tools.jsf.ui.bot.test.smoke.OpenOnTest;
import org.jboss.tools.jsf.ui.bot.test.templates.SetTemplateForUnknownTagTest;
import org.jboss.tools.jsf.ui.bot.test.templates.UnknownTemplateTest;

/**
 * 
 * This is a sample swtbot testcase for an eclipse application.
 * 
 */
public class JSFAllBotTests{
	public static Test suite(){
		TestSuite suite = new TestSuite("JSF all tests"); //$NON-NLS-1$
		suite.addTestSuite(CreateNewJSFProjectTest.class);		
		suite.addTestSuite(AddRemoveJSFCapabilitiesTest.class);
		suite.addTestSuite(JBIDE3148and4441Test.class);
		suite.addTestSuite(JBIDE4391Test.class);
		suite.addTestSuite(JBIDE3577Test.class);
		suite.addTestSuite(JBIDE3579Test.class);
		suite.addTestSuite(JBIDE3920Test.class);
		suite.addTestSuite(UnknownTemplateTest.class);
		suite.addTestSuite(SetTemplateForUnknownTagTest.class);
		suite.addTestSuite(CSSSelectorJBIDE3288.class);
		suite.addTestSuite(JSF2MoveParticipantTest.class);
		suite.addTestSuite(JSF2RenameParticipantTest.class);
		suite.addTestSuite(JSF2AttributeRenameTest.class);
		suite.addTestSuite(OpenOnTest.class);
		suite.addTestSuite(CodeCompletionTest.class);
		return suite;
	}
}