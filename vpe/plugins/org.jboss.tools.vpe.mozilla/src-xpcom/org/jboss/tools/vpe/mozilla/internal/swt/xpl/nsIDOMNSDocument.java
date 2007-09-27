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

public class nsIDOMNSDocument extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 10;

	public static final String NS_IDOMNSDOCUMENT_IID_STRING =
		"a6cf90cd-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMNSDOCUMENT_IID =
		new nsID(NS_IDOMNSDOCUMENT_IID_STRING);

	public nsIDOMNSDocument(int address) {
		super(address);
	}

	public int GetBoxObjectFor(int elt, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), elt, _retval);
	}


//	NS_IMETHOD GetCharacterSet(nsAString & aCharacterSet); \
//	NS_IMETHOD GetDir(nsAString & aDir); \
//	NS_IMETHOD SetDir(const nsAString & aDir); \
//	NS_IMETHOD GetLocation(nsIDOMLocation * *aLocation); \
//	NS_IMETHOD GetTitle(nsAString & aTitle); \
//	NS_IMETHOD SetTitle(const nsAString & aTitle); \
//	NS_IMETHOD GetContentType(nsAString & aContentType); \
//	NS_IMETHOD GetLastModified(nsAString & aLastModified); \
//	NS_IMETHOD GetBoxObjectFor(nsIDOMElement *elt, nsIBoxObject **_retval); \
//	NS_IMETHOD SetBoxObjectFor(nsIDOMElement *elt, nsIBoxObject *boxObject); 


	//=========================================================================

	public nsIBoxObject getBoxObjectFor(nsIDOMElement elt) {
		int[] result = new int[1];
		int rc = GetBoxObjectFor(elt.getAddress(), result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) {
			return null;
		} else {
			return new nsIBoxObject(result[0]);
		}
	}
}
