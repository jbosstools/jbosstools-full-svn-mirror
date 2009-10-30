package org.jboss.tools.portlet.core.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PortletCoreTestSuite {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(PortletPluginsLoadTest.class);
		return suite;
	}
}
