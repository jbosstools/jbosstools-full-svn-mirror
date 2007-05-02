package org.jboss.ide.eclipse.archives.ui;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.jboss.ide.eclipse.archives.core.CorePreferenceManager;

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
		try {
			prefs.flush();
		} catch (org.osgi.service.prefs.BackingStoreException e) { 
			e.printStackTrace();
		} // swallow
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
		IEclipsePreferences prefs = new InstanceScope().getNode(PackagesUIPlugin.PLUGIN_ID);
		prefs.putBoolean(key, val);
		try {
			prefs.flush();
		} catch (org.osgi.service.prefs.BackingStoreException e) { } // swallow

	}
	
	/**
	 * Get the global pref value for this key
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(String key) {
		return getBoolean(key, null, true);
	}
	
	/**
	 * Get the *effective* value of this preference upon this adaptable / resource
	 * Effective values are the stored value if project-specific prefs are turned on.
	 * Effective values are the global value if project-specific prefs are *NOT* turned on.
	 * 
	 * @param key
	 * @param adaptable
	 * @return
	 */
//	public static boolean getBoolean(String key, IAdaptable adaptable) {
//		return getBoolean(key, adaptable, true);
//	}
	
	/**
	 * 
	 * @param key  the preference to be gotten
	 * @param adaptable  the project / resource where the pref might be stored
	 * @param effective  whether or not to get the raw pref value or the effective value 
	 * 					(based on whether project specific prefs are turned on) 
	 * @return
	 */
	public static boolean getBoolean(String key, IAdaptable adaptable, boolean effective) {
		QualifiedName name = new QualifiedName(PackagesUIPlugin.PLUGIN_ID, key);
		if( adaptable != null && CorePreferenceManager.areProjectSpecificPrefsEnabled(adaptable)) {
			IResource project = (IResource)adaptable.getAdapter(IResource.class);
			try {
				if( project != null && project.getPersistentProperty(name) != null) {
					return Boolean.parseBoolean(project.getPersistentProperty(name));
				}
			} catch(CoreException ce) {}
		}
		return new InstanceScope().getNode(PackagesUIPlugin.PLUGIN_ID).getBoolean(key, false);
	}
}
