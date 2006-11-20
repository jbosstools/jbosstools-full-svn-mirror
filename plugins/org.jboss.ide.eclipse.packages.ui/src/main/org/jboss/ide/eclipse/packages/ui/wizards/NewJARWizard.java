package org.jboss.ide.eclipse.packages.ui.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.jboss.ide.eclipse.packages.core.model.IPackageWorkingCopy;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.core.model.types.JARPackageType;
import org.jboss.ide.eclipse.packages.ui.PackagesUIMessages;
import org.jboss.ide.eclipse.packages.ui.PackagesUIPlugin;

public class NewJARWizard extends AbstractPackageWizard
{
	public WizardPage[] createWizardPages() {
		
		return new WizardPage[0];
	}

	public NewJARWizard ()
	{
		setWindowTitle(PackagesUIMessages.NewJARWizard_windowTitle);
	}
	
	public boolean performFinish(IPackageWorkingCopy pkg) {
		pkg.setPackageType(PackagesCore.getPackageType(JARPackageType.TYPE_ID));
		return true;
	}
	
	public ImageDescriptor getImageDescriptor() {
		return PackagesUIPlugin.getImageDescriptor(PackagesUIPlugin.IMG_NEW_JAR_WIZARD);
	}
	
	public String getPackageExtension() {
		return "jar";
	}
}
