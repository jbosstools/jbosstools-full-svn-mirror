package org.jboss.ide.eclipse.packages.ui.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.Wizard;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSetWorkingCopy;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.ui.wizards.pages.FilesetInfoWizardPage;

public class FilesetWizard extends Wizard {

	private FilesetInfoWizardPage page1;
	private IPackageFileSet fileset;
	private IPackageNode parentNode;
	
	public FilesetWizard(IPackageFileSet fileset, IPackageNode parentNode)
	{
		this.fileset = fileset;
		this.parentNode = parentNode;
	}
	
	public boolean performFinish() {
		boolean createFileset = this.fileset == null;
		
		if (createFileset)
			this.fileset = PackagesCore.createPackageFileSet(parentNode.getProject());
		
		IPackageFileSetWorkingCopy filesetWC = fileset.createFileSetWorkingCopy();
		
		fillFilesetFromPage(filesetWC);
		
		if (createFileset)
			page1.getRootNode().addChild(this.fileset);
		
		filesetWC.save();
		
		return true;
	}
	
	private void fillFilesetFromPage (IPackageFileSetWorkingCopy fileset)
	{
		if (page1.isSingleFile())
		{
			if (page1.isFileWorkspaceRelative())
			{
				fileset.setSingleFile(page1.getWorkspaceFile());
			} else {
				fileset.setSingleFile(new Path(page1.getSingleFile()));
			}
		}
		else {
			fileset.setExcludesPattern(page1.getExcludes());
			fileset.setIncludesPattern(page1.getIncludes());
			if (page1.isRootDirWorkspaceRelative()) {
				IContainer dir = page1.getWorkspaceRootDir();
				if (!dir.getProject().equals(this.parentNode.getProject())) {
					fileset.setSourceProject(dir.getProject());
				}
				
				fileset.setSourceContainer(dir);
			} else {
				fileset.setSourceFolder(new Path(page1.getRootDir()));
			}
		}
	}

	public void addPages() {
		page1 = new FilesetInfoWizardPage(getShell(), fileset, parentNode);
		addPage(page1);
	}
}
