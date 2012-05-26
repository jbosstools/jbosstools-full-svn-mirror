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
package org.jboss.tools.vpe.ui.palette;

import org.eclipse.core.runtime.IAdaptable;
import org.jboss.tools.common.log.BaseUIPlugin;
import org.jboss.tools.common.log.IPluginLog;
import org.jboss.tools.common.model.ui.views.palette.IPaletteAdapter;

public class PalettePlugin extends BaseUIPlugin implements IAdaptable {

	public static final String PLUGIN_ID = "org.jboss.tools.vpe.ui.palette"; //$NON-NLS-1$
	private static PalettePlugin plugin;

	public PalettePlugin() { 
		plugin = this;
	}

	public static PalettePlugin getDefault() {
		return plugin;
	}

	public Object getAdapter(Class adapter) {
		if (adapter == (IPaletteAdapter.class))
			return new PaletteAdapter();
		else
			return null;
	}
	
	public static IPluginLog getPluginLog() {
		return getDefault();
	}
}