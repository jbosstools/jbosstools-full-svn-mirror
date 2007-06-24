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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.mozilla.browser.util.PRUnicharUtil;

public class nsISelection extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 19;

	public static final String NS_ISELECTION_IID_STRING =
		"b2c7ed59-8634-4352-9e37-5484c8b6e4e1";

	public static final nsID NS_ISELECTION_IID =
		new nsID(NS_ISELECTION_IID_STRING);

	public nsISelection(int address) {
		super(address);
	}

	public int GetAnchorNode(int[] aAnchorNode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aAnchorNode);
	}

	public int GetAnchorOffset(int[] aAnchorOffset) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aAnchorOffset);
	}

	public int GetFocusNode(int[] aFocusNode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aFocusNode);
	}

	public int GetFocusOffset(int[] aFocusOffset) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aFocusOffset);
	}

	public int GetIsCollapsed(boolean[] aIsCollapsed) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aIsCollapsed);
	}

	public int GetRangeCount(int[] aRangeCount) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aRangeCount);
	}

	public int GetRangeAt(int index, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), index, _retval);
	}

	public int Collapse(int parentNode, int offset) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), parentNode, offset);
	}

	public int Extend(int parentNode, int offset) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), parentNode, offset);
	}

	public int CollapseToStart() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress());
	}

	public int CollapseToEnd() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress());
	}

	public int ContainsNode(int node, boolean entirelyContained, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 12, getAddress(), node, entirelyContained, _retval);
	}

	public int SelectAllChildren(int parentNode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 13, getAddress(), parentNode);
	}

	public int AddRange(int range) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 14, getAddress(), range);
	}

	public int RemoveRange(int range) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 15, getAddress(), range);
	}

	public int RemoveAllRanges() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 16, getAddress());
	}

	public int DeleteFromDocument() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 17, getAddress());
	}

	public int SelectionLanguageChange(boolean langRTL) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 18, getAddress(), langRTL);
	}

	public int ToString(int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 19, getAddress(), _retval);
	}
	
	//=========================================================================

	public Node getAnchorNode() {
		int[] result = new int[] {0};
		int rc = GetAnchorNode(result);
		if (rc != XPCOM.NS_OK) error(rc);
		return nsIDOMNode.getNodeAtAddress(result[0]);
	}

	public int getAnchorOffset() {
		int[] aFocusOffset = new int[] {0};
		int rc = GetAnchorOffset(aFocusOffset);
		if (rc != XPCOM.NS_OK) error(rc);
		return aFocusOffset[0];
	}

	public Node getFocusNode() {
		int[] result = new int[] {0};
		int rc = GetFocusNode(result);
		if (rc != XPCOM.NS_OK) error(rc);
		return nsIDOMNode.getNodeAtAddress(result[0]);
	}

	public int getFocusOffset() {
		int[] aFocusOffset = new int[] {0};
		int rc = GetFocusOffset(aFocusOffset);
		if (rc != XPCOM.NS_OK) error(rc);
		return aFocusOffset[0];
	}

	public boolean isCollapsed() {
		boolean[] aIsCollapsed = new boolean[] {false};
		int rc = GetIsCollapsed(aIsCollapsed);
		if (rc != XPCOM.NS_OK) error(rc);
		return aIsCollapsed[0];
	}

	public int getRangeCount() {
		int[] aRangeCount = new int[] {0};
		int rc = GetRangeCount(aRangeCount);
		if (rc != XPCOM.NS_OK) error(rc);
		return aRangeCount[0];
	}

	public nsIDOMRange getRangeAt(int index) {
		int[] result = new int[] {0};
		int rc = GetRangeAt(index, result);
		if (rc != XPCOM.NS_OK || result[0] == 0) {
			return null;
		} else {
			return new nsIDOMRange(result[0]);
		}
	}

	public void collapse(nsIDOMNode parentNode, int offset) {
		int rc = Collapse(parentNode.getAddress(), offset);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void collapse(nsIDOMNode parentNode) {
		collapse(parentNode, 0);
	}

	public void extend(nsIDOMNode parentNode, int offset) {
		int rc = Extend(parentNode.getAddress(), offset);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void selectAllChildren(nsIDOMNode parentNode) {
		int rc = SelectAllChildren(parentNode.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
	}
	
	public void removeAllRanges() {
		int rc = RemoveAllRanges();
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public boolean containsNode(nsIDOMNode node, boolean entirelyContained) {
		boolean[] result = new boolean[] {false};
		int rc = ContainsNode(node.getAddress(), entirelyContained, result);
		if (rc != XPCOM.NS_OK) error(rc);
		return result[0];
	}

	public void addRange(nsIDOMRange range) {
		int rc = AddRange(range.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public Element getSelectedElement() {
		nsIDOMElement selectedElement = null;
		Node focusNode = getFocusNode();
		Node anchorNode = getAnchorNode();
		if (focusNode != null && focusNode.getNodeType() == Node.ELEMENT_NODE && focusNode.equals(anchorNode)) {
			int focusOffset = getFocusOffset();
			int anchorOffset = getAnchorOffset();
			if (Math.abs(focusOffset - anchorOffset) == 1) {
				nsIDOMNodeList children = (nsIDOMNodeList)focusNode.getChildNodes();
				nsIDOMNode selectedNode = (nsIDOMNode)children.item(Math.min(focusOffset, anchorOffset));
				if (selectedNode.getNodeType() == Node.ELEMENT_NODE) {
					selectedElement = (nsIDOMElement)selectedNode;
				} else {
					selectedNode.Release();
				}
				children.Release();
			}
		}
		if (focusNode != null) {
			((nsIDOMNode)focusNode).Release();
		}
		if (anchorNode != null) {
			((nsIDOMNode)anchorNode).Release();
		}
		return selectedElement;
	}

	public boolean contains(nsIDOMNode parent, int offset) {
		if (isCollapsed()) {
			return false;
		}
		nsIDOMRange range = getRangeAt(0);
		boolean inSelection = range.contains(parent, offset);
		if (inSelection) {
			nsIDOMNode endContainer = (nsIDOMNode)range.getEndContainer();
			if (endContainer.getNodeType() != Node.TEXT_NODE) {
				int endOffset = range.getEndOffset();
				inSelection = !(parent.equals(endContainer) && offset == endOffset);
			}
			endContainer.Release();
		}
		range.Release();
		return inSelection;
	}
	
	public String toString() {
		int[] result = new int[] {0};
		int rc = ToString(result);
		if (rc != XPCOM.NS_OK) error(rc);
		return PRUnicharUtil.toString(result[0]);
	}
}
