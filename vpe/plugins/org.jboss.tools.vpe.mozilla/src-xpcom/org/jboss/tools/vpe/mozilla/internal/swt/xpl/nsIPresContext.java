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

public class nsIPresContext extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 84;

	public static final String NS_IPRESCONTEXT_IID_STRING =
		"0a5d12e0-944e-11d1-9323-00805f8add32";

	public static final nsID NS_IPRESCONTEXT_IID =
		new nsID(NS_IPRESCONTEXT_IID_STRING);

	public nsIPresContext(int address) {
		super(address);
	}

	public int GetPixelsToTwips(float[] aResult) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 58, getAddress(), aResult);
	}

	//=========================================================================

	public float getPixelsToTwips() {
		float[] aResult = new float[] {0};
		int rc = GetPixelsToTwips(aResult);
		if (rc != XPCOM.NS_OK) error(rc);
		return aResult[0];
	}
}
