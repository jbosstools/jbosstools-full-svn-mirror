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

public class nsIDOMFocusListener extends nsIDOMEventListener {

	static final int LAST_METHOD_ID = nsIDOMEventListener.LAST_METHOD_ID + 2;

	public static final String NS_IDOMFOCUSLISTENER_IID_STRING =
		"80974670-ded6-11d1-bd85-00805f8ae3f4";

	public static final nsID NS_IDOMFOCUSLISTENER_IID =
		new nsID(NS_IDOMFOCUSLISTENER_IID_STRING);

	public nsIDOMFocusListener(int address) {
		super(address);
	}
}
