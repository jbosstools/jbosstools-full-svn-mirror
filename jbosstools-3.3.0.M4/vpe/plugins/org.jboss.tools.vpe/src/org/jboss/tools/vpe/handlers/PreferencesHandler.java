/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
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
import org.jboss.tools.vpe.editor.preferences.VpeEditorPreferencesPage;

/**
 * Handler for Preferences
 */
public class PreferencesHandler extends VisualPartAbstractHandler {
	public static final String COMMAND_ID = "org.jboss.tools.vpe.commands.preferencesCommand"; //$NON-NLS-1$
	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		VpeEditorPreferencesPage.openPreferenceDialog();
		return null;
	}
}
