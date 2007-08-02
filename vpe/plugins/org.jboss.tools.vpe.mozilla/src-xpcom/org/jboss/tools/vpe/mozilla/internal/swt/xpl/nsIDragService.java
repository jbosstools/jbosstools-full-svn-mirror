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

public class nsIDragService extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 4;

	public static final String NS_IDRAGSERVICE_IID_STRING =
		"8b5314bb-db01-11d2-96ce-0060b0fb9956";

	public static final nsID NS_IDRAGSERVICE_IID =
		new nsID(NS_IDRAGSERVICE_IID_STRING);

	public nsIDragService(int address) {
		super(address);
	}

	public static final int DRAGDROP_ACTION_NONE = 0;
	public static final int DRAGDROP_ACTION_COPY = 1;
	public static final int DRAGDROP_ACTION_MOVE = 2;
	public static final int DRAGDROP_ACTION_LINK = 4;

	public int InvokeDragSession(int aDOMNode, int aTransferables, int aRegion, int aActionType) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aDOMNode, aTransferables, aRegion);
	}

	public int GetCurrentSession(int[] aDragSession) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aDragSession);
	}

	public int StartDragSession() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress());
	}

	public int EndDragSession() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress());
	}

	//=========================================================================

	public nsIDragSession getCurrentSession() {
		int[] result = new int[1];
		int rc = GetCurrentSession(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) {
			return null;
		} else {
			return new nsIDragSession(result[0]);
		}
	}
}
