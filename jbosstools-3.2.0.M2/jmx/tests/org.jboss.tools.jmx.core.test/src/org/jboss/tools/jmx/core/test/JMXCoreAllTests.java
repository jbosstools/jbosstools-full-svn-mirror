package org.jboss.tools.jmx.core.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JMXCoreAllTests extends TestSuite {
    public static Test suite(){
        return new JMXCoreAllTests();
    }

	public JMXCoreAllTests() {
		super("JMX Core All Tests");
		addTest(new TestSuite(DefaultProviderTest.class));
		addTest(new TestSuite(NodeBuilderTestCase.class));
		addTestSuite(JMXExceptionTest.class);
		addTestSuite(ImpactTest.class);
		addTestSuite(ErrorRootTest.class);
	}
}
