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

public class nsIScrollPositionListener extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 2;

	public static final String NS_ISCROLLPOSITIONLISTENER_IID_STRING =
		"f8dfc500-6ad1-11d3-8360-a3f373ff79fc";

	public static final nsID NS_ISCROLLPOSITIONLISTENER_IID =
		new nsID(NS_ISCROLLPOSITIONLISTENER_IID_STRING);

	public nsIScrollPositionListener(int address) {
		super(address);
	}
}
