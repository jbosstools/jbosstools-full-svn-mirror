/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.ui.actions;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.BoundPointcut;
import org.jboss.ide.eclipse.jdt.aop.ui.wizards.ConvertToPointcutWizard;

/**
 * @author Marshall
 */
public class ConvertToPointcutAction extends PointcuttableAction {

	private ISelection currentSelection;
	private IWorkbenchPart part;
	
	public ConvertToPointcutAction () {}
	public ConvertToPointcutAction (Viewer viewer) { super(viewer); }
	
	protected void setSelection(ISelection selection) {
		currentSelection = selection;
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		part = targetPart;
	}

	public void runAction (IAction action) {
		
		if (currentSelection instanceof IStructuredSelection)
		{
			IStructuredSelection selection = (IStructuredSelection) currentSelection;
			if (selection.getFirstElement() instanceof IJavaElement)
			{
				IJavaElement element = (IJavaElement) selection.getFirstElement();
				AopCorePlugin.setCurrentJavaProject(element.getJavaProject());
				
				ConvertToPointcutWizard wizard = new ConvertToPointcutWizard(element);
				WizardDialog dialog = new WizardDialog(part.getSite().getShell(), wizard);
				
				dialog.create();
				int response = dialog.open();
				
				if (response == WizardDialog.OK)
				{
					BoundPointcut pointcut = wizard.getPointcut();
					AopDescriptor descriptor = AopCorePlugin.getDefault().getDefaultDescriptor(element.getJavaProject());
					
					descriptor.addPointcut(pointcut.getName(), pointcut.toString());
					descriptor.save();
				}
			}
		}
	
	}

}
