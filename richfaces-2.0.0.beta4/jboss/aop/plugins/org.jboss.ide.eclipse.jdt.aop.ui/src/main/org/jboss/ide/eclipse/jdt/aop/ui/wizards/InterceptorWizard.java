/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.ui.wizards;

import java.util.ArrayList;

import org.eclipse.jface.wizard.Wizard;

/**
 * @author Marshall
 */
public class InterceptorWizard extends Wizard {
	
	private ArrayList interceptors;
	private InterceptorWizardPage page1;
	
	public InterceptorWizard (ArrayList interceptors)
	{
		this.interceptors = interceptors;
		page1 = new InterceptorWizardPage(interceptors);
	}
	
	public void addPages ()
	{
		addPage(page1);
	}

	public boolean performFinish() {
		return true;
	}

	/**
	 * @return
	 */
	public ArrayList getSelectedInterceptors() {
		return page1.getSelectedInterceptors();
	}

}
