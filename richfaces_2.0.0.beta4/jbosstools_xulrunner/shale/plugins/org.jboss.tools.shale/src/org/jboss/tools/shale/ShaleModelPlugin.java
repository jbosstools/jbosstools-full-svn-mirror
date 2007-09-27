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
package org.jboss.tools.shale;

import org.jboss.tools.common.log.BaseUIPlugin;
import org.jboss.tools.common.log.IPluginLog;

public class ShaleModelPlugin extends BaseUIPlugin {
	public static final String PLUGIN_ID = "org.jboss.tools.shale";

	public ShaleModelPlugin() {
		super();
		INSTANCE = this;
	}

	public static boolean isDebugEnabled() {
		return INSTANCE.isDebugging();
	}

	public static ShaleModelPlugin getDefault() {
		return INSTANCE;
	}
	
	static ShaleModelPlugin INSTANCE = null; 

	/**
	 * @return IPluginLog object
	 */
	public static IPluginLog getPluginLog() {
		return getDefault();
	}

}
