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

public class nsIEditorDocShell extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 5;

	public static final String NS_IEDITORDOCSHELL_IID_STRING =
		"3bdb8f01-f141-11d4-a73c-fba4aba8a3fc";

	public static final nsID NS_IEDITORDOCSHELL_IID =
		new nsID(NS_IEDITORDOCSHELL_IID_STRING);

	public nsIEditorDocShell(int address) {
		super(address);
	}

	public int GetEditor(int[] aEditor) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aEditor);
	}

	//=========================================================================

	public nsIEditor getEditor() {
		int[] aEditor = new int[] {0};
		int rc = GetEditor(aEditor);
		if (rc != XPCOM.NS_OK) error(rc);
		if (aEditor[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return new nsIEditor(aEditor[0]);
	}
}
