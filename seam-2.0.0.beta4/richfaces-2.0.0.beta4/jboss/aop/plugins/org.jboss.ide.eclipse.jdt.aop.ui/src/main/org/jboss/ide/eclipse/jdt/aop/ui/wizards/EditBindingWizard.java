/*
 * Created on Jan 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.jdt.aop.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Binding;

/**
 * @author Marshall
 * 
 * A wizard for creating a new JBossAOP Binding (pointcut) definition
 */
public class EditBindingWizard extends Wizard implements INewWizard{

	protected EditBindingWizardPage page1;
	protected AopDescriptor descriptor;
	
	public EditBindingWizard ()
	{
		this (null, AopCorePlugin.getDefault().getDefaultDescriptor(AopCorePlugin.getCurrentJavaProject()));
	}
	
	public EditBindingWizard (Binding binding, AopDescriptor descriptor) {
		super();
		page1 = new EditBindingWizardPage(binding, descriptor);
		this.descriptor = descriptor;
	}

	public void addPages() {
		addPage(page1);
	}
	
	public boolean performFinish() {
		
		descriptor.save();
		
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
	}
}
