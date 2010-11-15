package org.jboss.tools.internal.deltacloud.ui.wizards;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.wizard.Wizard;
import org.jboss.tools.common.log.StatusFactory;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.ui.Activator;

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
		
		try {
			cloud.updateImageFilter(nameRule + ";" + //$NON-NLS-1$
					idRule + ";" + //$NON-NLS-1$
					archRule + ";" + //$NON-NLS-1$
					descRule);
		} catch (Exception e) {
			IStatus status = StatusFactory.getInstance(
					IStatus.ERROR,
					Activator.PLUGIN_ID,
					e.getMessage(),
					e);
			// TODO: internationalize strings
			ErrorDialog.openError(getShell(),
					"Error",
					"Cloud not get update filters on cloud " + cloud.getName(), status);
		}
		
		return true;
	}

}
