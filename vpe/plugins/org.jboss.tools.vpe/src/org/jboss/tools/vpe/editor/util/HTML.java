/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.vpe.editor.util;

/**
 * @author Sergey Vasilyev (svasilyev@exadel.com)
 * 
 */
public final class HTML {

	private HTML() {
	}

	public static final String TAG_HTML = "HTML"; //$NON-NLS-1$
	public static final String TAG_HEAD = "HEAD"; //$NON-NLS-1$
	public static final String TAG_BODY = "BODY"; //$NON-NLS-1$
	public static final String TAG_IMG = "IMG"; //$NON-NLS-1$
	public static final String TAG_LINK = "LINK"; //$NON-NLS-1$
	public static final String TAG_SELECT = "SELECT"; //$NON-NLS-1$
	public static final String TAG_OPTION = "OPTION"; //$NON-NLS-1$
	public static final String TAG_STYLE = "STYLE"; //$NON-NLS-1$
	public static final String TAG_TABLE = "TABLE"; //$NON-NLS-1$
	public static final String TAG_TBODY = "TBODY"; //$NON-NLS-1$
	public static final String TAG_THEAD = "THEAD"; //$NON-NLS-1$
	public static final String TAG_TFOOT = "TFOOT"; //$NON-NLS-1$
	public static final String TAG_CAPTION = "CAPTION"; //$NON-NLS-1$
	public static final String TAG_TH = "TH"; //$NON-NLS-1$
	public static final String TAG_TR = "TR"; //$NON-NLS-1$
	public static final String TAG_TD = "TD"; //$NON-NLS-1$
	public static final String TAG_DL = "DL"; //$NON-NLS-1$
	public static final String TAG_DT = "DT"; //$NON-NLS-1$
	public static final String TAG_DD = "DD"; //$NON-NLS-1$
	public static final String TAG_COL = "COL"; //$NON-NLS-1$
	public static final String TAG_COLS = "COLS"; //$NON-NLS-1$
	public static final String TAG_COLGROUP = "COLGROUP"; //$NON-NLS-1$
	public static final String TAG_BR = "BR"; //$NON-NLS-1$
	public static final String TAG_LI = "LI"; //$NON-NLS-1$
	public static final String TAG_DIV = "DIV"; //$NON-NLS-1$
	public static final String TAG_SPAN = "SPAN"; //$NON-NLS-1$
	public static final String TAG_P = "P"; //$NON-NLS-1$
	public static final String TAG_TEXTAREA = "TEXTAREA"; //$NON-NLS-1$
	public static final String TAG_INPUT = "INPUT"; //$NON-NLS-1$
	public static final String TAG_BUTTON = "BUTTON"; //$NON-NLS-1$
	public static final String TAG_OL = "OL"; //$NON-NLS-1$
	public static final String TAG_UL = "UL"; //$NON-NLS-1$
	public static final String TAG_CODE = "CODE"; //$NON-NLS-1$
	public static final String TAG_PRE = "PRE"; //$NON-NLS-1$
	public static final String TAG_B = "B"; //$NON-NLS-1$
	public static final String TAG_I = "I"; //$NON-NLS-1$
	public static final String TAG_U = "U"; //$NON-NLS-1$
	public static final String TAG_LABEL = "LABEL"; //$NON-NLS-1$
	public static final String TAG_A = "A"; //$NON-NLS-1$
	public static final String TAG_H1 = "H1"; //$NON-NLS-1$
	public static final String TAG_FORM = "FORM"; //$NON-NLS-1$
	
	/**Use this class if you want to wrap a text node in a span that
	 * must not affect visual representation of the text node. 
	 * <p/>
	 * See also EditorOverride.css .*/
	public static final String CLASS_VPE_TEXT = "vpe-text"; //$NON-NLS-1$

	public static final String ATTR_ID = "ID"; //$NON-NLS-1$
	public static final String ATTR_TYPE = "TYPE"; //$NON-NLS-1$
	public static final String ATTR_TEXT = "TEXT"; //$NON-NLS-1$
	public static final String ATTR_CLASS = "class"; //$NON-NLS-1$
	public static final String ATTR_TITLE = "TITLE"; //$NON-NLS-1$
	public static final String ATTR_NAME = "NAME"; //$NON-NLS-1$
	public static final String ATTR_VALUE = "value"; //$NON-NLS-1$
	public static final String ATTR_STYLE = "style"; //$NON-NLS-1$
	public static final String ATTR_SIZE = "size"; //$NON-NLS-1$
	public static final String ATTR_MULTIPLE = "MULTIPLE"; //$NON-NLS-1$
	public static final String ATTR_COLSPAN = "colspan"; //$NON-NLS-1$
	public static final String ATTR_CELLSPACING = "cellspacing"; //$NON-NLS-1$
	public static final String ATTR_CELLPADDING = "cellpadding"; //$NON-NLS-1$
	public static final String ATTR_WIDTH = "width"; //$NON-NLS-1$
	public static final String ATTR_HEIGHT = "height"; //$NON-NLS-1$
	public static final String ATTR_BORDER = "border"; //$NON-NLS-1$
	public static final String ATTR_FOR = "FOR"; //$NON-NLS-1$
	public static final String ATTR_DIR = "dir"; //$NON-NLS-1$
	public static final String ATTR_DISABLED = "disabled"; //$NON-NLS-1$
	public static final String ATTR_FRAME = "frame"; //$NON-NLS-1$
	public static final String ATTR_ROWSPAN = "rowspan"; //$NON-NLS-1$
	public static final String ATTR_ROWS = "rows"; //$NON-NLS-1$
	public static final String ATTR_COLS = "cols"; //$NON-NLS-1$
	public static final String ATTR_VALIGN = "valign"; //$NON-NLS-1$
	public static final String ATTR_ALIGN = "align"; //$NON-NLS-1$
	public static final String ATTR_SRC = "src"; //$NON-NLS-1$
	public static final String ATTR_ALT = "alt"; //$NON-NLS-1$
	public static final String ATTR_JSFC = "jsfc"; //$NON-NLS-1$
	public static final String ATTR_DISPLAY = "display"; //$NON-NLS-1$
	public static final String ATTR_READONLY = "readonly"; //$NON-NLS-1$
	public static final String ATTR_SCOPE = "scope"; //$NON-NLS-1$
	public static final String ATTR_SPAN = "span"; //$NON-NLS-1$
	public static final String ATTR_BACKGROUND = "background"; //$NON-NLS-1$
	public static final String ATTR_BGCOLOR = "bgcolor"; //$NON-NLS-1$

	/**Use this constant if you have to span a column to entire row.*/
	/* While in HTML 4.01 standard "colspan='0'" should be used for this purpose,
	 * it will not work with documents without DOCTYPE. It is the reason
	 * why the value '100' is used.*/
	public static final String VALUE_COLSPAN_ALL = "100"; //$NON-NLS-1$

	public static final String VALUE_ALIGN_TOP = "top"; //$NON-NLS-1$
	public static final String VALUE_ALIGN_RIGHT = "right"; //$NON-NLS-1$
	public static final String VALUE_ALIGN_LEFT = "left"; //$NON-NLS-1$
	public static final String VALUE_ALIGN_BOTTOM = "bottom"; //$NON-NLS-1$
	public static final String VALUE_ALIGN_MIDDLE = "middle"; //$NON-NLS-1$
	public static final String VALUE_ALIGN_CENTER = "center"; //$NON-NLS-1$
	public static final String VALUE_CLASS_DELIMITER = " "; //$NON-NLS-1$
	public static final String VALUE_STYLE_DELIMITER = ";"; //$NON-NLS-1$
	public static final String VALUE_TYPE_TEXT = "text"; //$NON-NLS-1$
	public static final String VALUE_TYPE_PASSWORD = "password"; //$NON-NLS-1$
	public static final String VALUE_TYPE_IMAGE = "image"; //$NON-NLS-1$
	public static final String VALUE_TYPE_RADIOBUTTON = "radiobutton"; //$NON-NLS-1$
	public static final String VALUE_TYPE_CHECKBOX = "checkbox"; //$NON-NLS-1$
	public static final String VALUE_TYPE_HIDDEN = "hidden"; //$NON-NLS-1$
	public static final String VALUE_TYPE_BUTTON= "button"; //$NON-NLS-1$
	public static final String VALUE_CLEAR_BOTH = "both"; //$NON-NLS-1$

	public static final String STYLE_PARAMETER_DISPLAY = "display"; //$NON-NLS-1$
	public static final String STYLE_VALUE_NONE = "none"; //$NON-NLS-1$
	public static final String STYLE_PARAMETER_WIDTH = "width"; //$NON-NLS-1$
	public static final String STYLE_PARAMETER_TOP = "top"; //$NON-NLS-1$
	public static final String STYLE_PARAMETER_LEFT = "left"; //$NON-NLS-1$
	public static final String STYLE_PARAMETER_HEIGHT = "height"; //$NON-NLS-1$
	public static final String STYLE_PARAMETER_BACKGROUND_IMAGE = "background-image"; //$NON-NLS-1$
	public static final String STYLE_PARAMETER_BORDER_WIDTH = "border-width"; //$NON-NLS-1$
	public static final String STYLE_PARAMETER_BORDER_STYLE = "border-style"; //$NON-NLS-1$
	public static final String STYLE_PARAMETER_MAX_HEIGHT = "max-height"; //$NON-NLS-1$	
	public static final String STYLE_PARAMETER_ZINDEX = "z-index"; //$NON-NLS-1$	
	public static final String STYLE_PARAMETER_CLEAR = "clear"; //$NON-NLS-1$
	public static final String STYLE_PARAMETER_TABLE_LAYOUT = "table-layout"; //$NON-NLS-1$
	public static final String STYLE_VALUE_FIXED = "fixed"; //$NON-NLS-1$
	public static final String STYLE_VALUE_MIDDLE = "middle"; //$NON-NLS-1$
}
