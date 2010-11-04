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

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;
import org.jboss.tools.vpe.editor.VpeController;

/**
 * Handler for ShowBundleAsEL
 */
public class ShowBundleAsELHandler extends AbstractHandler implements
		IElementUpdater {

	/**
	 * The constructor.
	 */
	public ShowBundleAsELHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		boolean oldToggleState = HandlerUtil.toggleCommandState(event
				.getCommand());
		JspEditorPlugin
				.getDefault()
				.getPreferenceStore()
				.setValue(
						IVpePreferencesPage.SHOW_RESOURCE_BUNDLES_USAGE_AS_EL,
						!oldToggleState);

		Command command = event.getCommand();
		ICommandService commandService = (ICommandService) PlatformUI
				.getWorkbench().getService(ICommandService.class);
		commandService.refreshElements(command.getId(), null);

		return null;
	}

	public void updateElement(UIElement element, Map parameters) {

		boolean toggleState = JspEditorPlugin
				.getDefault()
				.getPreferenceStore()
				.getBoolean(
						IVpePreferencesPage.SHOW_RESOURCE_BUNDLES_USAGE_AS_EL);

		IEditorReference[] openedEditors = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();
		for (IEditorReference openedEditor : openedEditors) {
			IEditorPart editor = openedEditor.getEditor(true);
			toggleShowBundleAsEl(editor, toggleState);
		}
	}

	private void toggleShowBundleAsEl(IEditorPart editor, boolean toggleState) {

		if (!(editor instanceof JSPMultiPageEditor)) {
			return;
		}

		JSPMultiPageEditor jspEditor = (JSPMultiPageEditor) editor;
		VpeController vpeController = (VpeController) jspEditor
				.getVisualEditor().getController();

		/*
		 * Update bundle messages.
		 */
		vpeController.getPageContext().getBundle()
				.updateShowBundleUsageAsEL(toggleState);
		vpeController.visualRefresh();
	}
}
