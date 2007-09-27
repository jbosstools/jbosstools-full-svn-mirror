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

public class nsIMemory extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 5;

	public static final String NS_IMEMORY_IID_STRING =
		"59e7e77a-38e4-11d4-8cf5-0060b0fc14a3";

	public static final nsID NS_IMEMORY_IID =
		new nsID(NS_IMEMORY_IID_STRING);

	public nsIMemory(int address) {
		super(address);
	}

	public int Alloc(int size) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), size);
	}

	public int Realloc(int ptr, int newSize) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), ptr, newSize);
	}

	public void Free(int ptr) {
		XPCOM.VtblCallNoRet(super.LAST_METHOD_ID + 3, getAddress(), ptr);
	}

	public int HeapMinimize(boolean immediate) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), immediate);
	}

	public int IsLowMemory(boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), _retval);
	}
}