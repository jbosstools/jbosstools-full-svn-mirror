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

import org.jboss.tools.vpe.mozilla.internal.swt.xpl.XPCOMObject;

public class nsISelectionController extends nsISelectionDisplay {

	static final int LAST_METHOD_ID = nsISelectionDisplay.LAST_METHOD_ID + 22;

	public static final String NS_ISELECTIONCONTROLLER_IID_STRING =
		"d2d1d179-85a7-11d3-9932-00108301233c";

	public static final nsID NS_ISELECTIONCONTROLLER_IID =
		new nsID(NS_ISELECTIONCONTROLLER_IID_STRING);

	public nsISelectionController(int address) {
		super(address);
	}

	// type
	public static final short SELECTION_NONE = 0;
	public static final short SELECTION_NORMAL = 1;
	public static final short SELECTION_SPELLCHECK = 2;
	public static final short SELECTION_IME_RAWINPUT = 4;
	public static final short SELECTION_IME_SELECTEDRAWTEXT = 8;
	public static final short SELECTION_IME_CONVERTEDTEXT = 16;
	public static final short SELECTION_IME_SELECTEDCONVERTEDTEXT = 32;
	public static final short SELECTION_ACCESSIBILITY = 64;
	
	public static final short NUM_SELECTIONTYPES = 8;

	// region
	public static final short SELECTION_ANCHOR_REGION = 0;
	public static final short SELECTION_FOCUS_REGION = 1;
	public static final short NUM_SELECTION_REGIONS = 2;
	
	// toggle
	public static final short SELECTION_OFF = 0;
	public static final short SELECTION_HIDDEN = 1;
	public static final short SELECTION_ON = 2;
	public static final short SELECTION_DISABLED = 3;
	public static final short SELECTION_ATTENTION = 4;

	public int SetDisplaySelection(short toggle) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), toggle);
	}

	public int GetDisplaySelection(short[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), _retval);
	}

	public int GetSelection(short type, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), type, _retval);
	}

	public int ScrollSelectionIntoView(short type, short region, boolean isSynchronous) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), type, region, isSynchronous);
	}

	public int RepaintSelection(short type) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), type);
	}

	public int SetCaretEnabled(boolean enabled) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), enabled);
	}

	public int GetCaretEnabled(boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), _retval);
	}
	
	//=========================================================================

	public void setDisplaySelection(short toggle) {
		int rc = SetDisplaySelection(toggle);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public short getDisplaySelection() {
		short[] aToggle = new short[] {0};
		int rc = GetDisplaySelection(aToggle);
		if (rc != XPCOM.NS_OK) error(rc);
		return aToggle[0];
	}

	public nsISelection getSelection(short type) {
		int[] result = new int[] {0};
		int rc = GetSelection(type, result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return new nsISelection(result[0]);
	}

	public nsISelection getSelection() {
		return getSelection(SELECTION_NORMAL);
	}

	public void scrollSelectionIntoView(short type, short region, boolean isSynchronous) {
		int rc = ScrollSelectionIntoView(type, region, isSynchronous);
//		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void scrollSelectionIntoView() {
		scrollSelectionIntoView(SELECTION_NORMAL, SELECTION_FOCUS_REGION, true);
	}

	public void repaintSelection(short type) {
		int rc = RepaintSelection(type);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void setCaretEnabled(boolean enabled) {
		int rc = SetCaretEnabled(enabled);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public boolean getCaretEnabled() {
		boolean[] result = new boolean[] {false};
		int rc = GetCaretEnabled(result);
		if (rc != XPCOM.NS_OK) error(rc);
		return result[0];
	}

	public void addSelectionListener(XPCOMObject listener) {
		nsISelection selection = getSelection();
		int aSelectionPrivate = nsISupports.queryInterface(selection.getAddress(), nsISelectionPrivate.NS_ISELECTIONPRIVATE_IID);
		nsISelectionPrivate selectionPrivate = new nsISelectionPrivate(aSelectionPrivate);
		selectionPrivate.addSelectionListener(listener);
		selectionPrivate.Release();
		selection.Release();
	}

	public void removeSelectionListener(XPCOMObject listener) {
		nsISelection selection = getSelection();
		int aSelectionPrivate = nsISupports.queryInterface(selection.getAddress(), nsISelectionPrivate.NS_ISELECTIONPRIVATE_IID);
		nsISelectionPrivate selectionPrivate = new nsISelectionPrivate(aSelectionPrivate);
		selectionPrivate.removeSelectionListener(listener);
		selectionPrivate.Release();
		selection.Release();
	}
}
