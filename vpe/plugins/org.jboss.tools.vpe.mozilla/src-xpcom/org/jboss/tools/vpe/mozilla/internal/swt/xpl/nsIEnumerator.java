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

public class nsIEnumerator extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 4;

	public static final String NS_IENUMERATOR_IID_STRING =
		"ad385286-cbc4-11d2-8cca-0060b0fc14a3";

	public static final nsID NS_IENUMERATOR_IID =
		new nsID(NS_IENUMERATOR_IID_STRING);

	public nsIEnumerator(int address) {
		super(address);
	}

	public int First() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress());
	}

	public int Next() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress());
	}

	public int CurrentItem(int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), _retval);
	}

	public int IsDone() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress());
	}
}
