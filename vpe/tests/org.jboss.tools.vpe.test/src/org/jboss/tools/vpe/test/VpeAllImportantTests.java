package org.jboss.tools.vpe.test;

import org.jboss.tools.vpe.editor.template.VpeTemplateManagerTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite containing all important VPE core tests.
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
public class VpeAllImportantTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(VpeAllImportantTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(TemplateLoadingTest.class);
		suite.addTestSuite(TemplateSchemeValidateTest.class);
		suite.addTestSuite(TemplatesExpressionParsingTest.class);
		suite.addTestSuite(VpeTemplateManagerTest.class);
		//$JUnit-END$
		return suite;
	}

}
