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

public class nsIWebProgress extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 4;

	public static final String NS_IWEBPROGRESS_IID_STRING =
		"570F39D0-EFD0-11d3-B093-00A024FFC08C";

	public static final nsID NS_IWEBPROGRESS_IID =
		new nsID(NS_IWEBPROGRESS_IID_STRING);

	public nsIWebProgress(int address) {
		super(address);
	}

	public static final int NOTIFY_STATE_REQUEST = 1;

	public static final int NOTIFY_STATE_DOCUMENT = 2;

	public static final int NOTIFY_STATE_NETWORK = 4;

	public static final int NOTIFY_STATE_WINDOW = 8;

	public static final int NOTIFY_STATE_ALL = 15;

	public static final int NOTIFY_PROGRESS = 16;

	public static final int NOTIFY_STATUS = 32;

	public static final int NOTIFY_SECURITY = 64;

	public static final int NOTIFY_LOCATION = 128;

	public static final int NOTIFY_ALL = 255;

	public int AddProgressListener(int listener, int aNotifyMask) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), listener, aNotifyMask);
	}

	public int RemoveProgressListener(int listener) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), listener);
	}

	public int GetDOMWindow(int[] aDOMWindow) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aDOMWindow);
	}

	public int GetIsLoadingDocument(boolean[] aIsLoadingDocument) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aIsLoadingDocument);
	}
}