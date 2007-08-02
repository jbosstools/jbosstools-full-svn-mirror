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

public class nsIContent extends nsISupports {
	
	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 37;

	public static final String NS_ICONTENT_IID_STRING =
		"78030220-9447-11d1-9323-00805f8add32";

	public static final nsID NS_ICONTENT_IID =
		new nsID(NS_ICONTENT_IID_STRING);

	public nsIContent(int address) {
		super(address);
	}
}
