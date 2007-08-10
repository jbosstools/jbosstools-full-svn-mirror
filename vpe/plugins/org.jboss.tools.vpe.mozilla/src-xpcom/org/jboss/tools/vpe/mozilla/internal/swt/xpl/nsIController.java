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

public class nsIController extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 4;

	public static final String NS_ICONTROLLER_IID_STRING =
		"D5B61B82-1DA4-11d3-BF87-00105A1B0627";

	public static final nsID NS_ICONTROLLER_IID =
		new nsID(NS_ICONTROLLER_IID_STRING);

	public nsIController(int address) {
		super(address);
	}

	public int IsCommandEnabled(byte[] command, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), command, _retval);
	}

	public int SupportsCommand(byte[] command, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), command, _retval);
	}

	public int DoCommand(byte[] command) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), command);
	}

	public int OnEvent(byte[] eventName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), eventName);
	}
}