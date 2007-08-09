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
package org.jboss.tools.vpe.editor.template;

// TODO Alexey Yukhovich add resizer support
//import org.jboss.tools.vpe.mozilla.internal.swt.xpl.VpeResizer;

public class VpeTagDescription {
	public static final int DISPLAY_TYPE_NONE = 0;
	public static final int DISPLAY_TYPE_BLOCK = 1;
	public static final int DISPLAY_TYPE_INLINE = 2;

	// TODO Alexey Yukhovich add resuzer support
//	public static final int RESIZE_CONSTRAINS_TOPLEFT = VpeResizer.eTopLeft;
//	public static final int RESIZE_CONSTRAINS_TOP = VpeResizer.eTop;
//	public static final int RESIZE_CONSTRAINS_TOPRIGHT = VpeResizer.eTopRight;
//	public static final int RESIZE_CONSTRAINS_LEFT = VpeResizer.eLeft;
//	public static final int RESIZE_CONSTRAINS_RIGHT = VpeResizer.eRight;
//	public static final int RESIZE_CONSTRAINS_BOTTOMLEFT = VpeResizer.eBottomLeft;
//	public static final int RESIZE_CONSTRAINS_BOTTOM = VpeResizer.eBottom;
//	public static final int RESIZE_CONSTRAINS_BOTTOMRIGHT = VpeResizer.eBottomRight;
	// TODO Alexey Yukhovich restore above resizer constraints
	public static final int RESIZE_CONSTRAINS_TOPLEFT = 1;
	public static final int RESIZE_CONSTRAINS_TOP = 2;
	public static final int RESIZE_CONSTRAINS_TOPRIGHT = 4;
	public static final int RESIZE_CONSTRAINS_LEFT = 8;
	public static final int RESIZE_CONSTRAINS_RIGHT = 16;
	public static final int RESIZE_CONSTRAINS_BOTTOMLEFT = 32;
	public static final int RESIZE_CONSTRAINS_BOTTOM = 64;
	public static final int RESIZE_CONSTRAINS_BOTTOMRIGHT = 128;

	public static final int RESIZE_CONSTRAINS_NONE = 0;
	public static final int RESIZE_CONSTRAINS_ALL = RESIZE_CONSTRAINS_TOPLEFT | RESIZE_CONSTRAINS_TOP | RESIZE_CONSTRAINS_TOPRIGHT | RESIZE_CONSTRAINS_LEFT | RESIZE_CONSTRAINS_RIGHT | RESIZE_CONSTRAINS_BOTTOMLEFT | RESIZE_CONSTRAINS_BOTTOM | RESIZE_CONSTRAINS_BOTTOMRIGHT;

	private int displayType = DISPLAY_TYPE_BLOCK;
	private int resizeConstrains = RESIZE_CONSTRAINS_NONE;
	
	public int getDisplayType() {
		return displayType;
	}
	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}
	
	public int getResizeConstrains() {
		return resizeConstrains;
	}
	public void setResizeConstrains(int resizeConstrains) {
		this.resizeConstrains = resizeConstrains;
	}
}
