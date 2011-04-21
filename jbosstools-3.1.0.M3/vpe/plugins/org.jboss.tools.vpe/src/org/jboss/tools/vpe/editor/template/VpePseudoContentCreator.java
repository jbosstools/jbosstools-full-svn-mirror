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

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Node;

public abstract class VpePseudoContentCreator {
	private static final String PSEUDO_CONTENT_ATTR = "vpe:pseudo-element"; //$NON-NLS-1$
	
	public abstract void setPseudoContent(VpePageContext pageContext, Node sourceContainer, nsIDOMNode visualContainer, nsIDOMDocument visualDocument) throws VpeExpressionException;
	
	public static void setPseudoAttribute(nsIDOMElement visualPseudoElement) {
		visualPseudoElement.setAttribute(PSEUDO_CONTENT_ATTR, "yes"); //$NON-NLS-1$
		visualPseudoElement.setAttribute("style", "font-style:italic; color:green"); //$NON-NLS-1$ //$NON-NLS-2$
		VpeHtmlTemplate.makeModify(visualPseudoElement, false);
	}
	
	public static boolean isPseudoElement(nsIDOMNode visualNode) {
		return visualNode != null && visualNode.getNodeType() == Node.ELEMENT_NODE && "yes".equalsIgnoreCase(((nsIDOMElement)visualNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID)).getAttribute(PSEUDO_CONTENT_ATTR)); //$NON-NLS-1$
	}
	
	public static nsIDOMNode getContainerForPseudoContent(nsIDOMNode visualNode) {
		if (visualNode == null) return null;
		nsIDOMNode visualElement;
		if (visualNode.getNodeType() == Node.TEXT_NODE) {
			visualElement = visualNode.getParentNode();
		} else {
			visualElement = visualNode;
		}
		if (!isPseudoElement(visualElement)) {
			if (visualElement != visualNode) {
			}
			return null;
		}
		do {
			nsIDOMNode lastNode = visualElement;
			visualElement = visualElement.getParentNode();
			if (lastNode != visualNode) {
			}
		} while (isPseudoElement(visualElement));
		return visualElement;
	}
}
