/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 

package org.jboss.tools.ws.core;

import org.eclipse.osgi.util.NLS;

public class JbossWSCoreMessages {

	private static final String BUNDLE_NAME = "org.jboss.tools.ws.core.JbossWSCore"; //$NON-NLS-1$

	private JbossWSCoreMessages() {
		// Do not instantiate
	}

	public static String PROGRESS_INSTALL_JBOSSWS_RUNTIME;
	public static String DIR_LIB;
	public static String DIR_WEB_INF;
	public static String DIR_WEB_CONTENT;
	public static String ERROR_COPY;


	static {
		NLS.initializeMessages(BUNDLE_NAME, JbossWSCoreMessages.class);
	}
}