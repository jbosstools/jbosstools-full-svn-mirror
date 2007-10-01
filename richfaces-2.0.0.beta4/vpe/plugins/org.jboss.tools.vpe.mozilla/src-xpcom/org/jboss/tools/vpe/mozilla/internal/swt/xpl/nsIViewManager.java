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

public class nsIViewManager extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 53;

	public static final String NS_IVIEWMANAGER_IID_STRING =
		"3a8863d0-a7f3-11d1-a824-0040959a28c9";

	public static final nsID NS_IVIEWMANAGER_IID =
		new nsID(NS_IVIEWMANAGER_IID_STRING);

	public nsIViewManager(int address) {
		super(address);
	}

	static final int NS_VMREFRESH_IMMEDIATE 	= 0x0002;
	static final int NS_VMREFRESH_NO_SYNC		= 0x0004;
	static final int NS_VMREFRESH_SMOOTHSCROLL	= 0x0008;

	public int Composite() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress());
	}

	public int SetViewObserver(int aObserver) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 30, getAddress(), aObserver);
	}

	public int GetViewObserver(int[] aObserver) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 31, getAddress(), aObserver);
	}

	public int GetRootScrollableView(int[] aScrollable) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 38, getAddress(), aScrollable);
	}

	public int AddCompositeListener(int aListener) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 40, getAddress(), aListener);
	}

	public int RemoveCompositeListener(int aListener) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 41, getAddress(), aListener);
	}

	//=========================================================================

	public nsIViewObserver getViewObserver() {
		int[] aObserver = new int[] {0};
		int rc = GetViewObserver(aObserver);
		if (rc != XPCOM.NS_OK) error(rc);
		if (aObserver[0] == 0) {
			return null;
		} else {
			return new nsIViewObserver(aObserver[0]);
		}
	}

	public void addCompositeListener(XPCOMObject listener) {
		if (listener != null) {
			int rc = AddCompositeListener(listener.getAddress());
		}
	}

	public void removeCompositeListener(XPCOMObject listener) {
		if (listener != null) {
			int rc = RemoveCompositeListener(listener.getAddress());
		}
	}

	public void composite() {
		int rc = Composite();
		if (rc != XPCOM.NS_OK) error(rc);
	}
}
