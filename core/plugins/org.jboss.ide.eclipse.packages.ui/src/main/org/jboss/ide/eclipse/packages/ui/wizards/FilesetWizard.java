package org.jboss.ide.eclipse.packages.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.jboss.ide.eclipse.packages.core.Trace;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageFileSetImpl;
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
			this.fileset = PackagesCore.createDetachedPackageFileSet(parentNode.getProject());
				
		fillFilesetFromPage(fileset);

		if (createFileset)
			page1.getRootNode().addChild(this.fileset);
		
		try {
			getContainer().run(false, false, new IRunnableWithProgress () {
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					PackagesCore.attach(fileset, monitor);	
				}
			});
		} catch (InvocationTargetException e) {
			Trace.trace(getClass(), e);
		} catch (InterruptedException e) {
			Trace.trace(getClass(), e);
		}
		
		((PackageFileSetImpl)fileset).flagAsChanged();
		
		return true;
	}
	
	private void fillFilesetFromPage (IPackageFileSet fileset)
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
