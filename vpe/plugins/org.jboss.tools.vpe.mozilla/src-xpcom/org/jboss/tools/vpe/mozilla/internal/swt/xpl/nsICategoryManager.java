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

public class nsICategoryManager extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 6;

	public static final String NS_ICATEGORYMANAGER_IID_STRING =
		"3275b2cd-af6d-429a-80d7-f0c5120342ac";

	public static final nsID NS_ICATEGORYMANAGER_IID =
		new nsID(NS_ICATEGORYMANAGER_IID_STRING);

	public nsICategoryManager(int address) {
		super(address);
	}

	public int GetCategoryEntry(byte[] aCategory, byte[] aEntry, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aCategory, aEntry, _retval);
	}

	public int AddCategoryEntry(byte[] aCategory, byte[] aEntry, byte[] aValue, boolean aPersist, boolean aReplace, int[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int DeleteCategoryEntry(byte[] aCategory, byte[] aEntry, boolean aPersist) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int DeleteCategory(byte[] aCategory) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aCategory);
	}

	public int EnumerateCategory(byte[] aCategory, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aCategory, _retval);
	}

	public int EnumerateCategories(int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), _retval);
	}
}