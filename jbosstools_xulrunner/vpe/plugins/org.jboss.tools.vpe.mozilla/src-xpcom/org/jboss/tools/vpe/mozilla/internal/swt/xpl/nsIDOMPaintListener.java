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

public class nsIDOMPaintListener extends nsIDOMEventListener {

	static final int LAST_METHOD_ID = nsIDOMEventListener.LAST_METHOD_ID + 3;

	public static final String NS_IDOMPAINTLISTENER_IID_STRING =
		"a6cf906a-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMPAINTLISTENER_IID =
		new nsID(NS_IDOMPAINTLISTENER_IID_STRING);

	public nsIDOMPaintListener(int address) {
		super(address);
	}

	public int Paint(int aEvent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aEvent);
	}

	public int Resize(int aEvent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aEvent);
	}

	public int Scroll(int aEvent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aEvent);
	}
}
