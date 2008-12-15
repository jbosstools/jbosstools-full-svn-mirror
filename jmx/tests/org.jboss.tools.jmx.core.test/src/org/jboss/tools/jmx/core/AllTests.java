package org.jboss.tools.jmx.core;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.jmx.core.tests.DefaultMBeanServerConnectionFactoryTest;
import org.jboss.tools.jmx.core.tests.DefaultProviderTest;
import org.jboss.tools.jmx.core.tests.NodeBuilderTestCase;


public class AllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(DefaultMBeanServerConnectionFactoryTest.class);
		suite.addTestSuite(DefaultProviderTest.class);
		suite.addTestSuite(NodeBuilderTestCase.class);
		return suite;
	}
}
