package org.jboss.ide.eclipse.packages.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.jboss.ide.eclipse.packages.core.Trace;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;
import org.jboss.ide.eclipse.packages.ui.PackageNodeFactory;
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
		try {
		final boolean createFileset = this.fileset == null;
		
		if (createFileset)
			this.fileset = PackageNodeFactory.createFileset();
				
		fillFilesetFromPage(fileset);
		try {
			getContainer().run(false, false, new IRunnableWithProgress () {
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					if (createFileset) {
						PackagesModel.instance().attach(parentNode, fileset, monitor);
					} else {
						PackagesModel.instance().saveModel(fileset.getProject(), monitor);
					}
				}
			});
		} catch (InvocationTargetException e) {
			Trace.trace(getClass(), e);
		} catch (InterruptedException e) {
			Trace.trace(getClass(), e);
		}
		
		} catch(Exception e) {e.printStackTrace();}
		return true;
	}
	
	private void fillFilesetFromPage (IPackageFileSet fileset) {
		fileset.setExcludesPattern(page1.getExcludes());
		fileset.setIncludesPattern(page1.getIncludes());
		fileset.setInWorkspace(page1.isRootDirWorkspaceRelative());
		fileset.setSourcePath(new Path(page1.getRootDir()));
	}

	public void addPages() {
		page1 = new FilesetInfoWizardPage(getShell(), fileset, parentNode);
		addPage(page1);
	}
}
