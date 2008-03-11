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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;

public class VpeTaglibCreator extends VpeAbstractCreator {
	private static final String ATTR_URI = "uri";
	private static final String ATTR_PREFIX = "prefix";

	VpeTaglibCreator(Element taglibElement, VpeDependencyMap dependencyMap) {
		build(taglibElement, dependencyMap);
	}

	private void build(Element taglibElement, VpeDependencyMap dependencyMap) {
		dependencyMap.setCreator(this, VpeExpressionBuilder.attrSignature(ATTR_URI, true));
		dependencyMap.setCreator(this, VpeExpressionBuilder.attrSignature(ATTR_PREFIX, true));
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, nsIDOMElement visualElement, Map visualNodeMap) {
		setTaglib(pageContext, (Element)sourceNode);
		return null;
	}

	public void setAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name, String value) {
		setTaglib(pageContext, sourceElement);
	}

	public void removeAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name) {
		setTaglib(pageContext, sourceElement);
	}

	public void removeElement(VpePageContext pageContext, Element sourceElement, Map visualNodeMap) {
		pageContext.setTaglib(sourceElement.hashCode(), null, null, false);
	}
	
	private void setTaglib(VpePageContext pageContext, Element sourceElement) {
		String uri = sourceElement.getAttribute(ATTR_URI);
		String prefix = sourceElement.getAttribute(ATTR_PREFIX);
		pageContext.setTaglib(sourceElement.hashCode(), uri, prefix, false);
	}
}
