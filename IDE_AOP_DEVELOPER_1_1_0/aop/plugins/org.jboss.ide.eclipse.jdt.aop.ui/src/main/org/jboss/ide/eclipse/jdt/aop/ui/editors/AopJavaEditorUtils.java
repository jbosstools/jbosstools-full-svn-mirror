/**
 * 
 */
package org.jboss.ide.eclipse.jdt.aop.ui.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModel;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvice;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvised;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvisor;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopInterceptor;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopModelChangeListener;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopTypeMatcher;
import org.jboss.ide.eclipse.jdt.aop.core.project.AopProjectNature;
import org.jboss.ide.eclipse.jdt.aop.ui.AopUiPlugin;
import org.jboss.ide.eclipse.jdt.aop.ui.views.AdvisedMembersView;
import org.jboss.ide.eclipse.jdt.aop.ui.views.AspectManagerView;


/**
 * @author Rob Stryker
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AopJavaEditorUtils implements IAopModelChangeListener, IWindowListener {
	
	public static final String MARKER_PROP_ADVISOR = "marker-advisor";
	public static final String MARKER_PROP_ADVISED = "marker-advised";
	public static final String MARKER_TYPEDEFED = "marker-typedefed";

	private static AopJavaEditorUtils instance = null;
	private AopEditorListener listener;
	
	public static AopJavaEditorUtils instance() {
		if( instance == null ) {
			instance = new AopJavaEditorUtils();
		}
		return AopJavaEditorUtils.instance;
	}
	
	
	private HashMap markers;
	
	public AopJavaEditorUtils() {
		markers = new HashMap();
		listener = new AopEditorListener();
		
		Workbench.getInstance().addWindowListener(this);
		AopModel.instance().addAopModelChangeListener(this);
	}
	
	private void addPartListeners (IWorkbenchWindow window)
	{
		IWorkbenchPage[] pages = window.getPages();
		for( int i = 0; i < pages.length; i++ ) {
			pages[i].addPartListener(listener);
		}
	}
	
	public void windowClosed(IWorkbenchWindow window) {	}
	public void windowDeactivated(IWorkbenchWindow window) { }
	
	public void windowOpened(IWorkbenchWindow window)
	{
		addPartListeners(window);
	}
	
	public void windowActivated(IWorkbenchWindow window) {
		addPartListeners(window);
	}
	
	public static class AopEditorListener implements IPartListener2 {
		public void partActivated(IWorkbenchPartReference partRef) {
			// refresh views
			refreshAdvisedMembers(partRef);
		}
		
		protected void refreshAdvisedMembers(IWorkbenchPartReference partRef) {
			if (getCompilationUnit(partRef) == null)
				return;
			
			IJavaElement element = getCompilationUnit(partRef);
			IJavaProject project = element.getJavaProject();
			
			if (AopCorePlugin.getDefault().getDefaultDescriptor(project, false) != null)
			{
				AopCorePlugin.setCurrentJavaProject(project);
			    			    
				if (AspectManagerView.instance() != null)
				{
				    AspectManagerView.instance().setDescriptorAsync(AopCorePlugin.getDefault().getDefaultDescriptor(project));
				}
				
				if (AdvisedMembersView.instance() != null)
				{
					AdvisedMembersView.instance().setInput(element);
				}
			}

		}
		
		public static ICompilationUnit getCompilationUnit(IWorkbenchPartReference partRef) {
			if( partRef instanceof IEditorReference ) {
				IEditorPart part = ((IEditorReference)partRef).getEditor(false);
				if( part != null && part instanceof JavaEditor ) {
					ICompilationUnit unit = JavaPlugin.getDefault().getWorkingCopyManager()
						.getWorkingCopy(part.getEditorInput());
					
					try {
						if( !inAopProject(unit)) return null;
					} catch( CoreException ce ) {
						return null;
					}

					return JavaPlugin.getDefault().getWorkingCopyManager()
						.getWorkingCopy(part.getEditorInput());
				}
			}
			return null;
		}
		protected static boolean inAopProject (ICompilationUnit unit)
			throws CoreException {
			if (unit == null
				|| unit.getJavaProject() == null
				|| unit.getJavaProject().getProject() == null
				|| ! unit.getJavaProject().getProject().isAccessible())
				return false;
			
			IProject project = unit.getJavaProject().getProject(); 
			return project.hasNature(AopProjectNature.NATURE_ID) && project.hasNature(JavaCore.NATURE_ID);
		}

		public void partDeactivated(IWorkbenchPartReference partRef) {
		}

		public void partBroughtToTop(IWorkbenchPartReference partRef) {
		}

		public void partClosed(IWorkbenchPartReference partRef) {
			// TODO: Finalize
		}

		public void partOpened(IWorkbenchPartReference partRef) {
			// TODO: Initialize here
			try {
				ICompilationUnit unit = getCompilationUnit(partRef);
				if( unit == null ) return;
				AopModel.instance().registerType(unit.findPrimaryType());
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		public void partHidden(IWorkbenchPartReference partRef) {
		}

		public void partVisible(IWorkbenchPartReference partRef) {
		}

		public void partInputChanged(IWorkbenchPartReference partRef) {
		}

	}


	
	/* 
	 * Implementor interfaces for the aop listener
	 */
	private void advisorAddedAdvice(IAopAdvised advised, IAopAdvice advice) {
		IJavaElement element = advised.getAdvisedElement();
		
		if (!elementHasMarkerOfType(element, AopUiPlugin.ADVISED_MARKER))
		{
			createAdvisedMarker(element, advice, AopUiPlugin.ADVISED_MARKER);
		}

		
		
		IMethod method = advice.getAdvisingMethod();
		if( method.getCompilationUnit() == null ) 
			return;
		
		
//		if (method.getCompilationUnit().equals(getInputJavaElement()))
//		{

			if (!elementHasMarkerOfType(method, AopUiPlugin.ADVICE_MARKER))
			{
				createAdvisorMarker(method, advised, AopUiPlugin.ADVICE_MARKER);
			}
//		}
	}
	
	protected boolean elementHasMarkerOfType (IJavaElement element, String markerType)
	{
		boolean hasMarker = false;
		
		try {
			ArrayList markers = getElementMarkers(element);
			for (Iterator iter = markers.iterator(); iter.hasNext(); )
			{
				IMarker marker = (IMarker) iter.next();
				if (marker.getType().equals(markerType))
				{
					hasMarker = true;
					break;
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return hasMarker;
	}




	private void advisorAddedInterceptor(IAopAdvised advised, IAopInterceptor interceptor) throws Exception {
		IJavaElement element = advised.getAdvisedElement();
		
		if (!elementHasMarkerOfType(element, AopUiPlugin.ADVISED_MARKER))
		{
			createAdvisedMarker(element, interceptor, AopUiPlugin.ADVISED_MARKER);
		}

		
		IType type = interceptor.getAdvisingType();
//		ICompilationUnit unit = (ICompilationUnit) getInputJavaElement();
//		
//		if (unit == null)
//			return;
//		
//		IType types[] = unit.getAllTypes();
//		for (int i = 0; i < types.length; i++)
//		{
//			if (types[i].equals(type))
//			{
				// marking the type doesn't do much good -- so we'll mark the invoke method instead
				IMethod invokeMethod = AopCorePlugin.getDefault().getInvokeMethod(type);
				if (invokeMethod != null) {
					if (!elementHasMarkerOfType(
							invokeMethod, AopUiPlugin.INTERCEPTOR_MARKER)) {
						createAdvisorMarker(invokeMethod, advised, 
								AopUiPlugin.INTERCEPTOR_MARKER);
					}
				}
//				break;
//			}
//		}
	}
		
	private void matchedTypeAdded(IType matchedType, IAopTypeMatcher matcher, String markerType) {
		if (!elementHasMarkerOfType(matchedType, markerType))
		{
			HashMap attributes = createElementMarkerAttributes(matchedType);

			attributes.put(IMarker.MESSAGE, "This member is advised.");
			attributes.put(MARKER_PROP_ADVISOR, matcher);
			
			createElementMarker(matchedType, attributes, markerType);
		}
	}
	
	private void matchedTypeRemoved(IType matchedType, IAopTypeMatcher matcher) {
		for (Iterator iter = getElementMarkers(matchedType).iterator(); iter.hasNext(); )
		{
			IMarker marker = (IMarker) iter.next();
			if (marker != null)
			{
				try {
					Object tmp = marker.getAttribute(MARKER_PROP_ADVISOR) ;
					if (matcher != null)
					{
						if (matcher.equals(tmp))
						{
							try {
								marker.delete();
								iter.remove();
							} catch( Exception e ) {
								
							}
						}
					}
				} catch( Exception e ) {
					
				}
			}
		}
	}

	public void typeMatchAdded(IType type, IAopTypeMatcher matcher) {
		if( matcher.getType() == IAopTypeMatcher.TYPEDEF ) 
			{matchedTypeAdded(type, matcher, AopUiPlugin.TYPEDEF_MARKER);return;}

		if( matcher.getType() == IAopTypeMatcher.INTRODUCTION ) 
			{matchedTypeAdded(type, matcher, AopUiPlugin.INTRODUCTION_MARKER);return;}

	}

	public void typeMatchRemoved(IType type, IAopTypeMatcher matcher) {
		if( matcher.getType() == IAopTypeMatcher.TYPEDEF ) 
			{matchedTypeRemoved(type, matcher);return;}
		if( matcher.getType() == IAopTypeMatcher.INTRODUCTION ) 
			{matchedTypeRemoved(type, matcher);return;}
	
	}

	
	public void advisorAdded(IAopAdvised advised, IAopAdvisor advisor)
	{
		try {
			if( advisor.getType() == IAopAdvisor.ADVICE )  
				{advisorAddedAdvice(advised, (IAopAdvice)advisor); return;}
			if( advisor.getType() == IAopAdvisor.INTERCEPTOR )  
				{advisorAddedInterceptor(advised, (IAopInterceptor)advisor); return;}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void advisorRemoved(IAopAdvised advised, IAopAdvisor advisor)
	{
		try {
			IJavaElement element = advised.getAdvisedElement();
			
			for (Iterator iter = getElementMarkers(element).iterator(); iter.hasNext(); )
			{
				IMarker marker = (IMarker) iter.next();
				if (marker != null)
				{
					IAopAdvisor markerAdvisor = (IAopAdvisor) marker.getAttribute(MARKER_PROP_ADVISOR) ;
					if (markerAdvisor != null)
					{
						if (markerAdvisor.equals(advisor))
						{
							marker.delete();
							iter.remove();
						}
					}
				}
			}
			
			element = advisor.getAdvisingElement();
			if( element == null ) return;
			for (Iterator iter = getElementMarkers(element).iterator(); iter.hasNext(); )
			{
				IMarker marker = (IMarker) iter.next();
				if (marker != null)
				{
					IAopAdvised markerAdvised = (IAopAdvised) marker.getAttribute(MARKER_PROP_ADVISED) ;
					if (markerAdvised != null)
					{
						if (markerAdvised.equals(advised))
						{
							marker.delete();
							iter.remove();
						}
					}
				}
			}
			
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	private HashMap createElementMarkerAttributes (IJavaElement element)
	{
		HashMap attributes = new HashMap();
		
		try {		

			ISourceRange elementRange = ((IMember)element).getNameRange();
			if (elementRange != null)
			{
				int offset = elementRange.getOffset();
				int length = elementRange.getLength();
				int end = 0;
				end = offset + length;
				
				
				MarkerUtilities.setCharStart(attributes, offset);
				MarkerUtilities.setCharEnd(attributes, end);
			}

		} catch (JavaModelException e) {
			e.printStackTrace();
		} 

		return attributes;
	}
	
	private void createElementMarker (IJavaElement element, HashMap attributes, String markerType)
	{
		final String finalMarkerType = markerType;
		final IJavaElement finalElement = element;
		final HashMap finalAttributes = attributes;
		IWorkspaceRunnable r = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				IMarker marker = finalElement.getResource().createMarker(finalMarkerType);
				marker.setAttributes(finalAttributes);
				
				getElementMarkers(finalElement).add(marker);	
			}
		};
		
		try {
			element.getResource().getWorkspace().run(r, null,IWorkspace.AVOID_UPDATE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		} catch( NullPointerException npe ) {
			// archive
		}
	}
	
	
	
	private void createAdvisedMarker (IJavaElement element, IAopAdvisor advisor, String markerType)
	{
		HashMap attributes = createElementMarkerAttributes(element);

		attributes.put(IMarker.MESSAGE, "This member is advised.");
		attributes.put(MARKER_PROP_ADVISOR, advisor);
		
		createElementMarker(element, attributes, markerType);
	}

	private void createAdvisorMarker (IJavaElement element, IAopAdvised advised, String markerType)
	{
		HashMap attributes = createElementMarkerAttributes(element);

		attributes.put(IMarker.MESSAGE, "This member is an advisor.");
		attributes.put(MARKER_PROP_ADVISED, advised);
		
		createElementMarker(element, attributes, markerType);
	}
	
	protected ArrayList getElementMarkers (IJavaElement element)
	{
		ArrayList elementMarkers = (ArrayList) markers.get(element);
		if (elementMarkers == null)
		{
			elementMarkers = new ArrayList();
			markers.put(element, elementMarkers);
		}
		
		return elementMarkers;
	}
	
	

}
