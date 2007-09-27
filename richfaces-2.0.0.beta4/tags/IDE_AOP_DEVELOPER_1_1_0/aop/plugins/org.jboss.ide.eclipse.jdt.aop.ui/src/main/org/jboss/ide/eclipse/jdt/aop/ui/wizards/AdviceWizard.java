/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.ui.wizards;

import java.util.ArrayList;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jface.wizard.Wizard;

/**
 * @author Marshall
 */
public class AdviceWizard extends Wizard {

	private AdviceWizardPage page1;
	
	public AdviceWizard (ArrayList pointcuts)
	{
		try {
			page1 = new AdviceWizardPage(pointcuts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void addPages() {
		addPage(page1);
	}
	
	public boolean performFinish() {
		return true;
	}
	
	public AdviceWizardPage getPage() {
		return page1;
	}
	
	public IMethod[] getSelectedAdviceMethods() {
		return page1.getSelectedAdviceMethods();
	}
}
