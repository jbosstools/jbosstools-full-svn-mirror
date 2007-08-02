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

public class nsIDOMAbstractView extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 1;

	public static final String NS_IDOMABSTRACTVIEW_IID_STRING =
		"f51ebade-8b1a-11d3-aae7-0010830123b4";

	public static final nsID NS_IDOMABSTRACTVIEW_IID =
		new nsID(NS_IDOMABSTRACTVIEW_IID_STRING);

	public nsIDOMAbstractView(int address) {
		super(address);
	}
}
