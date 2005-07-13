/*
 * Created on Jan 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.ejb3.wizards.ui.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.jboss.ide.eclipse.ejb3.wizards.ui.wizards.pages.NewMessageDrivenBeanWizardPage;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NewMessageDrivenBeanWizard extends NewElementWizard {

	NewMessageDrivenBeanWizardPage page;
	private IStructuredSelection selection;
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		page = new NewMessageDrivenBeanWizardPage();
		this.selection = selection;
		
		super.init(workbench, selection);
	}

	public boolean performFinish() {
		warnAboutTypeCommentDeprecation();
		boolean res= super.performFinish();
		if (res) {
			IResource resource= page.getModifiedResource();
			if (resource != null) {
				selectAndReveal(resource);
				openResource((IFile) resource);
			}	
		}
		return true;
	}

	public void addPages() {
		addPage (page);
		page.init(selection);
	}
	
	protected void finishPage(IProgressMonitor monitor)
		throws InterruptedException, CoreException
	{
		page.createType(monitor);
	}
	
	public IJavaElement getCreatedElement() {
		return page.getCreatedType();
	}
	
}
