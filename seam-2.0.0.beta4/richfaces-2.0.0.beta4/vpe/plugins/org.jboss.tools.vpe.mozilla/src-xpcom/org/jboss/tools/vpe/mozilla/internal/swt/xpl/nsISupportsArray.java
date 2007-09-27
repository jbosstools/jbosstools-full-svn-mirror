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

public class nsISupportsArray extends nsICollection {

	static final int LAST_METHOD_ID = nsISerializable.LAST_METHOD_ID + 23;

	public static final String NS_ISUPPORTSARRAY_IID_STRING =
		"791eafa0-b9e6-11d1-8031-006008159b5a";

	public static final nsID NS_ISUPPORTSARRAY_IID_IID =
		new nsID(NS_ISUPPORTSARRAY_IID_STRING);

	public nsISupportsArray(int address) {
		super(address);
	}

}
