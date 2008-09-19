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
	public static final String ATTR_WIDTH = "WIDTH"; //$NON-NLS-1$
	public static final String ATTR_HEIGHT = "HEIGHT"; //$NON-NLS-1$
	public static final String ATTR_BORDER = "border"; //$NON-NLS-1$
	public static final String ATTR_FOR = "FOR"; //$NON-NLS-1$
	public static final String ATTR_DIR = "dir"; //$NON-NLS-1$
	public static final String ATTR_DISABLED = "disabled"; //$NON-NLS-1$
	public static final String ATTR_FRAME = "frame"; //$NON-NLS-1$
	public static final String ATTR_ROWSPAN = "rowspan"; //$NON-NLS-1$
	public static final String ATTR_ROWS = "rows"; //$NON-NLS-1$
	public static final String ATTR_COLS = "cols"; //$NON-NLS-1$
	public static final String ATTR_VALIGN = "valign"; //$NON-NLS-1$
	public static final String ATTR_SRC = "src"; //$NON-NLS-1$
	public static final String ATTR_ALT = "alt"; //$NON-NLS-1$
	public static final String ATTR_JSFC = "jsfc";  //$NON-NLS-1$
	public static final String ATTR_DISPLAY = "display";  //$NON-NLS-1$

	public static final String VALUE_TOP_ALIGN = "top"; //$NON-NLS-1$
	public static final String VALUE_MIDDLE_ALIGN = "middle"; //$NON-NLS-1$
	public static final String VALUE_TEXT_TYPE = "text"; //$NON-NLS-1$
	public static final String VALUE_PASSWORD_TYPE = "password"; //$NON-NLS-1$
	public static final String VALUE_IMAGE_TYPE = "image"; //$NON-NLS-1$
	public static final String VALUE_RADIOBUTTON_TYPE = "radiobutton"; //$NON-NLS-1$
	public static final String VALUE_CHECKBOX_TYPE = "checkbox"; //$NON-NLS-1$
	public static final String VALUE_HIDDEN_TYPE = "hidden"; //$NON-NLS-1$
	public static final String VALUE_BUTTON_TYPE = "button"; //$NON-NLS-1$
	
}