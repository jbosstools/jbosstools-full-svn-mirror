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

public class nsIDOMWindowInternal extends nsIDOMWindow {

	static final int LAST_METHOD_ID = nsIDOMWindow.LAST_METHOD_ID + 66;

	public static final String NS_IDOMWINDOWINTERNAL_IID_STRING =
		"9c911860-7dd9-11d4-9a83-000064657374";

	public static final nsID NS_IDOMWINDOWINTERNAL_IID =
		new nsID(NS_IDOMWINDOWINTERNAL_IID_STRING);

	public nsIDOMWindowInternal(int address) {
		super(address);
	}

	public int GetWindow(int[] aWindow) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aWindow);
	}

	public int GetSelf(int[] aSelf) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aSelf);
	}

	public int GetContent(int[] aContent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aContent);
	}

	public int GetFrameElement(int[] aFrameElement) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 66, getAddress(), aFrameElement);
	}

	//=========================================================================

	public nsIDOMWindowInternal getWindow() {
		int[] aWindow = new int[] {0};
		int rc = GetWindow(aWindow);
		if (rc != XPCOM.NS_OK) error(rc);
		if (aWindow[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return new nsIDOMWindowInternal(aWindow[0]);
	}

	public nsIDOMWindowInternal getSelf() {
		int[] aSelf = new int[] {0};
		int rc = GetSelf(aSelf);
		if (rc != XPCOM.NS_OK) error(rc);
		if (aSelf[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return new nsIDOMWindowInternal(aSelf[0]);
	}

	public nsIDOMWindow getContent() {
		int[] aContent = new int[] {0};
		int rc = GetContent(aContent);
		if (rc != XPCOM.NS_OK) error(rc);
		if (aContent[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return new nsIDOMWindowInternal(aContent[0]);
	}

	public nsIDOMElement getFrameElement() {
		int[] aFrameElement = new int[] {0};
		int rc = GetFrameElement(aFrameElement);
		if (rc != XPCOM.NS_OK) error(rc);
		if (aFrameElement[0] == 0) {
			return null;
		} else {
			return new nsIDOMElement(aFrameElement[0]);
		}
	}
}
