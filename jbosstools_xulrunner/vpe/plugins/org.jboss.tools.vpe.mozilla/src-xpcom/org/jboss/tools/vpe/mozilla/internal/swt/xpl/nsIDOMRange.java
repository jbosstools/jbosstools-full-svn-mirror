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

public class nsIDOMRange extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 24;

	public static final String NS_IDOMRANGE_IID_STRING =
		"a6cf90ce-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMRANGE_IID =
		new nsID(NS_IDOMRANGE_IID_STRING);

	public nsIDOMRange(int address) {
		super(address);
	}

	public int GetStartContainer(int[] aStartContainer) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aStartContainer);
	}

	public int GetStartOffset(int[] aStartOffset) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aStartOffset);
	}

	public int GetEndContainer(int[] aEndContainer) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aEndContainer);
	}

	public int GetEndOffset(int[] aEndOffset) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aEndOffset);
	}

	public int GetCollapsed(boolean[] aCollapsed) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aCollapsed);
	}

	public int GetCommonAncestorContainer(int[] aCommonAncestorContainer) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aCommonAncestorContainer);
	}

	public int SetStart(int refNode, int offset) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), refNode, offset);
	}

	public int SetEnd(int refNode, int offset) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), refNode, offset);
	}

	public int SetStartBefore(int refNode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), refNode);
	}

	public int SetStartAfter(int refNode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), refNode);
	}

	public int SetEndBefore(int refNode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), refNode);
	}

	public int SetEndAfter(int refNode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 12, getAddress(), refNode);
	}

	public int Collapse(boolean toStart) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 13, getAddress(), toStart);
	}

	public int SelectNode(int refNode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 14, getAddress(), refNode);
	}

	public int SelectNodeContents(int refNode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 15, getAddress(), refNode);
	}

	public static final int START_TO_START = 0;
	public static final int START_TO_END = 1;
	public static final int END_TO_END = 2;
	public static final int END_TO_START = 3;


	public int CompareBoundaryPoints(char how, int sourceRange, char[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 16, getAddress(), how, sourceRange, _retval);
	}

	public int DeleteContents() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 17, getAddress());
	}

	public int ExtractContents(int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 18, getAddress(), _retval);
	}

	public int CloneContents(int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 19, getAddress(), _retval);
	}

	public int InsertNode(int newNode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 20, getAddress(), newNode);
	}

	public int SurroundContents(int newNode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 21, getAddress(), newNode);
	}

	public int CloneRange(int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 22, getAddress(), _retval);
	}

	public int ToString(int _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 23, getAddress(), _retval);
	}

	public int Detach() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 24, getAddress());
	}

	//=========================================================================

	public Node getStartContainer() {
		int[] aStartContainer = new int[] {0};
		int rc = GetStartContainer(aStartContainer);
		if (rc != XPCOM.NS_OK) error(rc);
		if (aStartContainer[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return nsIDOMNode.getNodeAtAddress(aStartContainer[0]);
	}

	public int getStartOffset() {
		int[] aStartOffset = new int[] {0};
		int rc = GetStartOffset(aStartOffset);
		if (rc != XPCOM.NS_OK) error(rc);
		return aStartOffset[0];
	}

	public Node getEndContainer() {
		int[] aEndContainer = new int[] {0};
		int rc = GetEndContainer(aEndContainer);
		if (rc != XPCOM.NS_OK) error(rc);
		if (aEndContainer[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return nsIDOMNode.getNodeAtAddress(aEndContainer[0]);
	}

	public int getEndOffset() {
		int[] aEndOffset = new int[] {0};
		int rc = GetEndOffset(aEndOffset);
		if (rc != XPCOM.NS_OK) error(rc);
		return aEndOffset[0];
	}

	public boolean isCollapsed() {
		boolean[] aCollapsed = new boolean[1];
		int rc = GetCollapsed(aCollapsed);
		if (rc != XPCOM.NS_OK) error(rc);
		return aCollapsed[0];
	}

	public Node getCommonAncestorContainer() {
		int[] aCommonAncestorContainer = new int[] {0};
		int rc = GetCommonAncestorContainer(aCommonAncestorContainer);
		if (rc != XPCOM.NS_OK) error(rc);
		if (aCommonAncestorContainer[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return nsIDOMNode.getNodeAtAddress(aCommonAncestorContainer[0]);
	}

	public void setStart(nsIDOMNode refNode, int offset) {
		int rc = SetStart(refNode.getAddress(), offset);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void setEnd(nsIDOMNode refNode, int offset) {
		int rc = SetEnd(refNode.getAddress(), offset);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void setEndAfter(nsIDOMNode refNode) {
		int rc = SetEndAfter(refNode.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
		
	}

	public void collapse(boolean toStart) {
		int rc = Collapse(toStart);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void selectNode(nsIDOMNode refNode) {
		int rc = SelectNode(refNode.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void selectNodeContents(nsIDOMNode refNode) {
		int rc = SelectNodeContents(refNode.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void deleteContents() {
		int rc = DeleteContents();
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public nsIDOMNSRange getDOMNSRange() {
		int aDOMNSRange = queryInterface(this, nsIDOMNSRange.NS_IDOMNSRANGE_IID);
		return new nsIDOMNSRange(aDOMNSRange);
	}

	public boolean contains(nsIDOMNode parent, int offset) {
		nsIDOMNSRange domNSRange = getDOMNSRange();
		boolean inRange = domNSRange.contains(parent, offset);
		domNSRange.Release();
		return inRange;
	}
}
