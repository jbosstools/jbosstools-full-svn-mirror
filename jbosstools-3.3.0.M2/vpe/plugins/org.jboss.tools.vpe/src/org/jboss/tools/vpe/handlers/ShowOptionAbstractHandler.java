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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.editor.VpeController;

/**
 * @author mareshkau
 *
 */
public abstract class ShowOptionAbstractHandler extends VisualPartAbstractHandler{
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		boolean toggleState = !HandlerUtil.toggleCommandState(event
				.getCommand());
		
		JspEditorPlugin
				.getDefault()
				.getPreferenceStore()
				.setValue(getPreferenceKey(),
						toggleState);
		
		IEditorReference[] openedEditors = PlatformUI.getWorkbench()
		.getActiveWorkbenchWindow().getActivePage()
		.getEditorReferences();
		for (IEditorReference openedEditor : openedEditors) {
			IEditorPart editor = openedEditor.getEditor(true);
			toggleShowOption(editor, toggleState);
		}
		return null;
	}	
	private void toggleShowOption(IEditorPart editor, boolean toggleState) {
		
		if (!(editor instanceof JSPMultiPageEditor)) {
			return;
		}

		JSPMultiPageEditor jspEditor = (JSPMultiPageEditor) editor;
		VpeController vpeController = (VpeController) jspEditor
				.getVisualEditor().getController();
		if (vpeController == null) {
			return;
		}

		/*
		 * Set new value to VpeVisualDomBuilder.
		 */
		toogleShow(vpeController,toggleState);
		/*
		 * Update VPE
		 */
		vpeController.visualRefresh();
	}
	public abstract String getPreferenceKey();
	/**
	 * Here should be changed appropriate property in visual builder
	 * @param vpeVisualDomBuilder
	 * @param state
	 */
	protected abstract void toogleShow(VpeController vpeController, boolean state);
}
