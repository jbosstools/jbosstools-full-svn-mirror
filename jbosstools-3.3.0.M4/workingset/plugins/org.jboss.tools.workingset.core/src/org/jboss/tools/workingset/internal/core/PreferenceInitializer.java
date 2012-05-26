package org.jboss.tools.workingset.internal.core;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import org.jboss.tools.workingset.core.Activator;
import org.jboss.tools.workingset.core.PreferenceConstants;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_ENABLE, false);
		store
				.setDefault(
						PreferenceConstants.P_PATTERNS,
						"org\\.([^\\.]+).*,$1,false" // major group, eclipse, jboss, hibernate, etc.
								//+ ";org\\.hibernate\\.eclipse\\.([^\\.]+).*,hibernate,true" // put hibernate tools in hibernate, not needed when grouping globally
								+ ";org\\.jboss\\.ide\\.eclipse\\.([^\\.]+).*,$1,true" // put as and archives in their own group
								//+ ";org\\.jbpm\\.gd\\.([^\\.]+).*,jbpm,true"
								+ ";org\\.jboss\\.tools\\.([^\\.]+).*,$1-jbt,true" // split out group for jboss tools
								+ ";org\\.eclipse\\.([^\\.]+).*,$1-eclipse,true"); // split out group for eclipse

	}
}
