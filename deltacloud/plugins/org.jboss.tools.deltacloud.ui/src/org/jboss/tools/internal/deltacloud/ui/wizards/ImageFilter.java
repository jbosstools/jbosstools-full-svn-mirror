package org.jboss.tools.internal.deltacloud.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;

public class ImageFilter extends Wizard {

	private DeltaCloud cloud;
	private ImageFilterPage mainPage;
	
	public ImageFilter(DeltaCloud cloud) {
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
		
		cloud.createImageFilter(nameRule + ";" + //$NON-NLS-1$
				idRule + ";" + //$NON-NLS-1$
				archRule + ";" + //$NON-NLS-1$
				descRule); //$NON-NLS-1$
		
		return true;
	}

}
