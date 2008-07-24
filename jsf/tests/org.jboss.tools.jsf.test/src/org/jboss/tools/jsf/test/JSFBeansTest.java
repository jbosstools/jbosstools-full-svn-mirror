package org.jboss.tools.jsf.test;

import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.test.util.TestProjectProvider;
import org.jboss.tools.jsf.model.pv.JSFProjectsRoot;
import org.jboss.tools.jsf.model.pv.JSFProjectsTree;
import org.jboss.tools.jst.web.model.pv.WebProjectNode;
import org.jboss.tools.jst.web.project.list.IWebPromptingProvider;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

import junit.framework.TestCase;

public class JSFBeansTest extends TestCase {
	TestProjectProvider provider = null;
	IProject project = null;
	boolean makeCopy = true;

	public JSFBeansTest() {}

	public void setUp() throws CoreException {
		provider = new TestProjectProvider("org.jboss.tools.jsf.test", null, "JSFKickStartOldFormat", false);
		project = provider.getProject();
		project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
	}
	
	public void testBeanWithSuper() {
		IModelNature n = EclipseResourceUtil.getModelNature(project);
		assertNotNull("Test project " + project.getName() + " has no model nature.", n);
		assertNotNull("XModel for project " + project.getName() + " is not loaded.", n.getModel());
		List<Object> result = WebPromptingProvider.getInstance().getList(n.getModel(), IWebPromptingProvider.JSF_BEAN_PROPERTIES, "user.", new Properties());
		assertNotNull("No results for bean " + " user.", n.getModel());
		
		assertTrue("Property 'parent' inherited from super class is not found in bean 'user'", result.contains("parent"));
		
	}
	
	public void testGettersAndSetters() {
		IModelNature n = EclipseResourceUtil.getModelNature(project);
		List<Object> result = WebPromptingProvider.getInstance().getList(n.getModel(), IWebPromptingProvider.JSF_BEAN_METHODS, "user.", new Properties());
		assertFalse("Method getX1 is not found. It is not a getter because it has type void.", result.contains("getX1"));
		assertFalse("Method getX2 is not found. It is not a getter because it has a parameter.", result.contains("getX2"));
		assertFalse("Method setX3 is not found. It is not a setter because it has 2 parameters", result.contains("setX3"));
	}

	protected void tearDown() throws CoreException{
		provider.dispose();
	}

}
