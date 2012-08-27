package org.jboss.tools.portlet.ui.bot.matcher.factory;

import org.jboss.tools.portlet.ui.bot.entity.PortletDefinition;
import org.jboss.tools.portlet.ui.bot.matcher.SWTMatcher;
import org.jboss.tools.portlet.ui.bot.matcher.browser.portlet.PortletLoadsInJBPortalMatcher;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;

public class PortletMatchersFactory {

	private PortletMatchersFactory(){
		// not to be initialized
	}
	
	public static SWTMatcher<PortletDefinition> canLoadAt4xRuntime(){
		return new PortletLoadsInJBPortalMatcher(TaskDuration.NORMAL);
	}
}
