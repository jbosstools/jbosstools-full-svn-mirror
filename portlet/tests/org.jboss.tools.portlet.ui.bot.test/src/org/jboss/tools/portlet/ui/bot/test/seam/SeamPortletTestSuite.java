package org.jboss.tools.portlet.ui.bot.test.seam;

import org.jboss.tools.portlet.ui.bot.test.RequirementAwareUsageWindowClosingSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareUsageWindowClosingSuite.class)
@Suite.SuiteClasses({
	CreateSeamPortletProject.class,
	CreateSeamPortletRuntime4x.class,
	CreateSeamPortletRuntime5x.class,
	RunSeamPortletOnServer.class
	})
public class SeamPortletTestSuite {

}
