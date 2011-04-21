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

import org.eclipse.wst.xml.core.internal.document.TextImpl;
import org.w3c.dom.Node;
//TODO Max Areshkau used to iterate over the nodes in navigatio by keys, but if we click left
public class FlatIterator {

	public static Node previous(Node forNode) {
		if (forNode == null || forNode.getNodeType() == Node.DOCUMENT_NODE) return null;
		
		Node parent = forNode;
		while (parent != null) {
			if (parent.getNodeType() == Node.DOCUMENT_NODE)
				return null;
			Node sibling = getPreviousSibling(parent);
			if (sibling != null) {
				// find last child of that sibling
				Node deepestChild = findDeepestLastChild(sibling);
				return (deepestChild != null ? deepestChild : sibling);
			}
		
			parent = forNode.getParentNode();
			if (valid(parent)) 
				break;
		}		
		return parent;
		
	}
	
	public static Node next (Node forNode) {
		if (forNode == null) return null;

		Node child = getFirstChild(forNode);
		if (child != null) return child;

		Node parent = forNode;
		while (parent != null) {
			Node sibling = getNextSibling(parent);
			if (sibling != null) return sibling;
			if (parent.getNodeType() == Node.DOCUMENT_NODE) break;
			
			parent = parent.getParentNode();
		}
		return null;
	}
	
	public static Node findDeepestLastChild(Node node) {
		Node child = null;
		Node deeper = getLastChild(node);
		while (deeper != null) {
			child = deeper;
			deeper = getLastChild(child);
		}
		return child;
	}
	
	
	private static Node getPreviousSibling(Node forNode) {
		if (forNode == null) return null;
		Node sibling = null;
		for (sibling = forNode.getPreviousSibling(); 
				sibling != null && !valid(sibling); 
				sibling = sibling.getPreviousSibling()) 
			; // Pass to previous sibling
		
		return sibling;
	}

	private static Node getNextSibling(Node forNode) {
		if (forNode == null) return null;
		Node sibling = null;
		for (sibling = forNode.getNextSibling(); 
				sibling != null && !valid(sibling); 
				sibling = sibling.getNextSibling()) 
			; // Pass to previous sibling
		
		return sibling;
	}
	
	private static Node getFirstChild(Node forNode) {
		if (forNode == null) return null;
		Node child = null;
		for (child = forNode.getFirstChild(); 
				child != null && !valid(child); 
				child = child.getNextSibling()) 
			; // Pass to next child
		return child;
	}
	
	private static Node getLastChild(Node forNode) {
		if (forNode == null || !forNode.hasChildNodes()) return null;
		Node lastChild = forNode.getLastChild();
		if (!valid(lastChild))
			lastChild = getPreviousSibling(lastChild);
		return lastChild;
	}
	
	private boolean isEmptyElement(Node node) {
		if (!node.hasChildNodes()) return true;
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() == Node.TEXT_NODE && !TextUtil.isWhitespaceText(child.getNodeValue()))
				return false;
			if (child.getNodeType() == Node.ELEMENT_NODE && !isEmptyElement(child)) 
				return false; 
		}
		return true;
	}

	private static boolean valid(Node testNode) {
		if (testNode == null) return false;
		if (testNode.getNodeType() == Node.TEXT_NODE) {
			return !TextUtil.isWhitespaceText(testNode.getNodeValue());
		}
		return (testNode.getNodeType() == Node.ELEMENT_NODE);
	}
}
