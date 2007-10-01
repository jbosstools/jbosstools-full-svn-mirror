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

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class nsIDOMNodeList extends nsISupports implements NodeList {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 2;

	public static final String NS_IDOMNODELIST_IID_STRING =
		"a6cf907d-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMNODELIST_IID =
		new nsID(NS_IDOMNODELIST_IID_STRING);

	public nsIDOMNodeList(int address) {
		super(address);
	}

	public int Item(int index, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), index, _retval);
	}

	public int GetLength(int aLength) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aLength);
	}

	//=========================================================================

	public Node item(int index) {
		int[] result = new int[1];
		int rc = Item(index, result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return nsIDOMNode.getNodeAtAddress(result[0]);
	}

	public int getLength() {
		int ptrLen = XPCOM.PR_Malloc(4);
		int rc = GetLength(ptrLen);
		if (rc != XPCOM.NS_OK) error(rc);
		int[] aLen = new int[] {0};
		XPCOM.memmove(aLen, ptrLen, 4);
		XPCOM.PR_Free(ptrLen);
		return aLen[0];
	}
}
