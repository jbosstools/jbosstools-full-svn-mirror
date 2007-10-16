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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

/**
 * @author Sergey Vasilyev
 */
public class Loger {
	/*
	 * code for all exception
	 * (maybe need separated code for each specifically error?)
	 */
	private final int code = 0;  
	private String pluginId;
	public Loger(String pluginId) {
		this.pluginId = pluginId;
	}
	
	/**
	 * Write message about exception in eclipse log
	 * @param severity - see Status class
	 * @param message - see Status class
	 * @param e - see Status class
	 */
	public void log(int severity, String message, Throwable e) {
		IStatus status = new Status(severity, pluginId,
				code, message,	e);
		Platform.getLog(Platform.getBundle(pluginId)).log(status);
	}
}
