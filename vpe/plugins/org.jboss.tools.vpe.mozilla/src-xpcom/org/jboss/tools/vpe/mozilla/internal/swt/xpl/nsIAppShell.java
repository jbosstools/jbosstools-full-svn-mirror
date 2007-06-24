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

public class nsIAppShell extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 8;

	public static final String NS_IAPPSHELL_IID_STRING =
		"a0757c31-eeac-11d1-9ec1-00aa002fb821";

	public static final nsID NS_IAPPSHELL_IID =
		new nsID(NS_IAPPSHELL_IID_STRING);

	public nsIAppShell(int address) {
		super(address);
	}

	public int Create(int[] argc, int[] argv) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), argc, argv);
	}

	public int Run() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress());
	}

	public int Spinup() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress());
	}

	public int Spindown() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress());
	}

	public int ListenToEventQueue(int aQueue, boolean aListen) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aQueue, aListen);
	}

	public int GetNativeEvent(int aRealEvent, int aEvent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aRealEvent, aEvent);
	}

	public int DispatchNativeEvent(boolean aRealEvent, int aEvent) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int Exit() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress());
	}
}