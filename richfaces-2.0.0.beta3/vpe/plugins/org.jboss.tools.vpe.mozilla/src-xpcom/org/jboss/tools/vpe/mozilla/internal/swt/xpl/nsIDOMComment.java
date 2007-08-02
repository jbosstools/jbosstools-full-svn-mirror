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

public class nsIDOMComment extends nsIDOMCharacterData {

	static final int LAST_METHOD_ID = nsIDOMCharacterData.LAST_METHOD_ID + 1;

	public static final String NS_IDOMCOMMENT_IID_STRING =
		"a6cf9073-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMCOMMENT_IID =
		new nsID(NS_IDOMCOMMENT_IID_STRING);

	public nsIDOMComment(int address) {
		super(address);
	}
}
