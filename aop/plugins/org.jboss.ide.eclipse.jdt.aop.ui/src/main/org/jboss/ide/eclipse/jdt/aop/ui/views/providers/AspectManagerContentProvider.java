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
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModel;

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
		
		List tmp;
		
		if (parentElement instanceof AopDescriptor)
		{
 			AopDescriptor descriptor = (AopDescriptor) parentElement;
 			Aop aop = descriptor.getAop();
 			
 			ArrayList children = new ArrayList();
 			
 			
 			tmp = AopModel.getTypeFromAop(Aspect.class, aop);
 			if (tmp.size() > 0)
 				children.add(tmp);
 			else 
 				children.add(AspectManagerContentProvider.ASPECTS);
 			
 			tmp = AopModel.getTypeFromAop(Binding.class, aop);
 			if (tmp.size() > 0)
 				children.add(tmp);
 			else
 				children.add(AspectManagerContentProvider.BINDINGS);
 			
 			tmp = AopModel.getTypeFromAop(Interceptor.class, aop);
 			if (tmp.size() > 0)
 				children.add(tmp);
 			else
 				children.add(AspectManagerContentProvider.INTERCEPTORS);
 			
 			tmp = AopModel.getTypeFromAop(Pointcut.class, aop);
 			if (tmp.size() > 0)
 				children.add(tmp);
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
			
			tmp = AopModel.getFromBinding(Advice.class, binding);
			if (tmp.size() > 0)
				children.addAll(tmp);

			tmp = AopModel.getFromBinding(InterceptorRef.class, binding);
			if (tmp.size() > 0)
				children.addAll(tmp);

			tmp = AopModel.getFromBinding(Interceptor.class, binding);
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
			return visitList( AopModel.getTypeFromAop(Binding.class, descriptor.getAop()), 
					new ListVisitor() {
				public Object visit(Object element) {
					if (((Binding)element).getElements().contains(child)) return element;
					return null;
				}
			});
		}
		else if (child instanceof InterceptorRef)
		{
			return visitList( AopModel.getTypeFromAop(Binding.class, descriptor.getAop()), 
					new ListVisitor() {
				public Object visit(Object element) {
					if (((Binding)element).getElements().contains(child)) return element;
					return null;
				}
			});
		}
		
		
		else if (child instanceof Interceptor)
		{
			Object result = visitList(AopModel.getTypeFromAop(Binding.class, descriptor.getAop()), new ListVisitor() {
				public Object visit(Object element) {
					if (((Binding)element).getElements().contains(child)) return element;
					return null;
				}
			});
			
			if (result == null)
			{
				return visitList(AopModel.getTypeFromAop(Interceptor.class, descriptor.getAop()), new ListVisitor() {
					public Object visit(Object element) {
						if (((Interceptor)element).equals(child)) return descriptor;
						return null;
					}
				});
			} else return result;
		} 
		else if (child instanceof String)
		{
			return visitList(AopModel.getTypeFromAop(Binding.class, descriptor.getAop()), new ListVisitor() {
				public Object visit(Object element) {
					if (((Binding)element).getPointcut().equals(child)) return element;
					return null;
				}
			});
		}
		else if (child instanceof Binding)
		{
			return AopModel.getTypeFromAop(Binding.class, descriptor.getAop());
		}
		else if (child instanceof Aspect)
		{
			return AopModel.getTypeFromAop(Aspect.class, descriptor.getAop());
		}
		else if (child instanceof Pointcut)
		{
			return AopModel.getTypeFromAop(Pointcut.class, descriptor.getAop());
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
