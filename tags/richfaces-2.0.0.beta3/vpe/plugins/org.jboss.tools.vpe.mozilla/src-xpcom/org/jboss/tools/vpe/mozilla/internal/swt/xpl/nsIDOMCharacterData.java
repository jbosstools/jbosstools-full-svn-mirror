/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Exadel, Inc.
 *     Red Hat, Inc. 
 *******************************************************************************/
package org.jboss.tools.vpe.mozilla.internal.swt.xpl;

public class nsIDOMCharacterData extends nsIDOMNode {

	static final int LAST_METHOD_ID = nsIDOMNode.LAST_METHOD_ID + 8;

	public static final String NS_IDOMCHARACTERDATA_IID_STRING =
		"a6cf9072-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMCHARACTERDATA_IID =
		new nsID(NS_IDOMCHARACTERDATA_IID_STRING);

	public nsIDOMCharacterData(int address) {
		super(address);
	}

	public int GetData(int aData) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aData);
	}

	public int SetData(int aData) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aData);
	}

	public int GetLength(int[] aLength) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aLength);
	}

	public int SubstringData(int offset, int count, int _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), offset, count, _retval);
	}

	public int AppendData(int arg) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), arg);
	}

	public int InsertData(int offset, int arg) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), offset, arg);
	}

	public int DeleteData(int offset, int count) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), offset, count);
	}

	public int ReplaceData(int offset, int count, int arg) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), offset, count, arg);
	}

	//=========================================================================

	public int getLength() {
		int[] aLength = new int[] {0};
		int rc = GetLength(aLength);
		if (rc != XPCOM.NS_OK) error(rc);
		return aLength[0];
	}
	
	public String substringData(int offset, int count) {
		nsString nsData = new nsString();
		int rc = SubstringData(offset, count, nsData.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
		String data = nsData.toString();
		nsData.dispose();
		return data;
	}
}
