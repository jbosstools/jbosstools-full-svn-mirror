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

public class nsIPresShell extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 72;

	public static final String NS_IPRESSHELL_IID_STRING =
		"76e79c60-944e-11d1-9323-00805f8add32";

	public static final nsID NS_IPRESSHELL_IID =
		new nsID(NS_IPRESSHELL_IID_STRING);

	public nsIPresShell(int address) {
		super(address);
	}

	public int GetPresContext(int[] aResult) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), aResult);
	}

	public int GetViewManager(int[] aResult) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), aResult);
	}

	public int GetFrameSelection(int[] aFrameSelection) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 20, getAddress(), aFrameSelection);
	}

	public int GetPrimaryFrameFor(int aContent, int[] aPrimaryFrame) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 29, getAddress(), aContent, aPrimaryFrame);
	}

	public int GetCaret(int[] aOutCaret) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 57, getAddress(), aOutCaret);
	}

	//=========================================================================

	public nsIPresContext getPresContext() {
		int[] aResult = new int[] {0};
		int rc = GetPresContext(aResult);
		if (rc != XPCOM.NS_OK) error(rc);
		if (aResult[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return new nsIPresContext(aResult[0]);
	}

	public nsIFrameSelection getFrameSelection() {
		int[] aFrameSelection = new int[] {0};
		int rc = GetFrameSelection(aFrameSelection);
		if (rc != XPCOM.NS_OK) error(rc);
		if (aFrameSelection[0] == 0) {
			return null;
		} else {
			return new nsIFrameSelection(aFrameSelection[0]);
		}
	}

	public nsIFrame getFrameFor(nsIDOMElement element) {
		int aContent = queryInterface(element.getAddress(), nsIContent.NS_ICONTENT_IID);
		int[] aPrimaryFrame = new int[] {0};
		int rc = GetPrimaryFrameFor(aContent, aPrimaryFrame);
		release(aContent);
		if (rc != XPCOM.NS_OK) error(rc);
		if (aPrimaryFrame[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
		return new nsIFrame(aPrimaryFrame[0]);
	}

	public nsICaret getCaret() {
		int[] aOutCaret = new int[] {0};
		int rc = GetCaret(aOutCaret);
		if (rc != XPCOM.NS_OK) error(rc);
		if (aOutCaret[0] == 0) {
			return null;
		} else {
			return new nsICaret(aOutCaret[0]);
		}
	}
}