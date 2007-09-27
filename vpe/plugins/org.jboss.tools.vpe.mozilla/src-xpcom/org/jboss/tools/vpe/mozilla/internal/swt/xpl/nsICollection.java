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

public class nsICollection extends nsISerializable {

	static final int LAST_METHOD_ID = nsISerializable.LAST_METHOD_ID + 8;

	public static final String NS_ICOLLECTION_IID_STRING =
		"83b6019c-cbc4-11d2-8cca-0060b0fc14a3";

	public static final nsID NS_ICOLLECTION_IID =
		new nsID(NS_ICOLLECTION_IID_STRING);

	public nsICollection(int address) {
		super(address);
	}

	public int Count(int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), _retval);
	}


//	NS_IMETHOD GetElementAt(PRUint32 index, nsISupports **_retval); \
//	NS_IMETHOD QueryElementAt(PRUint32 index, const nsIID & uuid, void * *result); \
//	NS_IMETHOD SetElementAt(PRUint32 index, nsISupports *item); \
//	NS_IMETHOD AppendElement(nsISupports *item); \
//	NS_IMETHOD RemoveElement(nsISupports *item); \
//	NS_IMETHOD Enumerate(nsIEnumerator **_retval); \
//	NS_IMETHOD Clear(void); 


	//=========================================================================

	public int count() {
		int[] result = new int[1];
		int rc = Count(result);
		if (rc != XPCOM.NS_OK) error(rc);
		return result[0];
	}
}
