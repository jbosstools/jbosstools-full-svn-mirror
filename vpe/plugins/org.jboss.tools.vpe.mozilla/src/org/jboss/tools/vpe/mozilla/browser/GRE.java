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

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.common.log.LogHelper;
import org.jboss.tools.vpe.mozilla.MozillaJavaXpComPlugin;

/**
 * @author Sergey Vasilyev
 */
public class GRE {
	public static String grePath = null;
	public static String mozillaPath = null;
	
	static {
		grePath = getGREPath();
		mozillaPath = getMozillaPath();
	}
	
	static IPath getBasePath()
	throws UnsupportedWindowsSystemException {
		FragmentPathFactory fragmentPathFactory = 
			new FragmentPathFactory("org.jboss.tools.vpe.mozilla",Platform.getWS());
		IFragmentPath fragmentPath = fragmentPathFactory.createFactory(); 
		return fragmentPath.getPath();
	} // getBasePath()
	
	static String getGREPath() {
		try {
			return getBasePath().toOSString();
		} catch (UnsupportedWindowsSystemException e) {
			LogHelper.logError(MozillaJavaXpComPlugin.ID, e);
			return null;
		}
	} // getGREPath()

	static String getMozillaPath() {
		return getGREPath();
	} // getMozillaPath()
} // class GRE
