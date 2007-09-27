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

public class nsISerializable extends nsISupports {
	
	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 2;

	public static final String NS_ISERIALIZABLE_IID_STRING =
		"91cca981-c26d-44a8-bebe-d9ed4891503a";

	public static final nsID NS_ISERIALIZABLE_IID =
		new nsID(NS_ISERIALIZABLE_IID_STRING);

	public nsISerializable(int address) {
		super(address);
	}


//	NS_IMETHOD Read(nsIObjectInputStream *aInputStream);
//	NS_IMETHOD Write(nsIObjectOutputStream *aOutputStream); 
}
