/**
 * 
 */
package org.jboss.ide.eclipse.jsr88deployer.core;

import java.util.ArrayList;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.TableItem;
import org.jboss.ide.eclipse.jsr88deployer.core.utils.StringProperties;


/**
 * @author Rob Stryker
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PreferenceStoreUtil {

	public static final String JAR_PREFIX = "__JAR_TO_SEARCH__";
	public static final String TARGET_PREFIX = "__TARGET_TO_DEPLOY_TO__";
	public static final String CONFIG_PREFIX = "__CONFIG_PREFIX__";
	public static final String ASSOC_PREFIX = "__ASSOC_PREFIX__";
	public static final String FILE_ASSOC_PREFIX = "__FILE_ASSOC_PREFIX__";
	
	
	public static final int ASSOCIATION_PREFERENCE_NAME = 0;
	public static final int ASSOCIATION_PREFERENCE_JAR = 1;
	public static final int ASSOCIATION_PREFERENCE_TARGET = 2;
	public static final int ASSOCIATION_PREFERENCE_CONFIG = 3;

	public static final int FILEASSOC_FILENAME = 0;
	public static final int FILEASSOC_ASSOC = 1;
	
	public static final int TARGET_NAME = 0;
	public static final int TARGET_URI  = 1;
	public static final int TARGET_USER = 2;
	public static final int TARGET_PASS = 3;

	

	public static void clearIncrementalPreferences(String prefix) {
		IPreferenceStore store = JSR88CorePlugin.getDefault().getPreferenceStore();
		boolean done = false;
		int i = 0;
		while( !done ) {
			String key = prefix + i;
			String value = store.getString(key);
			if( value == null || value.equals("")) {
				done = true;
			} else {
				store.setValue(key, "");
			}
			i++;
		}
	}
	
	public static void saveIncrementalPreferences(String prefix, TableItem[] items ) {
		IPreferenceStore store = JSR88CorePlugin.getDefault().getPreferenceStore();
		for( int i = 0; i < items.length; i++ ) {
			String key = prefix + i;
			String value = items[i].getData().toString();
			store.setValue(key, value);
		}
	}
	
	public static void saveIncrementalPreferences(String prefix, Object[] objects) {
		IPreferenceStore store = JSR88CorePlugin.getDefault().getPreferenceStore();
		for( int i = 0; i < objects.length; i++ ) {
			String key = prefix + i;
			String value = objects[i].toString();
			store.setValue(key, value);
		}		
	}
	
	public static void addIncrementalPreference(String prefix, Object obj) {
		IPreferenceStore store = JSR88CorePlugin.getDefault().getPreferenceStore();
		int i = -1;
		boolean done = false;
		while(!done ) {
			i++;
			done = !store.contains(prefix + i );
			if( !done && store.getString(prefix + i).equals(obj)) {
				// duplicate... ie overwriting a previous file. 
				done = true;
			}
		}
		store.setValue(prefix + i, obj.toString());
	}
	
	public static Object[] loadIncrementalPreferences(String prefix) {
		IPreferenceStore store = JSR88CorePlugin.getDefault().getPreferenceStore();
		ArrayList list = new ArrayList();
		boolean done = false;
		int i = 0;
		while( !done ) {
			String key = prefix + i;
			String value = store.getString(key);
			if( value == null || value.equals("")) {
				done = true;
			} else {
				list.add(value);
			}
			i++;
		}
		return list.toArray();
	}
	
	// Horribly done. Must re-do the exploded string model at some time.
	public static StringProperties getAssociationPreference(String name) {
		return getMatchingPreference(ASSOC_PREFIX, ASSOCIATION_PREFERENCE_NAME, name);
//		Object[] preferences = loadIncrementalPreferences(ASSOC_PREFIX);
//		for( int i = 0; i < preferences.length; i++ ) {
//			StringProperties prop = new StringProperties((String)preferences[i]);
//			if( prop.getPiece(ASSOCIATION_PREFERENCE_NAME).equals(name)) {
//				return prop;
//			}
//		}
//		return null;
	}
	
	/**
	 * Loads the preferences of type <code>prefix</code> and 
	 * checks if field <code>key</code> matches the parameter <code>value</code>.
	 * Then returns the matching one.
	 * @param prefix
	 * @param key
	 * @param value
	 * @return
	 */
	public static StringProperties getMatchingPreference(String prefix, int key, String value) {
		Object[] preferences = loadIncrementalPreferences(prefix);
		for( int i = 0; i < preferences.length; i++ ) {
			StringProperties prop = new StringProperties((String)preferences[i]);
			if( prop.getPiece(key).equals(value)) {
				return prop;
			}
		}
		return null;
		
	}
}
