package org.jboss.ide.eclipse.archives.ui;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.QualifiedName;
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
		
		prefs.putBoolean(PREF_SHOW_FULL_FILESET_ROOT_DIR, true);
		prefs.putBoolean(PREF_SHOW_PACKAGE_OUTPUT_PATH, true);
		prefs.putBoolean(PREF_SHOW_PROJECT_ROOT, true);
		prefs.putBoolean(PREF_SHOW_ALL_PROJECTS, false);
	}

	public static void setBoolean(String key, boolean val) {
		setBoolean(key, val, null);
	}
	
	public static void setBoolean(String key, boolean val, IAdaptable adaptable) {
		QualifiedName name = new QualifiedName(PackagesUIPlugin.PLUGIN_ID, key);
		if( adaptable != null ) {
			IResource project = (IResource)adaptable.getAdapter(IResource.class);
			try {
				if( project != null && project.getPersistentProperty(name) != null) {
					project.setPersistentProperty(name, new Boolean(val).toString());
					return;
				}
			} catch(CoreException ce) {}
		}
		new DefaultScope().getNode(PackagesUIPlugin.PLUGIN_ID).putBoolean(key, val);
	}
	
	public static boolean getBoolean(String key) {
		return getBoolean(key, null);
	}
	public static boolean getBoolean(String key, IAdaptable adaptable) {
		QualifiedName name = new QualifiedName(PackagesUIPlugin.PLUGIN_ID, key);
		if( adaptable != null ) {
			IResource project = (IResource)adaptable.getAdapter(IResource.class);
			try {
				if( project != null && project.getPersistentProperty(name) != null) {
					return Boolean.parseBoolean(project.getPersistentProperty(name));
				}
			} catch(CoreException ce) {}
		}
		return new DefaultScope().getNode(PackagesUIPlugin.PLUGIN_ID).getBoolean(key, false);
	}
}
