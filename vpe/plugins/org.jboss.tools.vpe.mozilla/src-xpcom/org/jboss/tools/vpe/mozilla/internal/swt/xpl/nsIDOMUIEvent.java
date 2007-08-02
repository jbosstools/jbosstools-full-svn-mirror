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
 * -  Copyright (C) 2003, 2004 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.jboss.tools.vpe.mozilla.internal.swt.xpl;

public class nsIDOMUIEvent extends nsIDOMEvent {

	static final int LAST_METHOD_ID = nsIDOMEvent.LAST_METHOD_ID + 3;

	public static final String NS_IDOMUIEVENT_IID_STRING =
		"a6cf90c3-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMUIEVENT_IID =
		new nsID(NS_IDOMUIEVENT_IID_STRING);

	public nsIDOMUIEvent(int address) {
		super(address);
	}
	
	public int GetView(int[] aView) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aView);
	}

	public int GetDetail(int[] aDetail) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aDetail);
	}

	public int InitEvent(int typeArg, boolean canBubbleArg, boolean cancelableArg, int[] viewArg, int detailArg) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}
}