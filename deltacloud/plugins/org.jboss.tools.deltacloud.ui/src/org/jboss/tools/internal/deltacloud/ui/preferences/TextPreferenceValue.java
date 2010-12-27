/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.internal.deltacloud.ui.preferences;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * @author Andre Dietisheim
 */
public class TextPreferenceValue {

	private Plugin plugin;
	private String prefsKey;

	public TextPreferenceValue(String prefsKey, Plugin plugin) {
		this.plugin = plugin;
		this.prefsKey = prefsKey;
	}

	public String get() {
		return get(null);
	}
	
	public String get(String currentValue) {
		if( currentValue == null || currentValue.equals("")) {
			// pre-set with previously used
			Preferences prefs = getPreferences();
			return prefs.get(prefsKey, "");
		} else {
			return currentValue;
		}
	}

	public void store(String value) {
		Preferences prefs = getPreferences();
		String prefsValue = prefs.get(prefsKey, "");
		if (prefsValue == null || prefsValue.equals("") || !prefsValue.equals(value)) {
			prefs.put(prefsKey, value);
			try {
				prefs.flush();
			} catch (BackingStoreException bse) {
				// intentionally ignore, non-critical
			}
		}

	}

	protected Preferences getPreferences() {
		return new InstanceScope().getNode(plugin.getBundle().getSymbolicName());
	}
}
