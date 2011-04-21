package org.jboss.tools.bpel.ui.test;

import org.jboss.tools.bpel.ui.test.editor.JBossBPELEditorTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class BpelUIAllTests extends TestCase {
	public static final String PLUGIN_ID = "org.jboss.tools.bpel.ui.test";
	public static Test suite ()
	{
		TestSuite suite = new TestSuite(BpelUIAllTests.class.getName());
	
		suite.addTestSuite(JBossBPELEditorTest.class);
		return suite;
	}
}
