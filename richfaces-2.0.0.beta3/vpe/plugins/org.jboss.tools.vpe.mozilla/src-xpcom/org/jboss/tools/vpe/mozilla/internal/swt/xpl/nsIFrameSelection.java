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

public class nsIFrameSelection extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 39;

	public static final String NS_IFRAMESELECTION_IID_STRING =
		"f46e4171-deaa-11d1-97fc-00609730c14e";

	public static final nsID NS_IFRAMESELECTION_IID =
		new nsID(NS_IFRAMESELECTION_IID_STRING);

	public nsIFrameSelection(int address) {
		super(address);
	}
	
	public int EnableFrameNotification(boolean aEnable) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 12, getAddress(), aEnable);
	}

	public int SetMouseDownState(boolean aState) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 14, getAddress(), aState);
	}

	public int GetMouseDownState(boolean[] aState) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 15, getAddress(), aState);
	}

	public int CharacterMove(boolean aForward, boolean aExtend) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 24, getAddress(), aForward, aExtend);
	}

	public int LineMove(boolean aForward, boolean aExtend) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 26, getAddress(), aForward, aExtend);
	}

	public int IntraLineMove(boolean aForward, boolean aExtend) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 27, getAddress(), aForward, aExtend);
	}

	//=========================================================================

	public void enableFrameNotification(boolean enable) {
		int rc = EnableFrameNotification(enable);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void setMouseDownState(boolean state) {
		int rc = SetMouseDownState(state);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public boolean getMouseDownState() {
		boolean[] aState = new boolean[] {false};
		int rc = GetMouseDownState(aState);
		if (rc != XPCOM.NS_OK) error(rc);
		return aState[0];
	}

	public boolean characterMove(boolean forward, boolean extend) {
		int rc = CharacterMove(forward, extend);
		return rc == XPCOM.NS_OK;
	}

	public boolean lineMove(boolean forward, boolean extend) {
		int rc = LineMove(forward, extend);
		return rc == XPCOM.NS_OK;
	}

	public boolean intraLineMove(boolean forward, boolean extend) {
		int rc = IntraLineMove(forward, extend);
		return rc == XPCOM.NS_OK;
	}
}
