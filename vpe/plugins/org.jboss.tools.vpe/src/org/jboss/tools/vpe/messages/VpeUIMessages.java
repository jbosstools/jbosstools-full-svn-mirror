/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.messages;

import org.eclipse.osgi.util.NLS;

public final class VpeUIMessages extends NLS {
	private static final String BUNDLE_NAME
			= "org.jboss.tools.vpe.messages.messages";//$NON-NLS-1$
	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, VpeUIMessages.class);		
	}
	private VpeUIMessages(){}

	public static String ATTRIBUTES_MENU_ITEM;
	public static String SELECT_THIS_TAG_MENU_ITEM;
	public static String STRIP_TAG_MENU_ITEM;
	public static String PARENT_TAG_MENU_ITEM;
	public static String NAMESPACE_NOT_DEFINED;
	public static String PREFERENCES;
	public static String REFRESH;
	public static String PAGE_DESIGN_OPTIONS;
	public static String MOZILLA_LOADING_ERROR;
	public static String MOZILLA_LOADING_ERROR_LINK_TEXT;
	public static String MOZILLA_LOADING_ERROR_LINK;
	public static String GENERAL;
	public static String TEMPLATES;
	public static String TEMPLATE;
	public static String USER_SPECIFIED_TAG_DIALOG_DESCRIPTION;
	public static String TAG_ATTRIBUTES;
	public static String TAG_NAME;
	public static String TAG_URI;
	public static String TAG_FOR_DISPLAY;
	public static String TAG_STYLE;
	public static String CHILDREN;
	public static String VALUE;
	public static String TAG_NAME_IS_NOT_VALID;
	public static String TAG_FOR_DISPLAY_IS_NOT_VALID;
	public static String VALUE_IS_NOT_VALID;
	public static String TAG_NAME_ALREADY_EXISTS;
	public static String URI_TAGLIB_NAMESPACE_ALREADY_DEFINED;
	public static String BACKGROUND_COLOR;
	public static String ERROR_OF_TYPE_CONVERSION;
	public static String INCORRECT_PARAMETER_ERROR;
	public static String MENU;
	public static String SET_BACKGROUND_COLOR;
	public static String SET_FOREGROUND_COLOR;
	public static String TEXT_FORMATTING;
	public static String BLOCK_FORMAT;
	public static String FONT_NAME;
	public static String FONT_SIZE;
	public static String BOLD;
	public static String ITALIC;
	public static String UNDERLINE;
	public static String FOREGROUND_COLOR;
	public static String VPE_UPDATE_JOB_TITLE;
	public static String VPE_VISUAL_REFRESH_JOB;
	public static String VpeDnD_PLACE_AFTER_INSIDE;
	public static String VpeDnD_PLACE_AT_THE_BEGINNING_OF;
	public static String VpeDnD_PLACE_AT_THE_END_OF;
	public static String VpeDnD_PLACE_BEFORE_INSIDE;
	public static String VpeExpressionBuilder_ClosingApostropheNotFound;
	public static String VpeExpressionBuilder_ClosingBracketNotFound;
	public static String VpeExpressionBuilder_FunctionNotFound;
	public static String VpeExpressionBuilder_UndefinedCharacter;
	public static String VpeExpressionBuilder_UndefinedName;
	public static String VpeExpressionBuilderException_Message;
	public static String VpeTextPseudoContentCreator_InsertContent;
	public static String VpeTextPseudoContentCreator_InsertContentFor;
	public static String NON_VISUAL_TAGS;
	public static String SHOW_NON_VISUAL_TAGS;
	public static String HIDE_NON_VISUAL_TAGS;
	public static String SHOW_TOOLBAR;
	public static String HIDE_TOOLBAR;
	public static String SHOW;
	public static String HIDE;
	public static String MenuCreationHelper_Cut;
	public static String MenuCreationHelper_Paste;
	public static String MenuCreationHelper_Test;
	public static String SelectionBar_MoreNodes;
	public static String TemplatesPreferencePage_Add;
	public static String TemplatesPreferencePage_Edit;
	public static String TemplatesPreferencePage_Remove;
	public static String TemplatesPreferencePage_Export;
	public static String TemplatesPreferencePage_Import;
	public static String TemplatesTableProvider_Children;
	public static String TemplatesTableProvider_No;
	public static String TemplatesTableProvider_TagForDisplay;
	public static String TemplatesTableProvider_TagName;
	public static String TemplatesTableProvider_URI;
	public static String TemplatesTableProvider_Yes;
	public static String STYLE;
	public static String MAX_SOURCE_PANE;
	public static String MAX_VISUAL_PANE;
	public static String RESTORE_PREVIOUS_LOCATION;
	public static String EDIT_STYLE_TIP;
	public static String BaseActionManager_InsertAfter;
	public static String BaseActionManager_InsertAround;
	public static String BaseActionManager_InsertBefore;
	public static String BaseActionManager_InsertTag;
	public static String BaseActionManager_ReplaceWith;
	public static String SETUP_TEMPLATE_FOR_MENU;
	public static String INSERT_AROUND;
	public static String INSERT_BEFORE;
	public static String INSERT_AFTER;
	public static String INSERT_INTO;
	public static String REPLACE_WITH;
	public static String FROM_PALETTE;
	public static String PAGE_DESIGN_OPTIONS_ABOUT;
	public static String ACTUAL_RUN_TIME_FOLDERS_ABOUT;
	public static String INCLUDED_CSS_FILES_ABOUT;
	public static String INCLUDED_TAG_LIBS_ABOUT;
	public static String SUBSTITUTED_EL_EXPRESSIONS_ABOUT;
	
	public static String GENERAL_TAB_TITLE;
	public static String VPE_PREFERENCES_PAGE_DESCRIPTION;
	public static String SHOW_BORDER_FOR_UNKNOWN_TAGS;
	public static String SHOW_SELECTION_TAG_BAR;
	public static String SHOW_TEXT_FORMATTING;
	public static String HIDE_TEXT_FORMATTING;
	public static String SHOW_RESOURCE_BUNDLES_USAGE_AS_EL;
	public static String SHOW_BUNDLES_AS_EL;
	public static String SHOW_BUNDLES_AS_MESSAGES;
	public static String EXTERNALIZE_STRINGS;
	public static String ASK_TAG_ATTRIBUTES_ON_TAG_INSERT;
	public static String ASK_CONFIRMATION_ON_CLOSING_SELECTION_BAR;
	public static String INFORM_WHEN_PROJECT_MIGHT_NOT_BE_CONFIGURED_PROPERLY_FOR_VPE;
	public static String DEFAULT_VPE_TAB;
	public static String VISUAL_SOURCE_EDITORS_SPLITTING;
	public static String VISUAL_SOURCE_EDITORS_WEIGHTS;
	public static String DEFAULT_VPE_TAB_VISUAL_SOURCE;
	public static String DEFAULT_VPE_TAB_SOURCE;
	public static String DEFAULT_VPE_TAB_PREVIEW;
	public static String SPLITTING_VERT_TOP_SOURCE;
	public static String SPLITTING_VERT_TOP_VISUAL;
	public static String SPLITTING_HORIZ_LEFT_SOURCE;
	public static String SPLITTING_HORIZ_LEFT_VISUAL;
	public static String VISUAL_APPEARANCE_GROUP_TITLE;
	public static String CONFIRMATION_GROUP_TITLE;
	public static String TABS_GROUP_TITLE;
	public static String IMPORT_USER_TAGS_TEMPLATES_WIZARD_PAGE;
	public static String EXPORT_USER_TAGS_TEMPLATES_WIZARD_PAGE;
	public static String EXPORT_USER_TAGS_PAGE_TITLE;
	public static String EXPORT_USER_TAGS_PAGE_DESCRIPTION;
	public static String IMPORT_USER_TAGS_PAGE_TITLE;
	public static String IMPORT_USER_TAGS_PAGE_DESCRIPTION;
	public static String SELECT_ALL;
	public static String DESELECT_ALL;
	public static String BROWSE_BUTTON_TEXT;
	public static String FILE_NAME_LABEL;
	public static String NONE_TEMPLATES_WERE_ADDED;
	public static String COULD_NOT_SET_TABLE_SELECTION;
	public static String CANNOT_LOAD_TAGLIBS_FROM_PAGE_CONTEXT;
	public static String LIST_IS_EMPTY;
	public static String EXTERNALIZE_STRINGS_DIALOG_TITLE;
	public static String EXTERNALIZE_STRINGS_DIALOG_DESCRIPTION;
	public static String EXTERNALIZE_STRINGS_DIALOG_TEXT_STRING;
	public static String EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_KEY;
	public static String EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_VALUE;
	public static String EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_FILE;
	public static String EXTERNALIZE_STRINGS_DIALOG_RESOURCE_BUNDLE_LIST;
	public static String EXTERNALIZE_STRINGS_DIALOG_PROPS_STRINGS_GROUP;
	public static String EXTERNALIZE_STRINGS_DIALOG_PROPS_FILES_GROUP;
	public static String EXTERNALIZE_STRINGS_DIALOG_INITIALIZATION_ERROR;
	public static String EXTERNALIZE_STRINGS_DIALOG_RB_IS_MISSING;
	public static String EXTERNALIZE_STRINGS_DIALOG_WRONG_SELECTION;
	public static String EXTERNALIZE_STRINGS_DIALOG_WRONG_SELECTED_TEXT;
	public static String EXTERNALIZE_STRINGS_DIALOG_PLEASE_SELECT_BUNDLE;
	public static String EXTERNALIZE_STRINGS_DIALOG_SELECTED_TEXT_IS_EMPTY;
	public static String EXTERNALIZE_STRINGS_DIALOG_KEY_MUST_BE_SET;
	public static String EXTERNALIZE_STRINGS_DIALOG_VALUE_MUST_BE_SET;
	public static String EXTERNALIZE_STRINGS_DIALOG_NEW_FILE;
	public static String EXTERNALIZE_STRINGS_DIALOG_DEFAULT_KEY;
	public static String EXTERNALIZE_STRINGS_DIALOG_DEFAULT_VALUE;
	public static String EXTERNALIZE_STRINGS_DIALOG_PROPERTY_NAME;
	public static String EXTERNALIZE_STRINGS_DIALOG_PROPERTY_VALUE;
	public static String EXTERNALIZE_STRINGS_DIALOG_KEY_ALREADY_EXISTS;
	public static String EXTERNALIZE_STRINGS_DIALOG_ENTER_KEY_NAME;
	public static String EXTERNALIZE_STRINGS_DIALOG_SELECT_RESOURCE_BUNDLE;
	
	public static String COULD_NOT_OPEN_VPE_RESOURCES_DIALOG;
	
	
}
