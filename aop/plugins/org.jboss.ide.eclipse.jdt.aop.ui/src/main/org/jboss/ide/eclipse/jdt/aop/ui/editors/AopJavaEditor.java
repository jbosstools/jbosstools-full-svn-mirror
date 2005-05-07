/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.ui.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModel;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvice;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvised;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvisor;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopInterceptor;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopModelChangeListener;
import org.jboss.ide.eclipse.jdt.aop.core.project.AopProjectNature;
import org.jboss.ide.eclipse.jdt.aop.ui.AopUiPlugin;
import org.jboss.ide.eclipse.jdt.aop.ui.views.AdvisedMembersView;
import org.jboss.ide.eclipse.jdt.aop.ui.views.AspectManagerView;

public class AopJavaEditor extends CompilationUnitEditor implements IAopModelChangeListener {

	private boolean registered;
	private static Hashtable markers;
	static {
		markers = new Hashtable();
	}
	
	public static final String MARKER_PROP_ADVISOR = "marker-advisor";
	public static final String MARKER_PROP_ADVISED = "marker-advised";
	
	public AopJavaEditor ()
	{
		super();
		registered = false;
	}
	
	protected boolean inAopProject ()
		throws CoreException
	{
		if (getInputJavaElement() == null
			|| getInputJavaElement().getJavaProject() == null
			|| getInputJavaElement().getJavaProject().getProject() == null
			|| ! getInputJavaElement().getJavaProject().getProject().isAccessible())
			return false;
		
		IProject project = getInputJavaElement().getJavaProject().getProject(); 
		return project.hasNature(AopProjectNature.NATURE_ID) && project.hasNature(JavaCore.NATURE_ID);
	}
	
	protected void doSetInput(IEditorInput input)
		throws CoreException
	{
		super.doSetInput(input);
		
		if ( ! registered && inAopProject() )
		{
			IJavaElement element = getInputJavaElement();
			
			AopModel.instance().addAopModelChangeListener(getInputJavaElement().getJavaProject(), this);
			if (element instanceof ICompilationUnit)
			{
				ICompilationUnit unit = (ICompilationUnit) element;
				try {
					AopModel.instance().registerType(unit.findPrimaryType());
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			registered = true;
		}
		
		refreshAdvisedMembers();
	}
	
	public void setFocus() {
		super.setFocus();
		
		refreshAdvisedMembers();
	}
	
	private void refreshAdvisedMembers ()
	{
		if (getInputJavaElement() == null)
			return;
		
		IJavaElement element = getInputJavaElement();
		IJavaProject project = element.getJavaProject();
		
		if (AopCorePlugin.getDefault().getDefaultDescriptor(project, false) != null)
		{
			AopCorePlugin.setCurrentJavaProject(project);
		    
//			if (AopCorePlugin.getDefault().getProjectReport(project) == null)
//		    {
//		    	AopCorePlugin.getDefault().updateProjectReport(project);
//		    }
		    
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
	
	public void advisorAdded(IAopAdvised advised, IAopAdvisor advisor)
	{
		try {
			IJavaElement element = advised.getAdvisedElement();
			
			if (!elementHasMarkerOfType(element, AopUiPlugin.ADVISED_MARKER))
			{
				createAdvisedMarker(element, advisor, AopUiPlugin.ADVISED_MARKER);
			}
			
			if (advisor.getType() == IAopAdvisor.ADVICE)
			{
				IAopAdvice advice = (IAopAdvice) advisor;
				IMethod method = advice.getAdvisingMethod();
				
				if (method.getCompilationUnit().equals(getInputJavaElement()))
				{

					if (!elementHasMarkerOfType(method, AopUiPlugin.ADVICE_MARKER))
					{
						createAdvisorMarker(method, advised, AopUiPlugin.ADVICE_MARKER);
					}
				}
			}
			
			if (advisor.getType() == IAopAdvisor.INTERCEPTOR)
			{
				IAopInterceptor interceptor = (IAopInterceptor) advisor;
				IType type = interceptor.getAdvisingType();
				ICompilationUnit unit = (ICompilationUnit) getInputJavaElement();
				
				if (unit == null)
					return;
				
				IType types[] = unit.getAllTypes();
				for (int i = 0; i < types.length; i++)
				{
					if (types[i].equals(type))
					{
						// marking the type doesn't do much good -- so we'll mark the invoke method instead
						IMethod invokeMethod = AopCorePlugin.getDefault().getInvokeMethod(types[i]);
						if (invokeMethod != null)
						{
							if (!elementHasMarkerOfType(invokeMethod, AopUiPlugin.INTERCEPTOR_MARKER))
							{
								createAdvisorMarker(invokeMethod, advised, AopUiPlugin.INTERCEPTOR_MARKER);
							}
						}
						break;
					}
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public void advisorRemoved(IAopAdvised advised, IAopAdvisor advisor)
	{
		try {
			IJavaElement element = advised.getAdvisedElement();
			ArrayList markersToRemove = new ArrayList();
			
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
			ISourceRange elementRange = AopUiPlugin.getDefault().getSourceRange(element);
			
			if (elementRange != null)
			{
				int offset = elementRange.getOffset();
				int length = elementRange.getLength();
				int end = 0;
				if (element instanceof IMethod)
				{
					ISourceRange range = ((IMethod)element).getSourceRange();
					end = range.getOffset() + range.getLength();
				}
				else if (element instanceof IField)
				{
					ISourceRange range = ((IField)element).getSourceRange();
					end = range.getOffset() + range.getLength();
				}
				else {
					end = offset + length;
				}

				MarkerUtilities.setCharStart(attributes, offset);
				MarkerUtilities.setCharEnd(attributes, end);
			}

		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (CoreException e) {
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
				
				addMarker(finalElement, marker);
			}
		};
		
		try {
			element.getResource().getWorkspace().run(r, null,IWorkspace.AVOID_UPDATE, null);
		} catch (CoreException e) {
			e.printStackTrace();
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
	
	protected void addMarker (IJavaElement element, IMarker marker)
	{
		getElementMarkers(element).add(marker);
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
	
	public void close(boolean save)
	{
		AopModel.instance().removeAopModelChangeListener(this);		
		super.close(save);
	}
	
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		
		if (AdvisedMembersView.instance() != null)
		{
			AdvisedMembersView.instance().refresh();
		}
	}
}