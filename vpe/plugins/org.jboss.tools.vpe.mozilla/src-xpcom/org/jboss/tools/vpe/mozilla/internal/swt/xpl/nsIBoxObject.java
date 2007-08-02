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

public class nsIBoxObject extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 22;

	public static final String NS_IBOXOBJECT_IID_STRING =
		"caabf76f-9d35-401f-beac-3955817c645c";

	public static final nsID NS_IBOXOBJECT_IID =
		new nsID(NS_IBOXOBJECT_IID_STRING);

	public nsIBoxObject(int address) {
		super(address);
	}

	public int GetX(int[] aX) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aX);
	}

	public int GetY(int[] aY) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), aY);
	}

	public int GetWidth(int[] aWidth) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), aWidth);
	}

	public int GetHeight(int[] aHeight) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), aHeight);
	}

	//=========================================================================

	public int getX() {
		int[] aX = new int[] {0};
		int rc = GetX(aX);
		if (rc != XPCOM.NS_OK) error(rc);
		return aX[0];
	}

	public int getY() {
		int[] aX = new int[] {0};
		int rc = GetY(aX);
		if (rc != XPCOM.NS_OK) error(rc);
		return aX[0];
	}

	public int getWidth() {
		int[] aWidth = new int[] {0};
		int rc = GetWidth(aWidth);
		if (rc != XPCOM.NS_OK) error(rc);
		return aWidth[0];
	}

	public int getHeight() {
		int[] aHeight = new int[] {0};
		int rc = GetHeight(aHeight);
		if (rc != XPCOM.NS_OK) error(rc);
		return aHeight[0];
	}
}
