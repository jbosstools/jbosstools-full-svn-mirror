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

public class nsIEditor extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 24;

	public static final String NS_IEDITOR_IID_STRING =
		"06b979ce-1dd2-11b2-b6c7-a8bc47aa06b6";

	public static final nsID NS_IEDITOR_IID =
		new nsID(NS_IEDITOR_IID_STRING);

	public nsIEditor(int address) {
		super(address);
	}
}
