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

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;
import org.jboss.tools.vpe.VpePlugin;

public class VpeUIMessages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.vpe.messages.messages";//$NON-NLS-1$
	private static ResourceBundle fResourceBundle;
	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, VpeUIMessages.class);		
	}
	private VpeUIMessages(){}
	
	public static ResourceBundle getResourceBundle() {
		try {
			if (fResourceBundle == null)
				fResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
		}
		catch (MissingResourceException x) {
			VpePlugin.getPluginLog().logError(x);
			fResourceBundle = null;
		}
		return fResourceBundle;
	}

	public static String NAMESPACE_NOT_DEFINED;
	public static String ACTUAL_RUN_TIME_ABSOLUTE_FOLDER;
	public static String PREFERENCES;
	public static String REFRESH;
	public static String PAGE_DESIGN_OPTIONS;
	public static String HIDE_SELECTIONBAR;
	public static String MOZILLA_LOADING_ERROR;
	public static String MOZILLA_LOADING_ERROR_LINK_TEXT;
	public static String MOZILLA_LOADING_ERROR_LINK;
	public static String SHOW_COMMENTS;
	public static String SHOW_BORDER_FOR_UNKNOWN_TAGS;
	public static String SHOW_BORDER_FOR_ALL_TAGS;
	public static String USE_DETAIL_BORDER;
	public static String SHOW_RESOURCE_BUNDLES_USAGE_AS_EL;
	public static String USE_ABSOLUTE_POSITION;
	public static String ALWAYS_PROMPT_FOR_TAG_ATTRIBUTES_DURING_TAG_INSERT;
	public static String OPTION_LIST;
	public static String SOURCE_VISUAL_EDITORS_WEIGHTS;
	public static String GENERAL;
	public static String TEMPLATES;
	public static String TEMPLATE;
	public static String TAG_ATTRIBUTES;
	public static String TAG_NAME;
	public static String CHILDREN;
	public static String DISPLAY;
	public static String ICON;
	public static String VALUE;
	public static String VALUE_COLOR;
	public static String VALUE_BACKGROUND_COLOR;
	public static String BACKGROUND_COLOR;
	public static String BORDER;
	public static String BORDER_COLOR;
	public static String ERROR_OF_TYPE_CONVERSION;
	public static String INCORRECT_PARAMETER_ERROR;
	public static String HIDE_TOOLBAR;
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
	public static String ALIGN_LEFT;
	public static String CENTER;
	public static String ALIGN_RIGHT;
	public static String JUSTIFY;
	public static String BULLETS;
	public static String NUMBERING;
	public static String CONFIRM_SELECTION_BAR_DIALOG_TITLE;
	public static String CONFIRM_SELECTION_BAR_DIALOG_MESSAGE;
	public static String CONFIRM_SELECTION_BAR_DIALOG_TOGGLE_MESSAGE;
	public static String VPE_UPDATE_JOB_TITLE;
	public static String VPE_VISUAL_REFRESH_JOB;
	
}
