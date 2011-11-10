/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.common.el.ui.ca;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author jeremy
 *
 */
public class Messages  extends NLS {

	private static final String BUNDLE_NAME = "org.jboss.tools.common.el.ui.ca.messages";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	public static String NO_JAVADOC;
	
	public static String ELInfoHover_baseName; 
	public static String ELInfoHover_propertyName;
	public static String ELInfoHover_resourceBundle; 
	public static String ELInfoHover_resourceBundlePropertyValue;
	public static String ELInfoHover_resourceBundlePropertyValueNotDefined; 
	public static String ELInfoHover_resourceBundleNotDefined;
	public static String ELInfoHover_newLine;
	public static String ELInfoHover_treeDots;

}