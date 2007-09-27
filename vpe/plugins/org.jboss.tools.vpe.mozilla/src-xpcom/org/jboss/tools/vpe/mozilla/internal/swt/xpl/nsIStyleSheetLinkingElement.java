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

public class nsIStyleSheetLinkingElement extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 6;

	public static final String NS_ISTYLESHEETLINKINGELEMENT_IID_STRING =
		"a6cf90e9-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_ISTYLESHEETLINKINGELEMENT_IID =
		new nsID(NS_ISTYLESHEETLINKINGELEMENT_IID_STRING);

	public nsIStyleSheetLinkingElement(int address) {
		super(address);
	}

	public int SetStyleSheet(int aStyleSheet) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aStyleSheet);
	}

	public int GetStyleSheet(int[] aStyleSheet) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aStyleSheet);
	}

	public int InitStyleLinkElement(int aParser, boolean aDontLoadStyle) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aParser, aDontLoadStyle);
	}

	public int UpdateStyleSheet(int aOldDocument, int aObserver) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aOldDocument, aObserver);
	}

	public int SetEnableUpdates(boolean aEnableUpdates) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aEnableUpdates);
	}

	//=========================================================================

	public void setEnableUpdates(boolean enableUpdates) {
		int rc = SetEnableUpdates(enableUpdates);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void removeStyleSheet() {
		nsIDOMNode node = new nsIDOMNode(queryInterface(nsIDOMNode.NS_IDOMNODE_IID));
		nsIDOMDocument domDocument = (nsIDOMDocument)node.getOwnerDocument();
		nsIDocument document = new nsIDocument(domDocument.queryInterface(nsIDocument.NS_IDOCUMENT_IID));
//		setEnableUpdates(true);
//		int rc = UpdateStyleSheet(document.getAddress(), 0);
		int rc = UpdateStyleSheet(0, 0);
		if (rc != XPCOM.NS_OK) error(rc);
	}
}
