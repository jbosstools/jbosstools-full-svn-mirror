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

public class nsIDOMDocumentRange extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 1;

	public static final String NS_IDOMDOCUMENTRANGE_IID_STRING =
		"7b9badc6-c9bc-447a-8670-dbd195aed24b";

	public static final nsID NS_IDOMDOCUMENTRANGE_IID =
		new nsID(NS_IDOMDOCUMENTRANGE_IID_STRING);

	public nsIDOMDocumentRange(int address) {
		super(address);
	}

	public int CreateRange(int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), _retval);
	}

	//=========================================================================

	public nsIDOMRange createRange() {
		int[] result = new int[1];
		int rc = CreateRange(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return new nsIDOMRange(result[0]);
	}
}
