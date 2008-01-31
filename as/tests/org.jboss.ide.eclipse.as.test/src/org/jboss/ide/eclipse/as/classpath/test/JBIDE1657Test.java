package org.jboss.ide.eclipse.as.classpath.test;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.wst.server.core.IRuntime;
import org.jboss.ide.eclipse.as.test.ASTest;
import org.jboss.ide.eclipse.as.test.util.ProjectRuntimeUtil;
import org.jboss.tools.common.test.util.TestProjectProvider;

public class JBIDE1657Test extends TestCase {
	private TestProjectProvider provider;
	private IProject project;

	protected void setUp() throws Exception {
		provider = new TestProjectProvider("org.jboss.ide.eclipse.as.test", null, "basicwebproject", true); 
		project = provider.getProject();
		project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
	}
	
	protected void tearDown() throws Exception {
		provider.dispose();
	}

	public void testJBIDE1657() {
		try {
			IJavaProject jp = JavaCore.create(project);
			verifyInitialClasspathEntries(jp);
			
			// lets try a runtime
			IRuntime createdRuntime = ProjectRuntimeUtil.createRuntime("runtime1", ASTest.JBOSS_RUNTIME_42, ASTest.JBOSS_AS_HOME);
			ProjectRuntimeUtil.setTargetRuntime(createdRuntime, project);
			verifyPostRuntimeCPE(jp);
			
			ProjectRuntimeUtil.clearRuntime(project);
			verifyInitialClasspathEntries(jp);
			
		} catch( JavaModelException jme ) {
			jme.printStackTrace();
			fail(jme.getMessage());
		} catch( CoreException ce ) {
			ce.printStackTrace();
			fail(ce.getMessage());
		}
	}
	
	protected void verifyPostRuntimeCPE(IJavaProject jp) throws CoreException {
		IClasspathEntry[] entries = jp.getRawClasspath();
		assertEquals(3, entries.length);
		String[] acceptable = new String[] { "org.eclipse.jst.server.core.container",
				"basicwebproject", "org.eclipse.jst.j2ee.internal.web.container" };
		verifyClasspathEntries(entries, acceptable);
	}

	protected void verifyInitialClasspathEntries(IJavaProject jp) throws CoreException {
		IClasspathEntry[] entries = jp.getRawClasspath();
		assertEquals(2, entries.length);
		
		// TODO: Currently fails because the JRE is not bound. This must be changed. 
		//IClasspathEntry[] resolved = jp.getResolvedClasspath(false);
		
		String[] acceptable = new String[] { "org.eclipse.jst.j2ee.internal.web.container",
				"basicwebproject"};
		verifyClasspathEntries(entries, acceptable);
	}
	
	protected void verifyClasspathEntries(IClasspathEntry[] entries, String[] acceptablePrefixes) {
		ArrayList list = new ArrayList(Arrays.asList(acceptablePrefixes));
		for( int i = 0; i < entries.length; i++ ) {
			if( list.contains(entries[i].getPath().segment(0)))
				list.remove(entries[i].getPath().segment(0));
			else
				fail("classpath contains unexpected entry: " + entries[i].getPath());
		}
		
		if( list.size() > 0 ) {
			String tmp = "Expected enties not found: ";
			for( int i = 0; i < list.size(); i++ ) {
				tmp += list.get(i) + ", ";
			}
			fail(tmp.substring(0, tmp.length() - 2));
		}
	}
}
