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
import java.util.Map;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.mozilla.interfaces.nsIDOMAttr;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VpeHtmlCreator extends VpeAbstractCreator {
	private String name;
	private VpeCreator attrs[];
	private VpeCreator nodes[];
	
	VpeHtmlCreator(Element htmlElement, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		build(htmlElement, dependencyMap, caseSensitive);
	}
		
	private void build(Element htmlElement, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		name = htmlElement.getNodeName();
		NamedNodeMap templAttrs = htmlElement.getAttributes();
		if (templAttrs != null) {
			int len = templAttrs.getLength();
			if (len > 0) {
				List creatorAttrs = new ArrayList(len); 
				for (int i = 0; i < len; i++) {
					Attr templAttr = (Attr)templAttrs.item(i);
					creatorAttrs.add(new VpeAttributeCreator(templAttr.getName(), templAttr.getValue(), dependencyMap, caseSensitive));
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
						String innerName = innerNode.getNodeName();
						if (innerName.startsWith(VpeTemplateManager.VPE_PREFIX)) {
							if (VpeTemplateManager.TAG_VALUE.equals(innerName)) {
								String valueVal = ((Element)innerNode).getAttribute(VpeTemplateManager.ATTR_VALUE_EXPR);
								creatorNodes.add(new VpeValueCreator(valueVal, dependencyMap, caseSensitive));
							}
						} else {
							creatorNodes.add(new VpeHtmlCreator((Element) innerNode, dependencyMap, caseSensitive));
						}
						break;
					case Node.TEXT_NODE:
						if (innerNode.getNodeValue().trim().length() > 0) {
							creatorNodes.add(new VpeTextCreator(innerNode, dependencyMap, caseSensitive));
						}
					}
				}
				nodes = (VpeCreator[]) creatorNodes.toArray(new VpeCreator[creatorNodes.size()]);
			}
		}
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, nsIDOMElement visualElement, Map visualNodeMap) {
		nsIDOMElement visualNewElement = visualDocument.createElement(name);
		if (attrs != null) {
			for (int i = 0; i < attrs.length; i++) {
				VpeCreatorInfo attributeInfo = attrs[i].create(pageContext, (Element) sourceNode, visualDocument, visualNewElement, visualNodeMap);
				if (attributeInfo != null) {
					nsIDOMAttr newVisualAttribute = (nsIDOMAttr)attributeInfo.getVisualNode();
					if (newVisualAttribute != null) {
						visualNewElement.setAttributeNode(newVisualAttribute);
					}
				}
			}
		}
		if (nodes != null) {
			for (int i = 0; i < nodes.length; i++) {
				VpeCreatorInfo nodeInfo = nodes[i].create(pageContext, sourceNode, visualDocument, visualNewElement, visualNodeMap);
				if (nodeInfo != null) {
					nsIDOMNode newVisualNode = nodeInfo.getVisualNode();
					if (newVisualNode != null) {
						visualNewElement.appendChild(newVisualNode);
					}
				}
			}
		}
		return new VpeCreatorInfo(visualNewElement);
	}
}
