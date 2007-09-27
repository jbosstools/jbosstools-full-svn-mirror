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

import java.lang.reflect.Constructor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;

/**
 * @author Sergey Vasilyev
 */
public class FragmentPathFactory {
	private Constructor factoryConstructor;
	private String pluginId; 
	public static final String WS_WIN32 = "win32";
	public static final String WS_GTK = "gtk";
	private Loger loger;
	
	public FragmentPathFactory(String pluginId, String ws)
	throws
		UnsupportedWindowsSystemException,
		RuntimeException {
		this.pluginId = pluginId;
		loger = new Loger(pluginId);
		
		Class[] params = { String.class };
		Class factoryClass;
		
		if (WS_WIN32.equals(ws)) {
			factoryClass = FragmentPathWin32.class;
		} else if (WS_GTK.equals(ws)) {
			factoryClass = FragmentPathGtk.class;			
		} else {
			UnsupportedWindowsSystemException e =
				new UnsupportedWindowsSystemException(Platform.getWS());
			loger.log(IStatus.ERROR,e.getMessage(),e);
			throw e;
		} 
		
		try {
			factoryConstructor = factoryClass.getConstructor(params);
		} catch (Exception e) {
			loger.log(IStatus.ERROR,"Error getting constructor for factory class WS="+ws,e);
			throw new RuntimeException(e);
		}
	} // FragmentPathFactory(String)
	
	public IFragmentPath createFactory()
	throws RuntimeException {
		Object[] args = { pluginId };
		Object factory;
		
		try {
			factory = factoryConstructor.newInstance(args);
		} catch (Exception e) {
			loger.log(IStatus.ERROR,"Error creating factory instance",e);
			throw new RuntimeException(e);
		} // try
		return (IFragmentPath)factory;
	} // createFactory()
} // class FragmentPathFactory
