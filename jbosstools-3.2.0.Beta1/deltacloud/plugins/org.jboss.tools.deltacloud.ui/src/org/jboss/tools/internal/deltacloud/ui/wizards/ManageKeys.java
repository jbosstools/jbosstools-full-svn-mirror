package org.jboss.tools.internal.deltacloud.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jboss.tools.deltacloud.core.DeltaCloud;

public class ManageKeys extends Wizard {

	private DeltaCloud cloud;
	private String fileExtension;
	private ManageKeysPage mainPage;
	private String keyname;
	
	public ManageKeys(DeltaCloud cloud, String fileExtension) {
		this.cloud = cloud;
		this.fileExtension = fileExtension;
	}
	
	public String getKeyName() {
		return keyname;
	}
	
	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		mainPage = new ManageKeysPage(cloud, fileExtension);
		addPage(mainPage);
	}
	
	@Override
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}
	
	@Override
	public boolean performFinish() {
		String currFile = mainPage.getCurrFile();
		keyname = currFile.substring(0, 
				currFile.length() - fileExtension.length());
		// TODO Auto-generated method stub
		return true;
	}

}
