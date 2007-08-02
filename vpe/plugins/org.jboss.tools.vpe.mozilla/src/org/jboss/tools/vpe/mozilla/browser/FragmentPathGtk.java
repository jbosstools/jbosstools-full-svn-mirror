/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.mozilla.browser;

/**
 * @author Sergey Vasilyev
 * FragmentPath for Gtk Windows System 
 */
public class FragmentPathGtk extends FragmentPath {
	public static final String WS_GTK = "gtk";
	public FragmentPathGtk(String pluginId) {
		super(pluginId);
	} // FragmentPathGtk(String)
	
	public String getFragmentId() {
		return getPluginId()+"."+WS_GTK;
	} // getFragmentId()
	
	public String getResultLocation(String location) {
		int pos = location.indexOf('@');
		if (pos != -1) {
			return location.substring(pos + 1);
		}
		
		return location;
	} // getResultLocation(String)
} // class FragmentPathGtk
