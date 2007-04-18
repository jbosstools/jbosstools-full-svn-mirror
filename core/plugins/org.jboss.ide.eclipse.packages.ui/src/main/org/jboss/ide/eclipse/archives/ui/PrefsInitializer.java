package org.jboss.ide.eclipse.archives.ui;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

public class PrefsInitializer extends AbstractPreferenceInitializer {


	// preference keys
	public static final String PREF_SHOW_PACKAGE_OUTPUT_PATH = "showPackageOutputPath";
	public static final String PREF_SHOW_FULL_FILESET_ROOT_DIR = "showFullFilesetRootDir";
	public static final String PREF_SHOW_PROJECT_ROOT = "showProjectRoot";
	public static final String PREF_SHOW_ALL_PROJECTS = "showAllProjects";
	
	public void initializeDefaultPreferences() {
		IEclipsePreferences prefs = new DefaultScope().getNode(PackagesUIPlugin.PLUGIN_ID);
		
		prefs.put(PREF_SHOW_FULL_FILESET_ROOT_DIR, "true");
		prefs.put(PREF_SHOW_PACKAGE_OUTPUT_PATH, "true");
		prefs.put(PREF_SHOW_PROJECT_ROOT, "true");
		prefs.put(PREF_SHOW_ALL_PROJECTS, "false");
	}

}
