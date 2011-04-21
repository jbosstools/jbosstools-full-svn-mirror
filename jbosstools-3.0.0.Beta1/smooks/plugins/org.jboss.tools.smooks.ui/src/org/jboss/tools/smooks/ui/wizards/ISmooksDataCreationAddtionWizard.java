/**
 * 
 */
package org.jboss.tools.smooks.ui.wizards;

import org.eclipse.jface.wizard.IWizardPage;

/**
 * @author dart
 *
 */
public interface ISmooksDataCreationAddtionWizard {
	
	public void addSourceWizardPage(IWizardPage page);
	
	public void removeSourceWIzardPage(IWizardPage page);
	
	public void clearSourceWizardPages();
	
	public void addTargetWizardPage(IWizardPage page);
	
	public void removeTargetWIzardPage(IWizardPage page);
	
	public void clearTargetWizardPages();
	
}
