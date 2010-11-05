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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;

/**
 * Handler for Refresh
 */
public class RefreshHandler extends VisualPartAbstractHandler {
	
	public static final String COMMAND_ID = "org.jboss.tools.vpe.commands.refreshCommand"; //$NON-NLS-1$
	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart activeEditor = HandlerUtil.getActiveEditorChecked(event);
		if (activeEditor instanceof JSPMultiPageEditor) {
			JSPMultiPageEditor jspEditor = (JSPMultiPageEditor) activeEditor;
			jspEditor.getVisualEditor().getController().visualRefresh();
		}
		return null;
	}
}
