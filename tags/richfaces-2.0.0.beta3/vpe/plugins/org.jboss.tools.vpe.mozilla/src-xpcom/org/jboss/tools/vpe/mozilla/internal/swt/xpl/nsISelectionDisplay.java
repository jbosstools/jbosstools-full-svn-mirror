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

public class nsISelectionDisplay extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 2;

	public static final String NS_ISELECTIONDISPLAY_IID_STRING =
		"0ddf9e1c-1dd2-11b2-a183-908a08aa75ae";

	public static final nsID NS_ISELECTIONDISPLAY_IID =
		new nsID(NS_ISELECTIONDISPLAY_IID_STRING);

	public nsISelectionDisplay(int address) {
		super(address);
	}

	public static final short DISPLAY_NONE = 0;
	public static final short DISPLAY_TEXT = 1;
	public static final short DISPLAY_IMAGES = 2;
	public static final short DISPLAY_FRAMES = 4;
	public static final short DISPLAY_ALL = 7;

	public int SetSelectionFlags(short toggle) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), toggle);
	}

	public int GetSelectionFlags(short[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), _retval);
	}
	
	//=========================================================================

	public void setSelectionFlags(short toggle) {
		int rc = SetSelectionFlags(toggle);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public short getSelectionFlags() {
		short[] result = new short[] {0};
		int rc = GetSelectionFlags(result);
		if (rc != XPCOM.NS_OK) error(rc);
		return result[0];
	}
}
