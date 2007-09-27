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

public class nsISimpleEnumerator extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 2;

	public static final String NS_ISIMPLEENUMERATOR_IID_STRING =
		"d1899240-f9d2-11d2-bdd6-000064657374";

	public static final nsID NS_ISIMPLEENUMERATOR_IID =
		new nsID(NS_ISIMPLEENUMERATOR_IID_STRING);

	public nsISimpleEnumerator(int address) {
		super(address);
	}
	
	public int HasMoreElements(boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), _retval);
	}

	public int GetNext(int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), _retval);
	}

	//=========================================================================

	public boolean hasMoreElements() {
		boolean[] result = new boolean[] {false};
		int rc = HasMoreElements(result);
		if (rc != XPCOM.NS_OK) error(rc);
		return result[0];
	}

	public nsISupports getNext() {
		int[] result = new int[] {0};
		int rc = GetNext(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) {
			return null;
		} else {
			return new nsISupports(result[0]);
		}
	}
}
