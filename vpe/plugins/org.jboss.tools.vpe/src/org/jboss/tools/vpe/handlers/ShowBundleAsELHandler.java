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
 * Handler for ShowBundleAsEL
 */
public class ShowBundleAsELHandler extends ShowOptionAbstractHandler {
	public static final String COMMAND_ID = "org.jboss.tools.vpe.commands.showBundleAsELCommand"; //$NON-NLS-1$

	@Override
	public String getPreferenceKey() {
		return IVpePreferencesPage.SHOW_RESOURCE_BUNDLES_USAGE_AS_EL;
	}

	@Override
	protected void toogleShow(VpeController vpeController,
			boolean state) {
		vpeController.getPageContext().getBundle()
		.updateShowBundleUsageAsEL(state);	
	}
}
