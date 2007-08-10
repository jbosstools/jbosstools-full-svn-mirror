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

public class nsICompositeListener extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 4;

	public static final String NS_ICOMPOSITELISTENER_IID_STRING =
		"5661ce55-7c42-11d3-9d1d-0060b0f8baff";

	public static final nsID NS_ICOMPOSITELISTENER_IID =
		new nsID(NS_ICOMPOSITELISTENER_IID_STRING);

	public nsICompositeListener(int address) {
		super(address);
	}
}
