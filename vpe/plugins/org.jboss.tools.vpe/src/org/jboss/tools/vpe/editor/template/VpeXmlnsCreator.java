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

import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;

public class VpeXmlnsCreator extends VpeAbstractCreator {
	private static final String ATTR_XMLNS = "xmlns";
	
	VpeXmlnsCreator(VpeDependencyMap dependencyMap) {
		build(dependencyMap);
	}
		
	private void build(VpeDependencyMap dependencyMap) {
		if (dependencyMap != null) {
			dependencyMap.setCreator(this, VpeExpressionBuilder.SIGNATURE_ANY_ATTR);
		}
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, Document visualDocument, Element visualElement, Map visualNodeMap) {
		NamedNodeMap attrs = ((Element)sourceNode).getAttributes();
		if (attrs != null) {
			for (int i = 0; i < attrs.getLength(); i++) {
				addTaglib(pageContext, (Element)sourceNode, visualNodeMap, attrs.item(i).getNodeName());
			}
		}
		return null;
	}

	public void setAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name, String value) {
		addTaglib(pageContext, sourceElement, visualNodeMap, name);
	}

	public void removeAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name) {
		Object id = visualNodeMap.get(name);
		if (id != null) {
			pageContext.setTaglib(((Integer)id).intValue(), null, null, true);
		}
	}

	private void addTaglib(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String attrName) {
		Attr attr = sourceElement.getAttributeNode(attrName);
		if (ATTR_XMLNS.equals(attr.getPrefix())) {
			visualNodeMap.put(attr.getNodeName(), new Integer(attr.hashCode()));
			pageContext.setTaglib(attr.hashCode(), attr.getNodeValue(), attr.getLocalName(), true);
		}
	}
}
