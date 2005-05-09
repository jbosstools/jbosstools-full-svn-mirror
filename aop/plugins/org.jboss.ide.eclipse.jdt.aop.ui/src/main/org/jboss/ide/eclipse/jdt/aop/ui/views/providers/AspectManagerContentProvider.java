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
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModelUtils;

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
	public static final AspectManagerContentProviderTypeWrapper TYPEDEFS = 
		new AspectManagerContentProviderTypeWrapper("_TYPEDEFS_");
	
	public static class AspectManagerContentProviderTypeWrapper {
		private String type;
		public AspectManagerContentProviderTypeWrapper( String type ) {
			this.type = type;
		}
	}
	
	public Object[] getChildren(Object parentElement) {
		
		List tmp;
		
		if (parentElement instanceof AopDescriptor)
		{
 			AopDescriptor descriptor = (AopDescriptor) parentElement;
 			Aop aop = descriptor.getAop();
 			
 			ArrayList children = new ArrayList();
 			
 			
			/*
			 * This section will designate a number of elements as children.
			 * If aspects exist, an arraylist of the aspects will be added.
			 * If no aspects exist, a constant will be added as a child.
			 */
 			tmp = AopModelUtils.getAspectsFromAop(aop);
			tmp.add(0, AspectManagerContentProvider.ASPECTS);
 			children.add(tmp);
 			
			
 			tmp = AopModelUtils.getBindingsFromAop(aop);
 			tmp.add(0,AspectManagerContentProvider.BINDINGS);
			children.add(tmp);
 			
 			tmp = AopModelUtils.getInterceptorsFromAop(aop);
			tmp.add(0,AspectManagerContentProvider.INTERCEPTORS);
			children.add(tmp);
 			
			tmp = AopModelUtils.getPointcutsFromAop(aop);
			tmp.add(0,AspectManagerContentProvider.POINTCUTS);
			children.add(tmp);
 			
			tmp = AopModelUtils.getTypedefsFromAop(aop);
 			tmp.add(0,AspectManagerContentProvider.TYPEDEFS);
			children.add(tmp);
			 			
 			
 			return children.toArray();
		}
		else if (parentElement instanceof List)
		{
			// if the parent is a list, then the children are the 
			// elements in the list. 
			return ((List) parentElement).subList(1, ((List)parentElement).size()).toArray();
		}
		else if (parentElement instanceof Binding)
		{
			Binding binding = (Binding) parentElement;
			ArrayList children = new ArrayList();
			
			tmp = AopModelUtils.getAdvicesFromBinding(binding);
			if (tmp.size() > 0)
				children.addAll(tmp);

			tmp = AopModelUtils.getInterceptorRefssFromBinding(binding);
			if (tmp.size() > 0)
				children.addAll(tmp);

			tmp = AopModelUtils.getInterceptorsFromBinding(binding);
			if (tmp.size() > 0)
				children.addAll(tmp);

			return children.toArray();
		}
		
		return EMPTY_ARRAY;
	}
	
	public Object getParent(Object possibleChild) {
		
		// ugh, why does they need to see the data from both views?
		final Object child = possibleChild;
		if (child instanceof Advice)
		{
			return visitList( AopModelUtils.getBindingsFromAop(descriptor.getAop()), 
					new ListVisitor() {
				public Object visit(Object element) {
					if (((Binding)element).getElements().contains(child)) return element;
					return null;
				}
			});
		}
		else if (child instanceof InterceptorRef)
		{
			return visitList( AopModelUtils.getBindingsFromAop(descriptor.getAop()), 
					new ListVisitor() {
				public Object visit(Object element) {
					if (((Binding)element).getElements().contains(child)) return element;
					return null;
				}
			});
		}
		
		
		else if (child instanceof Interceptor)
		{
			Object result = visitList(AopModelUtils.getBindingsFromAop(descriptor.getAop()), new ListVisitor() {
				public Object visit(Object element) {
					if (((Binding)element).getElements().contains(child)) return element;
					return null;
				}
			});
			
			if (result == null)
			{
				return visitList(AopModelUtils.getInterceptorsFromAop(descriptor.getAop()), new ListVisitor() {
					public Object visit(Object element) {
						if (((Interceptor)element).equals(child)) return descriptor;
						return null;
					}
				});
			} else return result;
		} 
		else if (child instanceof String)
		{
			return visitList(AopModelUtils.getBindingsFromAop(descriptor.getAop()), new ListVisitor() {
				public Object visit(Object element) {
					if (((Binding)element).getPointcut().equals(child)) return element;
					return null;
				}
			});
		}
		else if (child instanceof Binding)
		{
			return AopModelUtils.getBindingsFromAop(descriptor.getAop());
		}
		else if (child instanceof Aspect)
		{
			return AopModelUtils.getAspectsFromAop(descriptor.getAop());
		}
		else if (child instanceof Pointcut)
		{
			return AopModelUtils.getPointcutsFromAop(descriptor.getAop());
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
