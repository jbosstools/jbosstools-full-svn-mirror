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

public class nsIViewObserver extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 3;

	public static final String NS_IVIEWOBSERVER_IID_STRING =
		"6a1529e0-3d2c-11d2-a832-0040959a28c9";

	public static final nsID NS_IVIEWOBSERVER_IID =
		new nsID(NS_IVIEWOBSERVER_IID_STRING);

	public nsIViewObserver(int address) {
		super(address);
	}
}
