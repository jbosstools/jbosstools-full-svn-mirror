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

public class nsIDOMScrollListener extends nsIDOMEventListener {

	static final int LAST_METHOD_ID = nsIDOMEventListener.LAST_METHOD_ID + 3;

	public static final String NS_IDOMSCROLLLISTENER_IID_STRING =
		"00B04615-4BC4-11d4-BA11-001083023C1E";

	public static final nsID NS_IDOMSCROLLLISTENER_IID =
		new nsID(NS_IDOMSCROLLLISTENER_IID_STRING);

	public nsIDOMScrollListener(int address) {
		super(address);
	}

	public int Overflow(int aEvent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aEvent);
	}

	public int Underflow(int aEvent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aEvent);
	}

	public int OverflowChanged(int aEvent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aEvent);
	}
}
