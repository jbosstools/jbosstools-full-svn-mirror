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

public class nsISupportsPrimitive extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 1;

	public static final String NS_ISUPPORTSPRIMITIVE_IID_STRING =
		"d0d4b136-1dd1-11b2-9371-f0727ef827c0";

	public static final nsID NS_ISUPPORTSPRIMITIVE_IID =
		new nsID(NS_ISUPPORTSPRIMITIVE_IID_STRING);

	public nsISupportsPrimitive(int address) {
		super(address);
	}

	public static final int TYPE_ID = 1;
	public static final int TYPE_CSTRING = 2;
	public static final int TYPE_STRING = 3;
	public static final int TYPE_PRBOOL = 4;
	public static final int TYPE_PRUINT8 = 5;
	public static final int TYPE_PRUINT16 = 6;
	public static final int TYPE_PRUINT32 = 7;
	public static final int TYPE_PRUINT64 = 8;
	public static final int TYPE_PRTIME = 9;
	public static final int TYPE_CHAR = 10;
	public static final int TYPE_PRINT16 = 11;
	public static final int TYPE_PRINT32 = 12;
	public static final int TYPE_PRINT64 = 13;
	public static final int TYPE_FLOAT = 14;
	public static final int TYPE_DOUBLE = 15;
	public static final int TYPE_VOID = 16;
	public static final int TYPE_INTERFACE_POINTER = 17;

	public int GetType(int aType) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aType);
	}

	//=========================================================================

	public int getType() {
		int ptrType = XPCOM.PR_Malloc(2);
		int rc = GetType(ptrType);
		if (rc != XPCOM.NS_OK) error(rc);
		int[] aType = new int[] {0};
		XPCOM.memmove(aType, ptrType, 2);
		XPCOM.PR_Free(ptrType);
		return aType[0];
	}
}
