package org.jboss.ide.eclipse.packages.ui;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

public class PrefsInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IEclipsePreferences prefs = new DefaultScope().getNode(PackagesUIPlugin.PLUGIN_ID);
		
		prefs.put(PackagesUIPlugin.PREF_SHOW_FULL_FILESET_ROOT_DIR, "true");
		prefs.put(PackagesUIPlugin.PREF_SHOW_PACKAGE_OUTPUT_PATH, "true");
	}

}
