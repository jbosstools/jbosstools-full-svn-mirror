/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.core;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.aop.AspectXmlLoader;
import org.jboss.aop.advice.Scope;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.AOPType;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Advice;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aop;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aspect;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Binding;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Interceptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.InterceptorRef;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Pointcut;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Typedef;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModelUtils;
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
		JaxbAopUtil.instance().marshal(aop, file);
		
		try {
			System.out.println("[aop-descriptor] re-deploying descriptor..");
			AspectXmlLoader.deployXML(getFile().toURL());
			
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IWorkspaceRoot.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
