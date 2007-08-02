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

public class nsIDOMKeyEvent extends nsIDOMUIEvent {

	static final int LAST_METHOD_ID = nsIDOMUIEvent.LAST_METHOD_ID + 7;

	public static final String NS_IDOMKEYEVENT_IID_STRING =
		"028e0e6e-8b01-11d3-aae7-0010838a3123";

	public static final nsID NS_IDOMKEYEVENT_IID =
		new nsID(NS_IDOMKEYEVENT_IID_STRING);

	public nsIDOMKeyEvent(int address) {
		super(address);
	}

	public int GetCharCode(int[] aCharCode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aCharCode);
	}

	public int GetKeyCode(int[] aKeyCode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aKeyCode);
	}

	public int GetAltKey(boolean[] aAltKey) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aAltKey);
	}

	public int GetCtrlKey(boolean[] aCtrlKey) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aCtrlKey);
	}

	public int GetShiftKey(boolean[] aShiftKey) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aShiftKey);
	}

	public int GetMetaKey(boolean[] aMetaKey) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aMetaKey);
	}

	public int InitMouseEvent(int typeArg, boolean canBubbleArg, boolean cancelableArg, int[] viewArg, boolean ctrlKeyArg, boolean altKeyArg, boolean shiftKeyArg, boolean metaKeyArg, int keyCodeArg, int charCodeArg) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	//=========================================================================

	public int getCharCode() {
		int[] aCharCode = new int[] {0};
		int rc = GetCharCode(aCharCode);
		if (rc != XPCOM.NS_OK) error(rc);
		return aCharCode[0];
	}

	public int getKeyCode() {
		int[] aKeyCode = new int[] {0};
		int rc = GetKeyCode(aKeyCode);
		if (rc != XPCOM.NS_OK) error(rc);
		return aKeyCode[0];
	}

	public boolean isCtrlKey() {
		boolean[] aCtrlKey = new boolean[] {true};
		int rc = GetCtrlKey(aCtrlKey);
		if (rc != XPCOM.NS_OK) error(rc);
		return aCtrlKey[0];
	}

	public boolean isShiftKey() {
		boolean[] aShiftKey = new boolean[] {true};
		int rc = GetShiftKey(aShiftKey);
		if (rc != XPCOM.NS_OK) error(rc);
		return aShiftKey[0];
	}

	public boolean isMetaKey() {
		boolean[] aMetaKey = new boolean[] {false};
		int rc = GetMetaKey(aMetaKey);
		if (rc != XPCOM.NS_OK) error(rc);
		return aMetaKey[0];
	}
}
