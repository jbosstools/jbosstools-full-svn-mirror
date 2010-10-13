package org.jboss.tools.deltacloud.ui.internal.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jboss.tools.deltacloud.core.DeltaCloud;

public class InstanceFilter extends Wizard {

	private DeltaCloud cloud;
	private InstanceFilterPage mainPage;
	
	public InstanceFilter(DeltaCloud cloud) {
		this.cloud = cloud;
	}
	
	@Override
	public void addPages() {
		// TODO Auto-generated method stub
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
		
		cloud.createInstanceFilter(nameRule + ";" + //$NON-NLS-1$
				idRule + ";" + //$NON-NLS-1$
				imageIdRule + ";" + //$NON-NLS-1$
				ownerIdRule + ";" + //$NON-NLS-1$
				keyNameRule + ";" + //$NON-NLS-1$
				realmRule + ";" + //$NON-NLS-1$
				profileRule); //$NON-NLS-1$
		
		return true;
	}

}
