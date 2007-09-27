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

public class nsIDOMDocumentView extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 1;

	public static final String NS_IDOMDOCUMENTVIEW_IID_STRING =
		"1acdb2ba-1dd2-11b2-95bc-9542495d2569";

	public static final nsID NS_IDOMDOCUMENTVIEW_IID =
		new nsID(NS_IDOMDOCUMENTVIEW_IID_STRING);

	public nsIDOMDocumentView(int address) {
		super(address);
	}

	public int GetDefaultView(int[] aDefaultView) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aDefaultView);
	}

	//=========================================================================

	public nsIDOMAbstractView getDefaultView() {
		int[] aDefaultView = new int[1];
		int rc = GetDefaultView(aDefaultView);
		if (rc != XPCOM.NS_OK) error(rc);
		if (aDefaultView[0] == 0) {
			return null;
		} else {
			return new nsIDOMAbstractView(aDefaultView[0]);
		}
	}
}
