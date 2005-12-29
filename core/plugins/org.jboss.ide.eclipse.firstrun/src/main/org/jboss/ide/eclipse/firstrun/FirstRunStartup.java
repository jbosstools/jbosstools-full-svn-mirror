package org.jboss.ide.eclipse.firstrun;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.jboss.ide.eclipse.firstrun.wizard.FirstRunWizard;
import org.jboss.ide.eclipse.packaging.core.PackagingCorePlugin;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunPlugin;

public class FirstRunStartup implements IStartup {

	public void earlyStartup() {
		
		try {
			Display.getDefault().asyncExec(new Runnable() {
				public void run () {
					try {
						// force initialization
						PackagingCorePlugin.getDefault();
						XDocletRunPlugin.getDefault();
						
						IPreferenceStore store = FirstRunPlugin.getDefault().getPreferenceStore();
						
						if (! store.contains(FirstRunPlugin.FIRST_RUN_PROPERTY)
							|| !store.getBoolean(FirstRunPlugin.FIRST_RUN_PROPERTY))
						{
							FirstRunWizard wizard = new FirstRunWizard();
							WizardDialog dialog = new WizardDialog(null, wizard);
							
							dialog.open();
							
							// TODO UNCOMMENT ME
							store.setValue(FirstRunPlugin.FIRST_RUN_PROPERTY, true);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
