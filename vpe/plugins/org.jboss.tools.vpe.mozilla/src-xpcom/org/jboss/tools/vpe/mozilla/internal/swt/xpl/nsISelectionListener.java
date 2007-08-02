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

public class nsISelectionListener extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 1;

	public static final String NS_ISELECTIONLISTENER_IID_STRING =
		"a6cf90e2-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_ISELECTIONLISTENER_IID =
		new nsID(NS_ISELECTIONLISTENER_IID_STRING);

	public nsISelectionListener(int address) {
		super(address);
	}

	public static final int NO_REASON = 0;
	public static final int DRAG_REASON = 1;
	public static final int MOUSEDOWN_REASON = 2;
	public static final int MOUSEUP_REASON = 4;
	public static final int KEYPRESS_REASON = 8;
	public static final int SELECTALL_REASON = 16;

	public int NotifySelectionChanged(int doc, int sel, short reason) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), doc, sel, reason);
	}
}
