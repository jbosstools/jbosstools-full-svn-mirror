/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc. Distributed under license by
 * Red Hat, Inc. All rights reserved. This program is made available
 * under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributor: Red Hat,
 * Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.spring.template;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.util.VisualDomUtil;
import org.jboss.tools.vpe.spring.template.util.Spring;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Template class for spring:url and nested spring:param tags.
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
public class SpringUrlTemplate extends VpeAbstractTemplate {

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#create(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMDocument)
	 */
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {
		Element sourceElement = (Element) sourceNode; 
		nsIDOMElement urlContainer = VisualDomUtil.createBorderlessContainer(visualDocument);
		if (sourceElement.hasAttribute(Spring.ATTR_VALUE) &&
				!sourceElement.hasAttribute(Spring.ATTR_VAR)) {
			String valueAttribute = sourceElement.getAttribute(Spring.ATTR_VALUE);
			StringBuilder url = new StringBuilder(valueAttribute);
			
			boolean hasParams = valueAttribute.contains("?"); //$NON-NLS-1$
			NodeList childNodes = sourceElement.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				String childTemplateName = VpeTemplateManager.getInstance()
						.getTemplateName(pageContext, childNode);
				if (Spring.TAG_SPRING_PARAM.equals(childTemplateName)) {
					url.append(hasParams ? '&' : '?');
					hasParams = true;
					appendParam(url, (Element) childNode);
				}
			}
			urlContainer.appendChild(visualDocument.createTextNode(url.toString()));
		}
		return new VpeCreationData(urlContainer);
	}

	/**
	 * @param paramElement
	 */
	private void appendParam(StringBuilder url, Element paramElement) {
		if (paramElement.hasAttribute(Spring.ATTR_NAME)) {
			url.append(paramElement.getAttribute(Spring.ATTR_NAME));
			if (paramElement.hasAttribute(Spring.ATTR_VALUE)) {
				url.append('=').append(paramElement.getAttribute(Spring.ATTR_VALUE));
			}
		}
	}
}
