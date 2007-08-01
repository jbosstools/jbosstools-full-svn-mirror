/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.ui.dialogs;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.TwoPaneElementSelector;
import org.jboss.ide.eclipse.jdt.aop.ui.AopUiPlugin;

/**
 * @author Marshall
 */
public class MethodSelectionDialog extends TwoPaneElementSelector {

	private IType type;
	
	public MethodSelectionDialog (Shell shell, IType type)
	{
		super(shell, new MethodLabelProvider(), new MethodLabelProvider(true));
		this.type = type;
	}
	
	private static class MethodLabelProvider extends LabelProvider
	{
		private boolean showClass;
		
		public MethodLabelProvider () { this(false); }
		
		public MethodLabelProvider (boolean showClass)
		{
			this.showClass= showClass;
		}
		
		public Image getImage(Object element) {
			if (!showClass)
				return AopUiPlugin.getDefault().getTreeImage((IJavaElement) element);
			else
				return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_CLASS);
		}
		
		public String getText(Object element) {
			
			try {
				IMethod method = (IMethod) element;
				if (!showClass)
				{
					String methodLabel = Signature.toString(method.getReturnType()) + " ";
					methodLabel += method.getElementName() + "(";
					String paramNames[] = method.getParameterNames();
					String paramTypes[] = method.getParameterTypes();
					
					for (int i = 0; i < paramNames.length; i++)
					{
						methodLabel += Signature.toString(paramTypes[i]);
						methodLabel += " " + paramNames[i];
						
						if (i < paramNames.length - 1)
						{
							methodLabel += ", ";
						}
					}
					methodLabel += ")";
					return methodLabel;
				} else {
					return method.getDeclaringType().getElementName();
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public int open() {
	
		try {
			setElements(type.getMethods());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.open();
	}
}
