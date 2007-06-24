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

public class nsIDOMNSHTMLInputElement extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 7;

	public static final String NS_IDOMNSHTMLINPUTELEMENT_IID_STRING =
		"993d2efc-a768-11d3-bccd-0060b0fc76bd";

	public static final nsID NS_IDOMNSHTMLINPUTELEMENT_IID =
		new nsID(NS_IDOMNSHTMLINPUTELEMENT_IID_STRING);

	public nsIDOMNSHTMLInputElement(int address) {
		super(address);
	}

	// NS_IMETHOD GetControllers(nsIControllers * *aControllers);
	public int GetControllers(int[] aControllers) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aControllers);
	}

	// NS_IMETHOD GetSelectionStart(PRInt32 *aSelectionStart);
	public int GetSelectionStart(int[] aSelectionStart) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aSelectionStart);
	}

	// NS_IMETHOD SetSelectionStart(PRInt32 aSelectionStart);
	public int SetSelectionStart(int selectionStart) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), selectionStart);
	}

	//NS_IMETHOD GetSelectionEnd(PRInt32 *aSelectionEnd);
	public int GetSelectionEnd(int[] aSelectionEnd) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aSelectionEnd);
	}

	// NS_IMETHOD SetSelectionRange(PRInt32 selectionStart, PRInt32 selectionEnd); 
	public int SetSelectionRange(int selectionStart, int selectionEnd) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), selectionStart, selectionEnd);
	}

	//=========================================================================

	public nsIControllers getControllers() {
		int[] aControllers = new int[] {0};
		int rc = GetControllers(aControllers);
		if (rc != XPCOM.NS_OK) error(rc);
		if (aControllers[0] != 0) {
			return new nsIControllers(aControllers[0]);
		} else {
			return null;
		}
	}

	public int getSelectionStart() {
		int[] aSelectionStart = new int[] {0};
		int rc = GetSelectionStart(aSelectionStart);
		if (rc != XPCOM.NS_OK) error(rc);
		return aSelectionStart[0];
	}

	public void setSelectionStart(int selectionStart) {
		int rc = SetSelectionStart(selectionStart);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public int getSelectionEnd() {
		int[] aSelectionEnd = new int[] {0};
		int rc = GetSelectionEnd(aSelectionEnd);
		if (rc != XPCOM.NS_OK) error(rc);
		return aSelectionEnd[0];
	}

	public void setSelectionRange(int selectionStart, int selectionEnd) {
		int rc = SetSelectionRange(selectionStart, selectionEnd);
		if (rc != XPCOM.NS_OK) error(rc);
	}
}
