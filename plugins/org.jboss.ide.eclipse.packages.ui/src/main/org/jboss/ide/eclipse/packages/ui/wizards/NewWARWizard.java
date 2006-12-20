package org.jboss.ide.eclipse.packages.ui.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.ui.PackagesUIPlugin;
import org.jboss.ide.eclipse.packages.ui.wizards.pages.WARInfoWizardPage;

public class NewWARWizard extends AbstractPackageWizard {

	private WARInfoWizardPage warInfoPage;
	
	public NewWARWizard ()
	{
		setWindowTitle("New WAR");
	}
	
	public ImageDescriptor getImageDescriptor() {
		return PackagesUIPlugin.getImageDescriptor(PackagesUIPlugin.IMG_NEW_WAR_WIZARD);
	}

	public WizardPage[] createWizardPages() {
		warInfoPage = new WARInfoWizardPage();
		
		return new WizardPage[] { warInfoPage };
	}

	public boolean performFinish(IPackage pkg) {
		//pkg.setPackageType("war");
		return true;
	}
	
	public String getPackageExtension() {
		return "war";
	}

}
