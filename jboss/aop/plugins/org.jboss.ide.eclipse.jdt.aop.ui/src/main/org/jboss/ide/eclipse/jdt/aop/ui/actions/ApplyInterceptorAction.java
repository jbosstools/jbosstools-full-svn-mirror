/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.ui.actions;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IWorkbenchPart;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.BoundPointcut;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Binding;
import org.jboss.ide.eclipse.jdt.aop.ui.util.AdvisorDialogUtil;

/**
 * @author Marshall
 */
public class ApplyInterceptorAction extends PointcuttableAction {

	private ISelection currentSelection;
	private IWorkbenchPart part;
	private ArrayList availableInterceptors;
	private ArrayList selectedInterceptors;
	
	public ApplyInterceptorAction ()
	{
		availableInterceptors = new ArrayList();	
	}
	
	public ApplyInterceptorAction (Viewer viewer) { super(viewer); availableInterceptors = new ArrayList(); }
	
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		part = targetPart;
	}

	public void runAction (IAction action) {
		try {
			if (currentSelection instanceof IStructuredSelection)
			{
				ArrayList pointcuts = new ArrayList();
				IJavaProject project = AopCorePlugin.getCurrentJavaProject();
				
				IStructuredSelection selection = (IStructuredSelection) currentSelection;
				if (! selection.isEmpty())
				{
					Iterator iter = selection.iterator();	
					selectedInterceptors = AdvisorDialogUtil.openInterceptorDialog(part.getSite().getShell());
					
					if (selectedInterceptors == null)
					    return;
					
					while (iter.hasNext())
					{
						Object selected = iter.next();
						
						if (selected instanceof IJavaElement)
						{
							IJavaElement element = (IJavaElement) selected;
							
							if (project == null)
							{
								project = element.getJavaProject();
								AopCorePlugin.setCurrentJavaProject(project);
							}
							
							BoundPointcut pointcut = new BoundPointcut(element);
							pointcuts.add(pointcut);		
						}
						else if (selected instanceof Binding) {
							Binding binding = (Binding) selected;
							BoundPointcut pointcut = new BoundPointcut(binding);
							pointcuts.add(pointcut);
						}
					}
					
					AopDescriptor aopDescriptor = AopCorePlugin.getDefault().getDefaultDescriptor(project);
					Iterator pIter = pointcuts.iterator();
					while (pIter.hasNext())
					{
						BoundPointcut pointcut = (BoundPointcut) pIter.next();
						Iterator interceptorIterator = selectedInterceptors.iterator();
						while (interceptorIterator.hasNext())
						{
							IType type = (IType) interceptorIterator.next();
							aopDescriptor.bindInterceptor(pointcut.toString(), type.getFullyQualifiedName());
						}
					}
				
					aopDescriptor.save();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setSelection(ISelection selection) {
		currentSelection = selection;
	}
}
