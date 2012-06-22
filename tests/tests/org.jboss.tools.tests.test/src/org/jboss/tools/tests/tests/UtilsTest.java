package org.jboss.tools.tests.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.IProject;
import org.jboss.tools.test.util.TestProjectProvider;
import org.junit.Test;

public class UtilsTest {

	private static final String PROJECT_NAME = "DynamicWebProject";

	@Test
	public void testProjectProvider() throws Exception {
		TestProjectProvider provider = new TestProjectProvider(
				"org.jboss.tools.tests.test", null, PROJECT_NAME, false);
		IProject project = provider.getProject();
		assertNotNull("Project is not imported: " + PROJECT_NAME, project);
		assertTrue("Project doesn't exist: " + PROJECT_NAME, project.exists());
		assertTrue("Project isn't accessible: " + PROJECT_NAME, project.isAccessible());
		assertTrue("Project isn't open: " + PROJECT_NAME, project.isOpen());
		provider.dispose();
	}
}
