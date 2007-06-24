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

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public class nsIDOMAttr extends nsIDOMNode implements Attr {

	static final int LAST_METHOD_ID = nsIDOMNode.LAST_METHOD_ID + 5;

	public static final String NS_IDOMATTR_IID_STRING =
		"a6cf9070-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMATTR_IID =
		new nsID(NS_IDOMATTR_IID_STRING);

	public nsIDOMAttr(int address) {
		super(address);
	}

	public int GetName(int aName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aName);
	}

	public int GetSpecified(int aSpecified) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aSpecified);
	}

	public int GetValue(int aValue) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aValue);
	}

	public int SetValue(int aValue) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aValue);
	}

	public int GetOwnerElement(int[] aOwnerElement) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aOwnerElement);
	}

	//=========================================================================

	public String getName() {
		nsString nsName = new nsString();
		int rc = GetName(nsName.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
		String name = nsName.toString();
		nsName.dispose();
		return name;
	}

	public boolean getSpecified() {
		int ptrSpecified = XPCOM.PR_Malloc(4);
		int rc = GetSpecified(ptrSpecified);
		if (rc != XPCOM.NS_OK) error(rc);
		int[] aSpecified = new int[] {0};
		XPCOM.memmove(aSpecified, ptrSpecified, 4);
		XPCOM.PR_Free(ptrSpecified);
		return aSpecified[0] != 0;
	}

	public String getValue() {
		nsString nsValue = new nsString();
		int rc = GetValue(nsValue.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
		String value = nsValue.toString();
		nsValue.dispose();
		return value;
	}

	public void setValue(String value) {
		nsString nsValue = new nsString(value);
		int rc = SetValue(nsValue.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
		nsValue.dispose();
	}

	public Element getOwnerElement() {
		int[] result = new int[1];
		int rc = GetOwnerElement(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return new nsIDOMElement(result[0]);
	}

	public boolean isId() {
		// TODO Auto-generated method stub
		return false;
	}

	public TypeInfo getSchemaTypeInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object setUserData(String key, Object data, UserDataHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}
}