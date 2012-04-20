package org.jboss.tools.jsf.ui.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jst.jsp.ui.internal.validation.JSPContentSourceValidator;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.wst.validation.ValidationResult;
import org.eclipse.wst.validation.ValidationState;
import org.eclipse.wst.validation.internal.MarkerManager;
import org.eclipse.wst.validation.internal.core.Message;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.jboss.tools.jsf.ui.JsfUiPlugin;
import org.jboss.tools.jst.web.ui.action.JSPProblemMarkerResolutionGenerator;
import org.jboss.tools.tests.AbstractResourceMarkerTest;

public class JSPProblemMarkerResolutionTest extends AbstractResourceMarkerTest{
	IProject project = null;
	private static final String JSP_MARKER_TYPE = "org.eclipse.jst.jsp.core.validationMarker";
	private static final String XHTML_MARKER_TYPE = "org.eclipse.wst.html.core.validationMarker";
	private static final String JSP_EXT = "jsp";
	private static final String XHTML_EXT = "xhtml";

	public JSPProblemMarkerResolutionTest() {
		super("JSP Problem Marker Resolution Tests");
	}

	public JSPProblemMarkerResolutionTest(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("test_jsf_project");
	}
	
	private void validate(IFile file) throws CoreException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException{
		MarkerManager manager = MarkerManager.getDefault();
		
		if(JSP_EXT.equals(file.getFileExtension())){
			file.deleteMarkers(JSP_MARKER_TYPE, true, IResource.DEPTH_INFINITE);
			JSPContentSourceValidator validator = new JSPContentSourceValidator();
			
			ValidationResult result = validator.validate(file, 0, new ValidationState(), new NullProgressMonitor());
			
			IReporter reporter = result.getReporter(new NullProgressMonitor());
			List messages = reporter.getMessages();
			for(Object m : messages){
				if(m instanceof Message){
					Message message = (Message)m;
					IMarker marker = file.createMarker(JSP_MARKER_TYPE);
					marker.setAttributes(message.getAttributes());
					marker.setAttribute(IMarker.MESSAGE, message.getText());
					int markerSeverity = IMarker.SEVERITY_INFO;
					int sev = message.getSeverity();
					if ((sev & IMessage.HIGH_SEVERITY) != 0)markerSeverity = IMarker.SEVERITY_ERROR;
					else if ((sev & IMessage.NORMAL_SEVERITY) != 0)markerSeverity = IMarker.SEVERITY_WARNING;
					marker.setAttribute(IMarker.SEVERITY, markerSeverity);
					marker.setAttribute(IMarker.LINE_NUMBER, message.getLineNumber());
					marker.setAttribute(IMarker.CHAR_START, message.getOffset());
					marker.setAttribute(IMarker.CHAR_END, message.getOffset()+message.getLength());
				}
			}
		}else if(XHTML_EXT.equals(file.getFileExtension())){
			file.deleteMarkers(XHTML_MARKER_TYPE, true, IResource.DEPTH_INFINITE);

			// https://issues.jboss.org/browse/JBIDE-11596 compile failed in org.jboss.tools.jsf.ui.test when run against Juno TP
			// org.eclipse.wst.html.internal.validation.HTMLValidator was moved to org.eclipse.wst.html.core.internal.validation.HTMLValidator
			// We have to use reflection to be compilable in both Indigo and June.
//			org.eclipse.wst.html.internal.validation.HTMLValidator validator = new org.eclipse.wst.html.internal.validation.HTMLValidator();
//			ValidationResult result = validator.validate(file, 0, new ValidationState(), new NullProgressMonitor());
			Class validatorClass = null;
			try {
				validatorClass = JsfUiPlugin.getDefault().getBundle().loadClass("org.eclipse.wst.html.internal.validation.HTMLValidator");
			} catch (ClassNotFoundException e) {
				try {
					validatorClass = JsfUiPlugin.getDefault().getBundle().loadClass("org.eclipse.wst.html.core.internal.validation.HTMLValidator");
				} catch (ClassNotFoundException e1) {
					fail("Cannot load org.eclipse.wst.html.internal.validation.HTMLValidator (Eclipse Indigo) nor org.eclipse.wst.html.core.internal.validation.HTMLValidator (Eclipse Juno): ClassNotFoundException");
				}
			}
			Constructor constructor = validatorClass.getConstructor();
			Object validator = constructor.newInstance();
			Method validate = validatorClass.getMethod("validate", IResource.class, int.class, ValidationState.class, IProgressMonitor.class);
			ValidationResult result = (ValidationResult)validate.invoke(validator, file, 0, new ValidationState(), new NullProgressMonitor());

			IReporter reporter = result.getReporter(new NullProgressMonitor());
			List messages = reporter.getMessages();
			for(Object m : messages){
				if(m instanceof Message){
					Message message = (Message)m;
					IMarker marker = file.createMarker(XHTML_MARKER_TYPE);
					marker.setAttributes(message.getAttributes());
					marker.setAttribute(IMarker.MESSAGE, message.getText());
					int markerSeverity = IMarker.SEVERITY_INFO;
					int sev = message.getSeverity();
					if ((sev & IMessage.HIGH_SEVERITY) != 0)markerSeverity = IMarker.SEVERITY_ERROR;
					else if ((sev & IMessage.NORMAL_SEVERITY) != 0)markerSeverity = IMarker.SEVERITY_WARNING;
					marker.setAttribute(IMarker.SEVERITY, markerSeverity);
					marker.setAttribute(IMarker.LINE_NUMBER, message.getLineNumber());
					marker.setAttribute(IMarker.CHAR_START, message.getOffset());
					marker.setAttribute(IMarker.CHAR_END, message.getOffset()+message.getLength());
				}
			}
		}
	}
	
	public void testProblemMarkerResolutionInJSP() throws CoreException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		IFile jspFile = project.getFile("WebContent/pages/test_page1.jsp");
		
		assertTrue("File must be exists.",jspFile.exists());
		
		validate(jspFile);

		assertMarkerIsCreated(jspFile, JSP_MARKER_TYPE, "Unknown tag (h:commandButton).", true, 8);

		IMarker[] markers = findMarkers(jspFile, JSP_MARKER_TYPE, "Unknown tag (h:commandButton).");

		assertEquals(1, markers.length);

		JSPProblemMarkerResolutionGenerator generator = new JSPProblemMarkerResolutionGenerator();

		for(IMarker marker : markers){
			generator.hasResolutions(marker);
			IMarkerResolution[] resolutions = generator.getResolutions(marker);
			for(IMarkerResolution resolution : resolutions){
				resolution.run(marker);
			}
		}

		validate(jspFile);

		assertMarkerIsNotCreated(jspFile, JSP_MARKER_TYPE, "Unknown tag (h:commandButton).");
	}

	public void testProblemMarkerResolutionInXHTML() throws CoreException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		IFile jspFile = project.getFile("WebContent/pages/test_page2.xhtml");
		
		assertTrue("File must be exists.",jspFile.exists());
		
		validate(jspFile);

		assertMarkerIsCreated(jspFile, XHTML_MARKER_TYPE, "Unknown tag (ui:insert).", true, 8, 17, 31);

		IMarker[] markers = findMarkers(jspFile, XHTML_MARKER_TYPE, "Unknown tag (ui:insert).");

		assertEquals(3, markers.length);

		JSPProblemMarkerResolutionGenerator generator = new JSPProblemMarkerResolutionGenerator();

		for(IMarker marker : markers){
			generator.hasResolutions(marker);
			IMarkerResolution[] resolutions = generator.getResolutions(marker);
			for(IMarkerResolution resolution : resolutions){
				resolution.run(marker);
			}
		}

		validate(jspFile);

		assertMarkerIsNotCreated(jspFile, XHTML_MARKER_TYPE, "Unknown tag (ui:insert).");
	}
}