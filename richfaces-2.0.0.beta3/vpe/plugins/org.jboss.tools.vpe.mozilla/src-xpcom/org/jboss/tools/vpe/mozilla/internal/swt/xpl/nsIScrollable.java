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

import org.eclipse.swt.graphics.Point;

public class nsIScrollable extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 12;

	public static final String NS_ISCROLLABLE_IID_STRING =
		"61792520-82c2-11d3-af76-00a024ffc08c";

	public static final nsID NS_ISCROLLABLE_IID =
		new nsID(NS_ISCROLLABLE_IID_STRING);

	public nsIScrollable(int address) {
		super(address);
	}

	public static final int ScrollOrientation_Y = 1;
	public static final int ScrollOrientation_X = 2;

	public int GetCurScrollPos(int scrollOrientation, int[] curPos) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), scrollOrientation, curPos);
	}

	public int SetCurScrollPos(int scrollOrientation, int curPos) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), scrollOrientation, curPos);
	}

	public int SetCurScrollPosEx(int curHorizontalPos, int curVerticalPos) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), curHorizontalPos, curVerticalPos);
	}

	public int GetScrollRange(int scrollOrientation, int minPos, int maxPos) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), scrollOrientation, minPos, maxPos);
	}

	public int SetScrollRange(int scrollOrientation, int minPos, int maxPos) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), scrollOrientation, minPos, maxPos);
	}

	public int SetScrollRangeEx(int minHorizontalPos, int maxHorizontalPos, int minVerticalPos, int maxVerticalPos) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), minHorizontalPos, maxHorizontalPos, minVerticalPos, maxVerticalPos);
	}

	public static final int Scrollbar_Auto = 1;
	public static final int Scrollbar_Never = 2;
	public static final int Scrollbar_Always = 3;

	public int GetCurrentScrollbarPreferences(int scrollOrientation, int[] scrollbarPref) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), scrollOrientation, scrollbarPref);
	}

	public int SetCurrentScrollbarPreferences(int scrollOrientation, int scrollbarPref) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), scrollOrientation, scrollbarPref);
	}

	public int GetDefaultScrollbarPreferences(int scrollOrientation, int[] scrollbarPref) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), scrollOrientation, scrollbarPref);
	}

	public int SetDefaultScrollbarPreferences(int scrollOrientation, int scrollbarPref) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), scrollOrientation, scrollbarPref);
	}

	public int ResetScrollbarPreferences() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress());
	}

	public int GetScrollbarVisibility(boolean[] verticalVisible, boolean[] horizontalVisible) {
//		return XPCOM.VtblCall(super.LAST_METHOD_ID + 12, getAddress(), verticalVisible, horizontalVisible);
		return 0;
	}

	//=========================================================================

	public void setCurrentScrollbarPreferences(int scrollOrientation, int scrollbarPref) {
		int rc = SetCurrentScrollbarPreferences(scrollOrientation, scrollbarPref);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public int getCurrentScrollbarPreferences(int scrollOrientation) {
		int[] result = new int[] {0};
		int rc = GetCurrentScrollbarPreferences(scrollOrientation, result);
		if (rc != XPCOM.NS_OK) error(rc);
		return result[0];
	}

	public void setDefaultScrollbarPreferences(int scrollOrientation, int scrollbarPref) {
		int rc = SetDefaultScrollbarPreferences(scrollOrientation, scrollbarPref);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public int getDefaultScrollbarPreferences(int scrollOrientation) {
		int[] result = new int[] {0};
		int rc = GetDefaultScrollbarPreferences(scrollOrientation, result);
		if (rc != XPCOM.NS_OK) error(rc);
		return result[0];
	}

	public void setCurScrollPos(int scrollOrientation, int curPos) {
		int rc = SetCurScrollPos(scrollOrientation, curPos);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public Point getScrollRange(int scrollOrientation) {
		int minPtr = XPCOM.PR_Malloc(4);
		int maxPtr = XPCOM.PR_Malloc(4);
		int rc = GetScrollRange(scrollOrientation, minPtr, maxPtr);
		if (rc != XPCOM.NS_OK) error(rc);
		int[] minBuffer = new int[1];
		int[] maxBuffer = new int[1];
		XPCOM.memmove(minBuffer, minPtr, 4);
		XPCOM.memmove(maxBuffer, maxPtr, 4);
		XPCOM.PR_Free(minPtr);
		XPCOM.PR_Free(maxPtr);
		return new Point(minBuffer[0], maxBuffer[0]);
	}

	public int getCurScrollPos(int scrollOrientation) {
		int[] result = new int[] {0};
		int rc = GetCurScrollPos(scrollOrientation, result);
		if (rc != XPCOM.NS_OK) error(rc);
		return result[0];
	}
}
