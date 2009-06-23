package org.jboss.bpel.ui.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class BpelUIAllTests extends TestCase {
	public static final String PLUGIN_ID = "org.jboss.bpel.ui.tests";
	public static Test suite ()
	{
		TestSuite suite = new TestSuite(BpelUIAllTests.class.getName());
		return suite;
	}
}
