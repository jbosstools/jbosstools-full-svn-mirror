package org.jboss.tools.internal.deltacloud.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.ui.ErrorUtils;

public class InstanceFilterWizard extends Wizard {

	private DeltaCloud cloud;
	private InstanceFilterPage mainPage;
	
	public InstanceFilterWizard(DeltaCloud cloud) {
		this.cloud = cloud;
	}
	
	@Override
	public void addPages() {
		mainPage = new InstanceFilterPage(cloud);
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
		String imageIdRule = mainPage.getImageIdRule();
		String ownerIdRule = mainPage.getOwnerIdRule();
		String keyNameRule = mainPage.getKeyNameRule();
		String realmRule = mainPage.getRealmRule();
		String profileRule = mainPage.getProfileRule();
		
		try {
			cloud.updateInstanceFilter(nameRule + ";" + //$NON-NLS-1$
					idRule + ";" + //$NON-NLS-1$
					imageIdRule + ";" + //$NON-NLS-1$
					ownerIdRule + ";" + //$NON-NLS-1$
					keyNameRule + ";" + //$NON-NLS-1$
					realmRule + ";" + //$NON-NLS-1$
					profileRule);
			DeltaCloudManager.getDefault().saveClouds();
		} catch (Exception e) {
			// TODO: internationalize strings
			ErrorUtils.handleError(
					"Error",
					"Could not update filters", e, Display.getDefault().getActiveShell());
		}
		
		return true;
	}

}
