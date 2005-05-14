/*
 * Created on Jan 11, 2005
 */
package org.jboss.ide.eclipse.jdt.aop.core.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.corext.util.AllTypesCache;
import org.eclipse.jdt.internal.corext.util.TypeInfo;
import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.Scope;
import org.jboss.aop.pointcut.PointcutExpression;
import org.jboss.aop.pointcut.ast.ParseException;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Advice;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Binding;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Interceptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.InterceptorRef;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAdvisedCollector;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvice;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvised;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvisor;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAspect;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopInterceptor;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopModelChangeListener;
import org.jboss.ide.eclipse.jdt.aop.core.model.internal.AopAdvised;
import org.jboss.ide.eclipse.jdt.aop.core.model.internal.AopTypedef;
import org.jboss.ide.eclipse.jdt.aop.core.model.internal.ProjectAdvisors;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTPointcutExpression;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTTypedefExpression;


/**
 * @author Marshall
 */
public class AopModel {
	
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
		new AopModelElementChangedListener();
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
			AopTypedef typedefs[] = advisors.getTypedefs();
			
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
			
			
			for( int i = 0; i < typedefs.length; i++ ) {
				IAopAdvised advised[] = typedefs[i].getAdvised();
				for( int j = 0; j < advised.length; j++ ) {
					listener.advisorAdded(advised[j], typedefs[i]);					
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
				IType typeArray[] = new IType[]{type};
				findAllAdvised(typeArray, expressions[i], expressions[i].getCollector(), new NullProgressMonitor());
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
		AopTypedef typedefs[] = getProjectAdvisors(element.getJavaProject()).getTypedefs();
		
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
		
		for( int i = 0; i < typedefs.length; i++ ) {
			if( typedefs[i].advises(element)) {
				advisors.add(typedefs[i]);
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
	
	public void updateModel (IJavaProject project, IProgressMonitor monitor) {
		ModelInitAndUpdate.instance().updateModel(project, monitor);
	}
	
	/**
	 * Initialize the AOP model of the given project.
	 * Warning: This is a long running process, hence the required IProgressMonitor
	 * @param project
	 */
	public void initModel (IJavaProject project, IProgressMonitor monitor) {
		ModelInitAndUpdate.instance().initModel(project, monitor);
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
			
			for (Iterator iter2 = AopModelUtils.getAdvicesFromBinding(binding).iterator(); iter2.hasNext(); )
			{
				Advice advice = (Advice) iter2.next();
				IAopAdvice aopAdvice = advisors.addAdvice (advice.getAspect(), advice.getName());
				aopAdvice.assignPointcut(expression);
				
				collector.addAdvisor(aopAdvice);
			}
			
			for (Iterator iter2 = AopModelUtils.getInterceptorRefssFromBinding(binding).iterator(); iter2.hasNext(); )
			{
				InterceptorRef ref = (InterceptorRef) iter2.next();
				
				IAopInterceptor interceptor = advisors.findInterceptor(ref.getName());
				interceptor.assignPointcut(expression);
				
				collector.addAdvisor(interceptor);
			}
			
			for (Iterator iter2 = AopModelUtils.getInterceptorsFromBinding(binding).iterator(); iter2.hasNext(); )
			{
				Interceptor interceptor = (Interceptor) iter2.next();
				
				IAopInterceptor aopInterceptor = advisors.addInterceptor(interceptor.getClazz());
				aopInterceptor.assignPointcut(expression);
				
				collector.addAdvisor(aopInterceptor);
			}

			//IJavaElement elements[] = (IJavaElement[]) registeredTypes.toArray(new IJavaElement[registeredTypes.size()]);
			IType typeArray[] = (IType[]) registeredTypes.toArray(new IType[registeredTypes.size()]);
			if (typeArray.length > 0)
				findAllAdvised (typeArray, expression, collector, monitor);
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
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
	
	public IType[] getRegisteredTypesAsITypes() 
	{
		return (IType[])registeredTypes.toArray(new IType[registeredTypes.size()]);
	}
	
	public void findAllAdvised (IJavaElement element, JDTPointcutExpression expression, IAdvisedCollector collector, IProgressMonitor monitor)
		throws RuntimeException
	{
		findAllAdvised(new IJavaElement[]{element}, expression, collector, monitor);
	}
	
	
	/**
	 * This method will be deprecated or used scarcely in the future. 
	 * It is a long-running very slow implementation.
	 * 
	 * @param elements
	 * @param expression
	 * @param collector
	 * @param monitor
	 * @throws RuntimeException
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
	 * Much simpler implementation that requires types to begin with.
	 * 
	 * @param types       An array of types to check
	 * @param expression  A pointcut expression to match against
	 * @param collector   A collector of types
	 * @param monitor     Something to keep time, of course
	 * @throws RuntimeException
	 */
	public void findAllAdvised (final IType types[], final JDTPointcutExpression expression, final IAdvisedCollector collector, final IProgressMonitor monitor)
	throws RuntimeException
	{
		new Thread (new Runnable() {
			public void run () {
				AdvisedSearchRequestor requestor = new AdvisedSearchRequestor(expression, collector);
				try {
					requestor.start(types.length);
					for( int i = 0; i < types.length; i++ ) {
						requestor.acceptType(types[i]);
					}
					requestor.finished();
				} catch (Exception e) {
					requestor.handleException(e);
				}
			}
		}).start();
	}
	
	
	
	public void findAllAdvised(IType[] types, JDTTypedefExpression tdExpr, final IAdvisedCollector collector ) {
		collector.beginTask("Collecting matching types", types.length);
		for( int i = 0; i < types.length; i++ ) {
			if( tdExpr.matches(types[i])) {
				collector.collectAdvised(new AopAdvised(IAopAdvised.TYPE_CLASS, types[i]));
			}
		}
		collector.done();
	}
	
	public AopAdvised[] findAllAdvised(JDTTypedefExpression tdExpr) {
		ArrayList list = new ArrayList();
		IType[] types = getRegisteredTypesAsITypes();
		for( int i = 0; i < types.length; i++ ) {
			if( tdExpr.matches(types[i])) {
				list.add(new AopAdvised(IAopAdvised.TYPE_CLASS, types[i]));
			}
		}		
		return (AopAdvised[])(list.toArray(new AopAdvised[list.size()]));
	}
	
	/**
	 * Adds a new typedef expression to the model.
	 * @param project
	 * @param tdExpr
	 */
	public void addNewTypedef( IJavaProject project, JDTTypedefExpression tdExpr ) {
		try {
			AspectManager.instance().addTypedef(tdExpr);
			ProjectAdvisors advisors = getProjectAdvisors(project);
			AopTypedef typedef = advisors.addTypedef(tdExpr);
			IAopAdvised[] advisedClasses = AopModel.instance().findAllAdvised(tdExpr);
			typedef.addAdvised(advisedClasses);
			
			for( int i = 0; i < advisedClasses.length; i++ ) {
				fireAdvisorAdded(advisedClasses[i], typedef);
			}
			
			
		} catch( Exception e ) {
			
		}
		
	}
	
	/**
	 * Removes a typedef from the model, from the list of advisors,
	 * and from the underlying aspectmanager in main aop.
	 * @param project
	 * @param tdExpr
	 */
	public void removeTypedef(IJavaProject project, JDTTypedefExpression tdExpr) {
		AspectManager.instance().removeTypedef(tdExpr.getName());
		ProjectAdvisors advisors = getProjectAdvisors(project);
		AopTypedef advisorTypedef = advisors.getTypedef(tdExpr.getName());
		IAopAdvised[] advisedClasses = advisorTypedef.getAdvised();
		for( int i = 0; i < advisedClasses.length; i++ ) {
			fireAdvisorRemoved(advisedClasses[i], advisorTypedef);
		}
		advisors.removeTypedef(tdExpr);
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
	
	
	
	/*
	 * Simple utility functions that are used but not part of the model.
	 */
	
	public static Scope getScopeFromString( String s ) {
		if( s == "PER_VM" ) return Scope.PER_VM;
		if( s == "PER_CLASS" ) return Scope.PER_CLASS;
		if( s == "PER_CLASS_JOINPOINT") return Scope.PER_CLASS_JOINPOINT;
		if( s == "PER_INSTANCE") return Scope.PER_INSTANCE;
		if( s == "PER_JOINPOINT") return Scope.PER_JOINPOINT;
		return null;
	}
	
}
