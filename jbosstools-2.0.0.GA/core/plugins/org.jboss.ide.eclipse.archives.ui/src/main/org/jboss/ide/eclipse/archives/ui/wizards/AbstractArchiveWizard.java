package org.jboss.ide.eclipse.archives.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.ide.eclipse.archives.core.Trace;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.ui.views.ProjectArchivesView;
import org.jboss.ide.eclipse.archives.ui.wizards.pages.ArchiveInfoWizardPage;

public abstract class AbstractArchiveWizard extends WizardWithNotification implements INewWizard {
	private ArchiveInfoWizardPage firstPage;
	private WizardPage pages[];
	protected IProject project;
	protected Object selectedDestination;
	protected IArchive existingPackage;
	
	public AbstractArchiveWizard () {	
		this.project = ProjectArchivesView.getInstance().getCurrentProject();
	}
	
	public AbstractArchiveWizard (IArchive existingPackage) {
		this.existingPackage = existingPackage;
		this.project = findProject(existingPackage.getProjectPath());
	}
	protected IProject findProject(IPath path) {
		IProject[] list = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for( int i = 0; i < list.length; i++ )
			if( list[i].getProject().getLocation().equals(path))
				return list[i];
		return null;
	}
	public void addPages() {
		firstPage = new ArchiveInfoWizardPage(this, existingPackage);
		addPage(firstPage);
		
		pages = createWizardPages();
		for (int i = 0; i < pages.length; i++) {
			addPage(pages[i]);
		}
	}
	
	public boolean canFinish() {
		if (firstPage.isPageComplete()) {
			for (int i = 0; i < pages.length; i++) {
				if (!pages[i].isPageComplete()) return false;
			}
			return true;
		}
		
		return false;
	}
	
	public boolean performFinish() {
		IWizardPage currentPage = getContainer().getCurrentPage();
		
		if (currentPage instanceof WizardPageWithNotification) {
			((WizardPageWithNotification)currentPage).pageExited(WizardWithNotification.FINISH);
		}
		
		final boolean create = this.existingPackage == null;
		final IArchive pkg = firstPage.getArchive();
		final Object destination = firstPage.getPackageDestination();
		
		boolean performed = performFinish(pkg);
		
		if (performed) {
			try {
				getContainer().run(false, false, new IRunnableWithProgress () {
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						IArchiveNode parent = null;
						
						if (destination instanceof IArchiveNode) {
							// if we're modifying an existing package, remove old parentage
							if (!create && !destination.equals(pkg.getParent())) {
								if (pkg.getParent() != null) {
									pkg.getParent().removeChild(pkg);
								}
							}
							parent = (IArchiveNode)destination;
						} else {
							parent = ArchivesModel.instance().getRoot(project.getLocation(), true, monitor);
						}
						
						if( create ) 
							ArchivesModel.instance().attach(parent, pkg, monitor);
						else
							ArchivesModel.instance().saveModel(project.getLocation(), monitor);
					}
				});
			} catch (InvocationTargetException e) {
				Trace.trace(getClass(), e);
			} catch (InterruptedException e) {
				Trace.trace(getClass(), e);
			}
		}
		return performed;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		if (selection == null) return;
		project = ProjectArchivesView.getInstance().getCurrentProject();
		Object selected;
		
		if( selection.isEmpty() ) {
			selected = project;
		} else {
			selected = selection.getFirstElement();
		}

		if (selected instanceof IArchiveNode)
		{
			IArchiveNode node = (IArchiveNode) selected;
			if (node.getNodeType() == IArchiveNode.TYPE_ARCHIVE || node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FOLDER)
			{
				selectedDestination = selected;
			}
			project = findProject(node.getProjectPath());
		}
		else if (selected instanceof IContainer)
		{
			selectedDestination = selected;
		}
		else {
			// find project
			String proj = project.getLocation().toOSString().substring(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString().length()+1);
			selectedDestination = ResourcesPlugin.getWorkspace().getRoot().getProject(proj);
		}
		
		setNeedsProgressMonitor(true);
	}
	
	public Object getSelectedDestination ()
	{
		return selectedDestination;
	}
	
	public abstract boolean performFinish(IArchive pkg);
	public abstract WizardPage[] createWizardPages();
	public abstract ImageDescriptor getImageDescriptor();
	public abstract String getArchiveExtension();
	
	public IProject getProject() {
		return project;
	}
	
	/**
	 * Returns the package created by this wizard.
	 * Note: This should only be called after the first page has been completed
	 * @return The package
	 */
	public IArchive getArchive () {
		return firstPage.getArchive();
	}
}
