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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.util.MozillaSupports;

public abstract class VpePseudoContentCreator {
	private static final String PSEUDO_CONTENT_ATTR = "vpe:pseudo-element";
	
	public abstract void setPseudoContent(VpePageContext pageContext, Node sourceContainer, Node visualContainer, Document visualDocument);
	
	public static void setPseudoAttribute(Element visualPseudoElement) {
		visualPseudoElement.setAttribute(PSEUDO_CONTENT_ATTR, "yes");
		visualPseudoElement.setAttribute("style", "font-style:italic; color:green");
		VpeHtmlTemplate.makeModify(visualPseudoElement, false);
	}
	
	public static boolean isPseudoElement(Node visualNode) {
		return visualNode != null && visualNode.getNodeType() == Node.ELEMENT_NODE && "yes".equalsIgnoreCase(((Element)visualNode).getAttribute(PSEUDO_CONTENT_ATTR));
	}
	
	public static Node getContainerForPseudoContent(Node visualNode) {
		if (visualNode == null) return null;
		Node visualElement;
		if (visualNode.getNodeType() == Node.TEXT_NODE) {
			visualElement = visualNode.getParentNode();
		} else {
			visualElement = visualNode;
		}
		if (!isPseudoElement(visualElement)) {
			if (visualElement != visualNode) {
				MozillaSupports.release(visualElement);
			}
			return null;
		}
		do {
			Node lastNode = visualElement;
			visualElement = visualElement.getParentNode();
			if (lastNode != visualNode) {
				MozillaSupports.release(lastNode);
			}
		} while (isPseudoElement(visualElement));
		return visualElement;
	}
}
