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
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.util.MozillaSupports;

public class VpeCopyCreator extends VpeAbstractCreator {
	private boolean caseSensitive;
	private HashSet attrSet;
	private VpeCreator attrs[];
	
	VpeCopyCreator(Element copyElement, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		build(copyElement, dependencyMap);
	}

	private void build(Element copyElement, VpeDependencyMap dependencyMap) {
		dependencyMap.setCreator(this, VpeExpressionBuilder.SIGNATURE_ANY_ATTR);
		Attr attrsAttr = copyElement.getAttributeNode(VpeTemplateManager.ATTR_COPY_ATTRS);
		if (attrsAttr != null) {
			attrSet = new HashSet();
			String attrsValue = attrsAttr.getValue();
			String[] attrsArr = attrsValue.split(",");
			for (int i = 0; i < attrsArr.length; i++) { 
				String attr = attrsArr[i].trim();
				if (attr.length() > 0) {
					attrSet.add(caseSensitive ? attr : attr.toLowerCase());
				}
			}
		}
		NodeList copyChildren = copyElement.getChildNodes();
		if (copyChildren != null) {
			int len = copyChildren.getLength();
			if (len > 0) {
				List creatorAttrs = new ArrayList(len); 
				for (int i = 0; i < len; i++) {
					Node innerNode = copyChildren.item(i);
					if (innerNode.getNodeType() == Node.ELEMENT_NODE &&
							VpeTemplateManager.TAG_ATTRIBUTE.equals(innerNode.getNodeName())) {
						String attrName = ((Element)innerNode).getAttribute(VpeTemplateManager.ATTR_ATTRIBUTE_NAME).trim();
						if (attrName.length() > 0) {
							String attrValue = ((Element)innerNode).getAttribute(VpeTemplateManager.ATTR_ATTRIBUTE_VALUE).trim();
							creatorAttrs.add(new VpeAttributeCreator(attrName, attrValue, dependencyMap, caseSensitive));
						}
					}
				}
				if (creatorAttrs.size() > 0) {
					attrs = (VpeCreator[]) creatorAttrs.toArray(new VpeCreator[creatorAttrs.size()]);
				}
			}
		}
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, Document visualDocument, Element visualElement, Map visualNodeMap) {
		Element visualNewElement = visualDocument.createElement(sourceNode.getNodeName());
		visualNodeMap.put(this, visualNewElement);
		addAttributes((Element)sourceNode, visualNewElement);
		if (attrs != null) {
			for (int i = 0; i < attrs.length; i++) {
				VpeCreatorInfo attributeInfo = attrs[i].create(pageContext, (Element) sourceNode, visualDocument, visualNewElement, visualNodeMap);
				if (attributeInfo != null) {
					Attr newVisualAttribute = (Attr)attributeInfo.getVisualNode();
					if (newVisualAttribute != null) {
						visualNewElement.setAttributeNode(newVisualAttribute);
						MozillaSupports.release(newVisualAttribute);
					}
				}
			}
		}
		return new VpeCreatorInfo(visualNewElement);
	}
	
	public void setAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name, String value) {
		if (isAttribute(name)) {
			Element visualElement = (Element) visualNodeMap.get(this);
			visualElement.setAttribute(name, value);
		}
	}

	public void removeAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name) {
		if (isAttribute(name)) {
			Element visualElement = (Element) visualNodeMap.get(this);
			visualElement.removeAttribute(name);
		}
	}

	public void pseudo(VpePageContext pageContext, Node sourceNode, Node visualNode, Map visualNodeMap) {
		visualNodeMap.put(this, visualNode);
	}

	private void addAttributes(Element sourceElement, Element visualElement) {
		NamedNodeMap sourceAttributes = sourceElement.getAttributes();
		if (sourceAttributes == null) {
			return;
		}
		int len = sourceAttributes.getLength();
		for (int i = 0; i < len; i++) {
			Attr sourceAttr = (Attr) sourceAttributes.item(i);
			String name = sourceAttr.getName();
			if (isAttribute(name)) {
				visualElement.setAttribute(name, sourceAttr.getValue());
			}
		}
	}
	
	private boolean isAttribute(String name) {
		if (attrSet == null) {
			return true;
		}
		return attrSet.contains(caseSensitive ? name : name.toLowerCase());
	}
}
