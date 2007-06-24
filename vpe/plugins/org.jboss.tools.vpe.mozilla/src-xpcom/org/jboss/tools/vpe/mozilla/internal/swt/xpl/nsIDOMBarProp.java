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

public class nsIDOMBarProp extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 2;

	public static final String NS_IDOMBARPROP_IID_STRING =
		"9eb2c150-1d56-11d3-8221-0060083a0bcf";

	public static final nsID NS_IDOMBARPROP_IID =
		new nsID(NS_IDOMBARPROP_IID_STRING);

	public nsIDOMBarProp(int address) {
		super(address);
	}

	public int GetVisible(boolean[] aVisible) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aVisible);
	}

	public int SetVisible(boolean aVisible) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aVisible);
	}

	//=========================================================================

	public boolean getVisible() {
		boolean[] result = new boolean[] {false};
		int rc = GetVisible(result);
		if (rc != XPCOM.NS_OK) error(rc);
		return result[0];
	}

	public void setVisible(boolean visible) {
		int rc = SetVisible(visible);
		if (rc != XPCOM.NS_OK) error(rc);
	}
}
