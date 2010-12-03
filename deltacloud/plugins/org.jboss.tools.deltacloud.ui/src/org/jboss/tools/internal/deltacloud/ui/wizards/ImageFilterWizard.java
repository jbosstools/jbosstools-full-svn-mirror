package org.jboss.tools.internal.deltacloud.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.ui.ErrorUtils;

public class ImageFilterWizard extends Wizard {

	private DeltaCloud cloud;
	private ImageFilterPage mainPage;

	public ImageFilterWizard(DeltaCloud cloud) {
		this.cloud = cloud;
	}

	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		mainPage = new ImageFilterPage(cloud);
		addPage(mainPage);
	}

	@Override
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}

	@Override
	public boolean performFinish() {
		String nameRule = mainPage.getNameRule();
		String idRule = mainPage.getIdRule();
		String archRule = mainPage.getArchRule();
		String descRule = mainPage.getDescRule();

		try {
			cloud.updateImageFilter(nameRule + ";" + //$NON-NLS-1$
					idRule + ";" + //$NON-NLS-1$
					archRule + ";" + //$NON-NLS-1$
					descRule);
			DeltaCloudManager.getDefault().saveClouds();
		} catch (Exception e) {
			// TODO: internationalize strings
			ErrorUtils.handleError(
					"Error",
					"Cloud not get update filters on cloud " + cloud.getName(), e, getShell());
		}

		return true;
	}

}
