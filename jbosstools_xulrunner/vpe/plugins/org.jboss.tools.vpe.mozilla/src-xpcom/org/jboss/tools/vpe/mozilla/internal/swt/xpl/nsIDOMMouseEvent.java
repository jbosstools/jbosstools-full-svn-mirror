/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mozilla Communicator client code, released March 31, 1998.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by Netscape are Copyright (C) 1998-1999
 * Netscape Communications Corporation.  All Rights Reserved.
 *
 * Contributor(s):
 *
 * IBM
 * -  Binding to permit interfacing between Mozilla and SWT
 * -  Copyright (C) 2003, 2004 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.jboss.tools.vpe.mozilla.internal.swt.xpl;

import org.eclipse.swt.graphics.Point;
import org.w3c.dom.Node;

public class nsIDOMMouseEvent extends nsIDOMUIEvent {

	static final int LAST_METHOD_ID = nsIDOMUIEvent.LAST_METHOD_ID + 11;

	public static final String NS_IDOMMOUSEEVENT_IID_STRING =
		"a66b7b80-ff46-bd97-0080-5f8ae38add32";

	public static final nsID NS_IDOMMOUSEEVENT_IID =
		new nsID(NS_IDOMMOUSEEVENT_IID_STRING);

	public nsIDOMMouseEvent(int address) {
		super(address);
	}

	public int GetScreenX(int[] aScreenX) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aScreenX);
	}

	public int GetScreenY(int[] aScreenY) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aScreenY);
	}

	public int GetClientX(int[] aClientX) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aClientX);
	}

	public int GetClientY(int[] aClientY) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aClientY);
	}

	public int GetCtrlKey(boolean[] aCtrlKey) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aCtrlKey);
	}

	public int GetShiftKey(boolean[] aShiftKey) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aShiftKey);
	}

	public int GetAltKey(boolean[] aAltKey) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), aAltKey);
	}

	public int GetMetaKey(boolean[] aMetaKey) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), aMetaKey);
	}

	public int GetButton(short[] aButton) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), aButton);
	}

	public int GetRelatedTarget(int[] aRelatedTarget) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), aRelatedTarget);
	}
	
	public int InitMouseEvent(int typeArg, boolean canBubbleArg, boolean cancelableArg, int[] viewArg, int detailArg, int screenXArg, int screenYArg, int clientXArg, int clientYArg, boolean ctrlKeyArg, boolean altKeyArg, boolean shiftKeyArg, boolean metaKeyArg, short buttonArg, int[] relatedTargetArg) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	//=========================================================================

	public int getScreenX() {
		int[] aScreenX = new int[1];
		int rc = GetScreenX(aScreenX);
		if (rc != XPCOM.NS_OK) error(rc);
		return aScreenX[0];
	}

	public int getScreenY() {
		int[] aScreenY = new int[1];
		int rc = GetScreenY(aScreenY);
		if (rc != XPCOM.NS_OK) error(rc);
		return aScreenY[0];
	}

	public int getClientX() {
		int[] aClientX = new int[1];
		int rc = GetClientX(aClientX);
		if (rc != XPCOM.NS_OK) error(rc);
		return aClientX[0];
	}

	public int getClientY() {
		int[] aClientY = new int[1];
		int rc = GetClientY(aClientY);
		if (rc != XPCOM.NS_OK) error(rc);
		return aClientY[0];
	}

	public boolean isAltKey() {
		boolean[] aAltKey = new boolean[] {false};
		int rc = GetAltKey(aAltKey);
		if (rc != XPCOM.NS_OK) error(rc);
		return aAltKey[0];
		
	}

	public nsIDOMEventTarget getRelatedTarget() {
		int[] result = new int[] {0};
		int rc = GetRelatedTarget(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) {
			return null;
		} else {
			return new nsIDOMEventTarget(result[0]);
		}
	}

	public Node getRelatedTargetNode() {
		int[] result = new int[] {0};
		int rc = GetRelatedTarget(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) {
			return null;
		}
		int nodeAddress = nsISupports.queryInterface(result[0], nsIDOMNode.NS_IDOMNODE_IID);
		return nsIDOMNode.getNodeAtAddress(nodeAddress);
	}

	public Point getMousePoint() {
		int aNsuiEvent = queryInterface(getAddress(), nsIDOMNSUIEvent.NS_IDOMNSUIEVENT_IID);
		nsIDOMNSUIEvent nsuiEvent = new nsIDOMNSUIEvent(aNsuiEvent);
		int mouseX = nsuiEvent.getPageX();
		int mouseY = nsuiEvent.getPageY();
		return new Point(mouseX, mouseY);
	}
}