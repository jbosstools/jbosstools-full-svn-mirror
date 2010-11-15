package org.jboss.tools.internal.deltacloud.ui.wizards;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.common.log.StatusFactory;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.ui.Activator;

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
		
		try {
			cloud.updateInstanceFilter(nameRule + ";" + //$NON-NLS-1$
					idRule + ";" + //$NON-NLS-1$
					imageIdRule + ";" + //$NON-NLS-1$
					ownerIdRule + ";" + //$NON-NLS-1$
					keyNameRule + ";" + //$NON-NLS-1$
					realmRule + ";" + //$NON-NLS-1$
					profileRule);
		} catch (Exception e) {
			IStatus status = StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
			// TODO: internationalize strings
			ErrorDialog.openError(Display.getDefault().getActiveShell(),
					"Error",
					"Could not update filters", status);
		}
		
		return true;
	}

}
