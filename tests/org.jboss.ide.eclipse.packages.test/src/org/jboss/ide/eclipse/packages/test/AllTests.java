package org.jboss.ide.eclipse.packages.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTestSuite(PackagesEARTest.class);
		suite.addTestSuite(NewProjectTest.class);
		suite.addTestSuite(PackagesBuildTest.class);
		
		return suite;
	}
}
