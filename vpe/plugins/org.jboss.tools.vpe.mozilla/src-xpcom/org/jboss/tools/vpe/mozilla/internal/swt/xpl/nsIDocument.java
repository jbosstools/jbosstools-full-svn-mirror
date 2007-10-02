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

public class nsIDocument extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 11;

	public static final String NS_IDOCUMENT_IID_STRING =
		"94c6ceb0-9447-11d1-9323-00805f8add32";

//	{ 0x94c6ceb0, 0x9447, 0x11d1, \
//		  {0x93, 0x23, 0x00, 0x80, 0x5f, 0x8a, 0xdd, 0x32} }

	public static final nsID NS_IDOCUMENT_IID =
		new nsID(NS_IDOCUMENT_IID_STRING);

	public nsIDocument(int address) {
		super(address);
	}
}
