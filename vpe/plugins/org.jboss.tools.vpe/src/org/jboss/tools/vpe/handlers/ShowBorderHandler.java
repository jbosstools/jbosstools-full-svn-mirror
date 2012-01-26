/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.vpe.handlers;

import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;
import org.jboss.tools.vpe.editor.VpeController;

/**
 * Handler for ShowBorder
 */
public class ShowBorderHandler extends ShowOptionAbstractHandler {
	public static final String COMMAND_ID = "org.jboss.tools.vpe.commands.showBorderCommand"; //$NON-NLS-1$

	@Override
	public String getPreferenceKey() {
		return IVpePreferencesPage.SHOW_BORDER_FOR_UNKNOWN_TAGS;
	}

	@Override
	protected void toogleShow(VpeController vpeController, boolean state) {
		vpeController.getVisualBuilder().setShowBorderForUnknownTags(state);
	}
}