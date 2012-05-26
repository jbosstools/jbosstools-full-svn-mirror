package org.jboss.tools.portlet.core.preferences;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.jboss.tools.portlet.core.PortletCoreActivator;

public class JBossPortletPreferencesInitializer extends
		AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		Preferences preferences = PortletCoreActivator.getDefault().getPluginPreferences();
		preferences.setDefault(
				PortletCoreActivator.CHECK_RUNTIMES,
				PortletCoreActivator.DEFAULT_CHECK_RUNTIMES);
	}

}
