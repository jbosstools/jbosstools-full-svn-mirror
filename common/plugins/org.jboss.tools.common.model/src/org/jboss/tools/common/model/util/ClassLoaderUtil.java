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
package org.jboss.tools.common.model.util;

import java.util.*;

import org.eclipse.core.runtime.Platform;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.osgi.framework.Bundle;

public class ClassLoaderUtil {
    public static ClassLoader getClassLoader() {
    	return ClassLoaderUtil.class.getClassLoader();    
    }
    
    public static Properties getProperties() {
    	return System.getProperties();
    }
    
    public static String[] getClasspath() {
    	return new String[0];
    }
    
	private static String[][] activation = {
		{"org.jboss.tools.jst.web.ui", "org.jboss.tools.jst.web.ui.WebUiPlugin", "required"},
		{"org.jboss.tools.jst.web", "org.jboss.tools.jst.web.WebModelPlugin", "required"},
		{"org.jboss.tools.jsf.ui", "org.jboss.tools.jsf.ui.JsfUiPlugin", "required"},
		{"org.jboss.tools.struts.ui", "org.jboss.tools.struts.ui.StrutsUIPlugin", "optional"},
		{"org.jboss.tools.common.verification.ui", "org.jboss.tools.common.verification.ui.XStudioVerificationPlugin", "optional"},
	};
	
	private static void activate(int from, int to) {
		for (int i = from; i < activation.length && i < to; i++) {
			Bundle b = Platform.getBundle(activation[i][0]);
			if(b == null) {
				if(activation[i].length >= 2 && "required".equals(activation[i][2])) {
					ModelPlugin.getPluginLog().logInfo("ClassLoaderUtil:activate: Cannot find required plugin " + activation[i][0]);
				}
			} else if(b.getState() != Bundle.ACTIVE) {
				String n = activation[i][1];
				try {
					b.loadClass(n);
				} catch (ClassNotFoundException e) {
					ModelPlugin.getPluginLog().logError("ClassLoaderUtil:activate: Cannot find class " + n);
				}
			}
		}
	}
	
	static Object lock = new Object();
	static boolean activated = false;
	
	public static void init() {
		if(activated) return;
		synchronized(lock) {
			if(activated) return;
			activated = true;
			activate(0, 1);
		}
		activate(1, activation.length);
	}
	
}
