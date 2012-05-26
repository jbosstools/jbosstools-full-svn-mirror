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
package org.jboss.tools.esb.core;

import org.eclipse.osgi.util.NLS;

public class ESBCoreMessages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.esb.core.messages"; //$NON-NLS-1$

	public static String ESB_CORE_PLUGIN_NO_MESSAGE;
	
	public static String REFERENCES;
	public static String CHANNEL_ID_REFACTORING;
	public static String CHANNEL_ID;
	public static String CHANNEL_ID_REF;
	public static String SCHEDULE_ID;
	public static String SCHEDULE_ID_REF;

	static {
		NLS.initializeMessages(BUNDLE_NAME, ESBCoreMessages.class);
	}
}
