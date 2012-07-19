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

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.HandlerEvent;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.menus.UIElement;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.messages.VpeUIMessages;

/**
 * Handler for RotateEditors
 */
public class RotateEditorsHandler extends VisualPartAbstractHandler{
	
	public static final String COMMAND_ID="org.jboss.tools.vpe.commands.rotateEditorsCommand"; //$NON-NLS-1$
	
	private static List<String> layoutValues;
	private static Map<String, String> layoutIcons;
	private static Map<String, String> layoutNamesAndTooltips;
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

		layoutNamesAndTooltips = new HashMap<String, String>();
		layoutNamesAndTooltips.put(IVpePreferencesPage.SPLITTING_HORIZ_LEFT_SOURCE_VALUE,
				VpeUIMessages.SPLITTING_HORIZ_LEFT_SOURCE_TOOLTIP);
		layoutNamesAndTooltips.put(IVpePreferencesPage.SPLITTING_VERT_TOP_SOURCE_VALUE,
				VpeUIMessages.SPLITTING_VERT_TOP_SOURCE_TOOLTIP);
		layoutNamesAndTooltips.put(IVpePreferencesPage.SPLITTING_HORIZ_LEFT_VISUAL_VALUE,
				VpeUIMessages.SPLITTING_HORIZ_LEFT_VISUAL_TOOLTIP);
		layoutNamesAndTooltips.put(IVpePreferencesPage.SPLITTING_VERT_TOP_VISUAL_VALUE,
				VpeUIMessages.SPLITTING_VERT_TOP_VISUAL_TOOLTIP);

		layoutValues = new ArrayList<String>();
		layoutValues.add(IVpePreferencesPage.SPLITTING_HORIZ_LEFT_SOURCE_VALUE);
		layoutValues.add(IVpePreferencesPage.SPLITTING_VERT_TOP_SOURCE_VALUE);
		layoutValues.add(IVpePreferencesPage.SPLITTING_HORIZ_LEFT_VISUAL_VALUE);
		layoutValues.add(IVpePreferencesPage.SPLITTING_VERT_TOP_VISUAL_VALUE);
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPreferenceStore preferences = JspEditorPlugin.getDefault().getPreferenceStore();
		String orientation = preferences.getString
				(IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING);
		int currentOrientationIndex = layoutValues.indexOf(orientation);
		/*
		 * Rotate editors orientation clockwise.
		 */
		currentOrientationIndex++;
		if (currentOrientationIndex >= layoutValues.size()) {
			currentOrientationIndex = currentOrientationIndex % layoutValues.size();
		}

		String newOrientation = layoutValues.get(currentOrientationIndex);
		preferences.setValue(
				IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING,
				newOrientation);
		IEditorReference[] openedEditors = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();
		for (IEditorReference openedEditor : openedEditors) {
			IEditorPart editor = openedEditor.getEditor(true);
			rotateEditor(editor, newOrientation);
		}
		Command command = event.getCommand();
		ICommandService commandService = (ICommandService) PlatformUI
				.getWorkbench().getService(ICommandService.class);
		commandService.refreshElements(command.getId(), null);
		return null;
	}

	public void updateElement(UIElement element, Map parameters) {
		IPreferenceStore preferences = JspEditorPlugin.getDefault().getPreferenceStore();
		String orientation = preferences.getString(
				IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING);
		if (orientation.isEmpty()) {
			orientation = IVpePreferencesPage.SPLITTING_HORIZ_LEFT_SOURCE_VALUE;
			preferences.setValue(
					IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING,
					orientation);
		}
		/*
		 * https://issues.jboss.org/browse/JBIDE-12329
		 * 1) MenuHelper.getIconURI(..) used from SlaveCommandService
		 * to set the icon to the element cannot always find the path 
		 * to the icon used in ImageDescriptor(esp. from FileImageDescriptor).
		 * Thus ImageDescriptor should be of type URLImageDescriptor
		 * 2) Text is not required, icon is enough. 
		 */
		element.setIcon(VpePlugin.imageDescriptorFromPlugin(
				VpePlugin.PLUGIN_ID, layoutIcons.get(orientation)));
		element.setTooltip(layoutNamesAndTooltips.get(orientation));
		/*
		 * https://issues.jboss.org/browse/JBIDE-12344
		 * Listeners should get this event and update the icons state.
		 * This method is overridden from VisualPartAbstractHandler, 
		 *  so should fire the event also.
		 */
		fireHandlerChanged(new HandlerEvent(this, true, false));
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
