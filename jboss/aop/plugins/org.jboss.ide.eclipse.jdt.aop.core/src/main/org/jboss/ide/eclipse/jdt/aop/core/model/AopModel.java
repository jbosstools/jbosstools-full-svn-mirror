/*
 * Created on Jan 11, 2005
 */
package org.jboss.ide.eclipse.jdt.aop.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.internal.core.JavaElementDelta;
import org.eclipse.jdt.internal.core.SourceField;
import org.eclipse.jdt.internal.core.SourceMethod;
import org.eclipse.jdt.internal.corext.util.AllTypesCache;
import org.eclipse.jdt.internal.corext.util.TypeInfo;
import org.jboss.aop.AspectManager;
import org.jboss.aop.pointcut.PointcutExpression;
import org.jboss.aop.pointcut.ast.ParseException;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Advice;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aop;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aspect;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Binding;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Interceptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.InterceptorRef;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Pointcut;
import org.jboss.ide.eclipse.jdt.aop.core.model.internal.AopAdvised;
import org.jboss.ide.eclipse.jdt.aop.core.model.internal.ProjectAdvisors;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTPointcutExpression;


/**
 * @author Marshall
 */
public class AopModel implements IElementChangedListener
{
	
	private static AopModel instance;
	
	private Hashtable projectListeners, projectAdvisors;
	private ArrayList globalListeners;
	private ArrayList registeredTypes;
	
	public static AopModel instance ()
	{
		if (instance == null)
			instance = new AopModel();
		
		return instance;
	}
	
	protected AopModel ()
	{
		projectListeners = new Hashtable();
		projectAdvisors = new Hashtable();
		globalListeners = new ArrayList();
		registeredTypes = new ArrayList();
		
		JavaCore.addElementChangedListener(this);
	}
	
	protected IAopAdvisor[] getPointcutAdvisors (IJavaProject project, JDTPointcutExpression expression)
	{
		ProjectAdvisors advisors = getProjectAdvisors (project);
		ArrayList pointcutAdvisors = new ArrayList();
		
		IAopInterceptor interceptors[] = advisors.getInterceptors();
		IAopAspect aspects[] = advisors.getAspects();
		
		for (int i = 0; i < interceptors.length; i++)
		{
			JDTPointcutExpression expressions[] = interceptors[i].getAssignedPointcuts();
			
			for (int j = 0; j < expressions.length; j++)
			{
				if (expressions[j].equals(expression))
				{
					pointcutAdvisors.add(interceptors[i]);
					break;
				}
			}
		}
		
		for (int i = 0; i < aspects.length; i++)
		{
			IAopAdvice advice[] = aspects[i].getAdvice();
			for (int j = 0; j < advice.length; j++)
			{
				JDTPointcutExpression expressions[] = advice[j].getAssignedPointcuts();
				for (int k = 0; k < expressions.length; k++)
				{
					if (expressions[k].equals(expression))
					{
						pointcutAdvisors.add(advice[j]);
						break;
					}
				}
			}
		}
		
		return (IAopAdvisor[]) pointcutAdvisors.toArray(new IAopAdvisor[pointcutAdvisors.size()]);
	}
	
	
	protected JDTPointcutExpression[] getProjectPointcuts (IJavaProject project)
	{
		ProjectAdvisors advisors = getProjectAdvisors (project);
		ArrayList pointcuts = new ArrayList();
		
		IAopInterceptor interceptors[] = advisors.getInterceptors();
		IAopAspect aspects[] = advisors.getAspects();
		
		for (int i = 0; i < interceptors.length; i++)
		{
			JDTPointcutExpression expressions[] = interceptors[i].getAssignedPointcuts();
			
			for (int j = 0; j < expressions.length; j++)
			{
				pointcuts.add(expressions[j]);
			}
		}
		
		for (int i = 0; i < aspects.length; i++)
		{
			IAopAdvice advice[] = aspects[i].getAdvice();
			for (int j = 0; j < advice.length; j++)
			{
				JDTPointcutExpression expressions[] = advice[j].getAssignedPointcuts();
				for (int k = 0; k < expressions.length; k++)
				{
					pointcuts.add(expressions[k]);
				}
			}
		}
		
		return (JDTPointcutExpression[]) pointcuts.toArray(new JDTPointcutExpression[pointcuts.size()]);
	}
	
	protected ProjectAdvisors getProjectAdvisors (IJavaProject project)
	{
		ProjectAdvisors advisors = (ProjectAdvisors) projectAdvisors.get(project);
		if (advisors == null)
		{
			advisors = new ProjectAdvisors (project);
			projectAdvisors.put(project, advisors);
		}
		
		return advisors;
	}
	
	protected ArrayList getProjectAopModelChangeListeners (IJavaProject project)
	{
		ArrayList listeners = (ArrayList) projectListeners.get(project);
		if (listeners == null)
		{
			listeners = new ArrayList();
			projectListeners.put(project, listeners);
		}
		
		return listeners;
	}
	
	protected void refireEvents (IJavaProject project, IAopModelChangeListener listener)
	{
		Iterator projects = null;
		
		if (project == null) projects = projectAdvisors.keySet().iterator();
		else {
			ArrayList list = new ArrayList();
			list.add(project);
			projects = list.iterator();
		}
			
		while (projects.hasNext())
		{
			IJavaProject currentProject = (IJavaProject) projects.next();
			
			ProjectAdvisors advisors = getProjectAdvisors(currentProject);
			
			IAopInterceptor interceptors[] = advisors.getInterceptors();
			IAopAspect aspects[] = advisors.getAspects();
			
			for (int i = 0; i < interceptors.length; i++)
			{
				IAopAdvised advised[] = interceptors[i].getAdvised();
				
				for (int j = 0; j < advised.length; j++)
				{
					listener.advisorAdded(advised[j], interceptors[i]);
				}
			}
			
			for (int i = 0; i < aspects.length; i++)
			{
				IAopAdvice advice[] = aspects[i].getAdvice();
				for (int j = 0; j < advice.length; j++)
				{
					IAopAdvised advised[] = advice[j].getAdvised();	
					for (int k = 0; k < advised.length; k++)
					{
						listener.advisorAdded(advised[k], advice[j]);
					}
				}
			}
		}
	}
	
	public void addAopModelChangeListener (IJavaProject project, IAopModelChangeListener listener)
	{
		ArrayList listeners = getProjectAopModelChangeListeners(project);
		
		if (!listeners.contains(listener))
		{
			listeners.add(listener);
			
			refireEvents(project, listener);
		}
	}
	
	public void addAopModelChangeListener (IAopModelChangeListener listener)
	{
		if (! globalListeners.contains(listener))
		{
			globalListeners.add(listener);
			
			refireEvents(null, listener);
		}
	}

	public void removeAopModelChangeListener (IAopModelChangeListener listener)
	{
		for (Iterator iter = projectListeners.values().iterator(); iter.hasNext(); )
		{
			ArrayList listeners = (ArrayList) iter.next();
			if (listeners.contains(listener))
			{
				listeners.remove(listener);
			}
		}
		
		if (globalListeners.contains(listener))
		{
			globalListeners.remove(listener);
		}
	}
	
	public void registerType (IType type)
	{
		if (! registeredTypes.contains(type))
		{
			JDTPointcutExpression expressions[] = getProjectPointcuts(type.getJavaProject());
			for (int i = 0; i < expressions.length; i++)
			{
				findAllAdvised(type, expressions[i], expressions[i].getCollector(), new NullProgressMonitor());
			}
			
			registeredTypes.add(type);
		}
	}
	
	public ArrayList getAdvisedChildren (IType type)
	{
		ArrayList children = new ArrayList ();
		if( type == null ) return children;
		try {
			
			IField fields[] = type.getFields();
			IMethod methods[] = type.getMethods();
			
			for (int i = 0; i < fields.length; i++)
			{
				IAopAdvised[] advised = findAllAdvised (fields[i]);
				if (advised != null && advised.length > 0)
				{
					children.add(fields[i]);
				}
			}
			
			for (int i = 0; i < methods.length; i++)
			{
				IAopAdvised[] advised = findAllAdvised (methods[i]);
				if (advised != null && advised.length > 0)
				{
					children.add(methods[i]);
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return children;
	}
	
	/**
	 * Find all occurrences of advised elements that contain the supplied element
	 */
	public IAopAdvised[] findAllAdvised (IJavaElement element)
	{
		ArrayList advised = new ArrayList();
		
		IAopAspect aspects[] = getProjectAdvisors(element.getJavaProject()).getAspects();
		IAopInterceptor interceptors[] = getProjectAdvisors(element.getJavaProject()).getInterceptors();
		//System.out.println(aspects.length + " aspects and " + interceptors.length + " interceptors");
		for (int i = 0; i < aspects.length; i++)
		{
			IAopAdvice advice[] = aspects[i].getAdvice();
			for (int j =0; j < advice.length; j++)
			{
				if (advice[j].advises(element))
				{
					advised.add(advice[j].getAdvised(element));
				}
			}
		}
		for (int i = 0; i < interceptors.length; i++)
		{
			if (interceptors[i].advises(element))
			{
				advised.add(interceptors[i].getAdvised(element));
			}
		}
		
		return (IAopAdvised[]) advised.toArray(new IAopAdvised[advised.size()]);
	}
	
	public IAopAdvisor[] getElementAdvisors (IJavaElement element)
	{
		ArrayList advisors = new ArrayList();
		
		IAopAspect aspects[] = getProjectAdvisors(element.getJavaProject()).getAspects();
		IAopInterceptor interceptors[] = getProjectAdvisors(element.getJavaProject()).getInterceptors();
		
		for (int i = 0; i < aspects.length; i++)
		{
			IAopAdvice advice[] = aspects[i].getAdvice();
			for (int j =0; j < advice.length; j++)
			{
				if (advice[j].advises(element))
				{
					advisors.add(advice[j]);
				}
			}
		}
		
		for (int i = 0; i < interceptors.length; i++)
		{
			if (interceptors[i].advises(element))
			{
				advisors.add(interceptors[i]);
			}
		}
		
		return (IAopAdvisor[]) advisors.toArray(new IAopAdvisor[advisors.size()]);
	}
	
	public IAopAdvisor findAdvisor (IJavaElement element)
	{
		IAopAspect aspects[] = getProjectAdvisors(element.getJavaProject()).getAspects();
		IAopInterceptor interceptors[] = getProjectAdvisors(element.getJavaProject()).getInterceptors();
		
		for (int i = 0; i < aspects.length; i++)
		{
			IAopAdvice advice[] = aspects[i].getAdvice();
			for (int j =0; j < advice.length; j++)
			{
				if (advice[j].getAdvisingElement().equals(element))
				{
					return advice[j];
				}
			}
		}
		
		for (int i = 0; i < interceptors.length; i++)
		{
			if (interceptors[i].getAdvisingElement().equals(element))
			{
				return interceptors[i];
			}
		}
		
		return null;
	}
	
	protected void fireAdvisorAdded (IAopAdvised advised, IAopAdvisor advisor)
	{
		ArrayList listeners = getProjectAopModelChangeListeners(advised.getAdvisedElement().getJavaProject());
		for (Iterator iter = listeners.iterator(); iter.hasNext(); )
		{
			IAopModelChangeListener listener = (IAopModelChangeListener) iter.next();
			listener.advisorAdded(advised, advisor);
		}
		
		for (Iterator iter = globalListeners.iterator(); iter.hasNext(); )
		{
			IAopModelChangeListener listener = (IAopModelChangeListener) iter.next();
			listener.advisorAdded(advised, advisor);
		}
	}
	
	protected void fireAdvisorRemoved (IAopAdvised advised, IAopAdvisor advisor)
	{
		ArrayList listeners = getProjectAopModelChangeListeners(advised.getAdvisedElement().getJavaProject());
		for (Iterator iter = listeners.iterator(); iter.hasNext(); )
		{
			IAopModelChangeListener listener = (IAopModelChangeListener) iter.next();
			listener.advisorRemoved(advised, advisor);
		}
		
		for (Iterator iter = globalListeners.iterator(); iter.hasNext(); )
		{
			IAopModelChangeListener listener = (IAopModelChangeListener) iter.next();
			listener.advisorRemoved(advised, advisor);
		}
	}
	
	/**
	 * Update the internal AOP Model of the given project.
	 * This will still take time to execute, but nowhere near as long as initializing the model.
	 * Note that new/modified pointcuts will still have to search the global JDT type cache, but
	 * pointcuts which are the same and just have added/deleted advisors will have their deltas
	 * calculated and processed.
	 *  
	 * @param project
	 */
	
	public void updateModel (IJavaProject project, IProgressMonitor monitor)
	{
		// Rather than completely re-creating the entire internal model, we'll try to be "smart" about changed pointcuts,
		// and added/removed advisors
		
		ProjectAdvisors advisors = getProjectAdvisors (project);
		
		IAopInterceptor interceptors[] = advisors.getInterceptors();
		IAopAspect aspects[] = advisors.getAspects();
		
		AopDescriptor descriptor = AopCorePlugin.getDefault().getDefaultDescriptor(project);
		descriptor.update();
		
		Aop aop = descriptor.getAop();
		
		for (Iterator iter = aop.getPointcuts().iterator(); iter.hasNext(); )
		{
			Pointcut pointcut = (Pointcut) iter.next();
			if (AspectManager.instance().getPointcut(pointcut.getName()) == null)
			{
				try {
					JDTPointcutExpression expression = new JDTPointcutExpression(new PointcutExpression(pointcut.getName(), pointcut.getExpr()));
					
					AspectManager.instance().addPointcut(expression);
				}
				catch (ParseException e) 
				{
					
				}
			}
		}
		
		
		for (int i = 0; i < aspects.length; i++)
		{
			IAopAspect aspect = aspects[i];
			boolean found = false;
			
			for (Iterator iter = aop.getAspects().iterator(); iter.hasNext(); )
			{
				Aspect xmlAspect = (Aspect) iter.next();	
				if (xmlAspect.getClazz().equals(aspect.getType().getFullyQualifiedName()))
				{
					found = true;
					break;
				}
			}
			
			if (! found)
			{
				// Aspect was removed, remove all pertaining advice
				IAopAdvice advice[] = aspect.getAdvice();
				for (int j = 0; j < advice.length; j++)
				{
					IAopAdvised advised[] = advice[j].getAdvised();
					for (int k = 0; k < advised.length; k++)
					{
						fireAdvisorRemoved(advised[k], advice[j]);
					}
				}
				
				advisors.removeAspect(aspect);
			}
		}
		
		for (Iterator iter = aop.getAspects().iterator(); iter.hasNext(); )
		{
			Aspect xmlAspect = (Aspect) iter.next();
			if (! advisors.hasAspect(xmlAspect.getClazz()))
			{
				advisors.addAspect(xmlAspect.getClazz());
			}
		}
		
		
		/// Loop through the interceptors AT THE TOP LEVEL, find all removed and added, and update the model accordingly
		for (int i = 0; i < interceptors.length; i++)
		{
			boolean found = false;
			
			for (Iterator iter = aop.getInterceptors().iterator(); iter.hasNext(); )
			{
				Interceptor xmlInterceptor = (Interceptor) iter.next();	
				if (xmlInterceptor.getClazz().equals(interceptors[i].getAdvisingType().getFullyQualifiedName()))
				{
					found = true;
					break;
				}
			}
			
			if (!found)
			{
				//Interceptor was removed
				IAopAdvised advised[] = interceptors[i].getAdvised();
				for (int j = 0; j < advised.length; j++)
				{
					fireAdvisorRemoved (advised[j], interceptors[i]);
				}
				
				advisors.removeInterceptor (interceptors[i]);
			}
		}
		
		for (Iterator iter = aop.getInterceptors().iterator(); iter.hasNext(); )
		{
			Interceptor xmlInterceptor = (Interceptor) iter.next();
			if (! advisors.hasInterceptor(xmlInterceptor.getClazz()))
			{
				advisors.addInterceptor(xmlInterceptor.getClazz());
			}
		}
		
		
		/// After that, loop through all the descriptor's bindings, get all of the advisor elements for each pointcut expression
		/// (just instantiate JDTPointcutExpression--equals will work) then compare each advisor element to the xml objects
		/// and add / remove from the model as necessary.
		
		JDTPointcutExpression expressions[] = getProjectPointcuts(project);
		for (int i = 0; i < expressions.length; i++)
		{
			boolean found = false;
			
			for (Iterator iter = aop.getBindings().iterator(); iter.hasNext(); )
			{
				Binding binding = (Binding) iter.next();
				
				if (expressions[i].getExpr().equals(binding.getPointcut()))
				{
					found = true;
					
					// Rather than breaking, we still need to compare the advisor contents of this pointcut
					IAopAdvisor pointcutAdvisors[] = getPointcutAdvisors(project, expressions[i]);
					for (int j = 0; j < pointcutAdvisors.length; j++)
					{
						boolean foundAdvisor = false;
						
						if (pointcutAdvisors[j].getType() == IAopAdvisor.INTERCEPTOR)
						{
							IAopInterceptor pointcutInterceptor = (IAopInterceptor) pointcutAdvisors[j];
							
							for (Iterator iter2 = binding.getInterceptors().iterator(); iter2.hasNext(); )
							{
								Interceptor interceptor = (Interceptor) iter2.next();
								if (interceptor.getClazz().equals(pointcutInterceptor.getAdvisingType().getFullyQualifiedName()))
								{
									foundAdvisor = true;
									break;
								}
							}
							
							for (Iterator iter2 = binding.getInterceptorRefs().iterator(); iter2.hasNext(); )
							{
								InterceptorRef interceptorRef = (InterceptorRef) iter2.next();
								
								if (advisors.findInterceptor(interceptorRef.getName()).getAdvisingType().equals(pointcutInterceptor.getAdvisingType()))
								{
									foundAdvisor = true;
									break;
								}
							}
						}
						else if (pointcutAdvisors[j].getType() == IAopAdvisor.ADVICE)
						{
							IAopAdvice pointcutAdvice = (IAopAdvice) pointcutAdvisors[j];
							
							for (Iterator iter2 = binding.getAdvised().iterator(); iter2.hasNext(); )
							{
								Advice advice = (Advice) iter2.next();
								if (advice.getAspect().equals(pointcutAdvice.getAspect().getType().getFullyQualifiedName()))
								{
									if (advice.getName().equals(pointcutAdvice.getAdvisingMethod().getElementName()))
									{
										foundAdvisor = true;
										break;
									}
								}
							}
						}
						
						if (!foundAdvisor)
						{
							// Advisor was removed
							IAopAdvised advised[] = pointcutAdvisors[j].getAdvised();
							for (int a = 0; a < advised.length; a++)
							{
								fireAdvisorRemoved(advised[a], pointcutAdvisors[j]);
							}
							
							advisors.removeAdvisor (pointcutAdvisors[j]);
						}
					}
				}
				
				// At this point we've identified all "removed" advisors of this pointcut.
				// Now we need to reverse the loop and look for all "added" advisors, and
				// apply them to this pointcut. (and also start firing some advisor added events)
				for (Iterator iter2 = binding.getInterceptorRefs().iterator(); iter2.hasNext(); )
				{
					InterceptorRef interceptorRef = (InterceptorRef) iter2.next();
					IAopInterceptor interceptor = advisors.findInterceptor(interceptorRef.getName());
					
					if (interceptor != null)
					{
						boolean assignedToThisPointcut = false;
						
						IAopAdvisor pointcutAdvisors[] = getPointcutAdvisors(project, expressions[i]);
						for (int j = 0; j < pointcutAdvisors.length; j++)
						{
							if (pointcutAdvisors[j].equals(interceptor))
							{
								assignedToThisPointcut = true;
								break;
							}
						}
					
						if (!assignedToThisPointcut)
						{
							IAopAdvised pointcutAdvised[] = expressions[i].getAdvised();
							for (int j = 0; j < pointcutAdvised.length; j++)
							{
								fireAdvisorAdded(pointcutAdvised[j], interceptor);
								interceptor.addAdvised(pointcutAdvised[j]);
							}
							
							interceptor.assignPointcut(expressions[i]);
						}
					}
				}
				
				for (Iterator iter2 = binding.getInterceptors().iterator(); iter2.hasNext(); )
				{
					Interceptor xmlInterceptor = (Interceptor) iter2.next();
					IAopAdvisor pointcutAdvisors[] = getPointcutAdvisors(project, expressions[i]);
					boolean assignedToThisPointcut = false;
					
					for (int j = 0; j < pointcutAdvisors.length; j++)
					{
						if (pointcutAdvisors[j].getType() == IAopAdvisor.INTERCEPTOR)
						{
							IAopInterceptor interceptor = (IAopInterceptor) pointcutAdvisors[j];
							if (interceptor.getAdvisingType().getFullyQualifiedName().equals(xmlInterceptor.getClazz()))
							{
								assignedToThisPointcut = true;
								break;
							}
						}
					}
					
					if (!assignedToThisPointcut)
					{
						IAopInterceptor interceptor = advisors.addInterceptor(xmlInterceptor.getClazz());
						IAopAdvised pointcutAdvised[] = expressions[i].getAdvised();
						for (int j = 0; j < pointcutAdvised.length; j++)
						{
							fireAdvisorAdded(pointcutAdvised[j], interceptor);
							interceptor.addAdvised(pointcutAdvised[j]);
						}
						
						interceptor.assignPointcut(expressions[i]);
					}
				}
				
				for (Iterator iter2 = binding.getAdvised().iterator(); iter2.hasNext(); )
				{
					Advice xmlAdvice = (Advice) iter2.next();
					IAopAdvisor pointcutAdvisors[] = getPointcutAdvisors(project, expressions[i]);
					boolean assignedToThisPointcut = false;
					
					for (int j = 0; j < pointcutAdvisors.length; j++)
					{
						if (pointcutAdvisors[j].getType() == IAopAdvisor.ADVICE)
						{
							IAopAdvice advice = (IAopAdvice) pointcutAdvisors[j];
							if (advice.getAspect().getType().getFullyQualifiedName().equals(xmlAdvice.getAspect())
								&& advice.getAdvisingMethod().getElementName().equals(xmlAdvice.getName()))
							
							{
								assignedToThisPointcut = true;
								break;
							}
						}
					}
					
					if (!assignedToThisPointcut)
					{
						IAopAdvice advice = advisors.addAdvice(xmlAdvice.getAspect(), xmlAdvice.getName());
						IAopAdvised pointcutAdvised[] = expressions[i].getAdvised();
						for (int j = 0; j < pointcutAdvised.length; j++)
						{
							fireAdvisorAdded(pointcutAdvised[j], advice);
							advice.addAdvised(pointcutAdvised[j]);
						}
						
						advice.assignPointcut(expressions[i]);
					}
				}
			}
			
			if (!found)
			{
				// Pointcut was removed.
				IAopAdvisor pointcutAdvisors[] = getPointcutAdvisors(project, expressions[i]);
				IAopAdvised pointcutAdvised[] = expressions[i].getAdvised();
				
				for (int j = 0; j < pointcutAdvisors.length; j++)
				{
					for (int k = 0; k < pointcutAdvised.length; k++)
					{
						fireAdvisorRemoved(pointcutAdvised[k], pointcutAdvisors[j]);
						pointcutAdvisors[j].removeAdvised(pointcutAdvised[k]);
					}
					
					pointcutAdvisors[j].unassignPointcut(expressions[i]);
				}
				
				for (int k = 0; k < pointcutAdvised.length; k++)
				{
					expressions[i].removeAdvised(pointcutAdvised[k]);
				}
			}
		}
		
		
		// The entire AOP meta model has been pruned for deltas at this point.
		// Now it's time to go through and just manually add new binding/pointcuts
		// (the same we do in init.. )
		
		for (Iterator iter = aop.getBindings().iterator(); iter.hasNext(); )
		{
			Binding binding = (Binding) iter.next();
			boolean found = false;
			
			for (int i = 0; i < expressions.length; i++)
			{
				if (expressions[i].getExpr().equals(binding.getPointcut()))
				{
					found = true;
					break;
				}
			}
			
			if (! found)
			{
				// New pointcut to add.
				bindNewPointcut(project, binding, monitor);
			}
		}
	}
	
	/**
	 * Initialize the AOP model of the given project.
	 * Warning: This is a long running process, hence the required IProgressMonitor
	 * @param project
	 */
	public void initModel (IJavaProject project, IProgressMonitor monitor)
	{
		ProjectAdvisors advisors = getProjectAdvisors(project);
		
		AopDescriptor descriptor = AopCorePlugin.getDefault().getDefaultDescriptor(project);
		Aop aop = descriptor.getAop();
		
		for (Iterator iter = aop.getPointcuts().iterator(); iter.hasNext(); )
		{
			Pointcut pointcut = (Pointcut) iter.next();
			try {
				JDTPointcutExpression expression = new JDTPointcutExpression(new PointcutExpression(pointcut.getName(), pointcut.getExpr()));
				AspectManager.instance().addPointcut(expression);
			}
			catch (ParseException e) 
			{
				
			}
		}
		
		for (Iterator iter = aop.getAspects().iterator(); iter.hasNext(); )
		{
			Aspect aspect = (Aspect) iter.next();
			advisors.addAspect(aspect.getClazz());
		}
		
		monitor.worked(1);
		
		for (Iterator iter = aop.getInterceptors().iterator(); iter.hasNext(); )
		{
			Interceptor interceptor = (Interceptor) iter.next();
			advisors.addInterceptor(interceptor.getClazz());
		}
		
		monitor.worked(1); 
		
		// Register the source types of this project...
		try {
			ArrayList sourceTypes = new ArrayList();
			IPackageFragmentRoot roots[] = project.getAllPackageFragmentRoots();
			for (int i = 0; i < roots.length; i++)
			{
				if (!roots[i].isArchive() && !roots[i].isExternal())
				{
					IJavaElement children[] = roots[i].getChildren();
					
					for (int j = 0; j < children.length; j++)
					{
						processSourceElement (sourceTypes, children[j]);
					}
					
				}
			}
			
			for (Iterator iter = sourceTypes.iterator(); iter.hasNext(); )
			{
				registerType((IType)iter.next());
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Iterator iter = aop.getBindings().iterator(); iter.hasNext(); )
		{
			Binding binding = (Binding) iter.next();
			bindNewPointcut (project, binding, monitor);
		}
		
		monitor.worked(1);
	}
	
	private class PointcutAdvisedCollector extends AdvisedCollector
	{
		protected JDTPointcutExpression expression;
		protected ArrayList advisors;
		
		public PointcutAdvisedCollector (JDTPointcutExpression expression)
		{
			this.expression = expression;
			advisors = new ArrayList();
		}
		
		public void addAdvisor (IAopAdvisor advisor)
		{
			advisors.add(advisor);
		}

		public void collectAdvised (IAopAdvised advised)
		{
			expression.addAdvised(advised);

			for (Iterator iter = advisors.iterator(); iter.hasNext(); )
			{
				IAopAdvisor advisor = (IAopAdvisor) iter.next();
				
				fireAdvisorAdded(advised, advisor);
				advisor.addAdvised(advised);
			}
		}
		
		public void handleException(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void bindNewPointcut (IJavaProject project, Binding binding, IProgressMonitor monitor)
	{
		try {
			ProjectAdvisors advisors = getProjectAdvisors(project);
			JDTPointcutExpression expression = new JDTPointcutExpression(new PointcutExpression(null, binding.getPointcut()));
			PointcutAdvisedCollector collector = new PointcutAdvisedCollector(expression);
			expression.setCollector(collector);
			
			for (Iterator iter2 = binding.getAdvised().iterator(); iter2.hasNext(); )
			{
				Advice advice = (Advice) iter2.next();
				IAopAdvice aopAdvice = advisors.addAdvice (advice.getAspect(), advice.getName());
				aopAdvice.assignPointcut(expression);
				
				collector.addAdvisor(aopAdvice);
			}
			
			for (Iterator iter2 = binding.getInterceptorRefs().iterator(); iter2.hasNext(); )
			{
				InterceptorRef ref = (InterceptorRef) iter2.next();
				
				IAopInterceptor interceptor = advisors.findInterceptor(ref.getName());
				interceptor.assignPointcut(expression);
				
				collector.addAdvisor(interceptor);
			}
			
			for (Iterator iter2 = binding.getInterceptors().iterator(); iter2.hasNext(); )
			{
				Interceptor interceptor = (Interceptor) iter2.next();
				
				IAopInterceptor aopInterceptor = advisors.addInterceptor(interceptor.getClazz());
				aopInterceptor.assignPointcut(expression);
				
				collector.addAdvisor(aopInterceptor);
			}

			IJavaElement elements[] = (IJavaElement[]) registeredTypes.toArray(new IJavaElement[registeredTypes.size()]);
			
			if (elements.length > 0)
				findAllAdvised (elements, expression, collector, monitor);
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}
	
	protected void processSourceElement (ArrayList sourceTypes, IJavaElement element)
	{
		if (element instanceof ICompilationUnit)
		{
			ICompilationUnit unit = (ICompilationUnit) element;
			sourceTypes.add(unit.findPrimaryType());
		}
		else if (element instanceof IPackageFragment)
		{
			IPackageFragment fragment = (IPackageFragment) element;
			sourceTypes.addAll(findSourceTypesInPackageFragment(fragment));
		}
	}
	
	protected ArrayList findSourceTypesInPackageFragment (IPackageFragment fragment)
	{
		ArrayList types = new ArrayList();
		try {
			IJavaElement children[] = fragment.getChildren();
			
			for (int j = 0; j < children.length; j++)
			{
				processSourceElement (types, children[j]);
			}
			
			
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return types;
	}
	
	private class AdvisedSearchRequestor
	{
		protected JDTPointcutExpression expression;
		protected IAdvisedCollector collector;
		
		public AdvisedSearchRequestor (JDTPointcutExpression expression, IAdvisedCollector collector)
		{
			this.expression = expression;
			this.collector = collector;
		}
		
		public void start (int numberOfTypes)
		{
			collector.beginTask("Collecting Advised Types...", numberOfTypes);
		}
		
		public void acceptType (IType type)
			throws CoreException
		{
			if (type != null)
			{
				collector.worked(1);
				
				IField fields[] = type.getFields();
				for (int i = 0; i < fields.length; i++)
				{
					if (expression.matchesGet(fields[i]))
					{
						collector.collectAdvised(new AopAdvised(IAopAdvised.TYPE_FIELD_GET, fields[i]));
					}
					if (expression.matchesSet(fields[i]))
					{
						collector.collectAdvised(new AopAdvised(IAopAdvised.TYPE_FIELD_SET, fields[i]));
					}
				}
				
				IMethod methods[] = type.getMethods();
				for (int i = 0; i < methods.length; i++)
				{
					if (expression.matchesExecution(methods[i]))
					{
						collector.collectAdvised(new AopAdvised(IAopAdvised.TYPE_METHOD_EXECUTION, methods[i]));
					}
				}
			}
		}
		
		public void handleException (Exception e)
		{
			collector.handleException(e);
		}
		
		public void finished () {
			collector.done();
		}
	}
	
	/**
	 * Returns a list of all types that are registered with the AOP Model
	 * @return
	 */
	public IJavaElement[] getRegisteredTypes()
	{
		return (IJavaElement[]) registeredTypes.toArray(new IJavaElement[registeredTypes.size()]);
	}
	
	/**
	 * Finds all types in the supplied project that match the supplied pointcut expression,
	 * and returns them as an array. This is a convienence method for methods wanting to only pass in 1 element.
	 * 
	 * @param project The project to search for all advised types in.
	 * @param expression The pointcut expression to match
	 * @return
	 */
	public void findAllAdvised (IJavaElement element, JDTPointcutExpression expression, IAdvisedCollector collector, IProgressMonitor monitor)
		throws RuntimeException
	{
		findAllAdvised(new IJavaElement[]{element}, expression, collector, monitor);
	}
	
	/**
	 * Finds all types in the supplied project that match the supplied pointcut expression,
	 * and returns them as an array.
	 * 
	 * @param project The project to search for all advised types in.
	 * @param expression The pointcut expression to match
	 * @return
	 */
	public void findAllAdvised (final IJavaElement elements[], final JDTPointcutExpression expression, final IAdvisedCollector collector, final IProgressMonitor monitor)
		throws RuntimeException
	{
		new Thread (new Runnable() {
			public void run () {
				AdvisedSearchRequestor requestor = new AdvisedSearchRequestor(expression, collector);
				try {
					ArrayList allTypes = new ArrayList();
					IJavaSearchScope scope = SearchEngine.createJavaSearchScope(elements);
					AllTypesCache.getTypes(scope, IJavaSearchConstants.TYPE, new NullProgressMonitor(), allTypes);
					
					requestor.start(allTypes.size());
					for (Iterator iter = allTypes.iterator(); iter.hasNext(); )
					{
						TypeInfo typeInfo = (TypeInfo) iter.next();
						IType type = typeInfo.resolveType(scope);
						
						requestor.acceptType(type);
					}
					requestor.finished();
				} catch (Exception e) {
					requestor.handleException(e);
				}
			}
		}).start();
	}
	
	/**
	 * This method is used when elementChanged's delta's element is not directly
	 * associated with a project. Here, we search for its compilation unit
	 * recursively through the deltas until we find it. 
	 * 
	 * @param event The element changed event sent to the elementChanged method
	 * @return The compilation unit that's been changed
	 */
	public ICompilationUnit elementChangedGetCompilationUnit(ElementChangedEvent event) {
		ArrayList list = elementChangedGetAllAffected(event.getDelta(), true);
		Iterator i = list.iterator();
		while( i.hasNext() ) {
			JavaElementDelta delta = (JavaElementDelta)i.next();
			IJavaElement element = delta.getElement();
			if( element instanceof CompilationUnit ) {
				return (CompilationUnit)element;
			}
		}
		return null;
	}
	
	public void elementChanged (ElementChangedEvent event)
	{
		ArrayList changed = new ArrayList();
		IJavaProject project = event.getDelta().getElement().getJavaProject();
		if( project == null ) {
			ICompilationUnit unit = elementChangedGetCompilationUnit(event);
			if( unit == null ) {
				//System.out.println("Problem"); 
				return;
			}
			project = unit.getJavaProject();
			//System.out.println("Saved");
		}
		JDTPointcutExpression expressions[] = getProjectPointcuts( project );
		boolean annotationsHaveChanged = false;
		
		if( event.getDelta().getElement() instanceof CompilationUnit ) {
			annotationsHaveChanged = true;
		}

		
		changed.addAll(elementChangedGetAllAffected(event.getDelta()));		
		
		
		
		// The removed elements should be at the top now.  The added elements at the bottom. 
		Collections.sort(changed, new Comparator() {

			public int compare(Object first, Object second) {
				if( ((IJavaElementDelta)first).getKind() ==  ((IJavaElementDelta)second).getKind())
					return 0;
				
				if(((IJavaElementDelta)first).getKind() == IJavaElementDelta.REMOVED ) {
					return -1;
				}
				
				return 1;
			}
			
		});
		
		for( Iterator i = changed.iterator(); i.hasNext();) {
			IJavaElementDelta t = ((IJavaElementDelta)(i.next()));

			if( t.getKind() == IJavaElementDelta.ADDED ) 
				handleElementAdded(t, expressions);
			else 
				handleElementRemoved(t, expressions);

		}

		
		
		
		// Finally check the annotations
		if( annotationsHaveChanged ) {
			try {
				IType types[] = ((CompilationUnit)event.getDelta().getElement()).getTypes();
				for( int i = 0; i < types.length; i++ ) {
					elementChangedCompilationUnit(types[i], expressions);
				}
			} catch( CoreException e ) {
				//e.printStackTrace();
			}
		}
	}

	
	/**
	 * Convenience method
	 */
	private ArrayList elementChangedGetAllAffected(IJavaElementDelta delta ) {
		return elementChangedGetAllAffected(delta, false);
	}
	
	/**
	 * This is a private method that will recursively get a list
	 * of added, or deleted children to be used by the elementChanged 
	 * method. 
	 * It will not include anything except methods or fields. 
	 * It will not include changed elements. Only added or removed.
	 * @param delta The original event's delta
	 * @param includeCompilationUnit  Do we include the compilation unit in the returned list?
	 * @return tmp An arraylist 
	 */
	private ArrayList elementChangedGetAllAffected(IJavaElementDelta delta, boolean includeCompilationUnit) {
		ArrayList changed = new ArrayList();
		IJavaElementDelta changedChildren[] = delta.getAffectedChildren();
		if( delta.getElement() instanceof SourceMethod || delta.getElement() instanceof SourceField 
				|| (delta.getElement() instanceof CompilationUnit && includeCompilationUnit)) {
			
			// We only care about added or removed... not changed.
			if( delta.getKind() != IJavaElementDelta.CHANGED)
				changed.add(delta);

			// unless we're including the compilation unit
			if( includeCompilationUnit && delta.getKind() == IJavaElementDelta.CHANGED) {
				changed.add(delta);				
			}
		}
		for( int i = 0; i < changedChildren.length; i++ ) {
			changed.addAll(elementChangedGetAllAffected(changedChildren[i], includeCompilationUnit));
		}
		return changed;
	}
	

	
	/**
	 * A new element has been added through a delta to the elementChanged method. 
	 * We must check if it matches anything, and if so, add the match to the model.
	 * 
	 * 
	 * @param addedDelta
	 * @param expressions
	 */
	private void handleElementAdded (IJavaElementDelta addedDelta, JDTPointcutExpression expressions[])
	{
		IJavaElement added = addedDelta.getElement();
		
		for (int j = 0; j < expressions.length; j++)
		{
			if (added.getElementType() == IJavaElement.METHOD)
			{
				if (expressions[j].matchesExecution((IMethod) added))
				{
					addAdvisedToPointcutAdvisors(added, IAopAdvised.TYPE_METHOD_EXECUTION, expressions[j]);
				}
			}
			else if (added.getElementType() == IJavaElement.FIELD)
			{
				if (expressions[j].matchesGet((IField) added))
				{
					addAdvisedToPointcutAdvisors(added, IAopAdvised.TYPE_FIELD_GET, expressions[j]);
				}
				if (expressions[j].matchesSet((IField) added))
				{
					addAdvisedToPointcutAdvisors(added, IAopAdvised.TYPE_FIELD_SET, expressions[j]);
				}
			}
		}
	}
	
	/**
	 * An element has been removed through a delta to the changeEvent method.
	 * 
	 * @param removedDelta
	 * @param expressions
	 */
	private void handleElementRemoved (IJavaElementDelta removedDelta, JDTPointcutExpression expressions[])
	{
		IJavaElement removed = removedDelta.getElement();
		if( removed instanceof IMethod ) elementChangedRemoveMethod(((IMethod)removed), expressions);
		if( removed instanceof IField ) elementChangedRemoveField((IField)removed, expressions);
	}
	
	private void elementChangedRemoveMethod(IMethod method, JDTPointcutExpression expressions[])
	{
		IJavaProject project = method.getCompilationUnit().getJavaProject();
		for( int i = 0; i < expressions.length; i++ ) {
			IAopAdvised advised = expressions[i].getAdvised(method);
			if( advised != null ) removeAdvised(project, new IAopAdvised[] { advised });
		}
	}
	

	private void elementChangedRemoveField(IField field, JDTPointcutExpression expressions[]) {
		IJavaProject project = field.getCompilationUnit().getJavaProject();
		for( int i = 0; i < expressions.length; i++ ) {
			IAopAdvised getVal = 
				expressions[i].getAdvised(field, IAopAdvised.TYPE_FIELD_GET);
			IAopAdvised setVal = 
				expressions[i].getAdvised(field, IAopAdvised.TYPE_FIELD_SET);
			if( getVal != null ) removeAdvised(project, new IAopAdvised[] { getVal });
			if( setVal != null ) removeAdvised(project, new IAopAdvised[] { setVal });
		}
	}
	
	
	/**
	 * This method is only reached if the compilation unit has changed,
	 * which implies the annotations have changed.
	 * 
	 * This will add or remove advisors based on the changed annotations.
	 * Because the jdt model here only returns a compilation unit, 
	 * we're not exactly sure what's changed, so we have to check
	 * every method, field, and inner class for changes.
	 * 
	 * @param type
	 * @param expressions
	 */
	private void elementChangedCompilationUnit(IType type, JDTPointcutExpression expressions[] ) {
		try {
			IMethod[] methods = type.getMethods();
			IField[] fields = type.getFields();
			IType[] innerTypes = type.getTypes();
						

			/*
			 * Checking what fields are changed and update 
			 * the model accordingly. 
			 */
			for( int i = 0; i < fields.length; i++ ) {
				elementChangedVerifyField( fields[i], expressions);
			}
			
			/*
			 * Check what methods (and constructors) are changed 
			 * and update the model accordingly. 
			 */
			for( int i = 0; i < methods.length; i++ ) {
				elementChangedVerifyMethod(methods[i], expressions);
			}
			
			/*
			 * Finally, we should go recursively through subtypes.
			 */
			for( int i = 0; i < innerTypes.length; i++) {
				elementChangedCompilationUnit(innerTypes[i], expressions);
			}
			
			
		} catch (JavaModelException e ) {
		}
	}
	
	private void elementChangedVerifyField( IField field, JDTPointcutExpression expressions[]) {
		for( int j = 0; j < expressions.length; j++ ) {
			boolean matchesGet = expressions[j].matchesGet(field);
			boolean matchesSet = expressions[j].matchesSet(field);
			IAopAdvised getVal = 
				expressions[j].getAdvised(field, IAopAdvised.TYPE_FIELD_GET);
			IAopAdvised setVal = 
				expressions[j].getAdvised(field, IAopAdvised.TYPE_FIELD_SET);
			
			
			if( matchesGet && getVal == null ) {  // add him
				addAdvisedToPointcutAdvisors(field, IAopAdvised.TYPE_FIELD_GET, expressions[j]);
			} else if( !matchesGet && getVal != null ) { // remove him
				removeAdvisedFromPointcutAdvisors(getVal, expressions[j]);
			}
			
			if( matchesSet && setVal == null ) {
				addAdvisedToPointcutAdvisors(field, IAopAdvised.TYPE_FIELD_SET, expressions[j]);						
			} else if( !matchesSet && setVal != null ) {
				removeAdvisedFromPointcutAdvisors(setVal, expressions[j]);
			}
		}
	}

	private void elementChangedVerifyMethod(IMethod method, JDTPointcutExpression expressions[]) {
		for( int j = 0; j < expressions.length; j++ ) {
			boolean matches = expressions[j].matchesExecution(method);
			IAopAdvised advisedObj = expressions[j].getAdvised(method);
			
			if( matches && advisedObj == null ) {
				addAdvisedToPointcutAdvisors(method, 
						IAopAdvised.TYPE_METHOD_EXECUTION, expressions[j]);
			} else if( !matches && advisedObj != null ) {
				removeAdvisedFromPointcutAdvisors(advisedObj, expressions[j]);
			}
		}
	}
	
	/**
	 * Adds an IJavaElement to every advisor for a given expression. 
	 * The method creates an AopAdvised object from your IJavaElement first.
	 * @param advisedElement The Java element that is to be advised
	 * @param advisedType    A type used to create the AopAdvised object
	 * @param expression     The expression that this JavaElement matches
	 */
	protected void addAdvisedToPointcutAdvisors (IJavaElement advisedElement, int advisedType, JDTPointcutExpression expression)
	{
		IAopAdvisor advisors[] = getPointcutAdvisors(advisedElement.getJavaProject(), expression);
		IAopAdvised advised = new AopAdvised(advisedType, advisedElement);
		
		for (int i = 0; i < advisors.length; i++)
		{	
			advisors[i].addAdvised(advised);
			fireAdvisorAdded(advised, advisors[i]);
		}

		expression.addAdvised(advised);
	}

	/**
	 * Removes an IAopAdvised from every advisor for a given expression. 
	 * @param advised        The IAopAdvised object that no longer matches the expression
	 * @param expression     The expression that this JavaElement previously matched
	 */
	protected void removeAdvisedFromPointcutAdvisors 
		(IAopAdvised advised, JDTPointcutExpression expression)
	{
		IAopAdvisor advisors[] = getPointcutAdvisors(
				advised.getAdvisedElement().getJavaProject(), expression);
		
		
		for (int i = 0; i < advisors.length; i++)
		{	
			advisors[i].removeAdvised(advised);
			fireAdvisorRemoved(advised, advisors[i]);
		}

		expression.removeAdvised(advised);
	}

	
	/**
	 * Completely remove the advised elements from all 
	 * advice, interceptors, and expressions. 
	 * 
	 * @param project  The project the java elements belongs to
	 * @param advisedElements The elements that are to be removed entirely
	 */
	protected void removeAdvised (IJavaProject project, IAopAdvised advisedElements[])
	{
		IAopAspect aspects[] = getProjectAdvisors(project).getAspects();
		IAopInterceptor interceptors[] = getProjectAdvisors(project).getInterceptors();
		JDTPointcutExpression expressions[] = getProjectPointcuts(project);
		
		for (int e = 0; e < advisedElements.length; e++)
		{
			for (int i = 0; i < aspects.length; i++)
			{
				IAopAdvice advice[] = aspects[i].getAdvice();
				for (int j =0; j < advice.length; j++)
				{
					if (advice[j].advises(advisedElements[e]))
					{
						advice[j].removeAdvised(advisedElements[e]);
						fireAdvisorRemoved(advisedElements[e], advice[j]);
					}
				}
			}
			
			for (int i = 0; i < interceptors.length; i++)
			{
				if (interceptors[i].advises(advisedElements[e]))
				{
					interceptors[i].removeAdvised(advisedElements[e]);
					fireAdvisorRemoved(advisedElements[e], interceptors[i]);
				}
			}
			
			for (int ex = 0; ex < expressions.length; ex++)
			{
				if (expressions[ex].hasAdvised(advisedElements[e]))
					expressions[ex].removeAdvised(advisedElements[e]);
			}
		}
	}
}
