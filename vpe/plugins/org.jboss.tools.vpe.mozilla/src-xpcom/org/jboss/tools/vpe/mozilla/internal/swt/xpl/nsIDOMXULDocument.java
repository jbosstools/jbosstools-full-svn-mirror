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

public class nsIDOMXULDocument extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 11;

	public static final String NS_IDOMXULDOCUMENT_IID_STRING =
		"17ddd8c0-c5f8-11d2-a6ae-00104bde6048";

	public static final nsID NS_IDOMXULDOCUMENT_IID =
		new nsID(NS_IDOMXULDOCUMENT_IID_STRING);

	public nsIDOMXULDocument(int address) {
		super(address);
	}

	public int GetTooltipNode(int[] aTooltipNode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aTooltipNode);
	}

	//=========================================================================

	public nsIDOMNode getTooltipNode() {
		int[] result = new int[1];
		int rc = GetTooltipNode(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return nsIDOMNode.getNodeAtAddress(result[0]);
	}
}
