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

import org.eclipse.jface.preference.BooleanFieldEditor;
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
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.editor.IVisualEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeEditorPart;
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

		visualAppearanceGroup = new Group(pageContainer, SWT.SHADOW_ETCHED_IN);
		visualAppearanceGroup
				.setText(VpeUIMessages.VISUAL_APPEARANCE_GROUP_TITLE);
		layout = new GridLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		layout.horizontalSpacing = 10;
		layout.verticalSpacing = 10;
		visualAppearanceGroup.setLayout(layout);
		gd = new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1);
		visualAppearanceGroup.setLayoutData(gd);

		confirmationGroup = new Group(pageContainer, SWT.SHADOW_ETCHED_IN);
		confirmationGroup.setText(VpeUIMessages.CONFIRMATION_GROUP_TITLE);
		layout = new GridLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		layout.horizontalSpacing = 10;
		layout.verticalSpacing = 10;
		confirmationGroup.setLayout(layout);
		gd = new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1);
		confirmationGroup.setLayoutData(gd);

		tabsGroup = new Group(pageContainer, SWT.SHADOW_ETCHED_IN);
		tabsGroup.setText(VpeUIMessages.TABS_GROUP_TITLE);
		layout = new GridLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		tabsGroup.setLayout(layout);
		gd = new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1);
		tabsGroup.setLayoutData(gd);

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
		addField(new BooleanFieldEditor(SHOW_BORDER_FOR_UNKNOWN_TAGS,
				VpeUIMessages.SHOW_BORDER_FOR_UNKNOWN_TAGS,
				visualAppearanceGroup));
		addField(new BooleanFieldEditor(SHOW_NON_VISUAL_TAGS,
				VpeUIMessages.SHOW_NON_VISUAL_TAGS, visualAppearanceGroup));
		addField(new BooleanFieldEditor(SHOW_SELECTION_TAG_BAR,
				VpeUIMessages.SHOW_SELECTION_TAG_BAR, visualAppearanceGroup));
		addField(new BooleanFieldEditor(SHOW_TEXT_FORMATTING,
				VpeUIMessages.SHOW_TEXT_FORMATTING, visualAppearanceGroup));
		addField(new BooleanFieldEditor(SHOW_RESOURCE_BUNDLES_USAGE_AS_EL,
				VpeUIMessages.SHOW_RESOURCE_BUNDLES_USAGE_AS_EL,
				visualAppearanceGroup));
		addField(new BooleanFieldEditor(ASK_TAG_ATTRIBUTES_ON_TAG_INSERT,
				VpeUIMessages.ASK_TAG_ATTRIBUTES_ON_TAG_INSERT,
				confirmationGroup));
		addField(new BooleanFieldEditor(
				ASK_CONFIRMATION_ON_CLOSING_SELECTION_BAR,
				VpeUIMessages.ASK_CONFIRMATION_ON_CLOSING_SELECTION_BAR,
				confirmationGroup));
		addField(new BooleanFieldEditor(IGNORE_VPE_WARNINGS,
				VpeUIMessages.IGNORE_VPE_WARNINGS, confirmationGroup));
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
				}
			}
		}

		return true;
	}

	@Override
	public IPreferenceStore getPreferenceStore() {
		return JspEditorPlugin.getDefault().getPreferenceStore();
	}

}
