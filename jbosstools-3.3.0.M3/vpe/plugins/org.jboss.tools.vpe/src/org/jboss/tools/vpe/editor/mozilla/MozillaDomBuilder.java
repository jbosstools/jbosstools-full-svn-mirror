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
package org.jboss.tools.vpe.editor.mozilla;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class MozillaDomBuilder {
	private Document targetDocument;
	private Element targetContentArea;

	public MozillaDomBuilder(Document targetDocument, Element targetContentArea) {
		this.targetDocument = targetDocument;
		this.targetContentArea = targetContentArea;
	}
	
	public void build(Document sourceDocument) {
		Element body = getSourceBody(sourceDocument);
		if (body != null) {
			build(body, targetContentArea);
		}
	}
	
	private Element getSourceBody(Document sourceDocument) {
		Element docElement = sourceDocument.getDocumentElement();
		NodeList nodes = docElement.getChildNodes();
		int len = nodes.getLength();
		for (int i = 0; i < len; i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE && "BODY".equalsIgnoreCase(node.getNodeName())) { //$NON-NLS-1$
				return (Element)node;
			}
		}
		return null;
	}

	private void build(Element sourseContainer, Element targetContainer) {
		NodeList sourceNodes = sourseContainer.getChildNodes();
		int len = sourceNodes.getLength();

		for (int i = 0; i < len; i++) {
			Node sourceNode = sourceNodes.item(i);
			int type = sourceNode.getNodeType();

			switch (type) {
			case Node.ELEMENT_NODE:
				Element newTargetElement = targetDocument.createElement(sourceNode.getNodeName());
				buildAttributes((Element)sourceNode, newTargetElement);
				build((Element)sourceNode, newTargetElement);
				Node appendedTargetNode = targetContainer.appendChild(newTargetElement);
				break;

			case Node.TEXT_NODE:
				Text newTargetText = targetDocument.createTextNode(sourceNode.getNodeValue());
				Node appendedTargetText = targetContainer.appendChild(newTargetText);
				break;
			}
		}
	}

	private void buildAttributes(Element sourseElement, Element targetElement) {
		NamedNodeMap sourceAttributes = sourseElement.getAttributes();
		if (sourceAttributes == null) {
			return;
		}
		int len = sourceAttributes.getLength();
		for (int i = 0; i < len; i++) {
		    Attr sourceAttribute = (Attr)sourceAttributes.item(i);
			targetElement.setAttribute(sourceAttribute.getName(), sourceAttribute.getValue());
			
		}
	}
}
