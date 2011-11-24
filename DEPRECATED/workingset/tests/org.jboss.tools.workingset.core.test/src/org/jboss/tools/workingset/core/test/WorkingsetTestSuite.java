package org.jboss.tools.workingset.core.test;

import junit.framework.Test;
import junit.framework.TestSuite;


public class WorkingsetTestSuite {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(GroupingTest.class);
		return suite;
	}
}
