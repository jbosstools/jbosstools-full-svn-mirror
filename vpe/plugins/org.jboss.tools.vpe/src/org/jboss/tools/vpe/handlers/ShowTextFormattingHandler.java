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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.toolbar.IVpeToolBarManager;
import org.jboss.tools.vpe.editor.toolbar.VpeToolBarManager;
import org.jboss.tools.vpe.editor.toolbar.format.FormatControllerManager;
import org.jboss.tools.vpe.editor.toolbar.format.TextFormattingToolBar;

/**
 * Handler for ShowTextFormatting
 */
public class ShowTextFormattingHandler extends AbstractHandler implements
		IElementUpdater {

	/**
	 * The constructor.
	 */
	public ShowTextFormattingHandler() {
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
				.setValue(IVpePreferencesPage.SHOW_TEXT_FORMATTING,
						!oldToggleState);

		Command command = event.getCommand();
		ICommandService commandService = (ICommandService) PlatformUI
				.getWorkbench().getService(ICommandService.class);
		commandService.refreshElements(command.getId(), null);

		return null;
	}

	public void updateElement(UIElement element, Map parameters) {

		boolean toggleState = JspEditorPlugin.getDefault().getPreferenceStore()
				.getBoolean(IVpePreferencesPage.SHOW_TEXT_FORMATTING);

		IEditorPart activeEditor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (!(activeEditor instanceof JSPMultiPageEditor)) {
			return;
		}

		JSPMultiPageEditor jspEditor = (JSPMultiPageEditor) activeEditor;
		MozillaEditor mozillaEditor = ((VpeEditorPart) jspEditor
				.getVisualEditor()).getVisualEditor();
		IVpeToolBarManager vpeToolBarManager = mozillaEditor
				.getVpeToolBarManager();

		/*
		 * Update Text Formatting Bar
		 */
		vpeToolBarManager.setToolbarVisibility(toggleState);
	}
}
