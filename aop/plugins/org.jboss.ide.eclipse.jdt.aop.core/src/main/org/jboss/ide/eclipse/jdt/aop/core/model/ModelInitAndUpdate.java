package org.jboss.ide.eclipse.jdt.aop.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.advice.Scope;
import org.jboss.aop.pointcut.PointcutExpression;
import org.jboss.aop.pointcut.TypedefExpression;
import org.jboss.aop.pointcut.ast.ParseException;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Advice;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aop;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aspect;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Binding;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Interceptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.InterceptorRef;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Introduction;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Pointcut;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Typedef;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvice;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvised;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvisor;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAspect;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopInterceptor;
import org.jboss.ide.eclipse.jdt.aop.core.model.internal.AopInterfaceIntroduction;
import org.jboss.ide.eclipse.jdt.aop.core.model.internal.AopTypedef;
import org.jboss.ide.eclipse.jdt.aop.core.model.internal.ProjectAdvisors;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTInterfaceIntroduction;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTPointcutExpression;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTTypedefExpression;

/**
 * 
 * @author Rob Stryker
 */
public class ModelInitAndUpdate {

	
	private static ModelInitAndUpdate instance;
	
	public static ModelInitAndUpdate instance() {
		if( instance == null ) 
			instance = new ModelInitAndUpdate();
		return instance;
	}
	
	/**
	 * Initialize the AOP model of the given project.
	 * Warning: This is a long running process, hence the required IProgressMonitor
	 * @param project
	 */
	public void initModel (IJavaProject project, IProgressMonitor monitor)
	{
		ProjectAdvisors advisors = AopModel.instance().getProjectAdvisors(project);
		
		AopDescriptor descriptor = AopCorePlugin.getDefault().getDefaultDescriptor(project);
		Aop aop = descriptor.getAop();
		
		initializeSourceTypes(project, monitor);
		
		
		initializeTypedefs(aop, project, monitor);
		initializePointcuts(aop, monitor);
		initializeAspects(aop, monitor, advisors);
		initializeInterceptors(aop, project, monitor, advisors);
		initializeBindings(aop, project, monitor);
		initializeIntroductions(aop, project, monitor);
		
	}
	
	private void initializeSourceTypes(IJavaProject project, IProgressMonitor monitor) {
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
				AopModel.instance().registerType((IType)iter.next());
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		monitor.worked(1);

	}
	
	private void processSourceElement (ArrayList sourceTypes, IJavaElement element)
	{
		if (element instanceof ICompilationUnit)
		{
			ICompilationUnit unit = (ICompilationUnit) element;
			sourceTypes.add(unit.findPrimaryType());

			// TODO: fix? This may break it
			
			try {
				IType[] types = unit.getAllTypes();
				for(  int i = 0; i < types.length; i++ ) {
					sourceTypes.add(types[i]);
				}
			} catch( JavaModelException jme ) {
			}
			// end possible break
			
		} else if( element instanceof IType ) {
			try {
				IType[] types = ((IType)element).getTypes();
				sourceTypes.add(element);
				for( int i = 0; i < types.length; i++ ) processSourceElement(sourceTypes, element);
			} catch( JavaModelException jme) {
			}
		}
		else if (element instanceof IPackageFragment)
		{
			IPackageFragment fragment = (IPackageFragment) element;
			sourceTypes.addAll(findSourceTypesInPackageFragment(fragment));
		}
	}
	
	private ArrayList findSourceTypesInPackageFragment (IPackageFragment fragment)
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


	private void initializeTypedefs(Aop aop, IJavaProject project, IProgressMonitor monitor ) {
		for (Iterator iter = AopModelUtils.getTypedefsFromAop(aop).iterator(); iter.hasNext(); )
		{
			Typedef jaxbTypedef = (Typedef) iter.next();
			try {
				TypedefExpression typedefExpression = new TypedefExpression(jaxbTypedef.getName(), jaxbTypedef.getExpr());
				JDTTypedefExpression expr = new JDTTypedefExpression( typedefExpression );
				AopModel.instance().addNewTypedef(project, expr);
						
			} catch( Exception typedefExec ) {
				
			}
		}
		monitor.worked(1);
	}
	
	private void initializeIntroductions(Aop aop, IJavaProject project, IProgressMonitor monitor ) {
		for( Iterator iter = AopModelUtils.getIntroductionsFromAop(aop).iterator(); iter.hasNext();) {
			Introduction jaxbIntro = (Introduction)iter.next();
			JDTInterfaceIntroduction jdtIntro = AopModelUtils.toJDT(jaxbIntro);
			AopModel.instance().addInterfaceIntroduction(project, jdtIntro);
		}
	}
	
	private void initializePointcuts(Aop aop, IProgressMonitor monitor) {
		for (Iterator iter = AopModelUtils.getPointcutsFromAop(aop).iterator(); iter.hasNext(); )
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
		monitor.worked(1);
	}
	
	private void initializeAspects(Aop aop, IProgressMonitor monitor, ProjectAdvisors advisors) {
		for (Iterator iter = AopModelUtils.getAspectsFromAop(aop).iterator(); iter.hasNext(); )
		{
			Aspect aspect = (Aspect) iter.next();
			advisors.addAspect(aspect.getClazz());
			String scope = aspect.getScope();
			Scope sScope = AopModel.getScopeFromString(aspect.getScope());
			
			AspectDefinition def = new AspectDefinition(aspect.getClazz(), sScope, null);
			try {
				AspectManager.instance().addAspectDefinition(def);
			} catch( Exception e ) {
				
			}
		}
		
		monitor.worked(1);		
	}
	
	private void initializeInterceptors(Aop aop, IJavaProject project, 
			IProgressMonitor monitor, ProjectAdvisors advisors ) {
		for (Iterator iter = AopModelUtils.getInterceptorsFromAop(aop).iterator(); iter.hasNext(); )
		{
			Interceptor interceptor = (Interceptor) iter.next();
			advisors.addInterceptor(interceptor.getClazz());
		}
		
		monitor.worked(1); 
	}
	
	
	
	
	private void initializeBindings(Aop aop, IJavaProject project, IProgressMonitor monitor ) {
		for (Iterator iter = AopModelUtils.getBindingsFromAop(aop).iterator(); iter.hasNext(); )
		{
			Binding binding = (Binding) iter.next();
			AopModel.instance().bindNewPointcut (project, binding, monitor);
		}
		
		monitor.worked(1);		
	}
	
	
	
	
	/*
	 * 
	 * BEGINNING UPDATE
	 * 
	 * 
	 */
	
	
	
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
		AopModel model = AopModel.instance();
		ProjectAdvisors advisors = model.getProjectAdvisors (project);
		
		IAopInterceptor interceptors[] = advisors.getInterceptors();
		IAopAspect aspects[] = advisors.getAspects();
		
		AopDescriptor descriptor = AopCorePlugin.getDefault().getDefaultDescriptor(project);
		descriptor.update();
		
		Aop aop = descriptor.getAop();

		updateTypedefs(aop, project, monitor);
		updateIntroductions(aop, project, monitor);

		
		// Rather than completely re-creating the entire internal model, we'll try to be "smart" about changed pointcuts,
		// and added/removed advisors
		
		for (Iterator iter = AopModelUtils.getPointcutsFromAop(aop).iterator(); iter.hasNext(); )
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
			
			
			for (Iterator iter = AopModelUtils.getAspectsFromAop(aop).iterator(); iter.hasNext(); )
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
						model.fireAdvisorRemoved(advised[k], advice[j]);
					}
				}
				
				advisors.removeAspect(aspect);
				AspectManager.instance().removeAspectDefinition(aspect.getType().getFullyQualifiedName());
				
			}
		}
		
		for (Iterator iter = AopModelUtils.getAspectsFromAop(aop).iterator(); iter.hasNext(); )
		{
			Aspect xmlAspect = (Aspect) iter.next();
			if (! advisors.hasAspect(xmlAspect.getClazz()))
			{
				advisors.addAspect(xmlAspect.getClazz());
			}
			if( AspectManager.instance().getAspectDefinition(xmlAspect.getClazz()) == null ) {
				Scope sScope = AopModel.getScopeFromString(xmlAspect.getScope());
				
				AspectDefinition def = new AspectDefinition(xmlAspect.getClazz(), sScope, null);
				try {
					AspectManager.instance().addAspectDefinition(def);
				} catch( Exception e ) {
					
				}

			}
		}
		
		
		/// Loop through the interceptors AT THE TOP LEVEL, find all removed and added, and update the model accordingly
		for (int i = 0; i < interceptors.length; i++)
		{
			boolean found = false;
			
			for (Iterator iter = AopModelUtils.getInterceptorsFromAop(aop).iterator(); iter.hasNext(); )
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
					model.fireAdvisorRemoved (advised[j], interceptors[i]);
				}
				
				advisors.removeInterceptor (interceptors[i]);
				try {
					AspectManager.instance().removeAspectDefinition(interceptors[i].getName());
				} catch( Throwable thrw ) {
				}
			}
		}
		
		for (Iterator iter = AopModelUtils.getInterceptorsFromAop(aop).iterator(); iter.hasNext(); )
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
		
		JDTPointcutExpression expressions[] = model.getProjectPointcuts(project);
		for (int i = 0; i < expressions.length; i++)
		{
			boolean found = false;
			
			for (Iterator iter = AopModelUtils.getBindingsFromAop(aop).iterator(); iter.hasNext(); )
			{
				Binding binding = (Binding) iter.next();
				
				if (expressions[i].getExpr().equals(binding.getPointcut()))
				{
					found = true;
					
					// Rather than breaking, we still need to compare the advisor contents of this pointcut
					IAopAdvisor pointcutAdvisors[] = model.getPointcutAdvisors(project, expressions[i]);
					for (int j = 0; j < pointcutAdvisors.length; j++)
					{
						boolean foundAdvisor = false;
						
						if (pointcutAdvisors[j].getType() == IAopAdvisor.INTERCEPTOR)
						{
							IAopInterceptor pointcutInterceptor = (IAopInterceptor) pointcutAdvisors[j];
							
							for (Iterator iter2 = AopModelUtils.getInterceptorsFromBinding(binding).iterator(); iter2.hasNext(); )
							{
								Interceptor interceptor = (Interceptor) iter2.next();
								if (interceptor.getClazz().equals(pointcutInterceptor.getAdvisingType().getFullyQualifiedName()))
								{
									foundAdvisor = true;
									break;
								}
							}
							
							for (Iterator iter2 = AopModelUtils.getInterceptorRefssFromBinding(binding).iterator(); iter2.hasNext(); )
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
							
							for (Iterator iter2 = AopModelUtils.getAdvicesFromBinding(binding).iterator(); iter2.hasNext(); )
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
								model.fireAdvisorRemoved(advised[a], pointcutAdvisors[j]);
							}
							
							advisors.removeAdvisor (pointcutAdvisors[j]);
						}
					}
				}
				
				// At this point we've identified all "removed" advisors of this pointcut.
				// Now we need to reverse the loop and look for all "added" advisors, and
				// apply them to this pointcut. (and also start firing some advisor added events)
				for (Iterator iter2 = AopModelUtils.getInterceptorRefssFromBinding(binding).iterator(); iter2.hasNext(); )
				{
					InterceptorRef interceptorRef = (InterceptorRef) iter2.next();
					IAopInterceptor interceptor = advisors.findInterceptor(interceptorRef.getName());
					
					if (interceptor != null)
					{
						boolean assignedToThisPointcut = false;
						
						IAopAdvisor pointcutAdvisors[] = model.getPointcutAdvisors(project, expressions[i]);
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
								model.fireAdvisorAdded(pointcutAdvised[j], interceptor);
								interceptor.addAdvised(pointcutAdvised[j]);
							}
							
							interceptor.assignPointcut(expressions[i]);
						}
					}
				}
				
				for (Iterator iter2 = AopModelUtils.getInterceptorsFromBinding(binding).iterator(); iter2.hasNext(); )
				{
					Interceptor xmlInterceptor = (Interceptor) iter2.next();
					IAopAdvisor pointcutAdvisors[] = model.getPointcutAdvisors(project, expressions[i]);
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
							model.fireAdvisorAdded(pointcutAdvised[j], interceptor);
							interceptor.addAdvised(pointcutAdvised[j]);
						}
						
						interceptor.assignPointcut(expressions[i]);
					}
				}
				
				for (Iterator iter2 = AopModelUtils.getAdvicesFromBinding(binding).iterator(); iter2.hasNext(); )
				{
					Advice xmlAdvice = (Advice) iter2.next();
					IAopAdvisor pointcutAdvisors[] = model.getPointcutAdvisors(project, expressions[i]);
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
							model.fireAdvisorAdded(pointcutAdvised[j], advice);
							advice.addAdvised(pointcutAdvised[j]);
						}
						
						advice.assignPointcut(expressions[i]);
					}
				}
			}
			
			if (!found)
			{
				// Pointcut was removed.
				IAopAdvisor pointcutAdvisors[] = model.getPointcutAdvisors(project, expressions[i]);
				IAopAdvised pointcutAdvised[] = expressions[i].getAdvised();
				
				for (int j = 0; j < pointcutAdvisors.length; j++)
				{
					for (int k = 0; k < pointcutAdvised.length; k++)
					{
						model.fireAdvisorRemoved(pointcutAdvised[k], pointcutAdvisors[j]);
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
		
		for (Iterator iter = AopModelUtils.getBindingsFromAop(aop).iterator(); iter.hasNext(); )
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
				model.bindNewPointcut(project, binding, monitor);
			}
		}
	}


	private class PojoTypedef {
		private String name;
		private String expr;
		public PojoTypedef(String name, String expr) {
			this.name = name;
			this.expr = expr;
		}
		public String getName() { return name; }
		public String getExpr() { return expr; }
	}
	
	private void updateTypedefs(Aop aop, IJavaProject project, IProgressMonitor monitor) {
		ArrayList deleted,added;
		deleted = new ArrayList();
		added = new ArrayList();
		
		HashMap jaxbMap = new HashMap();
		HashMap jdtMap = new HashMap();

		
		// Load the map of the new stuff
		List typedefList = AopModelUtils.getTypedefsFromAop(aop);
		for(Iterator i = typedefList.iterator();i.hasNext();){
			Typedef d = (Typedef)i.next();
			jaxbMap.put(d.getName(), d);
		}
		
		/*
		// Load the map of the old stuff (from aspectmanager)
		Iterator j = AspectManager.instance().getTypedefs().keySet().iterator();
		while(j.hasNext()) {
			String key = (String)j.next();
			jdtMap.put(key, AspectManager.instance().getTypedef(key));
		}
		*/
		
		/* Do it from the projectAdvisors instead. */
		ProjectAdvisors advisors = AopModel.instance().getProjectAdvisors(project);
		AopTypedef[] aopTypedefs = advisors.getTypedefs();
		for( int i = 0; i < aopTypedefs.length; i++ ) {
			JDTTypedefExpression jdtExpr = aopTypedefs[i].getTypedef();
			jdtMap.put(jdtExpr.getName(), jdtExpr);
		}
		
		
		// Both maps are loaded. Now we'll go through both and see what we find.
		Iterator jaxbKeyIterator = jaxbMap.keySet().iterator();
		while(jaxbKeyIterator.hasNext()) {
			String key = (String)jaxbKeyIterator.next();
			Typedef jaxbTypedef = (Typedef)jaxbMap.get(key);
			JDTTypedefExpression jdtTypedef = (JDTTypedefExpression)jdtMap.get(key);

			jaxbKeyIterator.remove();

			if( jdtTypedef == null ) {
				// this jaxb one is new.
				try {
					JDTTypedefExpression newTypedef = new JDTTypedefExpression(
							new TypedefExpression(jaxbTypedef.getName(), jaxbTypedef.getExpr()));
					
					added.add(newTypedef);
				} catch( Exception e ) {					
				}
			} else {
				// the key is present in both. But do the expressions match?
				if( jdtTypedef.getExpr().equals(jaxbTypedef.getExpr())) {
					// they match. Remove them from both maps.
					jdtMap.remove(key);
				} else {
					// They do NOT match. jaxb is added, jdt is removed.
					try {
						JDTTypedefExpression newTypedef = new JDTTypedefExpression(
								new TypedefExpression(jaxbTypedef.getName(), jaxbTypedef.getExpr()));
	
						added.add(newTypedef);
						deleted.add(jdtTypedef);
						
						jdtMap.remove(key);
					} catch( Exception e ) {
						System.out.println("DEAD");
					}
				}
			}
			
		}
		
		
		// Whatever's left has been removed.
		Iterator i = jdtMap.keySet().iterator();
		while(i.hasNext()) {
			String key = (String)i.next();
			JDTTypedefExpression del = (JDTTypedefExpression)jdtMap.get(key);
			deleted.add(del);
		}
		
		// Tell the model to fire the actions
		Iterator deletedIter = deleted.iterator();
		while(deletedIter.hasNext()) {
			JDTTypedefExpression del = (JDTTypedefExpression)deletedIter.next();
			AopModel.instance().removeTypedef(project, del);
		}
		
		Iterator addedIter = added.iterator();
		while(addedIter.hasNext()) {
			JDTTypedefExpression add = (JDTTypedefExpression)addedIter.next();
			AopModel.instance().addNewTypedef(project,add);
		}

	}
	
	private void updateIntroductions(Aop aop, IJavaProject project, IProgressMonitor monitor) {
		ArrayList deleted,added;
		deleted = new ArrayList();
		added = new ArrayList();
		
		HashMap jaxbMap = new HashMap();
		HashMap jdtMap = new HashMap();

		
		// Load the map of the new stuff
		List introductionList = AopModelUtils.getIntroductionsFromAop(aop);
		for(Iterator i = introductionList.iterator();i.hasNext();){
			Introduction in = (Introduction)i.next();
			String key = in.getClazz() != null ? in.getClazz() : in.getExpr();
			jaxbMap.put(key, in);
		}
		
		
		/* Do it from the projectAdvisors instead. */
		ProjectAdvisors advisors = AopModel.instance().getProjectAdvisors(project);
		AopInterfaceIntroduction[] introductions = advisors.getIntroductions();
		for( int i = 0; i < introductions.length; i++ ) {
			JDTInterfaceIntroduction jdtInter = introductions[i].getIntroduction();
			jdtMap.put(jdtInter.getName(), jdtInter);
		}

		
		// Both maps are loaded. Now we'll go through both and see what we find.
		Iterator jaxbKeyIterator = jaxbMap.keySet().iterator();
		while(jaxbKeyIterator.hasNext()) {
			String key = (String)jaxbKeyIterator.next();
			Introduction jaxbIntro = (Introduction)jaxbMap.get(key);
			JDTInterfaceIntroduction jdtIntro = (JDTInterfaceIntroduction)jdtMap.get(key);

			jaxbKeyIterator.remove();
			
			if( jdtIntro == null ) {
				// this jaxb one is new.
				try {
					JDTInterfaceIntroduction newJDTIntro = 
						AopModelUtils.toJDT(jaxbIntro);
					added.add(newJDTIntro);
				} catch( Exception e ) {					
				}
			} else {
				// the key is present in both. But do the expressions match?
				if( true ) {
					// they match. Remove them from both maps.
					jdtMap.remove(key);
				} else {
					// They do NOT match. jaxb is added, jdt is removed.
					try {
						JDTInterfaceIntroduction newJDTIntro = 
							AopModelUtils.toJDT(jaxbIntro);
						added.add(newJDTIntro);
						deleted.add(jdtIntro);
	
						jdtMap.remove(key);
					} catch( Exception e ) {
						System.out.println("DEAD");
					}
				}
				
			}
			
		}
		
		// Whatever's left has been removed.
		Iterator i = jdtMap.keySet().iterator();
		while(i.hasNext()) {
			String key = (String)i.next();
			JDTInterfaceIntroduction del = (JDTInterfaceIntroduction)jdtMap.get(key);
			deleted.add(del);
		}
		
		// Tell the model to fire the actions
		Iterator deletedIter = deleted.iterator();
		while(deletedIter.hasNext()) {
			JDTInterfaceIntroduction del = (JDTInterfaceIntroduction)deletedIter.next();
			AopModel.instance().removeInterfaceIntroduction(project, del);
		}
		
		Iterator addedIter = added.iterator();
		while(addedIter.hasNext()) {
			JDTInterfaceIntroduction add = (JDTInterfaceIntroduction)addedIter.next();
			AopModel.instance().addInterfaceIntroduction(project,add);
		}

	}

		
}
