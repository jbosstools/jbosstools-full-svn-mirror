/*******************************************************************************
 * Copyright (c) 2007-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;

/**
 * Class that set flag for synchronize scrolling between source and visual panes 
 */
public class ScrollLockSourceVisualHandler extends VisualPartAbstractHandler {

	public static final String COMMAND_ID="org.jboss.tools.vpe.commands.scrollLockSourceVisual"; //$NON-NLS-1$
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		/*
		 * Change the enabled state, listeners in VpeController will do the rest
		 */
		JspEditorPlugin.getDefault().getPreferenceStore().setValue(
				IVpePreferencesPage.SYNCHRONIZE_SCROLLING_BETWEEN_SOURCE_VISUAL_PANES,
						!HandlerUtil.toggleCommandState(event.getCommand()));
		return null;
	}
}