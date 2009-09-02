package org.jboss.tools.birt.core.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jboss.tools.tests.AbstractPluginsLoadTest;
import org.jboss.tools.tests.ToolsAllTests;

public class BirtCoreTestSuite extends TestCase {

	public static Test suite ()
	{
		TestSuite suite = new TestSuite(ToolsAllTests.class.getName());
		suite.addTestSuite(BirtPluginsLoadTest.class);
		return suite;
	}
	
	
	static public class BirtPluginsLoadTest extends AbstractPluginsLoadTest {
		
		public BirtPluginsLoadTest() {}
		
		public void testBirtPluginsAreResolvedAndActivated() {
			testBundlesAreLoadedFor("org.jboss.tools.birt.feature");
		}
	}
}
