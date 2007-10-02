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

public class nsIDOMMutationListener extends nsIDOMEventListener {

	static final int LAST_METHOD_ID = nsIDOMEventListener.LAST_METHOD_ID + 7;

	public static final String NS_IDOMMUTATIONLISTENER_IID_STRING =
		"0666EC94-3C54-4e16-8511-E8CC865F236C";

	public static final nsID NS_IDOMMUTATIONLISTENER_IID =
		new nsID(NS_IDOMMUTATIONLISTENER_IID_STRING);

	public nsIDOMMutationListener(int address) {
		super(address);
	}

	public int SubtreeModified(int aMutationEvent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aMutationEvent);
	}

	public int NodeInserted(int aMutationEvent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aMutationEvent);
	}

	public int NodeRemoved(int aMutationEvent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aMutationEvent);
	}

	public int NodeRemovedFromDocument(int aMutationEvent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aMutationEvent);
	}

	public int NodeInsertedIntoDocument(int aMutationEvent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aMutationEvent);
	}

	public int AttrModified(int aMutationEvent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aMutationEvent);
	}

	public int CharacterDataModified(int aMutationEvent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), aMutationEvent);
	}
}
