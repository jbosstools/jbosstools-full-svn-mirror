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

public class nsIWebBrowserFocus extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 8;

	public static final String NS_IWEBBROWSERFOCUS_IID_STRING =
		"9c5d3c58-1dd1-11b2-a1c9-f3699284657a";

	public static final nsID NS_IWEBBROWSERFOCUS_IID =
		new nsID(NS_IWEBBROWSERFOCUS_IID_STRING);

	public nsIWebBrowserFocus(int address) {
		super(address);
	}

	public int Activate() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress());
	}

	public int Deactivate() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress());
	}

	public int SetFocusAtFirstElement() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress());
	}

	public int SetFocusAtLastElement() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress());
	}

	public int GetFocusedWindow(int[] aFocusedWindow) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aFocusedWindow);
	}

	public int SetFocusedWindow(int aFocusedWindow) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aFocusedWindow);
	}

	public int GetFocusedElement(int[] aFocusedElement) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), aFocusedElement);
	}

	public int SetFocusedElement(int aFocusedElement) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), aFocusedElement);
	}
}