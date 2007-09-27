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

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class nsIDOMNamedNodeMap extends nsISupports implements NamedNodeMap {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 8;

	public static final String NS_IDOMNAMEDNODEMAP_IID_STRING =
		"a6cf907b-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMNAMEDNODEMAP_IID =
		new nsID(NS_IDOMNAMEDNODEMAP_IID_STRING);

	public nsIDOMNamedNodeMap(int address) {
		super(address);
	}

	public int GetNamedItem(int name, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), name, _retval);
	}

	public int SetNamedItem(int arg, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), arg, _retval);
	}

	public int RemoveNamedItem(int name, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), name, _retval);
	}

	public int Item(int index, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), index, _retval);
	}

	public int GetLength(int aLength) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aLength);
	}

	public int GetNamedItemNS(int namespaceURI, int localName, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), namespaceURI, localName, _retval);
	}

	public int SetNamedItemNS(int arg, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), arg, _retval);
	}

	public int RemoveNamedItemNS(int namespaceURI, int localName, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), namespaceURI, localName, _retval);
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

	public Node getNamedItem(String name) {
		nsString nsValue = new nsString(name);
		int[] result = new int[1];
		int rc = GetNamedItem(nsValue.getAddress(), result);
		if (rc != XPCOM.NS_OK) error(rc);
		nsValue.dispose();
		return nsIDOMNode.getNodeAtAddress(result[0]);
	}

	public Node removeNamedItem(String name) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public Node setNamedItem(Node arg) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public Node setNamedItemNS(Node arg) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public Node getNamedItemNS(String namespaceURI, String localName) {
		// TODO Auto-generated method stub
		return null;
	}

	public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}
}
