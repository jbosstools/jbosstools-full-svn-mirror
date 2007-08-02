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

import org.jboss.tools.vpe.mozilla.internal.swt.xpl.XPCOMObject;

public class nsIDOMEventTarget extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 3;

	public static final String NS_IDOMEVENTTARGET_IID_STRING =
		"1c773b30-d1cf-11d2-bd95-00805f8ae3f4";

	public static final nsID NS_IDOMEVENTTARGET_IID =
		new nsID(NS_IDOMEVENTTARGET_IID_STRING);

	public nsIDOMEventTarget(int address) {
		super(address);
	}

	public int AddEventListener(int type, int listener, boolean useCapture) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), type, listener, useCapture);
	}

	public int RemoveEventListener(int type, int listener, boolean useCapture) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), type, listener, useCapture);
	}

	public int DispatchEvent(int evt, int _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), evt, _retval);
	}

	//=========================================================================

	public void addEventListener(String type, XPCOMObject listener, boolean useCapture) {
		if (listener != null) {
			nsString nsType = new nsString(type);
			int rc = AddEventListener(nsType.getAddress(), listener.getAddress(), false);
			nsType.dispose();
			if (rc != XPCOM.NS_OK) error(rc);
		}
	}

	public void addEventListener(String type, XPCOMObject listener) {
		addEventListener(type, listener, false);
	}

	public void removeEventListener(String type, XPCOMObject listener, boolean useCapture) {
		if (listener != null) {
			nsString nsType = new nsString(type);
			int rc = RemoveEventListener(nsType.getAddress(), listener.getAddress(), false);
			nsType.dispose();
//			if (rc != XPCOM.NS_OK) error(rc);
		}
	}

	public void removeEventListener(String type, XPCOMObject listener) {
		removeEventListener(type, listener, false);
	}
}
