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

public class nsIControllers extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 11;

	public static final String NS_ICONTROLLERS_IID_STRING =
		"A5ED3A01-7CC7-11d3-BF87-00105A1B0627";

	public static final nsID NS_ICONTROLLERS_IID =
		new nsID(NS_ICONTROLLERS_IID_STRING);

	public nsIControllers(int address) {
		super(address);
	}

	public int GetCommandDispatcher(int[] aCommandDispatcher) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aCommandDispatcher);
	}

	public int SetCommandDispatcher(int aCommandDispatcher) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aCommandDispatcher);
	}

	public int GetControllerForCommand(byte[] command, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), command, _retval);
	}

	public int InsertControllerAt(int index, int controller) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), index, controller);
	}

	public int RemoveControllerAt(int index, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), index, _retval);
	}

	public int GetControllerAt(int index, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), index, _retval);
	}

	public int AppendController(int controller) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), controller);
	}

	public int RemoveController(int controller) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), controller);
	}

	public int GetControllerId(int controller, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), controller, _retval);
	}

	public int GetControllerById(int controllerID, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), controllerID, _retval);
	}

	public int GetControllerCount(int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), _retval);
	}

	//=========================================================================

	public nsIController removeControllerAt(int index) {
		int[] aController = new int[] {0};
		int rc = RemoveControllerAt(index, aController);
		if (rc != XPCOM.NS_OK) error(rc);
		if (aController[0] != 0) {
			return new nsIController(aController[0]);
		} else {
			return null;
		}
	}

	public int getControllerCount() {
		int[] aControllerCount = new int[] {0};
		int rc = GetControllerCount(aControllerCount);
		if (rc != XPCOM.NS_OK) error(rc);
		return aControllerCount[0];
	}
}