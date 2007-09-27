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

public class nsIDOMNSHTMLDocument extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 35;

	public static final String NS_IDOMNSHTMLDOCUMENT_IID_STRING =
		"a6cf90c5-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMNSHTMLDOCUMENT_IID =
		new nsID(NS_IDOMNSHTMLDOCUMENT_IID_STRING);

	public nsIDOMNSHTMLDocument(int address) {
		super(address);
	}

	public int GetDesignMode(int aDesignMode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 26, getAddress(), aDesignMode);
	}

	public int SetDesignMode(int aDesignMode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 27, getAddress(), aDesignMode);
	}

	//=========================================================================

	public String getDesignMode() {
		nsString nsDesignMode = new nsString();
		int rc = GetDesignMode(nsDesignMode.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
		String designMode = nsDesignMode.toString();
		nsDesignMode.dispose();
		return designMode;
	}

	public void setDesignMode(String designMode) {
		nsString nsDesignMode = new nsString(designMode);
		int rc = SetDesignMode(nsDesignMode.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
		nsDesignMode.dispose();
	}
}
