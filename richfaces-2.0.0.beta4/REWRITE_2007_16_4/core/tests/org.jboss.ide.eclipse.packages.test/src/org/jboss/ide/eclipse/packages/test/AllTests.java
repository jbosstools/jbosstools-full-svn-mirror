package org.jboss.ide.eclipse.packages.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.ide.eclipse.packages.test.build.FileOpsTest;
import org.jboss.ide.eclipse.packages.test.build.IncrementalBuilderTest;

public class AllTests {

	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTestSuite(PackagesEARTest.class);
		suite.addTestSuite(NewProjectTest.class);
		suite.addTestSuite(IncrementalBuilderTest.class);
		suite.addTestSuite(FileOpsTest.class);
		return suite;
	}
}
