package org.jboss.ide.eclipse.archives.ui.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.jboss.ide.eclipse.archives.core.Trace;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.types.JARArchiveType;
import org.jboss.ide.eclipse.archives.ui.ArchivesSharedImages;
import org.jboss.ide.eclipse.archives.ui.ArchivesUIMessages;

public class NewJARWizard extends AbstractArchiveWizard
{
	public WizardPage[] createWizardPages() {
		return new WizardPage[0];
	}

	public NewJARWizard () {
		setWindowTitle(ArchivesUIMessages.NewJARWizard_windowTitle);
	}
	
	public NewJARWizard (IArchive existingPackage) {
		super(existingPackage);
		
		setWindowTitle(ArchivesUIMessages.NewJARWizard_windowTitle_editJAR);
	}
	
	public boolean performFinish(IArchive pkg) {
		Trace.trace(getClass(), "performing finish");
		
		pkg.setArchiveType(ArchivesCore.getArchiveType(JARArchiveType.TYPE_ID));
		return true;
	}
	
	public ImageDescriptor getImageDescriptor() {
		return ArchivesSharedImages.getImageDescriptor(ArchivesSharedImages.IMG_NEW_JAR_WIZARD);
	}
	
	public String getArchiveExtension() {
		return "jar";
	}
}
