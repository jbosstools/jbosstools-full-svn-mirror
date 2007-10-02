/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mozilla Communicator client code, released March 31, 1998.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by Netscape are Copyright (C) 1998-1999
 * Netscape Communications Corporation.  All Rights Reserved.
 *
 * Contributor(s):
 *
 * IBM
 * -  Binding to permit interfacing between Mozilla and SWT
 * -  Copyright (C) 2003 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.jboss.tools.vpe.mozilla.internal.swt.xpl;

import org.eclipse.swt.graphics.Point;

public class nsIBaseWindow extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 24;

	public static final String NS_IBASEWINDOW_IID_STRING =
		"046BC8A0-8015-11d3-AF70-00A024FFC08C";

	public static final nsID NS_IBASEWINDOW_IID =
		new nsID(NS_IBASEWINDOW_IID_STRING);

	public nsIBaseWindow(int address) {
		super(address);
	}

	public int InitWindow(int parentNativeWindow, int parentWidget, int x, int y, int cx, int cy) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), parentNativeWindow, parentWidget, x, y, cx, cy);
	}

	public int Create() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress());
	}

	public int Destroy() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress());
	}

	public int SetPosition(int x, int y) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), x, y);
	}

	public int GetPosition(int[] x, int[] y) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), x, y);
	}

	public int SetSize(int cx, int cy, boolean fRepaint) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), cx, cy, fRepaint);
	}

	public int GetSize(int[] cx, int[] cy) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), cx, cy);
	}

	public int SetPositionAndSize(int x, int y, int cx, int cy, boolean fRepaint) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), x, y, cx, cy, fRepaint);
	}

	public int GetPositionAndSize(int[] x, int[] y, int[] cx, int[] cy) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), x, y, cx, cy);
	}

	public int Repaint(boolean force) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), force);
	}

	public int GetParentWidget(int[] aParentWidget) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), aParentWidget);
	}

	public int SetParentWidget(int aParentWidget) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 12, getAddress(), aParentWidget);
	}

	public int GetParentNativeWindow(int[] aParentNativeWindow) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 13, getAddress(), aParentNativeWindow);
	}

	public int SetParentNativeWindow(int aParentNativeWindow) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 14, getAddress(), aParentNativeWindow);
	}

	public int GetVisibility(boolean[] aVisibility) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 15, getAddress(), aVisibility);
	}

	public int SetVisibility(boolean aVisibility) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 16, getAddress(), aVisibility);
	}

	public int GetEnabled(boolean[] aEnabled) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 17, getAddress(), aEnabled);
	}

	public int SetEnabled(boolean aEnabled) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 18, getAddress(), aEnabled);
	}

	public int GetBlurSuppression(boolean[] aBlurSuppression) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 19, getAddress(), aBlurSuppression);
	}

	public int SetBlurSuppression(boolean aBlurSuppression) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 20, getAddress(), aBlurSuppression);
	}

	public int GetMainWidget(int[] aMainWidget) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 21, getAddress(), aMainWidget);
	}

	public int SetFocus() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 22, getAddress());
	}

	public int GetTitle(int[] aTitle) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 23, getAddress(), aTitle);
	}

	public int SetTitle(char[] aTitle) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 24, getAddress(), aTitle);
	}

	//=========================================================================
	
	public void setSize(int cx, int cy, boolean fRepaint) {
		int rc = SetSize(cx, cy, fRepaint);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public Point getSize() {
		int[] cx = new int[] {0};
		int[] cy = new int[] {0};
		int rc = GetSize(cx, cy);
		if (rc != XPCOM.NS_OK) error(rc);
		return new Point(cx[0], cy[0]);
	}

	public Point getPosition() {
		int[] x = new int[] {0};
		int[] y = new int[] {0};
		int rc = GetPosition(x, y);
		if (rc != XPCOM.NS_OK) error(rc);
		return new Point(x[0], y[0]);
	}

	public nsIWidget getMainWidget() {
		int[] result = new int[1];
		int rc = GetMainWidget(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return new nsIWidget(result[0]);
	}
	
	public void repaint(boolean force) {
		int rc = Repaint(force);
		if (rc != XPCOM.NS_OK) error(rc);
	}
}