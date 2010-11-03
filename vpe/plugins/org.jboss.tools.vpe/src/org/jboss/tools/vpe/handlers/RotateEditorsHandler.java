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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.menus.UIElement;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.messages.VpeUIMessages;

/**
 * Handler for RotateEditors
 */
public class RotateEditorsHandler extends AbstractHandler implements
		IElementUpdater {

	private static List<String> layoutValues;
	private static Map<String, String> layoutIcons;
	private static Map<String, String> layoutNames;
	private static final String ICON_ORIENTATION_SOURCE_LEFT = "icons/source_left.gif"; //$NON-NLS-1$
	private static final String ICON_ORIENTATION_SOURCE_TOP = "icons/source_top.gif"; //$NON-NLS-1$
	private static final String ICON_ORIENTATION_VISUAL_LEFT = "icons/visual_left.gif"; //$NON-NLS-1$
	private static final String ICON_ORIENTATION_VISUAI_TOP = "icons/visual_top.gif"; //$NON-NLS-1$

	static {
		/*
		 * Values from <code>layoutValues</code> should correspond to the order
		 * when increasing the index of the array will cause the source editor
		 * rotation
		 */
		layoutIcons = new HashMap<String, String>();
		layoutIcons.put(IVpePreferencesPage.SPLITTING_HORIZ_LEFT_SOURCE_VALUE,
				ICON_ORIENTATION_SOURCE_LEFT);
		layoutIcons.put(IVpePreferencesPage.SPLITTING_VERT_TOP_SOURCE_VALUE,
				ICON_ORIENTATION_SOURCE_TOP);
		layoutIcons.put(IVpePreferencesPage.SPLITTING_HORIZ_LEFT_VISUAL_VALUE,
				ICON_ORIENTATION_VISUAL_LEFT);
		layoutIcons.put(IVpePreferencesPage.SPLITTING_VERT_TOP_VISUAL_VALUE,
				ICON_ORIENTATION_VISUAI_TOP);

		layoutNames = new HashMap<String, String>();
		layoutNames.put(IVpePreferencesPage.SPLITTING_HORIZ_LEFT_SOURCE_VALUE,
				VpeUIMessages.SPLITTING_HORIZ_LEFT_SOURCE);
		layoutNames.put(IVpePreferencesPage.SPLITTING_VERT_TOP_SOURCE_VALUE,
				VpeUIMessages.SPLITTING_VERT_TOP_SOURCE);
		layoutNames.put(IVpePreferencesPage.SPLITTING_HORIZ_LEFT_VISUAL_VALUE,
				VpeUIMessages.SPLITTING_HORIZ_LEFT_VISUAL);
		layoutNames.put(IVpePreferencesPage.SPLITTING_VERT_TOP_VISUAL_VALUE,
				VpeUIMessages.SPLITTING_VERT_TOP_VISUAL);

		layoutValues = new ArrayList<String>();
		layoutValues.add(IVpePreferencesPage.SPLITTING_HORIZ_LEFT_SOURCE_VALUE);
		layoutValues.add(IVpePreferencesPage.SPLITTING_VERT_TOP_SOURCE_VALUE);
		layoutValues.add(IVpePreferencesPage.SPLITTING_HORIZ_LEFT_VISUAL_VALUE);
		layoutValues.add(IVpePreferencesPage.SPLITTING_VERT_TOP_VISUAL_VALUE);
	}

	/**
	 * The constructor.
	 */
	public RotateEditorsHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IPreferenceStore preferences = JspEditorPlugin.getDefault()
				.getPreferenceStore();
		String orientation = preferences
				.getString(IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING);
		int currentOrientationIndex = layoutValues.indexOf(orientation);

		/*
		 * Rotate editors orientation clockwise.
		 */
		currentOrientationIndex++;
		if (currentOrientationIndex >= layoutValues.size()) {
			currentOrientationIndex = currentOrientationIndex
					% layoutValues.size();
		}

		String newOrientation = layoutValues.get(currentOrientationIndex);
		preferences.setValue(
				IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING,
				newOrientation);

		Command command = event.getCommand();
		ICommandService commandService = (ICommandService) PlatformUI
				.getWorkbench().getService(ICommandService.class);
		commandService.refreshElements(command.getId(), null);
		return null;
	}

	public void updateElement(UIElement element, Map parameters) {

		IPreferenceStore preferences = JspEditorPlugin.getDefault()
				.getPreferenceStore();
		String orientation = preferences
				.getString(IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING);
		if (orientation.isEmpty()) {
			orientation = IVpePreferencesPage.SPLITTING_HORIZ_LEFT_SOURCE_VALUE;
			preferences.setValue(
					IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING,
					orientation);
		}

		/*
		 * Update icon and tooltip
		 */
		element.setIcon(ImageDescriptor.createFromFile(this.getClass(),
				layoutIcons.get(orientation)));
		String orientationName = layoutNames.get(orientation);
		element.setTooltip(orientationName);
		element.setText(orientationName);

		/*
		 * Call <code>filContainer()</code> from VpeEditorPart to redraw
		 * CustomSashForm with new layout.
		 */
		IEditorReference[] openedEditors = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();
		for (IEditorReference openedEditor : openedEditors) {
			IEditorPart editor = openedEditor.getEditor(true);
			rotateEditor(editor, orientation);
		}
	}

	private void rotateEditor(IEditorPart editor, String orientation) {

		if (!(editor instanceof JSPMultiPageEditor)) {
			return;
		}

		JSPMultiPageEditor jspEditor = (JSPMultiPageEditor) editor;
		VpeController vpeController = (VpeController) jspEditor
				.getVisualEditor().getController();
		// if called in initialization time, vpe controller is null
		// added by Maksim Areshkau
		if (vpeController != null)
			vpeController.getPageContext().getEditPart()
					.fillContainer(true, orientation);

	}
}
