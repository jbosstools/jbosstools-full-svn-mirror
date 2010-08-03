package org.jboss.tools.internal.deltacloud.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;

public class NewInstance extends Wizard {

	private final static String MAINPAGE_NAME = "NewInstance.name"; //$NON-NLS-1$
	private NewInstancePage mainPage;
	
	private DeltaCloud cloud;
	private DeltaCloudImage image;

	public NewInstance(DeltaCloud cloud, DeltaCloudImage image) {
		this.cloud = cloud;
		this.image = image;
	}
	
	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		mainPage = new NewInstancePage(cloud, image);
		addPage(mainPage);
	}
	
	@Override
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}
	
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}
