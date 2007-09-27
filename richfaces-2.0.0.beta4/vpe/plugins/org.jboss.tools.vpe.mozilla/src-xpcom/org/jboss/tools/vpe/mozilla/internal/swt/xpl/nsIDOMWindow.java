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

public class nsIDOMWindow extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 17;

	public static final String NS_IDOMWINDOW_IID_STRING =
		"a6cf906b-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMWINDOW_IID =
		new nsID(NS_IDOMWINDOW_IID_STRING);

	public nsIDOMWindow(int address) {
		super(address);
	}

	public int GetDocument(int[] aDocument) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aDocument);
	}

	public int GetParent(int[] aParent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aParent);
	}

	public int GetTop(int[] aTop) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aTop);
	}

	public int GetScrollbars(int[] scrollbars) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), scrollbars);
	}

	public int GetFrames(int[] frames) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), frames);
	}

	public int GetName(int aName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aName);
	}

	public int SetName(int aName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), aName);
	}

	public int GetTextZoom(float[] aTextZoom) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), aTextZoom);
	}

	public int SetTextZoom(float aTextZoom) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), aTextZoom);
	}

	public int GetScrollX(int[] aScrollX) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), aScrollX);
	}

	public int GetScrollY(int[] aScrollY) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), aScrollY);
	}

	public int ScrollTo(int xScroll, int yScroll) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 12, getAddress(), xScroll, yScroll);
	}

	public int ScrollBy(int xScrollDif, int yScrollDif) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 13, getAddress(), xScrollDif, yScrollDif);
	}

	public int GetSelection(int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 14, getAddress(), _retval);
	}

	public int ScrollByLines(int numLines) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 15, getAddress(), numLines);
	}

	public int ScrollByPages(int numPages) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 16, getAddress(), numPages);
	}

	public int SizeToContent() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 17, getAddress());
	}
	
	//=========================================================================

	public nsIDOMDocument getDOMDocument() {
		int[] result = new int[1];
		int rc = GetDocument(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return new nsIDOMDocument(result[0]);
	}

	public nsIDOMBarProp getScrollbars() {
		int[] result = new int[] {0};
		int rc = GetScrollbars(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return new nsIDOMBarProp(result[0]);
	}

	public int getScrollX() {
		int[] result = new int[] {0};
		int rc = GetScrollX(result);
		if (rc != XPCOM.NS_OK) error(rc);
		return result[0];
	}

	public int getScrollY() {
		int[] result = new int[] {0};
		int rc = GetScrollY(result);
		if (rc != XPCOM.NS_OK) error(rc);
		return result[0];
	}

	public nsISelection getSelection() {
		int[] result = new int[] {0};
		int rc = GetSelection(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return new nsISelection(result[0]);
		
	}
}