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

public class nsIHTMLObjectResizer extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 11;

	public static final String NS_IHTMLOBJECTRESIZER_IID_STRING =
		"b0338f6c-ded3-4c39-a953-56e8bae494f5";

	public static final nsID NS_IHTMLOBJECTRESIZER_IID =
		new nsID(NS_IHTMLOBJECTRESIZER_IID_STRING);

	public nsIHTMLObjectResizer(int address) {
		super(address);
	}

//	NS_IMETHOD ShowResizers(nsIDOMElement *aResizedElement); \
	public int ShowResizers(int aResizedElement) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aResizedElement);
	}

	//=========================================================================

	public void showResizers(nsIDOMElement resizedElement) {
		int rc = ShowResizers(resizedElement.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
	}
}
