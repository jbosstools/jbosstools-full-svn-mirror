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
package org.jboss.tools.vpe.mozilla.browser.util;

import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMNode;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMNodeList;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsISelection;
import org.w3c.dom.Node;


public class DebugUtil {

	public static void printSelection(nsISelection selection) {
		StringBuffer buffer = new StringBuffer("--- SELECTION");
		if (!selection.isCollapsed()) {
			buffer.append("\r    Anchor: " + selectionPointToString((nsIDOMNode)selection.getAnchorNode(), selection.getAnchorOffset()));
		}
		buffer.append("\r    Focus: " + selectionPointToString((nsIDOMNode)selection.getFocusNode(), selection.getFocusOffset()));
		System.out.println(buffer.toString());
	}

	private static String selectionPointToString(nsIDOMNode parent, int offset) {
		String str;
		if (parent != null) {
			switch (parent.getNodeType()) {
			case Node.TEXT_NODE:
				str = "#text(" + parent.getNodeValue() + ")  offset=" + offset;
				break;
			case Node.ELEMENT_NODE:
			case Node.DOCUMENT_NODE:
				nsIDOMNodeList children = (nsIDOMNodeList)parent.getChildNodes();
				if (children != null) {
					int length = children.getLength();
					if (offset < length) {
						nsIDOMNode child = (nsIDOMNode)children.item(offset);
						str = "before " + nodeToString(child);
						child.Release();
					} else if (length > 0) {
						nsIDOMNode child = (nsIDOMNode)children.item(offset);
						str = "after " + nodeToString(child);
						child.Release();
					} else {
						str = "into " + nodeToString(parent);
					}
					children.Release();
				} else {
					str = "into " + nodeToString(parent);
				}
				break;
			default:
				str = "";
			break;
			}
		} else {
			str = "NONE";
		}
		return str;
	}

	private static String nodeToString(nsIDOMNode node) {
		return node.getNodeName() + "(" + node.getAddress() + ")";
	}
}
