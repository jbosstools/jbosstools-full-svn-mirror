package org.jboss.tools.vpe.html.test;

import org.jboss.tools.vpe.html.test.jbide.JBIDE3280Test;
import org.jboss.tools.vpe.html.test.jbide.TestNPEinPreviewJbide10178;

import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * Test suite containing all important HTML tests
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
public class HtmlAllImportantTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(HtmlAllImportantTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(JBIDE3280Test.class);
		suite.addTestSuite(TestNPEinPreviewJbide10178.class);
		//$JUnit-END$
		return suite;
	}

}
