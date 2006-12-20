package org.jboss.ide.eclipse.packages.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.packages.core.Trace;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.ui.wizards.pages.PackageInfoWizardPage;
import org.jboss.ide.eclipse.ui.wizards.WizardPageWithNotification;
import org.jboss.ide.eclipse.ui.wizards.WizardWithNotification;

public abstract class AbstractPackageWizard extends WizardWithNotification implements INewWizard
{
	private PackageInfoWizardPage firstPage;
	private WizardPage pages[];
	protected IProject project;
	protected Object selectedDestination;
	
	public void addPages() {
		firstPage = new PackageInfoWizardPage(this);
		addPage(firstPage);
		
		pages = createWizardPages();
		for (int i = 0; i < pages.length; i++)
		{
			addPage(pages[i]);
		}
	}
	
	public boolean canFinish() {
		if (firstPage.isPageComplete())
		{
			for (int i = 0; i < pages.length; i++)
			{
				if (!pages[i].isPageComplete()) return false;
			}
			return true;
		}
		
		return false;
	}
	
	public boolean performFinish() {
		IWizardPage currentPage = getContainer().getCurrentPage();
		
		if (currentPage instanceof WizardPageWithNotification)
		{
			((WizardPageWithNotification)currentPage).pageExited(WizardWithNotification.FINISH);
		}
		
		final IPackage pkg = firstPage.getPackage();
		Object destination = firstPage.getPackageDestination();
		
		boolean performed = performFinish(pkg);
		
		if (performed)
		{
			try {
				getContainer().run(false, false, new IRunnableWithProgress () {
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						PackagesCore.attach(pkg, monitor);	
					}
				});
			} catch (InvocationTargetException e) {
				Trace.trace(getClass(), e);
			} catch (InterruptedException e) {
				Trace.trace(getClass(), e);
			}
			
			if (destination instanceof IPackageNode) {
				IPackageNode node = (IPackageNode) destination;
				node.addChild(pkg);
			}
		}
		return performed;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		if (selection == null) return;
		
		project = ProjectUtil.getProject(selection.getFirstElement());
		Object selected = selection.getFirstElement();
		if (selected instanceof IPackageNode)
		{
			IPackageNode node = (IPackageNode) selected;
			if (node.getNodeType() == IPackageNode.TYPE_PACKAGE || node.getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER)
			{
				selectedDestination = selected;
			}
		}
		else if (selected instanceof IContainer)
		{
			selectedDestination = selected;
		}
		else {
			selectedDestination = project;
		}
		
		setNeedsProgressMonitor(true);
	}
	
	public Object getSelectedDestination ()
	{
		return selectedDestination;
	}
	
	public abstract boolean performFinish(IPackage pkg);
	public abstract WizardPage[] createWizardPages();
	public abstract ImageDescriptor getImageDescriptor();
	public abstract String getPackageExtension();
	
	public IProject getProject() {
		return project;
	}
	
	/**
	 * Returns the package created by this wizard.
	 * Note: This should only be called after the first page has been completed
	 * @return The package
	 */
	public IPackage getPackage ()
	{
		return firstPage.getPackage();
	}
}
