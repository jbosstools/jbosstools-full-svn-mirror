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

import org.jboss.tools.vpe.mozilla.browser.MozillaBrowser;

public class nsIDOMNSEvent extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 5;

	public static final String NS_IDOMNSEVENT_IID_STRING =
		"a90977dd-a80b-49bb-8169-cc90e3d1da98";

	public static final nsID NS_IDOMNSEVENT_IID =
		new nsID(NS_IDOMNSEVENT_IID_STRING);

	public nsIDOMNSEvent(int address) {
		super(address);
	}

	public int GetOriginalTarget(int[] aOriginalTarget) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aOriginalTarget);
	}

	public int GetExplicitOriginalTarget(int[] aExplicitOriginalTarget) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aExplicitOriginalTarget);
	}

	public int GetTmpRealOriginalTarget(int[] aTmpRealOriginalTarget) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aTmpRealOriginalTarget);
	}

	public int PreventBubble() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress());
	}

	public int PreventCapture() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress());
	}

	//=========================================================================

	public nsIDOMEventTarget getOriginalTarget() {
		int[] result = new int[] {0};
		int rc = GetOriginalTarget(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) MozillaBrowser.error(XPCOM.NS_ERROR_NO_INTERFACE);
		return new nsIDOMEventTarget(result[0]);
	}

	public nsIDOMEventTarget getExplicitOriginalTarget() {
		int[] result = new int[] {0};
		int rc = GetExplicitOriginalTarget(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) MozillaBrowser.error(XPCOM.NS_ERROR_NO_INTERFACE);
		return new nsIDOMEventTarget(result[0]);
	}

	public nsIDOMEventTarget getTmpRealOriginalTarget() {
		int[] result = new int[] {0};
		int rc = GetTmpRealOriginalTarget(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) MozillaBrowser.error(XPCOM.NS_ERROR_NO_INTERFACE);
		return new nsIDOMEventTarget(result[0]);
	}
}
