package org.jboss.tools.openshift.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

/**
 * Domain creation consists of creating the SSH key pair, storing user password
 * in the secure storage and creating the domain itself.
 * 
 * @author sbunciak
 * 
 */
public class CreateDomain extends SWTTestExt {

	@Test
	public void canCreateDomain() throws InterruptedException {

			SWTBotText domainText = bot.text(0);
			
			assertTrue("Domain should not be set at this stage!", domainText
					.getText().equals(""));

			domainText.setText(TestProperties.getProperty("openshift.domain"));
			bot.button(IDELabel.Button.FINISH).click();

			bot.waitUntil(new NonSystemJobRunsCondition());

			bot.waitForShell("New OpenShift Express Application", 100);
			
			//@SuppressWarnings("unchecked")
			//Matcher<Widget> matcher = WidgetMatcherFactory.allOf(
			//		WidgetMatcherFactory.widgetOfType(Shell.class),
			//		WidgetMatcherFactory.withText("New OpenShift Express Application"));

			//bot.waitUntilWidgetAppears(Conditions.waitForWidget(matcher));
			bot.waitWhile(new ICondition() {
				@Override
				public boolean test() {
					return bot.shell(IDELabel.Shell.SECURE_STORAGE).isVisible();
				}

				@Override
				public void init(SWTBot bot) {
					// keep empty
				}

				@Override
				public String getFailureMessage() {
					return "Domain creation wizard still visible in user account after reasonable timeout.";
				}
				
			}, TIME_20S, TIME_1S);
			
			bot.waitUntil(new NonSystemJobRunsCondition());
	}

}
