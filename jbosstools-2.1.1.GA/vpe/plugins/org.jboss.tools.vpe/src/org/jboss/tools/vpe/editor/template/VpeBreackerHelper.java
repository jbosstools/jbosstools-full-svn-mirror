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
package org.jboss.tools.vpe.editor.template;

import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.selection.VpeSourceSelection;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMNode;

public class VpeBreackerHelper {

	public static boolean selectItem(VpePageContext pageContext, Document sourceDocument,  Node sourceNode, nsIDOMNode visualNode, Object data, long charCode, VpeSourceSelection selection) {
		Attr attr = selection.getFocusAttribute();
		if (attr != null) {
			Point range = selection.getFocusAttributeRange();
			if (range.y > 0) {
				String value = attr.getNodeValue();
				attr.setNodeValue(value.substring(0, range.x) + value.substring(range.x + range.y));
				range.y = 0;
			}
			Node selectItemNode = attr.getOwnerElement();
			if (selectItemNode != null) {
				Node listNode = selectItemNode.getParentNode();
				if (listNode != null) {
					Node newSelectItemNode = selectItemNode.cloneNode(true);
					listNode.insertBefore(newSelectItemNode, selectItemNode);
					attr.setNodeValue(attr.getNodeValue().substring(range.x));
					Node newAttr = newSelectItemNode.getAttributes().getNamedItem(attr.getNodeName());
					if (newAttr != null) {
						newAttr.setNodeValue(newAttr.getNodeValue().substring(0, range.x));
					}
					pageContext.getSourceBuilder().setAttributeSelection((Attr)attr, 0, 0);
					return true;
				}
			}
		}
		return false;
	}

	public static boolean breakInline(VpePageContext pageContext, Document sourceDocument,  Node sourceNode, Node visualNode, Object data, int charCode, VpeSourceSelection selection) {
		Attr attr = selection.getFocusAttribute();
		if (attr != null) {
			Point range = selection.getFocusAttributeRange();
			if (range.y > 0) {
				String value = attr.getNodeValue();
				attr.setNodeValue(value.substring(0, range.x) + value.substring(range.x + range.y));
				range.y = 0;
			}
			Node selectItemNode = attr.getOwnerElement();
			if (selectItemNode != null) {
				Node listNode = selectItemNode.getParentNode();
				Node newSelectItemNode = null;
				if (listNode != null) {
					newSelectItemNode = selectItemNode.cloneNode(true);
					listNode.insertBefore(newSelectItemNode, selectItemNode);
					attr.setNodeValue(attr.getNodeValue().substring(range.x));
					Node newAttr = newSelectItemNode.getAttributes().getNamedItem(attr.getNodeName());
					if (newAttr != null) {
						newAttr.setNodeValue(newAttr.getNodeValue().substring(0, range.x));
					}
					pageContext.getSourceBuilder().setAttributeSelection((Attr)attr, 0, 0);
				}
				Node parent = selectItemNode.getParentNode();
				if (parent != null) {
					Node p1 = parent.getOwnerDocument().createElement(HTML.TAG_P);
					Node p2 = parent.getOwnerDocument().createElement(HTML.TAG_P);
					p1 = parent.insertBefore(p1, selectItemNode);
					selectItemNode = parent.removeChild(selectItemNode);
					p1.appendChild(selectItemNode);
					p2 = parent.insertBefore(p2, newSelectItemNode);
					selectItemNode = parent.removeChild(newSelectItemNode);
					p2.appendChild(newSelectItemNode);

					pageContext.getSourceBuilder().getStructuredTextViewer()
					.setSelectedRange(((IndexedRegion)p1).getStartOffset(), ((IndexedRegion)p2).getEndOffset() - ((IndexedRegion)p1).getStartOffset());

					return true;
				}
			}
		}
		return false;
	}
}
