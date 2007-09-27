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

public class nsIDragSession extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 11;

	public static final String NS_IDRAGSESSION_IID_STRING =
		"8b5314bb-db01-11d2-96ce-0060b0fb9956";

	public static final nsID NS_IDRAGSESSION_IID =
		new nsID(NS_IDRAGSESSION_IID_STRING);

	public nsIDragSession(int address) {
		super(address);
	}

	public int GetCanDrop(boolean[] aCanDrop) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aCanDrop);
	}

	public int SetCanDrop(boolean aCanDrop) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aCanDrop);
	}

	public int GetDragAction(int[] aDragAction) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aDragAction);
	}

	public int SetDragAction(int aDragAction) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aDragAction);
	}

	public int GetTargetSize(int aTargetSize) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aTargetSize);
	}

	public int SetTargetSize(int aTargetSize) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aTargetSize);
	}

	public int GetNumDropItems(int[] aNumDropItems) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), aNumDropItems);
	}

	public int GetSourceDocument(int[] aSourceDocument) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), aSourceDocument);
	}

	public int GetSourceNode(int[] aSourceNode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), aSourceNode);
	}

	public int GetData(int aTransferable, int aItemIndex) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), aTransferable, aItemIndex);
	}

	public int IsDataFlavorSupported(byte[] aDataFlavor, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), aDataFlavor, _retval);
	}

	//=========================================================================

	public void setCanDrop(boolean canDrop) {
		int rc = SetCanDrop(canDrop);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void getData(nsITransferable aTransferable, int aItemIndex) {
		int rc = GetData(aTransferable.getAddress(), aItemIndex);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public nsIDOMNode getSourceNode() {
		int[] result = new int[] {0};
		int rc = GetSourceNode(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) {
			return null;
		} else {
			return new nsIDOMNode(result[0]);
		}
	}

	public int getNumDropItems() {
		int[] aNumDropItems = new int[] {0};
		int rc = GetNumDropItems(aNumDropItems);
		if (rc != XPCOM.NS_OK) error(rc);
		return aNumDropItems[0];
	}

	public boolean isDataFlavorSupported(String flavor) {
		byte[] aFlavor = new byte[flavor.length() + 1];
		System.arraycopy(flavor.getBytes(), 0, aFlavor, 0, flavor.length());
		boolean[] retval = new boolean[] {false};
		int rc = IsDataFlavorSupported(aFlavor, retval);
		if (rc != XPCOM.NS_OK) error(rc);
		return retval[0];
	}
}
