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

public class nsICaret extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 13;

	public static final String NS_ICARET_IID_STRING =
		"a6cf90e1-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_ICARET_IID =
		new nsID(NS_ICARET_IID_STRING);

	public nsICaret(int address) {
		super(address);
	}

	public int Init(int inPresShell) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), inPresShell);
	}

	public int Terminate() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress());
	}

	public int GetCaretDOMSelection(int[] aDOMSel) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aDOMSel);
	}

	public int SetCaretDOMSelection(int aDOMSel) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aDOMSel);
	}

	public int SetCaretVisible(boolean inMakeVisible) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), inMakeVisible);
	}

	public int GetCaretVisible(boolean[] outMakeVisible) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), outMakeVisible);
	}

	public int SetCaretReadOnly(boolean inMakeReadonly) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), inMakeReadonly);
	}

	public int GetCaretCoordinates(int aRelativeToType, int aDOMSel, int outCoordinates, boolean[] outIsCollapsed, int[] outView) {
//		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), aRelativeToType, aDOMSel, outCoordinates, outIsCollapsed, outView);
		return 0;
	}

	public int ClearFrameRefs(int aFrame) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), aFrame);
	}

	public int EraseCaret() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress());
	}

	public int SetCaretWidth(int aPixels) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), aPixels);
	}

	public int SetVisibilityDuringSelection(boolean aVisibilityDuringSelection) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 12, getAddress(), aVisibilityDuringSelection);
	}

	public int DrawAtPosition(int aNode, int aOffset) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 13, getAddress(), aNode, aOffset);
	}

	//=========================================================================

	public void init(nsIPresShell presShell) {
		int rc = Init(presShell.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void setCaretVisible(boolean visible) {
		int rc = SetCaretVisible(visible);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public boolean isCaretVisible() {
		boolean[] aVisible = new boolean[] {false}; 
		int rc = GetCaretVisible(aVisible);
		if (rc != XPCOM.NS_OK) error(rc);
		return aVisible[0];
	}
	
	public void setCaretReadOnly(boolean readonly) {
		int rc = SetCaretReadOnly(readonly);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void eraseCaret() {
		int rc = EraseCaret();
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void setCaretWidth(int pixels) {
		int rc = SetCaretWidth(pixels);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void drawAtPosition(nsIDOMNode node, int offset) {
		int rc = DrawAtPosition(node.getAddress(), offset);
		if (rc != XPCOM.NS_OK) error(rc);
	}
}
