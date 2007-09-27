/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.aop.AspectManager;
import org.jboss.aop.AspectXmlLoader;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.advice.Scope;
import org.jboss.aop.introduction.InterfaceIntroduction;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.AOPType;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Advice;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Annotation;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.AnnotationIntroduction;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aop;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aspect;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Binding;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.CFlowStack;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.DynamicCFlow;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Interceptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.InterceptorRef;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Introduction;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.MetaDataLoader;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.PluggablePointcut;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Pointcut;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Stack;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Typedef;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.AOPType.Metadata;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.AOPType.Prepare;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModel;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModelUtils;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTInterfaceIntroduction;
import org.jboss.ide.eclipse.jdt.aop.core.util.JaxbAopUtil;

/**
 * This class represents an aop descriptor file, and it's 
 * corresponding jaxb implementation.
 * 
 * It is responsible for adding and removing elements from both
 * the jaxb object and the underlying file.
 * 
 * @author Marshall
 */
public class AopDescriptor {
	
	private Aop aop;
	private File file;
	
	/*
	 * This 'dirty' isn't a real check for a dirty buffer.
	 * It will only be used to make sure an unmarshall isn't
	 * performed right after a marshall (ie a load right after a save)
	 * 
	 * After any save, it is bound to happen that updateModel is called.
	 * Calling updateModel should not re-load, but  
	 * should then set dirty to true so further reloads work as planned.
	 */
	private boolean dirty;
	
	public AopDescriptor() {
		this.dirty = true;
	}
	
	/*
	 * Simple getters and setters section
	 */
	
	/**
	 * @return Returns the aop.
	 */
	public Aop getAop() {
		return aop;
	}
	/**
	 * @param aop The aop to set.
	 */
	public void setAop(Aop aop) {
		this.aop = aop;
	}
	/**
	 * @return Returns the file.
	 */
	public File getFile() {
		return file;
	}
	/**
	 * @param file The file to set.
	 */
	public void setFile(File file) {
		this.file = file;
	}
	

	
	
	
	/**
	 * Returns true if the files are equal, as determined
	 * by equalsFile(File)
	 */
	
	public boolean equals (Object other)
	{
		if (other instanceof AopDescriptor)
		{
			AopDescriptor otherDescriptor = (AopDescriptor) other;
			return equalsFile(otherDescriptor.getFile());
		}
		else if (other instanceof File)
		{
			return equalsFile((File) other);
		}
		return false;
	}
	
	private boolean equalsFile(File other)
	{
		if (other == null || getFile() == null)
			return false;
		
		if (other.getPath() == null || getFile().getPath() == null)
			return false;
		
		return other.getPath().equals(getFile().getPath());
	}
	
	public Binding findBinding (String pointcut)
	{
		List binds = AopModelUtils.getBindingsFromAop(getAop()); 
		Iterator bIter = binds.iterator();
		while (bIter.hasNext())
		{
			Binding binding = (Binding) bIter.next();
			if (binding.getPointcut() != null)
			{
				if (binding.getPointcut().equals(pointcut)) return binding;
			}
		}
		
		try {
			// No binding found -- create a new one and return it
			AOPType.Bind binding = JaxbAopUtil.instance().getFactory().createAOPTypeBind();
			binding.setPointcut(pointcut);
			getAop().getTopLevelElements().add(binding);
			
			return binding;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private Aspect findAspect (String className)
	{
		return findAspect(className, "PER_VM");
	}
	
	private Aspect findAspect (String className, String scope)
	{
		List aspects = AopModelUtils.getAspectsFromAop(getAop()); 
		Iterator aIter = aspects.iterator();
		while (aIter.hasNext())
		{
			Aspect aspect = (Aspect) aIter.next();
			if (aspect.getClazz() != null)
			{
				if (aspect.getClazz().equals(className)) {
					return aspect;
				}
			}
		}
		
		try {
			// No aspect found, create new one and return it
			Aspect aspect = JaxbAopUtil.instance().getFactory().createAOPTypeAspect();
			aspect.setClazz(className);
			aspect.setScope(scope);
			getAop().getTopLevelElements().add(aspect);
			
			// add it to the underlying model as well.
			Scope sScope = AopModel.getScopeFromString(aspect.getScope());
			
			AspectDefinition def = new AspectDefinition(aspect.getClazz(), sScope, null);
			try {
				AspectManager.instance().addAspectDefinition(def);
			} catch( Exception e ) {
				
			}
			return aspect;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Interceptor bindInterceptor (String pointcut, String className)
	{
		try {
			Binding binding = findBinding(pointcut);
			Interceptor interceptor = JaxbAopUtil.instance().getFactory().createBindingInterceptor();
			interceptor.setClazz(className);
			binding.getElements().add(interceptor);
			return interceptor;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void bindInterceptorRef (String pointcut, String name)
	{
		try {
			Binding binding = findBinding(pointcut);
			InterceptorRef interceptorRef = JaxbAopUtil.instance().getFactory().createBindingInterceptorRef();
			interceptorRef.setName(name);
			
			binding.getElements().add(interceptorRef);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public Advice bindAdvice (String pointcut, String aspectClass, String adviceName)
	{
		try {
			Binding binding = findBinding(pointcut);
			Aspect aspect = findAspect(aspectClass);
			Advice advice = JaxbAopUtil.instance().getFactory().createBindingAdvice();
			advice.setAspect(aspect.getClazz());
			advice.setName(adviceName);
			
			binding.getElements().add(advice);
			return advice;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void addAspect(String className)
	{
		addAspect(className, Scope.PER_VM);
	}
	
	public void addAspect(String className, Scope scope)
	{
		try {
			Aspect aspect = JaxbAopUtil.instance().getFactory().createAOPTypeAspect();
			aspect.setClazz(className);
			aspect.setScope(scope.name());
			getAop().getTopLevelElements().add(aspect);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public void addPointcut(String name, String expr)
	{
		try {
			Pointcut pointcut = JaxbAopUtil.instance().getFactory().createAOPTypePointcut();
			pointcut.setName(name);
			pointcut.setExpr(expr);
			
			getAop().getTopLevelElements().add(pointcut);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	

	public void addInterfaceIntroduction(JDTInterfaceIntroduction intro) {
		Introduction jaxbIntro = AopModelUtils.toJaxb(intro);
		getAop().getTopLevelElements().add(jaxbIntro);
	}
	
	private List getParent (Interceptor interceptor)
	{
		List interceptors = AopModelUtils.getInterceptorsFromAop(getAop());
		if ( interceptors.contains((interceptor)))
				return interceptors;
		else {
			Iterator bIter = AopModelUtils.getBindingsFromAop(getAop()).iterator();
			while (bIter.hasNext())
			{
				Binding binding = (Binding) bIter.next();
				List bindInterceptors = AopModelUtils.getInterceptorsFromBinding(binding);
				if (bindInterceptors.contains(interceptor))
				{
					return binding.getElements();
				}
			}
		}
		
		return null;
	}
	
	private List getParent (Advice advice)
	{
		Iterator bIter = AopModelUtils.getBindingsFromAop(getAop()).iterator();
		while (bIter.hasNext())
		{
			Binding binding = (Binding) bIter.next();
			List advised = AopModelUtils.getAdvicesFromBinding(binding);
			if (advised.contains(advice))
			{
				return binding.getElements();
			}
		}
		return null;
	}
	
	
	private List getParent (InterceptorRef interceptorRef)
	{
		Iterator bIter = AopModelUtils.getBindingsFromAop(getAop()).iterator();
		while (bIter.hasNext())
		{
			Binding binding = (Binding) bIter.next();
			List referenceList = AopModelUtils.getInterceptorRefssFromBinding(binding);
			if (referenceList.contains(interceptorRef))
			{
				return binding.getElements();
			}
		}
		return null;
	}
	
	/**
	 * Used only with a parameter that can only be found as an element
	 * of the top level of the tree.
	 * 
	 * Interceptors, interceptorrefs, etc, are not applicable.
	 * They can be found under AOP or under BINDING.
	 * @param o
	 * @return
	 */
	private List getTopLevelParent(Object o) {
		if( getAop().getTopLevelElements().contains(o))
			return getAop().getTopLevelElements();
		return null;
	}
	
	
	/**
	 * This method removes an object from its parent in the descriptor file
	 * and the descriptor's jaxb implementation.
	 * 
	 * IT DOES NOT DELETE IT FROM THE REST OF THE MODEL!!!
	 * 
	 * A call to updateModel, in AopModel, will most likely 
	 * do the further evaluations and determine it should be
	 * removed from the model, but AopModel.updateModel is a 
	 * fairly long running process.
	 * 
	 * 
	 * @param object
	 */
	public void remove (Object object)
	{
		List parent = null;
		if (object instanceof Interceptor)
			parent = getParent ((Interceptor) object);
		else if (object instanceof Advice)
			parent = getParent ((Advice) object);
		else if (object instanceof InterceptorRef)
			parent = getParent ((InterceptorRef) object);
		else if (object instanceof Binding) {
			parent = getTopLevelParent((Binding)object);
		} else if (object instanceof Pointcut) {
			parent = getTopLevelParent((Pointcut)object);
		} else if (object instanceof Typedef) {
			parent = getTopLevelParent((Typedef)object);
		} else if( object instanceof Introduction ) {
			parent = getTopLevelParent((Introduction)object);
		}
		
		if (parent != null)
		{
			parent.remove(object);
		}
	}
	
	
	/**
	 * Unmarshalls aop directly from file, overwriting 
	 * any unsaved changes.
	 *
	 */
	public void update ()
	{
		if( this.dirty ) {
			System.out.println("[aop-descriptor] - updating...");
			this.aop = JaxbAopUtil.instance().unmarshal(getFile());
		} else {
			System.out.println("[aop-descriptor] - NOT updating...");
			this.dirty = true;			
		}
	}
	
	/**
	 * Saves the live aop to a file. 
	 */
	
	public void save ()
	{
		this.dirty = false;
		sortAop();
		JaxbAopUtil.instance().marshal(aop, file);
		
		try {
			System.out.println("[aop-descriptor] re-deploying descriptor..");
			AspectXmlLoader.deployXML(getFile().toURL());
			
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IWorkspaceRoot.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sortAop() {
		List elements = getAop().getTopLevelElements();
		Collections.sort(elements, new Comparator() {
			
			public int compare(Object arg0, Object arg1) {
				return getIntVal(arg0) - getIntVal(arg1);
			} 
			
			private int getIntVal(Object arg) {
				if( arg instanceof Typedef ) return 0;
				if( arg instanceof Pointcut ) return 1;
				if( arg instanceof Interceptor ) return 2;
				if( arg instanceof Aspect ) return 3;
				if( arg instanceof Binding ) return 4;
				if( arg instanceof Introduction ) return 5;
				if( arg instanceof MetaDataLoader) return 6;
				if( arg instanceof Metadata ) return 7;
				if( arg instanceof Stack ) return 8;
				if( arg instanceof PluggablePointcut ) return 9;
				if( arg instanceof Prepare ) return 10;
				if( arg instanceof CFlowStack ) return 11;
				if( arg instanceof DynamicCFlow ) return 12;
				if( arg instanceof AnnotationIntroduction ) return 13;
				if( arg instanceof Annotation ) return 14;
				return -1;
			}
		});
	}

}
