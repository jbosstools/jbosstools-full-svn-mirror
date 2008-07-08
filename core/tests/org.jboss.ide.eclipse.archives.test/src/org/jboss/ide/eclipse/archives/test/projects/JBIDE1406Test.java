package org.jboss.ide.eclipse.archives.test.projects;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.build.ArchiveBuildDelegate;
import org.jboss.ide.eclipse.archives.test.ArchivesTest;
import org.jboss.tools.common.test.util.TestProjectProvider;

public class JBIDE1406Test extends TestCase {
	private TestProjectProvider provider;
	private IProject project;
	private IPath outputDir;
	private IPath propsFile;
	protected void setUp() throws Exception {
		provider = new TestProjectProvider(ArchivesTest.PLUGIN_ID, 
				"inputs" + Path.SEPARATOR + "projects" + Path.SEPARATOR + "JBIDE1406",
				null, true); 
		project = provider.getProject();
		project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		outputDir = project.getLocation().append("output").append("JBIDE1406.jar");
		propsFile = outputDir.append("src").append("in.properties");
	}
	
	protected void tearDown() throws Exception {
		provider.dispose();
	}

	public void testJBIDE1406() {
		ArchiveBuildDelegate delegate = new ArchiveBuildDelegate();
		try {
			delegate.fullProjectBuild(project.getLocation());
			assertTrue(outputDir.toFile().isDirectory());
			assertTrue(propsFile.toFile().exists());
			assertTrue(propsFile.toFile().isFile());
		} catch( AssertionFailedError afe) {
			throw afe;
		} catch( RuntimeException re ) {
			fail(re.getMessage());
		}
	}
}
