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

public class nsIDOMEventListener extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 1;

	public static final String NS_IDOMEVENTLISTENER_IID_STRING =
		"df31c120-ded6-11d1-bd85-00805f8ae3f4";

	public static final nsID NS_IDOMEVENTLISTENER_IID =
		new nsID(NS_IDOMEVENTLISTENER_IID_STRING);

	public nsIDOMEventListener(int address) {
		super(address);
	}

	public int HandleEvent(int event) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), event);
	}

}
