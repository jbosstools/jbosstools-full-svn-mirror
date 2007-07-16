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

public class nsIClipboard extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 6;

	public static final String NS_ICLIPBOARD_IID_STRING =
		"8b5314ba-db01-11d2-96ce-0060b0fb9956";

	public static final nsID NS_ICLIPBOARD_IID =
		new nsID(NS_ICLIPBOARD_IID_STRING);

	public nsIClipboard(int address) {
		super(address);
	}

	public static final int kSelectionClipboard = 0;
	public static final int kGlobalClipboard = 1;
	
	public int SetData(int aTransferable, int anOwner, int aWhichClipboard) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aTransferable, anOwner, aWhichClipboard);
	}

	public int GetData(int aTransferable, int aWhichClipboard) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aTransferable, aWhichClipboard);
	}

	public int EmptyClipboard(int aWhichClipboard) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aWhichClipboard);
	}

	public int ForceDataToClipboard(int aWhichClipboard) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aWhichClipboard);
	}

	public int HasDataMatchingFlavors(int aFlavorList, int aWhichClipboard, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aFlavorList, aWhichClipboard, _retval);
	}

	public int SupportsSelectionClipboard(boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), _retval);
	}
}
