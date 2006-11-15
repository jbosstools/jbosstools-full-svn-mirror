package org.jboss.ide.eclipse.packages.ui.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageWorkingCopy;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.ui.wizards.pages.PackageInfoWizardPage;

public abstract class AbstractPackageWizard extends Wizard implements INewWizard
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
		IPackage pkg = PackagesCore.createPackage(project);
		IPackageWorkingCopy packageWC = pkg.createPackageWorkingCopy();
		
		packageWC.setName(firstPage.getPackageName());
		packageWC.setExploded(firstPage.isPackageExploded());
		if (firstPage.isManifestEnabled())
		{
			packageWC.setManifest(firstPage.getManifestFile());
		}
		
		Object destContainer = firstPage.getPackageDestination();
		if (!destContainer.equals(project) && destContainer instanceof IContainer) {
			packageWC.setDestinationContainer((IContainer)destContainer);
		}
		else if (destContainer instanceof IPath)
		{
			packageWC.setDestinationFolder((IPath) destContainer);
		}
		
		boolean performed = performFinish(packageWC);
		
		if (performed)
		{
			pkg = packageWC.savePackage();
			if (destContainer instanceof IPackageNode) {
				IPackageNode node = (IPackageNode) destContainer;
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
	}
	
	public Object getSelectedDestination ()
	{
		return selectedDestination;
	}
	
	public abstract boolean performFinish(IPackageWorkingCopy pkg);
	public abstract WizardPage[] createWizardPages();
	public abstract ImageDescriptor getImageDescriptor();
	public abstract String getPackageExtension();
	
	public IProject getProject() {
		return project;
	}
}
