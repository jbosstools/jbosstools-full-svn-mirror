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

import java.text.MessageFormat;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMText;

public class VpeTextPseudoContentCreator extends VpePseudoContentCreator {
	private String text;
	private String attrName;

	public VpeTextPseudoContentCreator(String text, String attrName) {
		this.text = text;
		if (attrName != null) {
			attrName = attrName.trim();
			if (attrName.length() > 0) {
				this.attrName = attrName;
			}
		}
	}

	public void setPseudoContent(VpePageContext pageContext, Node sourceContainer, nsIDOMNode visualContainer, nsIDOMDocument visualDocument) {
		nsIDOMElement visualNewElement = visualDocument.createElement(HTML.TAG_SPAN);
		setPseudoAttribute(visualNewElement);
		String text = this.text;
		if (text == null) {
			if (sourceContainer.getNodeType() == Node.ELEMENT_NODE) {
				String name = null;
				if (attrName != null) {
					name = ((Element)sourceContainer).getAttribute(attrName);
					if (name != null) {
						name = name.trim();
					}
				}
				if (name == null || attrName.length() <= 0) {
					name = ((Element)sourceContainer).getNodeName();
				}
				text = MessageFormat.format(VpeUIMessages.VpeTextPseudoContentCreator_InsertContentFor, name);
			} else {
				text = VpeUIMessages.VpeTextPseudoContentCreator_InsertContent;
			}
		}
		nsIDOMText newTextNode = visualDocument.createTextNode(text);
		visualNewElement.appendChild(newTextNode);
		visualContainer.appendChild(visualNewElement);
	}
}
