package org.jboss.tools.portlet.core;

import org.jboss.tools.tests.AbstractPluginsLoadTest;

public class PortletPluginsLoadTest extends AbstractPluginsLoadTest {
	
	public void testPortlrtPlugisAreResolvedAndActivated() {
		testBundlesAreLoadedFor("org.jboss.tools.portlet.feature");
	}
}
