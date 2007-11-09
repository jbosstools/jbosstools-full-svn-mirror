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

import org.jboss.tools.vpe.xulrunner.editor.IXulRunnerVpeResizer;

public class VpeTagDescription {
	public static final int DISPLAY_TYPE_NONE = 0;
	public static final int DISPLAY_TYPE_BLOCK = 1;
	public static final int DISPLAY_TYPE_INLINE = 2;

	public static final int RESIZE_CONSTRAINS_TOPLEFT = IXulRunnerVpeResizer.RESIZER_MARKER_TOPLEFT;
	public static final int RESIZE_CONSTRAINS_TOP = IXulRunnerVpeResizer.RESIZER_MARKER_TOP;
	public static final int RESIZE_CONSTRAINS_TOPRIGHT = IXulRunnerVpeResizer.RESIZER_MARKER_TOPRIGHT;
	public static final int RESIZE_CONSTRAINS_LEFT = IXulRunnerVpeResizer.RESIZER_MARKER_LEFT;
	public static final int RESIZE_CONSTRAINS_RIGHT = IXulRunnerVpeResizer.RESIZER_MARKER_RIGHT;
	public static final int RESIZE_CONSTRAINS_BOTTOMLEFT = IXulRunnerVpeResizer.RESIZER_MARKER_BOTTOMLEFT;
	public static final int RESIZE_CONSTRAINS_BOTTOM = IXulRunnerVpeResizer.RESIZER_MARKER_BOTTOM;
	public static final int RESIZE_CONSTRAINS_BOTTOMRIGHT = IXulRunnerVpeResizer.RESIZER_MARKER_BOTTOMRIGHT;

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
