package org.jboss.ide.eclipse.packages.ui.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.jboss.ide.eclipse.packages.core.Trace;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.core.model.types.JARPackageType;
import org.jboss.ide.eclipse.packages.ui.PackagesSharedImages;
import org.jboss.ide.eclipse.packages.ui.PackagesUIMessages;

public class NewJARWizard extends AbstractPackageWizard
{
	public WizardPage[] createWizardPages() {
		return new WizardPage[0];
	}

	public NewJARWizard () {
		setWindowTitle(PackagesUIMessages.NewJARWizard_windowTitle);
	}
	
	public NewJARWizard (IPackage existingPackage) {
		super(existingPackage);
		
		setWindowTitle(PackagesUIMessages.NewJARWizard_windowTitle_editJAR);
	}
	
	public boolean performFinish(IPackage pkg) {
		Trace.trace(getClass(), "performing finish");
		
		pkg.setPackageType(PackagesCore.getPackageType(JARPackageType.TYPE_ID));
		return true;
	}
	
	public ImageDescriptor getImageDescriptor() {
		return PackagesSharedImages.getImageDescriptor(PackagesSharedImages.IMG_NEW_JAR_WIZARD);
	}
	
	public String getPackageExtension() {
		return "jar";
	}
}
