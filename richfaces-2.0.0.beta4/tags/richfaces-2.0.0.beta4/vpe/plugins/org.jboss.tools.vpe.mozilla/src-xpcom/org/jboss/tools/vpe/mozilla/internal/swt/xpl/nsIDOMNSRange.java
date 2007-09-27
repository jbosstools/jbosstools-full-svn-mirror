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

public class nsIDOMNSRange extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 6;

	public static final String NS_IDOMNSRANGE_IID_STRING =
		"a6cf90f2-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMNSRANGE_IID =
		new nsID(NS_IDOMNSRANGE_IID_STRING);

	public nsIDOMNSRange(int address) {
		super(address);
	}

	//NS_IMETHOD CreateContextualFragment(const nsAString & fragment, nsIDOMDocumentFragment **_retval);
	public int CreateContextualFragment(int fragment, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), fragment, _retval);
	}

	//NS_IMETHOD IsPointInRange(nsIDOMNode *parent, PRInt32 offset, PRBool *_retval);
	public int IsPointInRange(int parent, int offset, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), parent, offset, _retval);
	}
	  
	//NS_IMETHOD ComparePoint(nsIDOMNode *parent, PRInt32 offset, PRInt16 *_retval);
	public int ComparePoint(int parent, int offset, short[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), parent, offset, _retval);
	}
	  
	//NS_IMETHOD IntersectsNode(nsIDOMNode *n, PRBool *_retval);
	public int IntersectsNode(int n, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), n, _retval);
	}
	  
	//NS_IMETHOD CompareNode(nsIDOMNode *n, PRUint16 *_retval);
	public int CompareNode(int n, short[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), n, _retval);
	}
	  
	//NS_IMETHOD NSDetach(void);
	public int NSDetach() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress());
	}

	//=========================================================================

	public boolean contains(nsIDOMNode parent, int offset) {
		boolean[] aRetVal = new boolean[] {false};
		int rc = IsPointInRange(parent.getAddress(), offset, aRetVal);
		if (rc != XPCOM.NS_OK) error(rc);
		return aRetVal[0];
	}
}
