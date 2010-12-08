package org.jboss.tools.internal.deltacloud.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jboss.tools.deltacloud.core.DeltaCloud;

public class FindImageWizard extends Wizard {

	private DeltaCloud cloud;
	private FindImagePage mainPage;
	private String imageId;
	
	public FindImageWizard(DeltaCloud cloud) {
		this.cloud = cloud;
	}
	
	public String getImageId() {
		return imageId;
	}
	
	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		mainPage = new FindImagePage(cloud);
		addPage(mainPage);
	}
	
	@Override
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}
	
	@Override
	public boolean performFinish() {
		imageId = mainPage.getImageId();
		return true;
	}

}
