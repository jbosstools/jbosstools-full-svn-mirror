/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Exadel, Inc.
 *     Red Hat, Inc. 
 *******************************************************************************/
package org.jboss.tools.vpe.mozilla.internal.swt.xpl;

import org.eclipse.swt.graphics.Point;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.XPCOMObject;


public class nsIScrollableView extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 22;

	public static final String NS_ISCROLLABLEVIEW_IID_STRING =
		"c95f1830-c376-11d1-b721-00600891d8c9";

	public static final nsID NS_ISCROLLABLEVIEW_IID =
		new nsID(NS_ISCROLLABLEVIEW_IID_STRING);

	public nsIScrollableView(int address) {
		super(address);
	}

	public int GetContainerSize(int[] aWidth, int[] aHeight) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aWidth, aHeight);
	}

	public int GetScrollPosition(int[] aX, int[] aY) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), aX, aY);
	}

	public int ScrollTo(int aX, int aY, int aUpdateFlags) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), aX, aY, aUpdateFlags);
	}

	public int SetLineHeight(int aHeight) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 15, getAddress(), aHeight);
	}

	public int GetLineHeight(int[] aHeight) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 16, getAddress(), aHeight);
	}

	public int AddScrollPositionListener(int aListener) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 21, getAddress(), aListener);
	}

	public int RemoveScrollPositionListener(int aListener) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 22, getAddress(), aListener);
	}

	//=========================================================================

	public Point getContainerSize() {
		int[] aWidth = new int[] {0};
		int[] aHeight = new int[] {0};
		int rc = GetContainerSize(aWidth, aHeight);
		if (rc != XPCOM.NS_OK) error(rc);
		return new Point(aWidth[0], aHeight[0]);
	}

	public Point getScrollPosition() {
		int[] aX = new int[] {0};
		int[] aY = new int[] {0};
		int rc = GetScrollPosition(aX, aY);
		if (rc != XPCOM.NS_OK) error(rc);
		return new Point(aX[0], aY[0]);
	}

	public void scrollTo(int aX, int aY) {
		int rc = ScrollTo(aX, aY, nsIViewManager.NS_VMREFRESH_IMMEDIATE);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void scrollTo(Point pos) {
		scrollTo(pos.x, pos.y);
	}

	public void setLineHeight(int aHeight) {
		int rc = SetLineHeight(aHeight);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public int getLineHeight() {
		int[] aHeight = new int[] {0};
		int rc = GetLineHeight(aHeight);
		if (rc != XPCOM.NS_OK) error(rc);
		return aHeight[0];
	}

	public void addScrollPositionListener(XPCOMObject listener) {
		if (listener != null) {
			AddScrollPositionListener(listener.getAddress());
		}
	}

	public void removeScrollPositionListener(XPCOMObject listener) {
		if (listener != null) {
			RemoveScrollPositionListener(listener.getAddress());
		}
	}
}
