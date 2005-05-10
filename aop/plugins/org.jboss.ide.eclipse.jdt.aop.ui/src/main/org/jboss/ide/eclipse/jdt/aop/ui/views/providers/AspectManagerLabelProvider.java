/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.ui.views.providers;

import java.util.List;

import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Advice;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aspect;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Binding;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Interceptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.InterceptorRef;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Pointcut;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Typedef;
import org.jboss.ide.eclipse.jdt.aop.ui.AopSharedImages;

/**
 * @author Marshall
 */
public class AspectManagerLabelProvider extends LabelProvider {
	
	public String getText(Object obj)
	{
		if (obj instanceof AopDescriptor)
		{
			return "Aspect Manager";
		}
		else if (obj instanceof List)
		{
			List list = (List) obj;
			
			
			Object firstElement = list.get(0);
			// TODO: Is this right? What about Advice and InterceptorRef ?
			if( firstElement == AspectManagerContentProvider.ASPECTS ) return "Aspects";
			else if( firstElement == AspectManagerContentProvider.BINDINGS ) return "Bindings";
			else if( firstElement == AspectManagerContentProvider.INTERCEPTORS ) return "Interceptors";
			else if( firstElement == AspectManagerContentProvider.POINTCUTS ) return "Pointcuts";
			else if( firstElement == AspectManagerContentProvider.TYPEDEFS ) return "Typedefs";
			
		}
		else if (obj instanceof Aspect)
		{
			Aspect aspect = (Aspect)obj;
			return aspect.getClazz() + " : " + aspect.getScope();
		}
		else if (obj instanceof Binding)
		{
			Binding binding = (Binding)obj;
			return binding.getPointcut();
		}
		else if (obj instanceof Interceptor)
		{
			Interceptor interceptor = (Interceptor)obj;
			return 
				interceptor.getName() == null ?
				interceptor.getClazz() : interceptor.getName() + " : " + interceptor.getClazz();
		}
		else if (obj instanceof Pointcut)
		{
			Pointcut pointcut = (Pointcut)obj;
			return pointcut.getName() + " : " + pointcut.getExpr();
		}
		else if (obj instanceof Advice)
		{
			Advice advice = (Advice) obj;
			return advice.getName() + " : " + advice.getAspect();
		}
		else if (obj instanceof InterceptorRef)
		{
			InterceptorRef ref = (InterceptorRef) obj;
			return ref.getName();
		}
		else if( obj instanceof Typedef ) 
		{
			Typedef typedef = (Typedef)obj;
			return typedef.getName() + " : " + typedef.getExpr();
		}
		return obj.getClass().toString();
	}
	
	private static final Image folderImage = PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FOLDER);
	public Image getImage(Object obj) {
		
		if (obj instanceof AopDescriptor)
		{
			return folderImage;
		}
		else if (obj instanceof List)
		{
			List list = (List)obj;
			Object firstElement = list.get(0);
			if( firstElement == AspectManagerContentProvider.ASPECTS ) return AopSharedImages.getImage(AopSharedImages.IMG_ASPECT);
			else if( firstElement == AspectManagerContentProvider.BINDINGS ) return AopSharedImages.getImage(AopSharedImages.IMG_BINDING);
			else if( firstElement == AspectManagerContentProvider.INTERCEPTORS ) return AopSharedImages.getImage(AopSharedImages.IMG_INTERCEPTOR);
			else if( firstElement == AspectManagerContentProvider.POINTCUTS ) return AopSharedImages.getImage(AopSharedImages.IMG_POINTCUT);
			else if( firstElement == AspectManagerContentProvider.TYPEDEFS ) return AopSharedImages.getImage(AopSharedImages.IMG_TYPEDEF);
			
		}
		else if (obj instanceof Aspect)
		{
			return AopSharedImages.getImage(AopSharedImages.IMG_ASPECT);
		}
		else if (obj instanceof Binding)
		{
			return AopSharedImages.getImage(AopSharedImages.IMG_BINDING);
		}
		else if (obj instanceof Interceptor)
		{
			return AopSharedImages.getImage(AopSharedImages.IMG_INTERCEPTOR);
		}
		else if (obj instanceof Pointcut)
		{
			return AopSharedImages.getImage(AopSharedImages.IMG_POINTCUT);
		}
		else if (obj instanceof Advice)
		{
			return AopSharedImages.getImage(AopSharedImages.IMG_ADVICE);
		}
		else if (obj instanceof InterceptorRef)
		{
			return AopSharedImages.getImage(AopSharedImages.IMG_INTERCEPTOR);
		}
		else if (obj instanceof Typedef)
		{
			return AopSharedImages.getImage(AopSharedImages.IMG_TYPEDEF);
		}
		return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_DEFAULT);
	}
}
