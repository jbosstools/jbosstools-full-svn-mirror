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
package org.jboss.tools.jst.jsp.messages;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;
import org.jboss.tools.jst.jsp.JspEditorPlugin;

public class JstUIMessages extends NLS {

	private static final String BUNDLE_NAME = "org.jboss.tools.jst.jsp.messages.messages";//$NON-NLS-1$
	private static ResourceBundle fResourceBundle;
	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, JstUIMessages.class);		
	}
	private JstUIMessages(){}
	
	public static ResourceBundle getResourceBundle() {
		try {
			if (fResourceBundle == null)
				fResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
		}
		catch (MissingResourceException x) {
			JspEditorPlugin.getPluginLog().logError(x);
			fResourceBundle = null;
		}
		return fResourceBundle;
	}

	public static String CSS_FILE_SELECT_DIALOG_TITLE;
	public static String CSS_FILE_SELECT_DIALOG_LABEL;
	public static String CSS_FILE_SELECT_DIALOG_EMPTY_MESSAGE;
	public static String CSS_STYLE_CLASS_EDITOR_HEADER_TITLE;
	public static String CSS_STYLE_CLASS_EDITOR_DESCRIPTION;
	public static String CSS_STYLE_CLASS_EDITOR_TITLE;
	public static String CSS_STYLE_EDITOR_TITLE;
    public static String CSS_EMPTY_FILE_PATH_MESSAGE;
    public static String CSS_EMPTY_STYLE_CLASS_MESSAGE;
    public static String CSS_SAVE_DIALOG_TITLE;
    public static String CSS_SAVE_DIALOG_MESSAGE;
	public static String CSS_CLASS_DIALOG_FILE_LABEL;
	public static String CSS_BROWSE_BUTTON_TOOLTIP;
	public static String CSS_CLASS_DIALOG_STYLE_CLASS_LABEL;
	public static String CSS_CLEAR_STYLE_SHEET;
	public static String CSS_APPLY_CHANGES;
	public static String ADD_FONT_FAMILY_TIP;
	public static String REMOVE_FONT_FAMILY_TIP;
	public static String FONT_SIZE;
	public static String FONT_STYLE;
	public static String FONT_WEIGHT;
	public static String FONT_FAMILY;
	public static String FONT_FAMILY_DIALOG_TITLE;
	public static String IMAGE_COMBO_TABLE_TOOL_TIP;
	public static String COLOR_DIALOG_TITLE;
	public static String IMAGE_DIALOG_MESSAGE;
	public static String IMAGE_DIALOG_TITLE;
	public static String IMAGE_DIALOG_EMPTY_MESSAGE;
	public static String FONT_FAMILY_TIP;
	public static String ALL_FILES;
	public static String ALL_IMAGE_FILES;
	public static String IMAGE_PREVIEW;
	public static String TEXT_FONT_TAB_NAME;
	public static String BACKGROUND_TAB_NAME;
	public static String BOXES_TAB_NAME;
	public static String PROPERTY_SHEET_TAB_NAME;
	public static String QUICK_EDIT_TAB_NAME;
	public static String BACKGROUND_COLOR;
	public static String BACKGROUND_COLOR_TIP;
	public static String BACKGROUND_IMAGE;
	public static String BACKGROUND_REPEAT;
	public static String DIMENSION_TITLE;
	public static String WIDTH;
	public static String HEIGHT;
	public static String BORDER_TITLE;
	public static String BORDER_STYLE;
	public static String BORDER_COLOR;
	public static String BORDER_COLOR_TIP;
	public static String BORDER_WIDTH;
	public static String MARGIN_PADDING_TITLE;
	public static String MARGIN;
	public static String PADDING;
	public static String COLOR;
	public static String COLOR_TIP;
	public static String TEXT_DECORATION;
	public static String TEXT_ALIGN;
	public static String BUTTON_APPLY;
	public static String BUTTON_CLEAR;
	public static String PREVIEW_SHEET_TAB_NAME;
	public static String DEFAULT_PREVIEW_TEXT;
	public static String DEFAULT_TEXT_FOR_BROWSER_PREVIEW;
	public static String CSS_NO_EDITED_PROPERTIES;
}
