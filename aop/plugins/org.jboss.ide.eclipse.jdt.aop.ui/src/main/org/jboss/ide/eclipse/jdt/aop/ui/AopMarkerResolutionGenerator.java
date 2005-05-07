package org.jboss.ide.eclipse.jdt.aop.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.ui.viewsupport.JavaUILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolution2;
import org.eclipse.ui.IMarkerResolutionGenerator;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModel;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvice;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvised;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvisor;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopInterceptor;
import org.jboss.ide.eclipse.jdt.aop.ui.util.JumpToCodeUtil;

/**
 * @author Marshall
 */
public class AopMarkerResolutionGenerator implements IMarkerResolutionGenerator
{
	protected static Hashtable markerAdvisors;
	protected static JavaUILabelProvider delegate;
	
	static {
		markerAdvisors = new Hashtable();
		delegate = new JavaUILabelProvider();
	}
	
	public static void setElementAdvisors (String id, Collection advisors)
	{
		markerAdvisors.put(id, advisors);
	}

	public IMarkerResolution[] getResolutions(IMarker marker) {
		
		ArrayList resolutions = new ArrayList();
		
		try {

			System.out.println("get resolution for marker (of type:"+marker.getType()+")");
			IJavaElement markerElement = AopUiPlugin.getDefault().findMarkerElement (marker);
			
			if (markerElement != null)
			{
				IAopAdvisor advisors[] = AopModel.instance().getElementAdvisors(markerElement);
				
				if (advisors != null && advisors.length > 0)
				{
					for (int j = 0; j < advisors.length; j++)
					{
						IAopAdvisor advisor = advisors[j];
						
						if (advisor instanceof IAopAdvice)
						{
							IAopAdvice advice = (IAopAdvice) advisor;
							IMethod method = advice.getAdvisingMethod();
							//AopCorePlugin.getDefault().findAdviceMethod(advice.getReportAdvice());
							resolutions.add(new AdvisedMarkerResolution(markerElement, advice.getAdvisingMethod(), AdvisedMarkerResolution.TYPE_ADVICE));
						}
						else if (advisor instanceof IAopInterceptor)
						{
							IAopInterceptor interceptor = (IAopInterceptor) advisor;		
							IType type = (IType) interceptor.getAdvisingElement();
							
							resolutions.add(new AdvisedMarkerResolution(markerElement, type, AdvisedMarkerResolution.TYPE_INTERCEPTOR));
						}
						
					}
				}
				else {
					IAopAdvisor advisor = AopModel.instance().findAdvisor(markerElement);
					if (advisor == null && markerElement instanceof IMethod)
					{
						// We mark the "invoke" method in Interceptors, so we need to get the type to find the model advisor
						
						IType type = ((IMethod)markerElement).getDeclaringType();
						advisor = AopModel.instance().findAdvisor(type);
					}
					
					if (advisor != null)
					{
						IAopAdvised advisorAdvised[] = advisor.getAdvised();
						Set elements = new HashSet();
						
						for (int i = 0; i < advisorAdvised.length; i++)
						{
							elements.add(advisorAdvised[i].getAdvisedElement());
						}
						
						for (Iterator iter = elements.iterator(); iter.hasNext();)
						{
							IJavaElement element = (IJavaElement) iter.next();
							AdvisorMarkerResolution resolution = new AdvisorMarkerResolution(element);
							resolutions.add(resolution);
						}
					}
				}
			}
			
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return (IMarkerResolution []) resolutions.toArray(new IMarkerResolution [resolutions.size()]);
	}

	private static String getAdvisedTypeLabel (IJavaElement element)
	{
		IAopAdvised advised[] = AopModel.instance().findAllAdvised(element);
		boolean get = false, set = false, execute = false;
		
		for (int i = 0; i < advised.length; i++)
		{
			switch (advised[i].getType())
			{
				case IAopAdvised.TYPE_FIELD_GET: get = true; break;
				case IAopAdvised.TYPE_FIELD_SET: set = true; break;
				case IAopAdvised.TYPE_METHOD_EXECUTION: execute = true;
			}
		}
		
		if (get && !set)
		{
			return "(get)";
		}
		else if (!get && set)
		{
			return "(set)";
		}
		else if (get && set)
		{
			return "(get/set)";
		}
		else if (execute)
		{
			return "(execute)";
		}
		
		return "";
	}
	
	protected static class AdvisedMarkerResolution implements IMarkerResolution2
	{
		public static final int TYPE_INTERCEPTOR = 0;
		public static final int TYPE_ADVICE = 1;
		
		protected IJavaElement advised;
		protected IJavaElement advisor;
		protected int type;
		
		public AdvisedMarkerResolution (IJavaElement advised, IJavaElement advisor, int type)
		{
			this.advised = advised;
			this.advisor = advisor;
			this.type = type;
		}
		
		public String getLabel() {
			String label = "";
			switch (type)
			{
				case TYPE_INTERCEPTOR:
				{
					label = "Go to Interceptor '" + advisor.getElementName() + "'";
					
				} break;
				case TYPE_ADVICE:
				{
					IMethod method = (IMethod) advisor;
					
					label = "Go to Advice '" + method.getDeclaringType().getElementName() + "." + method.getElementName() + "'";
				} break; 
				default: label = "Go to '" + advisor.getElementName() + "'"; break;
			}
			
			return label + " " + getAdvisedTypeLabel(advised);
		}

		public Image getImage() {
			switch (type)
			{
				case TYPE_INTERCEPTOR:
					return AopSharedImages.getImage(AopSharedImages.IMG_INTERCEPTOR);
				case TYPE_ADVICE:
					return AopSharedImages.getImage(AopSharedImages.IMG_ADVICE);
				default: break;
			}
			return null;
		}
		
		public String getDescription() {
			return getLabel();
		}

		public void run(IMarker marker) {
			JumpToCodeUtil.jumpTo(advisor);
		}
	}
	
	protected static class AdvisorMarkerResolution implements IMarkerResolution2
	{
		private IJavaElement advised;
		
		public AdvisorMarkerResolution (IJavaElement advised)
		{
			this.advised = advised;
		}
		
		public String getDescription()
		{
			return getLabel();
		}

		public Image getImage()
		{
			return delegate.getImage(advised);
		}

		public String getLabel()
		{
			String description = "";
			if (advised instanceof IMethod)
			{
				IMethod method = (IMethod) advised;
				String methodAndArgs = delegate.getText(method);
				String className = method.getDeclaringType().getElementName();
				
				description = className + "." + methodAndArgs;
				description += " " + getAdvisedTypeLabel(advised);
			}
			
			else if (advised instanceof IField)
			{
				IField field = (IField) advised;
				String fieldName = field.getElementName();
				String className = field.getDeclaringType().getElementName();
				
				description = className + "." + fieldName;
				description += " " + getAdvisedTypeLabel(advised);
			}
			
			if (description.length() > 0)
			{
				return "Go to '" + description + "'";
			}
			else return null;
		}

		public void run(IMarker marker)
		{
			JumpToCodeUtil.jumpTo(advised);
		}
	}
}
