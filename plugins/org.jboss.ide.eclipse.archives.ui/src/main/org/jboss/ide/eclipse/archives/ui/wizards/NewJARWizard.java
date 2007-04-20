package org.jboss.ide.eclipse.archives.ui.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.jboss.ide.eclipse.archives.core.Trace;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.types.JARPackageType;
import org.jboss.ide.eclipse.archives.ui.PackagesSharedImages;
import org.jboss.ide.eclipse.archives.ui.PackagesUIMessages;

public class NewJARWizard extends AbstractPackageWizard
{
	public WizardPage[] createWizardPages() {
		return new WizardPage[0];
	}

	public NewJARWizard () {
		setWindowTitle(PackagesUIMessages.NewJARWizard_windowTitle);
	}
	
	public NewJARWizard (IArchive existingPackage) {
		super(existingPackage);
		
		setWindowTitle(PackagesUIMessages.NewJARWizard_windowTitle_editJAR);
	}
	
	public boolean performFinish(IArchive pkg) {
		Trace.trace(getClass(), "performing finish");
		
		pkg.setArchiveType(ArchivesCore.getPackageType(JARPackageType.TYPE_ID));
		return true;
	}
	
	public ImageDescriptor getImageDescriptor() {
		return PackagesSharedImages.getImageDescriptor(PackagesSharedImages.IMG_NEW_JAR_WIZARD);
	}
	
	public String getPackageExtension() {
		return "jar";
	}
}
