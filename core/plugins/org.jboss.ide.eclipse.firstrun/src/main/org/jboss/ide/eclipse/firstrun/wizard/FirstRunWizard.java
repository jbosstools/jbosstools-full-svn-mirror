package org.jboss.ide.eclipse.firstrun.wizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.wizard.Wizard;
import org.jboss.ide.eclipse.firstrun.wizard.pages.FirstRunFinalPage;
import org.jboss.ide.eclipse.firstrun.wizard.pages.FirstRunInfoPage;
import org.jboss.ide.eclipse.firstrun.wizard.pages.FirstRunPackagingProjectsPage;
import org.jboss.ide.eclipse.firstrun.wizard.pages.FirstRunXDocletProjectsPage;
import org.jboss.ide.eclipse.packaging.core.PackagingCorePlugin;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunPlugin;

public class FirstRunWizard extends Wizard {

	private FirstRunInfoPage page1;
	private FirstRunPackagingProjectsPage page2;
	private FirstRunXDocletProjectsPage page3;
	private FirstRunFinalPage page4;
	
	public boolean performFinish() {
		
		IProject packagingProjectsToConvert[] = page2.getSelectedProjects();
		IProject xdocletProjectsToConvert[] = page3.getSelectedProjects();
		
		for (int i = 0; i < packagingProjectsToConvert.length; i++)
		{
			PackagingCorePlugin.getDefault().enablePackagingBuilder(JavaCore.create(packagingProjectsToConvert[i]), true);
		}
		
		for (int i = 0; i < xdocletProjectsToConvert.length; i++)
		{
			XDocletRunPlugin.getDefault().enableXDocletBuilder(JavaCore.create(xdocletProjectsToConvert[i]), true);
		}
		
		return true;
	}
	
	public boolean canFinish() {
		if (page4 == null) return false;
		
		return page4.isPageComplete();
	}
	
	
	
	public void addPages() {
		page1 = new FirstRunInfoPage();
		page2 = new FirstRunPackagingProjectsPage();
		page3 = new FirstRunXDocletProjectsPage();
		page4 = new FirstRunFinalPage();
		
		addPage (page1);
		addPage (page2);
		addPage (page3);
		addPage (page4);
	}

}
