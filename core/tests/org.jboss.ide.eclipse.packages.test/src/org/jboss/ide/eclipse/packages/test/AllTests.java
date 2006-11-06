package org.jboss.ide.eclipse.packages.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new PackagesEARTest("testCorrectBinding"));
		suite.addTest(new PackagesEARTest("testModelBridge"));
		suite.addTest(new PackagesEARTest("testSave"));
		suite.addTest(new PackagesEARTest("testWorkingCopies"));
		suite.addTest(new PackagesEARTest("testBuild"));
		
		suite.addTest(new NewProjectTest("testXbConsistency"));
		suite.addTest(new NewProjectTest("testEclipseModelConsistency"));
		
		return suite;
	}
}
