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

import org.eclipse.gef.ui.palette.DefaultPaletteViewerPreferences;
import org.eclipse.jface.preference.IPreferenceStore;

public class PaletteViewerPreferences extends DefaultPaletteViewerPreferences {

	public PaletteViewerPreferences() {
		this(PalettePlugin.getDefault().getPreferenceStore());
	}

	public PaletteViewerPreferences(IPreferenceStore store) {
		super(store);
	}
}
