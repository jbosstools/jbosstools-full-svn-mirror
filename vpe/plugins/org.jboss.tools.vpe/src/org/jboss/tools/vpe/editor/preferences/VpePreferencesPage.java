/*******************************************************************************
 * Copyright (c) 2007-2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.preferences;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.State;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.editor.IVisualEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;
import org.jboss.tools.jst.jsp.selection.bar.SelectionBarHandler;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.handlers.ScrollLockSourceVisualHandler;
import org.jboss.tools.vpe.handlers.ShowBorderHandler;
import org.jboss.tools.vpe.handlers.ShowBundleAsELHandler;
import org.jboss.tools.vpe.handlers.ShowNonVisualTagsHandler;
import org.jboss.tools.vpe.handlers.ShowTextFormattingHandler;
import org.jboss.tools.vpe.messages.VpeUIMessages;

public class VpePreferencesPage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage, IVpePreferencesPage {

	private static final String[][] DEFAULT_VPE_TAB_COMBO_BOX_VALUES = {
			{ VpeUIMessages.DEFAULT_VPE_TAB_VISUAL_SOURCE,
					DEFAULT_VPE_TAB_VISUAL_SOURCE_VALUE },
			{ VpeUIMessages.DEFAULT_VPE_TAB_SOURCE,
					DEFAULT_VPE_TAB_SOURCE_VALUE },
			{ VpeUIMessages.DEFAULT_VPE_TAB_PREVIEW,
					DEFAULT_VPE_TAB_PREVIEW_VALUE } };
	private static final String[][] SPLITTING_COMBO_BOX_VALUES = {
			{ VpeUIMessages.SPLITTING_VERT_TOP_SOURCE,
					SPLITTING_VERT_TOP_SOURCE_VALUE },
			{ VpeUIMessages.SPLITTING_VERT_TOP_VISUAL,
					SPLITTING_VERT_TOP_VISUAL_VALUE },
			{ VpeUIMessages.SPLITTING_HORIZ_LEFT_SOURCE,
					SPLITTING_HORIZ_LEFT_SOURCE_VALUE },
			{ VpeUIMessages.SPLITTING_HORIZ_LEFT_VISUAL,
					SPLITTING_HORIZ_LEFT_VISUAL_VALUE } };

	private Composite pageContainer;
	private Group visualAppearanceGroup;
	private Group confirmationGroup;
	private Group tabsGroup;
	private Group visualEditorToolbarGroup;

	private ICommandService commandService = null;
	
	public VpePreferencesPage() {
		super();
		setPreferenceStore(getPreferenceStore());
	}

	public void init(IWorkbench workbench) {
		/*
		 * Do nothing
		 */
	}

	public String getTitle() {
		return VpeUIMessages.GENERAL_TAB_TITLE;
	}

	@Override
	protected Control createContents(Composite parent) {

		pageContainer = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		layout.horizontalSpacing = 10;
		layout.verticalSpacing = 10;
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		pageContainer.setLayout(layout);
		pageContainer.setLayoutData(gd);
		
		visualEditorToolbarGroup = createLayoutGroup(pageContainer,
				SWT.SHADOW_ETCHED_IN,
				VpeUIMessages.VISUAL_EDITOR_TOOLBAR_BEHAVIOR);
		
		visualAppearanceGroup = createLayoutGroup(pageContainer,
				SWT.SHADOW_ETCHED_IN,
				VpeUIMessages.VISUAL_APPEARANCE_GROUP_TITLE);
		
		confirmationGroup = createLayoutGroup(pageContainer,
				SWT.SHADOW_ETCHED_IN, 
				VpeUIMessages.CONFIRMATION_GROUP_TITLE);
		
		tabsGroup = createLayoutGroup(pageContainer, 
				SWT.SHADOW_ETCHED_IN, 
				VpeUIMessages.TABS_GROUP_TITLE);

		createFieldEditors();
		initialize();
		checkState();

		return pageContainer;
	}

	@Override
	protected Composite getFieldEditorParent() {
		return pageContainer;
	}

	@Override
	protected void createFieldEditors() {
		addField(new VpeBooleanFieldEditor(SHOW_VISUAL_TOOLBAR,
				VpeUIMessages.SHOW_VPE_TOOLBAR,
				visualEditorToolbarGroup));
		addField(new VpeBooleanFieldEditor(SHOW_BORDER_FOR_UNKNOWN_TAGS,
				VpeUIMessages.SHOW_BORDER_FOR_UNKNOWN_TAGS,
				visualAppearanceGroup));
		addField(new VpeBooleanFieldEditor(SHOW_NON_VISUAL_TAGS,
				VpeUIMessages.SHOW_NON_VISUAL_TAGS, visualAppearanceGroup));
		addField(new VpeBooleanFieldEditor(SHOW_SELECTION_TAG_BAR,
				VpeUIMessages.SHOW_SELECTION_TAG_BAR, visualAppearanceGroup));
		addField(new VpeBooleanFieldEditor(SHOW_TEXT_FORMATTING,
				VpeUIMessages.SHOW_TEXT_FORMATTING, visualAppearanceGroup));
		addField(new VpeBooleanFieldEditor(SHOW_RESOURCE_BUNDLES_USAGE_AS_EL,
				VpeUIMessages.SHOW_RESOURCE_BUNDLES_USAGE_AS_EL,
				visualAppearanceGroup));
		
		addField(new VpeColorFieldEditor(SELECTION_VISIBLE_BORDER_COLOR, 
				VpeUIMessages.SELECTION_VISIBLE_BORDER_COLOR, visualAppearanceGroup));
		addField(new VpeColorFieldEditor(SELECTION_HIDDEN_BORDER_COLOR, 
				VpeUIMessages.SELECTION_HIDDEN_BORDER_COLOR, visualAppearanceGroup));
		
		addField(new VpeBooleanFieldEditor(ASK_TAG_ATTRIBUTES_ON_TAG_INSERT,
				VpeUIMessages.ASK_TAG_ATTRIBUTES_ON_TAG_INSERT,
				confirmationGroup));
		addField(new VpeBooleanFieldEditor(INFORM_WHEN_PROJECT_MIGHT_NOT_BE_CONFIGURED_PROPERLY_FOR_VPE,
				VpeUIMessages.INFORM_WHEN_PROJECT_MIGHT_NOT_BE_CONFIGURED_PROPERLY_FOR_VPE,
				confirmationGroup));
		addField(new VpeBooleanFieldEditor(SYNCHRONIZE_SCROLLING_BETWEEN_SOURCE_VISUAL_PANES,
				VpeUIMessages.SYNCHRONIZE_SCROLLING_BETWEEN_SOURCE_VISUAL_PANES, tabsGroup));
		addField(new VpeComboFieldEditor(DEFAULT_VPE_TAB,
				VpeUIMessages.DEFAULT_VPE_TAB,
				DEFAULT_VPE_TAB_COMBO_BOX_VALUES, tabsGroup));
		addField(new VpeComboFieldEditor(VISUAL_SOURCE_EDITORS_SPLITTING,
				VpeUIMessages.VISUAL_SOURCE_EDITORS_SPLITTING,
				SPLITTING_COMBO_BOX_VALUES, tabsGroup));
		addField(new SliderFieldEditor(VISUAL_SOURCE_EDITORS_WEIGHTS,
				VpeUIMessages.VISUAL_SOURCE_EDITORS_WEIGHTS, tabsGroup));
	}

	@Override
	public boolean performOk() {
		super.performOk();
		IEditorReference[] editors = VpePlugin.getDefault().getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();
		for (IEditorReference editor : editors) {
			IEditorPart editorPart = editor.getEditor(false);
			if ((editorPart != null)
					&& (editorPart instanceof JSPMultiPageEditor)) {
				JSPMultiPageEditor mpe = (JSPMultiPageEditor) editorPart;
				IVisualEditor visualEditor = (mpe).getVisualEditor();
				if (visualEditor instanceof VpeEditorPart) {
					VpeEditorPart vep = (VpeEditorPart) visualEditor;
					/*
					 * Update visual editor
					 */
					vep.updatePartAccordingToPreferences();
					/*
					 * Change selected tab Commented to fix
					 * https://jira.jboss.org/jira/browse/JBIDE-4941 Do not
					 * update VPE splitting, weights, tabs for current page,
					 * only for newly opened.
					 */
					// mpe.updatePartAccordingToPreferences();
					
					/*
					 * https://issues.jboss.org/browse/JBIDE-10745
					 */
					boolean presfShowBorderForUnknownTags = JspEditorPlugin.getDefault()
							.getPreferenceStore().getBoolean(IVpePreferencesPage.SHOW_BORDER_FOR_UNKNOWN_TAGS);
					boolean prefsShowNonVisualTags = JspEditorPlugin.getDefault()
							.getPreferenceStore().getBoolean(IVpePreferencesPage.SHOW_NON_VISUAL_TAGS);
					boolean prefsShowSelectionBar = JspEditorPlugin.getDefault()
							.getPreferenceStore().getBoolean(IVpePreferencesPage.SHOW_SELECTION_TAG_BAR);
					boolean prefsShowTextFormattingBar = JspEditorPlugin.getDefault()
							.getPreferenceStore().getBoolean(IVpePreferencesPage.SHOW_TEXT_FORMATTING);
					boolean prefsShowBundlesAsEL = JspEditorPlugin.getDefault()
							.getPreferenceStore().getBoolean(IVpePreferencesPage.SHOW_RESOURCE_BUNDLES_USAGE_AS_EL);
					boolean prefsSynchronizeScrolling = JspEditorPlugin.getDefault()
							.getPreferenceStore().getBoolean(IVpePreferencesPage.SYNCHRONIZE_SCROLLING_BETWEEN_SOURCE_VISUAL_PANES);
					
					setCommandToggleState(ShowBorderHandler.COMMAND_ID, presfShowBorderForUnknownTags);
					setCommandToggleState(ShowNonVisualTagsHandler.COMMAND_ID, prefsShowNonVisualTags);
					setCommandToggleState(SelectionBarHandler.COMMAND_ID, prefsShowSelectionBar);
					setCommandToggleState(ShowTextFormattingHandler.COMMAND_ID, prefsShowTextFormattingBar);
					setCommandToggleState(ShowBundleAsELHandler.COMMAND_ID, prefsShowBundlesAsEL);
					setCommandToggleState(ScrollLockSourceVisualHandler.COMMAND_ID, prefsSynchronizeScrolling);
				}
			}
		}
		return true;
	}
	
	/**
	 * Set toolbar command state after Preferences Page has been changed.
	 * @param commandId command id
	 * @param newState new command state
	 */
	private void setCommandToggleState(String commandId, boolean newState) {
		if (commandService == null) {
			commandService = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
		}
		Command command = commandService.getCommand(commandId);
		State state = command.getState("org.eclipse.ui.commands.toggleState"); //$NON-NLS-1$
		boolean oldState = ((Boolean) state.getValue()).booleanValue();
		if (oldState != newState) {
			state.setValue(new Boolean(newState));
		}
	}

	@Override
	public IPreferenceStore getPreferenceStore() {
		return JspEditorPlugin.getDefault().getPreferenceStore();
	}
/**
 * Creates a layout group for vpe preferences
 * @param parent
 * @param style
 * @param groupTitle
 * @return layout group for VPE Preferences
 * @author mareshkau
 */
	private static Group createLayoutGroup(final Composite parent,final int style, final String groupTitle){
		Group prefGroup = new Group(parent, style);
		prefGroup.setText(groupTitle);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		layout.horizontalSpacing = 10;
		layout.verticalSpacing = 10;
		prefGroup.setLayout(layout);
		GridData gd = new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1);
		prefGroup.setLayoutData(gd);
		return prefGroup;
	}
}
