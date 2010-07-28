package org.jboss.tools.internal.deltacloud.ui.wizards;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.ui.Activator;

public class NewCloudConnection extends Wizard implements INewWizard {

	private static final String MAINPAGE_NAME = "NewCloudConnection.name"; //$NON-NLS-1$
	private NewCloudConnectionPage mainPage;
	
	public NewCloudConnection() {
		super();
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		mainPage = new NewCloudConnectionPage(WizardMessages.getString(MAINPAGE_NAME));
		addPage(mainPage);
	}

	@Override
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}

	@Override
	public boolean performFinish() {
		String name = mainPage.getName();
		String url = mainPage.getURL();
		String username = mainPage.getUsername();
		String password = mainPage.getPassword();
		try {
			DeltaCloud newCloud = new DeltaCloud(name, new URL(url), username, password);
			DeltaCloudManager.getDefault().addCloud(newCloud);
		} catch (MalformedURLException e) {
			Activator.log(e);
			return false;
		}
		return true;
	}

}
