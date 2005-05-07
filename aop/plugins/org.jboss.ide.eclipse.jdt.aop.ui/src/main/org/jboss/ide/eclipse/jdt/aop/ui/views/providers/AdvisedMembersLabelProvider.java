/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.ui.views.providers;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.ui.viewsupport.JavaUILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvice;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopInterceptor;
import org.jboss.ide.eclipse.jdt.aop.ui.AopSharedImages;
import org.jboss.ide.eclipse.jdt.aop.ui.AopUiPlugin;

/**
 * @author Marshall
 */
public class AdvisedMembersLabelProvider extends LabelProvider {

	private JavaUILabelProvider javaUILabelProviderDelegate;
	
	public AdvisedMembersLabelProvider ()
	{
		javaUILabelProviderDelegate = new JavaUILabelProvider();
	}
	
	public Image getImage(Object element) {
		if (element instanceof IAopAdvice) {
			return AopSharedImages.getImage(AopSharedImages.IMG_ADVICE_24);
		}
		else if (element instanceof IAopInterceptor) {
			return AopSharedImages.getImage(AopSharedImages.IMG_INTERCEPTOR_24);
		}
		
		else if (element instanceof IJavaElement) {
			
			// For some reason, when we try to pass the image handling off to the delegate
			// our icons are stretched and look ugly.. 
			//return AopUiPlugin.getDefault().getTreeImage((IJavaElement)element);
			return javaUILabelProviderDelegate.getImage(element);
		}
		
		else if (element.equals(AdvisedMembersContentProvider.NO_ADVISED_CHILDREN[0]))
		{
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK);
		}
		
		return null;
	}
	
	public String getText(Object element) {
		
		if (element instanceof IAopAdvice)
		{
			IAopAdvice advice = (IAopAdvice) element;
			
			return advice.getAdvisingMethod().getDeclaringType().getElementName()
				+ "." + javaUILabelProviderDelegate.getText(advice.getAdvisingElement());
		}
		else if (element instanceof IAopInterceptor) 
		{
			IAopInterceptor interceptor = (IAopInterceptor) element;
			
			return javaUILabelProviderDelegate.getText(interceptor.getAdvisingElement());
		}
		else if (element.equals(AdvisedMembersContentProvider.NO_ADVISED_CHILDREN[0]))
		{
		    return "No Advised Members.";
		}
		
		if( element instanceof IType ) {
			return ((IType)element).getElementName();
		}
		return javaUILabelProviderDelegate.getText(element);
	}
}
