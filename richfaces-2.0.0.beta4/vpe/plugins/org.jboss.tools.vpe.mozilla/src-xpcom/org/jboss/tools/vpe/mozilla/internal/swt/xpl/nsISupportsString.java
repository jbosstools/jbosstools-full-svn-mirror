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

public class nsISupportsString extends nsISupportsPrimitive {

	static final int LAST_METHOD_ID = nsIDOMNode.LAST_METHOD_ID + 3;

	public static final String NS_ISUPPORTSSTRING_IID_STRING =
		"d79dc970-4a1c-11d3-9890-006008962422";

	public static final nsID NS_ISUPPORTSSTRING_IID =
		new nsID(NS_ISUPPORTSSTRING_IID_STRING);

	public nsISupportsString(int address) {
		super(address);
	}

	public int GetData(int aData) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aData);
	}

	//=========================================================================

	public String getData() {
		nsString nsData = new nsString();
		int rc = GetData(nsData.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
		String data = nsData.toString();
		nsData.dispose();
		return data;
	}
}
