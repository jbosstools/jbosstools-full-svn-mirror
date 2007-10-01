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

public class nsIIOService extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 10;

	public static final String NS_IIOSERVICE_IID_STRING =
		"bddeda3f-9020-4d12-8c70-984ee9f7935e";

	public static final nsID NS_IIOSERVICE_IID =
		new nsID(NS_IIOSERVICE_IID_STRING);

	public nsIIOService(int address) {
		super(address);
	}

	public int GetProtocolHandler(byte[] aScheme, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aScheme, _retval);
	}

	public int GetProtocolFlags(byte[] aScheme, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aScheme, _retval);
	}

	public int NewURI(int aSpec, byte[] aOriginCharset, int aBaseURI, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aSpec, aOriginCharset, aBaseURI, _retval);
	}

	public int NewFileURI(int aFile, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aFile, _retval);
	}

	public int NewChannelFromURI(int aURI, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aURI, _retval);
	}

	public int NewChannel(int aSpec, byte[] aOriginCharset, int aBaseURI, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aSpec, aOriginCharset, aBaseURI, _retval);
	}

	public int GetOffline(boolean[] aOffline) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), aOffline);
	}

	public int SetOffline(boolean aOffline) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), aOffline);
	}

	public int AllowPort(int aPort, byte[] aScheme, boolean[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int ExtractScheme(int urlString, int _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), urlString, _retval);
	}
}