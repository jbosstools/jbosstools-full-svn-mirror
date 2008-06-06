package org.hibernate.eclipse.console.test;

import java.io.IOException;

import org.hibernate.eclipse.console.test.mappingproject.HibernateAllMappingTests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ConsolePluginAllTests {

	public static Test suite() throws IOException {
		TestSuite suite = new TestSuite(
				ConsoleTestMessages.ConsolePluginAllTests_test_for );

		suite.addTestSuite( KnownConfigurationsTest.class );
		suite.addTestSuite( QueryParametersTest.class );
		suite.addTestSuite( PerspectiveTest.class );
		suite.addTestSuite( ConsoleConfigurationTest.class );
		suite.addTestSuite( JavaFormattingTest.class );
		suite.addTestSuite( RefactoringTest.class );

		suite.addTestSuite( HibernateAllMappingTests.class );

		// core tests
		//Properties properties = new Properties();
		//properties.load(ConsolePluginAllTests.class.getResourceAsStream("plugintest-hibernate.properties"));

		//System.getProperties().putAll(properties);

		//suite.addTest(org.hibernate.tool.ToolAllTests.suite() );


		return suite;
	}

}
