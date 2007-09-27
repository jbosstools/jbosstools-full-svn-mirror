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

import org.w3c.dom.Node;

public class nsIDOMMutationEvent extends nsIDOMEvent {

	static final int LAST_METHOD_ID = nsIDOMEvent.LAST_METHOD_ID + 6;

	public static final String NS_IDOMMUTATIONEVENT_IID_STRING =
		"8e440d86-886a-4e76-9e59-c13b939c9a4b";

	public static final nsID NS_IDOMMUTATIONEVENT_IID =
		new nsID(NS_IDOMMUTATIONEVENT_IID_STRING);

	public nsIDOMMutationEvent(int address) {
		super(address);
	}

	public static final int MODIFICATION = 1;
	
	public static final int ADDITION = 2;
	
	public static final int REMOVAL = 3;

	public int GetRelatedNode(int[] aRelatedNode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aRelatedNode);
	}

	public int GetPrevValue(int aPrevValue) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aPrevValue);
	}

	public int GetNewValue(int aNewValue) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aNewValue);
	}

	public int GetAttrName(int aAttrName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aAttrName);
	}

	public int GetAttrChange(short[] aAttrChange) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aAttrChange);
	}

	public int InitMutationEvent(int typeArg, boolean canBubbleArg, boolean cancelableArg, int relatedNodeArg, int prevValueArg, int newValueArg, int attrNameArg, short attrChangeArg) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), typeArg, canBubbleArg, cancelableArg, relatedNodeArg, prevValueArg, newValueArg, attrNameArg, attrChangeArg);
	}

	//=========================================================================

	public Node getRelatedNode() {
		int[] result = new int[] {0};
		int rc = GetRelatedNode(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) {
			return null;
		} else {
			return nsIDOMNode.getNodeAtAddress(result[0]);
		}
	}
	
	public String getPrevValue() {
		nsString nsPrevValue = new nsString();
		int rc = GetPrevValue(nsPrevValue.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
		String prevValue = nsPrevValue.toString();
		nsPrevValue.dispose();
		return prevValue;
	}
	
	public String getNewValue() {
		nsString nsNewValue = new nsString();
		int rc = GetNewValue(nsNewValue.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
		String newValue = nsNewValue.toString();
		nsNewValue.dispose();
		return newValue;
	}
}
