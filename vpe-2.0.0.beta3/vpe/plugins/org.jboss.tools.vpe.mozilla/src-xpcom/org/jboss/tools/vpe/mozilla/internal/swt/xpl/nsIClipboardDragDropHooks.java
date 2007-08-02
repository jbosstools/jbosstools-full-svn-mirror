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

public class nsIClipboardDragDropHooks extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 4;

	public static final String NS_ICLIPBOARDDRAGDROPHOOKS_IID_STRING =
		"e03e6c5e-0d84-4c0b-8739-e6b8d51922de";

	public static final nsID NS_ICLIPBOARDDRAGDROPHOOKS_IID =
		new nsID(NS_ICLIPBOARDDRAGDROPHOOKS_IID_STRING);

	public nsIClipboardDragDropHooks(int address) {
		super(address);
	}

	public int AllowStartDrag(int event, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), event, _retval);
	}

	public int AllowDrop(int event, int session, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), event, session, _retval);
	}

	public int OnCopyOrDrag(int event, int trans, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), event, trans, _retval);
	}

	public int OnPasteOrDrop(int event, int trans, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), event, trans, _retval);
	}

	//=========================================================================

	public boolean allowDrop(nsIDOMEvent event, nsIDragSession session) {
		boolean[] result = new boolean[] {false};
		int rc = AllowDrop(event.getAddress(), session.getAddress(), result);
		if (rc != XPCOM.NS_OK) error(rc);
		return result[0];
	}
}
