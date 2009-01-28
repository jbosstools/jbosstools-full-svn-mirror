package org.jboss.tools.jsf.ui.test;

import org.eclipse.jface.wizard.IWizard;


public class PropertiesNewWizardTest extends WizardTest {
	public PropertiesNewWizardTest(){
		super("org.jboss.tools.common.model.ui.wizard.newfile.NewPropertiesFileWizard");
	}
	
	public void testPropertiesFileNewWizardTestIsCreated() {
		wizardIsCreated();
	}
	
	public void testPropertiesFileNewWizardValidation() {
		IWizard wizard = getWizardWithoutSelection();
		
		boolean canFinish = wizard.canFinish();
		
		assertFalse("Finish button is enabled at first wizard page.", canFinish);
	}
	
	public void testPropertiesFileNewWizardValidation2() {
		IWizard wizard = getWizardOnProject();
		
		boolean canFinish = wizard.canFinish();
		
		// Assert Finish button is enabled by default if wizard is called on Project
		assertTrue("Finish button is disabled at first wizard page.", canFinish);
		
		// Assert Finish button is disabled and error is present if 
		// 		Folder field is empty
		// 		All other fields are correct
		
		// Assert Finish button is disabled and error is present if 
		// 		Folder field points to folder that doesn't exist
		// 		All other fields are correct
		
		// Assert Finish button is disabled and error is present if
		//		Folder field is correct
		//		Name field is empty
		
		// Assert Finish button is disabled and error is present if
		//		Folder field is correct
		//		Name field contains forbidden characters
		
		// Assert Finish button is disabled and error is present if
		//		Folder field is correct
		//		Name field contains file name that already exists
	}
}
