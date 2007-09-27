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

import org.jboss.tools.vpe.mozilla.internal.swt.xpl.XPCOMObject;

public class nsISelectionPrivate extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 13;

	public static final String NS_ISELECTIONPRIVATE_IID_STRING =
		"2d5535e2-1dd2-11b2-8e38-d53ec833adf6";

	public static final nsID NS_ISELECTIONPRIVATE_IID =
		new nsID(NS_ISELECTIONPRIVATE_IID_STRING);

	public nsISelectionPrivate(int address) {
		super(address);
	}

	public int AddSelectionListener(int newListener) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), newListener);
	}

	public int RemoveSelectionListener(int listenerToRemove) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), listenerToRemove);
	}
	
	//=========================================================================

	public void addSelectionListener(XPCOMObject newListener) {
		int rc = AddSelectionListener(newListener.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void removeSelectionListener(XPCOMObject newListener) {
		int rc = RemoveSelectionListener(newListener.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
	}
}
