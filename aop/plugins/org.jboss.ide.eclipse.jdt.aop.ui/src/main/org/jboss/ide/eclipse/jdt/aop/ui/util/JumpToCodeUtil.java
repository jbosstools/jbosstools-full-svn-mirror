/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.ui.util;

import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Advice;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aspect;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Interceptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.ReportAdvice;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.ReportInterceptor;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvisor;

/**
 * @author Honain, Marshall
 * 
 * This class opens common Aop objects in a Java editor.
 */
public class JumpToCodeUtil
{

	public static IDoubleClickListener getDoubleClickListener()
	{
		return new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event)
			{
				if (event.getSelection() instanceof IStructuredSelection)
				{
					IStructuredSelection selection = (IStructuredSelection) event
							.getSelection();
					Object object = selection.getFirstElement();

					jumpTo(object);
				}
			}
		};
	}

	public static void jumpTo(Object object)
	{
		if (object instanceof Interceptor)
		{
			Interceptor selectedInterceptor = (Interceptor) object;
			openClassInEditor(selectedInterceptor.getClazz());
		}
		else if (object instanceof Advice)
		{
			Advice selectedAdvice = (Advice) object;
			jumpToAdvice(selectedAdvice);
		}
		else if (object instanceof Aspect)
		{
			Aspect dblClickedAspect = (Aspect) object;
			openClassInEditor(dblClickedAspect.getClazz());
		}
		else if (object instanceof ReportAdvice)
		{
			ReportAdvice advice = (ReportAdvice) object;
			jumpToAdvice(advice);
		}
		else if (object instanceof ReportInterceptor)
		{
			ReportInterceptor interceptor = (ReportInterceptor) object;
			openClassInEditor(interceptor.getClazz());
		}
		else if (object instanceof IJavaElement)
		{
			jumpToJavaElement ((IJavaElement) object);
		}
		else if (object instanceof IAopAdvisor)
		{
			jumpTo(((IAopAdvisor) object).getAdvisingElement());
		}
	}

	public static void jumpToJavaElement (IJavaElement element)
	{
		try
		{
			IEditorPart editor = JavaUI.openInEditor(element);
			JavaUI.revealInEditor(editor, element);
			editor.setFocus();
		}
		catch (PartInitException e)
		{
			e.printStackTrace();
		}
		catch (JavaModelException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void openClassInEditor(String clazzToOpen)
	{
		try
		{
			IJavaElement element = AopCorePlugin.getCurrentJavaProject()
				.findElement(
					new Path(clazzToOpen.replace('.', Path.SEPARATOR) + ".java"));
			
			jumpToJavaElement(element);
		}
		catch (JavaModelException e)
		{
			e.printStackTrace();
		}

	}

	public static void jumpToMember(IJavaElement member)
	{
		try
		{
			IEditorPart editor = null;

			if (member instanceof IMethod)
			{
				IMethod method = (IMethod) member;
				editor = JavaUI.openInEditor(method.getCompilationUnit());
			}
			else if (member instanceof IField)
			{
				IField field = (IField) member;
				editor = JavaUI.openInEditor(field.getCompilationUnit());
			}

			if (editor != null)
			{
				JavaUI.revealInEditor(editor, member);
				editor.setFocus();
			}

		}
		catch (PartInitException e)
		{
			e.printStackTrace();
		}
		catch (JavaModelException e)
		{
			e.printStackTrace();
		}
	}

	public static void jumpToAdvice(ReportAdvice advice)
	{
		IMethod method = AopCorePlugin.getDefault().findAdviceMethod(advice);
		jumpToJavaElement (method);
	}

	public static void jumpToAdvice(Advice advice)
	{
		IMethod method = AopCorePlugin.getDefault().findAdviceMethod(advice);
		jumpToJavaElement (method);
	}
}