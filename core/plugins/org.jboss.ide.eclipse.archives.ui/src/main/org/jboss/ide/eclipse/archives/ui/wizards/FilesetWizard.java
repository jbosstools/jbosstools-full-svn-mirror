package org.jboss.ide.eclipse.archives.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.jboss.ide.eclipse.archives.core.Trace;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchivesModel;
import org.jboss.ide.eclipse.archives.ui.PackageNodeFactory;
import org.jboss.ide.eclipse.archives.ui.wizards.pages.FilesetInfoWizardPage;

public class FilesetWizard extends Wizard {

	private FilesetInfoWizardPage page1;
	private IArchiveFileSet fileset;
	private IArchiveNode parentNode;
	
	public FilesetWizard(IArchiveFileSet fileset, IArchiveNode parentNode)
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
						ArchivesModel.instance().attach(parentNode, fileset, monitor);
					} else {
						ArchivesModel.instance().saveModel(fileset.getProject(), monitor);
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
	
	private void fillFilesetFromPage (IArchiveFileSet fileset) {
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
