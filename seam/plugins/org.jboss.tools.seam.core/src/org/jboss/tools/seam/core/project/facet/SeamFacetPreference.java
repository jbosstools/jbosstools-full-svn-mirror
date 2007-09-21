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

package org.jboss.tools.seam.core.project.facet;

import org.jboss.tools.seam.core.SeamCorePlugin;

/**
 * @author eskimo
 *
 */
public class SeamFacetPreference {
	public static final String SEAM_RUNTIME_NAME = SeamCorePlugin.PLUGIN_ID + ".project.facet.runtime.name";
	public static final String SEAM_DEFAULT_CONNECTION_PROFILE = SeamCorePlugin.PLUGIN_ID + ".project.facet.default.conn.profile";
	public static final String RUNTIME_CONFIG_FORMAT_VERSION = SeamCorePlugin.PLUGIN_ID + ".runtime.config.format.version";
	public static final String RUNTIME_LIST = SeamCorePlugin.PLUGIN_ID+".runtime.list";
	public static final String RUNTIME_DEFAULT = SeamCorePlugin.PLUGIN_ID+".runtime.default";
	
	
	public static String getStringPreference(final String name) {
		return SeamCorePlugin.getDefault().getPreferenceStore().getString(name);
	}
}
