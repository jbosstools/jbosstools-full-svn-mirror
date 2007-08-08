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
package org.jboss.tools.vpe.mozilla;

import org.jboss.tools.common.log.BaseUIPlugin;
import org.jboss.tools.common.log.IPluginLog;

/**
 * 
 * @author eskimo(dgolovin@exadel.com)
 */
public class MozillaJavaXpComPlugin extends BaseUIPlugin {
	public static final String PLUGIN_ID = "org.jboss.tools.vpe.mozilla";
	
	private static MozillaJavaXpComPlugin instance;
	
	public MozillaJavaXpComPlugin() {
		instance = this;
	}
	
	public static MozillaJavaXpComPlugin getDefault() {
		return instance;
	}
	
	public static IPluginLog getPluginLog() {
		return getDefault();
	}
}
