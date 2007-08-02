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

import org.w3c.dom.Node;

import org.jboss.tools.vpe.mozilla.browser.MozillaBrowser;

public class nsIDOMEvent extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 10;

	public static final String NS_IDOMEVENT_IID_STRING =
		"a66b7b80-ff46-bd97-0080-5f8ae38add32";

	public static final nsID IDOMEVENT_IID =
		new nsID(NS_IDOMEVENT_IID_STRING);

	public nsIDOMEvent(int address) {
		super(address);
	}

	public static final int CAPTURING_PHASE = 1;
	
	public static final int AT_TARGET = 2;
	
	public static final int BUBBLING_PHASE = 3;
	
	public int GetType(int aType) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aType);
	}

	public int GetTarget(int[] aTarget) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aTarget);
	}

	public int GetCurrentTarget(int[] aCurrentTarget) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aCurrentTarget);
	}

	public int GetEventPhase(int aEventPhase) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aEventPhase);
	}

	public int GetBubbles(boolean[] aBubbles) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aBubbles);
	}

	public int GetCancelable(boolean[] aCancelable) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aCancelable);
	}

	public int GetTimeStamp(int[] aTimeStamp) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), aTimeStamp);
	}

	public int StopPropagation() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress());
	}

	public int PreventDefault() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress());
	}

	public int InitEvent(int eventTypeArg, boolean canBubbleArg, boolean cancelableArg) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), eventTypeArg, canBubbleArg, cancelableArg);
	}

	//=========================================================================

	public static nsIDOMMouseEvent queryMouseEvent(int address) {
		int aMouseEvent = nsISupports.queryInterface(address, nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
		return new nsIDOMMouseEvent(aMouseEvent);
	}

	public static nsIDOMNSUIEvent queryNSUIEvent(int address) {
		int aNSUIEvent = nsISupports.queryInterface(address, nsIDOMNSUIEvent.NS_IDOMNSUIEVENT_IID);
		return new nsIDOMNSUIEvent(aNSUIEvent);
	}

	public String getType() {
		nsString nsType = new nsString();
		int rc = GetType(nsType.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
		String type = nsType.toString();
		nsType.dispose();
		return type;
	}

	public nsIDOMEventTarget getTarget() {
		int[] result = new int[] {0};
		int rc = GetTarget(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) MozillaBrowser.error(XPCOM.NS_ERROR_NO_INTERFACE);
		return new nsIDOMEventTarget(result[0]);
	}

	public Node getTargetNode() {
		int[] result = new int[] {0};
		int rc = GetTarget(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) return null;
		int nodeAddress = nsISupports.queryInterface(result[0], nsIDOMNode.NS_IDOMNODE_IID);
		return nsIDOMNode.getNodeAtAddress(nodeAddress);
	}

	public Node getCurrentTargetNode() {
		int[] result = new int[] {0};
		int rc = GetCurrentTarget(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) {
			return null;
		}
		int nodeAddress = nsISupports.queryInterface(result[0], nsIDOMNode.NS_IDOMNODE_IID);
		return nsIDOMNode.getNodeAtAddress(nodeAddress);
	}

	public int getEventPhase() {
		int ptrEventPhase = XPCOM.PR_Malloc(2);
		int rc = GetEventPhase(ptrEventPhase);
		if (rc != XPCOM.NS_OK) error(rc);
		int[] aEventPhase = new int[] {0};
		XPCOM.memmove(aEventPhase, ptrEventPhase, 2);
		XPCOM.PR_Free(ptrEventPhase);
		return aEventPhase[0];
	}
	
	public void stopPropagation() {
		int rc = StopPropagation();
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void preventDefault() {
		int rc = PreventDefault();
		if (rc != XPCOM.NS_OK) error(rc);
	}
}