package org.jboss.tools.jst.web.ui.test;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.jboss.tools.jst.web.ui.action.JSPProblemMarkerResolutionGenerator;
import org.jboss.tools.test.util.JobUtils;
import org.jboss.tools.test.util.ProjectImportTestSetup;
import org.jboss.tools.tests.AbstractResourceMarkerTest;
import org.jboss.tools.tests.IMarkerFilter;

public class JSPProblemMarkerResolutionTest extends AbstractResourceMarkerTest{
	IProject project = null;

	public JSPProblemMarkerResolutionTest() {
		super("JSP Problem Marker Resolution Tests");
	}

	public JSPProblemMarkerResolutionTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		JobUtils.waitForIdle();
		IResource project = ResourcesPlugin.getWorkspace().getRoot().findMember("test_jsf_project");
		if(project == null) {
			ProjectImportTestSetup setup = new ProjectImportTestSetup(
					this,
					"org.jboss.tools.jst.web.ui.test",
					"projects/test_jsf_project",
					"test_jsf_project");
			project = setup.importProject();
		}
		this.project = project.getProject();
		JobUtils.waitForIdle();
	}
	
	public void testProblemMarkerResolutionInJSP() throws CoreException {
		IFile jspFile = project.getFile("WebContent/pages/test_page1.jsp");
		
		assertMarkerIsCreated(jspFile, "org.eclipse.jst.jsp.core.validationMarker", "Unknown tag.*");
		
		IMarker[] markers = findMarkers(jspFile, "org.eclipse.jst.jsp.core.validationMarker", "Unknown tag.*");
		
		assertEquals(1, markers.length);
		
		JSPProblemMarkerResolutionGenerator generator = new JSPProblemMarkerResolutionGenerator();
		
		for(IMarker marker : markers){
			generator.hasResolutions(marker);
			IMarkerResolution[] resolutions = generator.getResolutions(marker);
			for(IMarkerResolution resolution : resolutions){
				resolution.run(marker);
			}
		}
		
		refreshProject(project);
		
		assertMarkerIsNotCreated(jspFile, "org.eclipse.jst.jsp.core.validationMarker", "Unknown tag.*");
	}
	
	public void testProblemMarkerResolutionInXHTML() throws CoreException {
		IFile jspFile = project.getFile("WebContent/pages/test_page2.xhtml");
		
		assertMarkerIsCreated(jspFile, "org.eclipse.wst.html.core.validationMarker", "Unknown tag.*");
		
		IMarker[] markers = findMarkers(jspFile, "org.eclipse.wst.html.core.validationMarker", "Unknown tag.*");
		
		assertEquals(3, markers.length);
		
		JSPProblemMarkerResolutionGenerator generator = new JSPProblemMarkerResolutionGenerator();
		
		for(IMarker marker : markers){
			generator.hasResolutions(marker);
			IMarkerResolution[] resolutions = generator.getResolutions(marker);
			for(IMarkerResolution resolution : resolutions){
				resolution.run(marker);
			}
		}
		
		refreshProject(project);
		
		assertMarkerIsNotCreated(jspFile, "org.eclipse.wst.html.core.validationMarker", "Unknown tag.*");
	}
	
	private void refreshProject(IProject project){
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
			JobUtils.waitForIdle();
			JobUtils.delay(2000);
		} catch (CoreException e) {
			// ignore
		}
	}
}
