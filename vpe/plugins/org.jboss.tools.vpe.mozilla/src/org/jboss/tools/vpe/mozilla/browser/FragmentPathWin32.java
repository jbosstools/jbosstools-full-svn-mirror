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
 */
public class FragmentPathWin32 extends FragmentPath {
	public static final String WS_WIN32 = "win32";
	
	public FragmentPathWin32(String pluginId) {
		super(pluginId);
	} // FragmentPathWin32(String)
	
	
	public String getFragmentId() {
		return getPluginId()+"."+WS_WIN32;
	} // getFragmentId()
	
	public String getResultLocation(String location) {
		int pos = location.indexOf('@');
		if (pos != -1) {
			return location.substring(pos + 1);
		}
		
		return location;
	} // getResultLocation(String)
} // class FragmentPathWin32
