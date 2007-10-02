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

import org.jboss.tools.vpe.mozilla.browser.MozillaDebug;
import org.jboss.tools.vpe.mozilla.browser.ProblemReporter;

public class nsISupports  {

	static final int LAST_METHOD_ID = 2;

	public static final String NS_ISUPPORTS_IID_STRING =
		"00000000-0000-0000-c000-000000000046";

	public static final nsID NS_ISUPPORTS_IID =
		new nsID(NS_ISUPPORTS_IID_STRING);

	private int address;
	private int refCount = 1;

	public nsISupports(int address) {
		this.address = address;
	}

	public int getAddress() {
		return this.address;
	}

	public int QueryInterface(nsID uuid, int[] result) {
		return XPCOM.VtblCall(0, getAddress(), uuid, result);
	}

	public int AddRef() {
		refCount++;
		return XPCOM.VtblCall(1, getAddress());
	}

	public int Release() {
		refCount--;
		if (refCount < 0 && MozillaDebug.debugRefCount) {
			try {
				throw new Exception("------ nsISupports.Release()");
			} catch (Exception e) {
				ProblemReporter.reportProblem(e);
			}
		}
		return XPCOM.VtblCall(2, getAddress());
	}

	//=========================================================================
	
	public int getRefCount() {
		return refCount;
	}

	public int queryInterface(nsID uuid) {
		return queryInterface(address, uuid);
	}

	public int getInterface(nsID uuid) {
		int aInterfaceRequestor = queryInterface(nsIInterfaceRequestor.NS_IINTERFACEREQUESTOR_IID);
		nsIInterfaceRequestor interfaceRequestor = new nsIInterfaceRequestor(aInterfaceRequestor);
		int aInterface = interfaceRequestor.getInterface(uuid);
		interfaceRequestor.Release();
		return aInterface;
	}

	public static int queryInterface(int address, nsID uuid) {
		int[] result = new int[] {0};
		int rc = XPCOM.VtblCall(0, address, uuid, result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
		return result[0];
	}

	public static int queryInterface(nsISupports object, nsID uuid) {
		return queryInterface(object.getAddress(), uuid);
	}

	public int hashCode() {
		return address;
	}

	public boolean equals(Object obj) {
		return (obj instanceof nsISupports) && ((nsISupports)obj).getAddress() == address;
	}
    
	static String error(int code) {
		throw new XPCOMError(code);
	}

	static void release(int address) {
		XPCOM.VtblCall(2, address);
	}
}