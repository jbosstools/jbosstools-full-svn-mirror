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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.util.MozillaSupports;

public class VpeHtmlPseudoContentCreator extends VpePseudoContentCreator {
	private String name;
	private VpeCreator attrs[];
	private VpeCreator nodes[];

	public VpeHtmlPseudoContentCreator(Element htmlElement) {
		name = htmlElement.getNodeName();
		NamedNodeMap templAttrs = htmlElement.getAttributes();
		if (templAttrs != null) {
			int len = templAttrs.getLength();
			if (len > 0) {
				List creatorAttrs = new ArrayList(len); 
				for (int i = 0; i < len; i++) {
					Attr templAttr = (Attr)templAttrs.item(i);
					creatorAttrs.add(new VpeAttributeCreator(templAttr.getName(), templAttr.getValue(), null, false));
				}
				attrs = (VpeCreator[]) creatorAttrs.toArray(new VpeCreator[len]);
			}
		}
		NodeList htmlChildren = htmlElement.getChildNodes();
		if (htmlChildren != null) {
			int len = htmlChildren.getLength();
			if (len > 0) {
				List creatorNodes = new ArrayList(len); 
				for (int i = 0; i < len; i++) {
					Node innerNode = htmlChildren.item(i);
					switch (innerNode.getNodeType()) {
					case Node.ELEMENT_NODE:
						creatorNodes.add(new VpeHtmlPseudoContentCreator((Element) innerNode));
						break;
					case Node.TEXT_NODE:
						if (innerNode.getNodeValue().trim().length() > 0) {
							creatorNodes.add(new VpeTextCreator(innerNode, null, false));
						}
					}
				}
				nodes = (VpeCreator[]) creatorNodes.toArray(new VpeCreator[creatorNodes.size()]);
			}
		}
	}

	public void setPseudoContent(VpePageContext pageContext, Node sourceContainer, Node visualContainer, Document visualDocument) {
		Element visualNewElement = visualDocument.createElement(name);
		setPseudoAttribute(visualNewElement);
		if (attrs != null) {
			for (int i = 0; i < attrs.length; i++) {
				VpeCreatorInfo attributeInfo = attrs[i].create(pageContext, (Element) sourceContainer, visualDocument, visualNewElement, null);
				if (attributeInfo != null) {
					Attr newVisualAttribute = (Attr)attributeInfo.getVisualNode();
					if (newVisualAttribute != null) {
						visualNewElement.setAttributeNode(newVisualAttribute);
						MozillaSupports.release(newVisualAttribute);
					}
				}
			}
		}
		if (nodes != null) {
			for (int i = 0; i < nodes.length; i++) {
				VpeCreatorInfo nodeInfo = nodes[i].create(pageContext, sourceContainer, visualDocument, visualNewElement, null);
				if (nodeInfo != null) {
					Node newVisualNode = nodeInfo.getVisualNode();
					if (newVisualNode != null) {
						visualNewElement.appendChild(newVisualNode);
					}
				}
			}
		}
		visualContainer.appendChild(visualNewElement);
	}
}
