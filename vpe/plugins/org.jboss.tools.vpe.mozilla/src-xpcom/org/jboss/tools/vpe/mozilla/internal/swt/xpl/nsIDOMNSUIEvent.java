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

public class nsIDOMNSUIEvent extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 11;

	public static final String NS_IDOMNSUIEVENT_IID_STRING =
		"a6cf90c4-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMNSUIEVENT_IID =
		new nsID(NS_IDOMNSUIEVENT_IID_STRING);

	public nsIDOMNSUIEvent(int address) {
		super(address);
	}

	public int GetPreventDefault(boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), _retval);
	}

	public int GetLayerX(int[] aLayerX) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aLayerX);
	}

	public int GetLayerY(int[] aLayerY) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aLayerY);
	}

	public int GetPageX(int[] aPageX) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aPageX);
	}

	public int GetPageY(int[] aPageY) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aPageY);
	}

	public int GetWhich(int[] aWhich) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aWhich);
	}

	public int GetRangeParent(int[] aRangeParent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), aRangeParent);
	}

	public int GetRangeOffset(int[] aRangeOffset) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), aRangeOffset);
	}

	public int GetCancelBubble(boolean[] aCancelBubble) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), aCancelBubble);
	}

	public int SetCancelBubble(boolean aCancelBubble) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), aCancelBubble);
	}

	public int GetIsChar(boolean[] aIsChar) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), aIsChar);
	}

	//=========================================================================

	public boolean getPreventDefault() {
		boolean[] aRetval = new boolean[] {false};
		int rc = GetPreventDefault(aRetval);
		if (rc != XPCOM.NS_OK) error(rc);
		return aRetval[0];
	}
//		public int GetPreventDefault(boolean[] _retval) {

	public Node getRangeParent() {
		int[] result = new int[] {0};
		int rc = GetRangeParent(result);
		if (rc != XPCOM.NS_OK) error(rc);
		return nsIDOMNode.getNodeAtAddress(result[0]);
	}

	public int getRangeOffset() {
		int[] aRangeOffset = new int[] {0};
		int rc = GetRangeOffset(aRangeOffset);
		if (rc != XPCOM.NS_OK) error(rc);
		return aRangeOffset[0];
	}

	public int getPageX() {
		int[] aPageX = new int[] {0};
		int rc = GetPageX(aPageX);
		if (rc != XPCOM.NS_OK) error(rc);
		return aPageX[0];
	}

	public int getPageY() {
		int[] aPageY = new int[] {0};
		int rc = GetPageY(aPageY);
		if (rc != XPCOM.NS_OK) error(rc);
		return aPageY[0];
	}

	public int getLayerX() {
		int[] aLayerX = new int[] {0};
		int rc = GetLayerX(aLayerX);
		if (rc != XPCOM.NS_OK) error(rc);
		return aLayerX[0];
	}

	public int getLayerY() {
		int[] aLayerY = new int[] {0};
		int rc = GetLayerY(aLayerY);
		if (rc != XPCOM.NS_OK) error(rc);
		return aLayerY[0];
	}

	public boolean getCancelBubble() {
		boolean[] aRetval = new boolean[] {false};
		int rc = GetCancelBubble(aRetval);
		if (rc != XPCOM.NS_OK) error(rc);
		return aRetval[0];
	}

	public void setCancelBubble(boolean cancelBubble) {
		int rc = SetCancelBubble(cancelBubble);
		if (rc != XPCOM.NS_OK) error(rc);
	}
}
