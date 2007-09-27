/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.ui.views.providers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Advice;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aop;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aspect;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Binding;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Interceptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.InterceptorRef;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Pointcut;

/**
 * @author Marshall
 */
public class AspectManagerContentProvider implements IStructuredContentProvider, ITreeContentProvider {
	private AopDescriptor descriptor;
	public static Object[] EMPTY_ARRAY = new Object[0];
	public static final AspectManagerContentProviderTypeWrapper ASPECTS = 
		new AspectManagerContentProviderTypeWrapper("_ASPECTS_");
	public static final AspectManagerContentProviderTypeWrapper BINDINGS = 
		new AspectManagerContentProviderTypeWrapper("_BINDINGS_");
	public static final AspectManagerContentProviderTypeWrapper INTERCEPTORS = 
		new AspectManagerContentProviderTypeWrapper("_INTERCEPTORS_");
	public static final AspectManagerContentProviderTypeWrapper POINTCUTS = 
		new AspectManagerContentProviderTypeWrapper("_POINTCUTS_");
	
	public static class AspectManagerContentProviderTypeWrapper {
		private String type;
		public AspectManagerContentProviderTypeWrapper( String type ) {
			this.type = type;
		}
	}
	
	public Object[] getChildren(Object parentElement) {
		
		if (parentElement instanceof AopDescriptor)
		{
 			AopDescriptor descriptor = (AopDescriptor) parentElement;
 			Aop aop = descriptor.getAop();
 			
 			ArrayList children = new ArrayList();
 			
 			if (aop.getAspects().size() > 0)
 				children.add(aop.getAspects());
 			else 
 				children.add(AspectManagerContentProvider.ASPECTS);
 			
 			if (aop.getBindings().size() > 0)
 				children.add(aop.getBindings());
 			else
 				children.add(AspectManagerContentProvider.BINDINGS);
 			
 			if (aop.getInterceptors().size() > 0)
 				children.add(aop.getInterceptors());
 			else
 				children.add(AspectManagerContentProvider.INTERCEPTORS);
 			
 			if (aop.getPointcuts().size() > 0)
 				children.add(aop.getPointcuts());
 			else
 				children.add(AspectManagerContentProvider.POINTCUTS);
 			
 			
 			return children.toArray();
		}
		else if (parentElement instanceof List)
		{
			return ((List) parentElement).toArray();
		}
		else if (parentElement instanceof Binding)
		{
			Binding binding = (Binding) parentElement;
			ArrayList children = new ArrayList();
			
			if (binding.getAdvised().size() > 0)
				children.addAll(binding.getAdvised());
			if (binding.getInterceptorRefs().size() > 0)
				children.addAll(binding.getInterceptorRefs());
			if (binding.getInterceptors().size() > 0)
				children.addAll(binding.getInterceptors());
			
			return children.toArray();
		}
		
		return EMPTY_ARRAY;
	}
	
	public Object getParent(Object possibleChild) {
		
		// ugh, why does they need to see the data from both views?
		final Object child = possibleChild;
		if (child instanceof Advice)
		{
			return visitList(descriptor.getAop().getBindings(), new ListVisitor() {
				public Object visit(Object element) {
					if (((Binding)element).getAdvised().contains(child)) return element;
					return null;
				}
			});
		}
		else if (child instanceof InterceptorRef)
		{
			return visitList(descriptor.getAop().getBindings(), new ListVisitor() {
				public Object visit(Object element) {
					if (((Binding)element).getInterceptorRefs().contains(child)) return element;
					return null;
				}
			});
		}
		else if (child instanceof Interceptor)
		{
			Object result = visitList(descriptor.getAop().getBindings(), new ListVisitor() {
				public Object visit(Object element) {
					if (((Binding)element).getInterceptors().contains(child)) return element;
					return null;
				}
			});
			
			if (result == null)
			{
				return visitList(descriptor.getAop().getInterceptors(), new ListVisitor() {
					public Object visit(Object element) {
						if (((Interceptor)element).equals(child)) return descriptor;
						return null;
					}
				});
			} else return result;
		}
		else if (child instanceof String)
		{
			return visitList(descriptor.getAop().getBindings(), new ListVisitor() {
				public Object visit(Object element) {
					if (((Binding)element).getPointcut().equals(child)) return element;
					return null;
				}
			});
		}
		else if (child instanceof Binding)
		{
			return descriptor.getAop().getBindings();
		}
		else if (child instanceof Aspect)
		{
			return descriptor.getAop().getAspects();
		}
		else if (child instanceof Pointcut)
		{
			return descriptor.getAop().getPointcuts();
		}
		
		return null;
	}
	
	// the first time something returns non null, the visit loop stops
	private Object visitList (List list, ListVisitor visitor)
	{
		if (list != null)
		{
			Iterator iter = list.iterator();
			while (iter.hasNext())
			{
				Object result = visitor.visit(iter.next());
				if (result != null) return result;
			}	
		}
		return EMPTY_ARRAY;
	}
	
	private interface ListVisitor
	{
		public Object visit(Object element);
	}

	public boolean hasChildren(Object element) {
        if (element instanceof AopDescriptor)
            return true;
        else if (element instanceof List) {
            return !((List) element).isEmpty();
        } else if (element instanceof Binding)
            return true;
        return false;
	}
	
	public Object[] getElements(Object inputElement) {	return getChildren(inputElement); }

	public void dispose() {
	    descriptor = null;
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {	}

	public AopDescriptor getDescriptor() {
		return descriptor;
	}
	public void setDescriptor(AopDescriptor descriptor) {
		this.descriptor = descriptor;
	}
}
