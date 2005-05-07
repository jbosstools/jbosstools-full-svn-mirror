/*
 * Created on Jan 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.jdt.aop.ui.wizards;

import java.util.Iterator;

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
	protected boolean create;
	protected Binding bind;
	
	public EditBindingWizard ()
	{
		this (null, AopCorePlugin.getDefault().getDefaultDescriptor(AopCorePlugin.getCurrentJavaProject()));
	}
	
	public EditBindingWizard (Binding binding, AopDescriptor descriptor) {
		super();
		this.create = binding == null ? true : false;
		this.bind = binding;
		this.page1 = new EditBindingWizardPage(binding, descriptor);
		this.descriptor = descriptor;
	}

	public void addPages() {
		addPage(page1);
	}
	
	public boolean performFinish() {
		descriptor.save();
		return true;
	}
	
	public boolean performCancel() {
		if( create ) {
			// delete the new binding that was created
			if( page1.getBinding() != null ) 
				descriptor.remove(page1.getBinding());
		} else {
			// be very upset... Time to restore the state
			this.bind.setName(page1.getOriginalName());
			this.bind.setPointcut(page1.getOriginalPointcut());
			this.bind.getElements().clear();
			Iterator i = page1.getOriginalElements().iterator();
			while(i.hasNext()) {
				this.bind.getElements().add(i.next());
			}
		}
		
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
	}
}
