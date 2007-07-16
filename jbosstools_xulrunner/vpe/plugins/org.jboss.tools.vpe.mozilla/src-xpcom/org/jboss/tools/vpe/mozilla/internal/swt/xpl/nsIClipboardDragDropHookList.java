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

public class nsIClipboardDragDropHookList extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 3;

	public static final String NS_ICLIPBOARDDRAGDROPHOOKLIST_IID_STRING =
		"876a2015-6b66-11d7-8f18-0003938a9d96";

	public static final nsID NS_ICLIPBOARDDRAGDROPHOOKLIST_IID =
		new nsID(NS_ICLIPBOARDDRAGDROPHOOKLIST_IID_STRING);

	public nsIClipboardDragDropHookList(int address) {
		super(address);
	}

	public int AddClipboardDragDropHooks(int aHooks) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aHooks);
	}

	public int RemoveClipboardDragDropHooks(int aHooks) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aHooks);
	}

	public int GetHookEnumerator(int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), _retval);
	}

	//=========================================================================

	public void addClipboardDragDropHooks(XPCOMObject listener) {
		int rc = AddClipboardDragDropHooks(listener.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void removeClipboardDragDropHooks(XPCOMObject listener) {
		int rc = RemoveClipboardDragDropHooks(listener.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public nsISimpleEnumerator getHookEnumerator() {
		int[] result = new int[] {0};
		int rc = GetHookEnumerator(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) {
			return null;
		} else {
			return new nsISimpleEnumerator(result[0]);
		}
	}
}
