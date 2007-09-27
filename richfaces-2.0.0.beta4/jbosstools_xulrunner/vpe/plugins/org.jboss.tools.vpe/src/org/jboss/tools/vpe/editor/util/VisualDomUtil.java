/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.util;

import org.eclipse.swt.graphics.Point;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNSRange;
import org.mozilla.interfaces.nsIDOMNSUIEvent;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.interfaces.nsIDOMRange;
import org.mozilla.interfaces.nsISelection;
import org.w3c.dom.Node;


public class VisualDomUtil {

	static public nsIDOMNode getAncestorNode(nsIDOMNode visualNode, String tagName){
		if (tagName == null) return null;
		nsIDOMNode element = visualNode;
		
		while (true){
			if (tagName.equalsIgnoreCase(element.getNodeName())) {
				return element;
			}
			
			element = element.getParentNode();
			
			if (element == null) {
				break;
			}
		}
		
		return null;
	}

	public static Point getMousePoint(nsIDOMMouseEvent mouseEvent) {
		nsIDOMNSUIEvent uiEvent = (nsIDOMNSUIEvent) mouseEvent.queryInterface(nsIDOMNSUIEvent.NS_IDOMNSUIEVENT_IID);
		return new Point(uiEvent.getPageX(), uiEvent.getPageY());
	}
	
	public static long getChildCount(nsIDOMNode node) {
		long count = 0;
		nsIDOMNodeList children = node.getChildNodes();
		if (children != null) {
			count = children.getLength();
		}
		return count;
	}
	
	public static nsIDOMNode getChildNode(nsIDOMNode node, long index) {
		nsIDOMNode child = null;
		nsIDOMNodeList children = node.getChildNodes();
		if (children != null && index >= 0 && index < children.getLength()) {
			child = children.item(index);
		}
		return child;
	}
	
	public static long getOffset(nsIDOMNode node) {
		long offset = 0;
		nsIDOMNode previousSibling = node;
		while ((previousSibling = previousSibling.getPreviousSibling()) != null) {
			offset++;
		}
		return offset;
	}
	
	public static nsIDOMNode getTargetNode(nsIDOMEvent event) {
		return (nsIDOMNode) event.getTarget().queryInterface(nsIDOMNode.NS_IDOMNODE_IID);
	}
	
	public static boolean isSelectionContains(nsISelection selection, nsIDOMNode parent, int offset) {
		if (selection.getIsCollapsed()) {
			return false;
		}
		nsIDOMRange range = selection.getRangeAt(0);
		boolean inSelection = isRangeContains(range, parent, offset);
		if (inSelection) {
			nsIDOMNode endContainer = (nsIDOMNode)range.getEndContainer();
			if (endContainer.getNodeType() != Node.TEXT_NODE) {
				int endOffset = range.getEndOffset();
				inSelection = !(parent.equals(endContainer) && offset == endOffset);
			}
		}
		return inSelection;
	}

	public static boolean isRangeContains(nsIDOMRange range, nsIDOMNode parent, int offset) {
		nsIDOMNSRange domNSRange = (nsIDOMNSRange) range.queryInterface(nsIDOMNSRange.NS_IDOMNSRANGE_IID);
		boolean inRange = domNSRange.isPointInRange(parent, offset);
		return inRange;
	}
	
}
