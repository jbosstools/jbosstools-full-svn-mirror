package org.jboss.tools.portlet.ui.internal.preferences;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.portlet.ui.Messages;
import org.jboss.tools.portlet.core.PortletCoreActivator;

public class JBossPortletPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private IWorkbench workbench;
	private Button button;

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);
		
		button = new Button(composite,SWT.CHECK);
		button.setText(Messages.JBossPortletPreferencePage_Check_Runtimes_for_Portlet_Components);
		button.setSelection(PortletCoreActivator.getDefault().getPluginPreferences().getBoolean(PortletCoreActivator.CHECK_RUNTIMES));
		return composite;
	}

	public void init(IWorkbench workbench) {
		this.workbench = workbench;
	}

	@Override
	protected void performDefaults() {
		button.setSelection(PortletCoreActivator.DEFAULT_CHECK_RUNTIMES);
		super.performDefaults();
	}

	@Override
	public boolean performOk() {
		Preferences preferences = PortletCoreActivator.getDefault().getPluginPreferences();
		preferences.setDefault(PortletCoreActivator.CHECK_RUNTIMES, button.getSelection());
		return super.performOk();
	}

}
