/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.jboss.tools.common.preferences.SeverityPreferences;
import org.jboss.tools.jst.web.kb.WebKbPlugin;

/**
 * @author Viacheslav Kabanovich
 */
public class ELPreferenceInitializer extends AbstractPreferenceInitializer {

	public ELPreferenceInitializer() {}

	@Override
	public void initializeDefaultPreferences() {

		IEclipsePreferences defaultPreferences = ((IScopeContext) new DefaultScope()).getNode(WebKbPlugin.PLUGIN_ID);
		
		//EL Preferences
		defaultPreferences.putBoolean(SeverityPreferences.ENABLE_BLOCK_PREFERENCE_NAME, true);
		defaultPreferences.put(SeverityPreferences.WRONG_BUILDER_ORDER_PREFERENCE_NAME, ELSeverityPreferences.ERROR);
		for (String name : ELSeverityPreferences.SEVERITY_OPTION_NAMES) {
			defaultPreferences.put(name, ELSeverityPreferences.ERROR);
		}
		defaultPreferences.put(ELSeverityPreferences.UNKNOWN_EL_VARIABLE_NAME, ELSeverityPreferences.IGNORE);
		defaultPreferences.put(ELSeverityPreferences.UNKNOWN_EL_VARIABLE_PROPERTY_NAME, ELSeverityPreferences.WARNING);
		defaultPreferences.put(ELSeverityPreferences.UNPAIRED_GETTER_OR_SETTER, ELSeverityPreferences.IGNORE);
		defaultPreferences.put(ELSeverityPreferences.EL_SYNTAX_ERROR, ELSeverityPreferences.WARNING);
		defaultPreferences.put(ELSeverityPreferences.CHECK_VARS, ELSeverityPreferences.ENABLE);
		defaultPreferences.put(ELSeverityPreferences.RE_VALIDATE_UNRESOLVED_EL, ELSeverityPreferences.ENABLE);
		defaultPreferences.putInt(SeverityPreferences.MAX_NUMBER_OF_MARKERS_PREFERENCE_NAME, SeverityPreferences.DEFAULT_MAX_NUMBER_OF_MARKERS_PER_FILE);
		defaultPreferences.put(ELSeverityPreferences.NON_EXTERNALIZED_STRINGS, ELSeverityPreferences.IGNORE);

		//KB preferences
		defaultPreferences.put(KBSeverityPreferences.REQUIRED_KB_CAPABILITIES_ARE_MISSING, SeverityPreferences.WARNING);
		defaultPreferences.put(KBSeverityPreferences.KB_CAPABILITIES_ARE_NOT_ENABLED_IN_JAVA_MODULE, SeverityPreferences.WARNING);
	}
}