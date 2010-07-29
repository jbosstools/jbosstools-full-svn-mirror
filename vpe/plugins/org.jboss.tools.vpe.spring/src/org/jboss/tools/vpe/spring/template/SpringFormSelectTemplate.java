/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.spring.template;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.spring.template.util.Spring;
import org.jboss.tools.vpe.spring.template.util.VpeSpringUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * VPE template for Spring form:select tag.
 * 
 * @author Yahor Radtsevich (yradtsevich)
 * @see <a href="http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/spring-form.tld.html#spring-form.tld.select">The select tag</> 
 */
public class SpringFormSelectTemplate extends VpeAbstractTemplate {

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#create(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMDocument)
	 */
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {
		Element sourceElement = (Element) sourceNode;
		nsIDOMElement select = visualDocument.createElement(HTML.TAG_SELECT);
		
		VpeSpringUtil.copyCommonAttributes(sourceElement, select);
		VpeSpringUtil.copyAttribute(sourceElement, HTML.ATTR_SIZE,
				select, HTML.ATTR_SIZE);
		VpeSpringUtil.copyAttribute(sourceElement, HTML.ATTR_MULTIPLE,
				select, HTML.ATTR_MULTIPLE);
		
		if (sourceElement.getAttribute(Spring.ATTR_DISABLED).equals(Spring.VALUE_TRUE)) {
			select.setAttribute(HTML.ATTR_DISABLED, HTML.ATTR_DISABLED);
		}
		
		if (sourceElement.hasAttribute("items")) {
			// an inner 'option' tag has to be generated
			String optionBody = sourceElement.getAttribute("items");
			if (sourceElement.hasAttribute("itemLabel")) {
				optionBody += '.' + sourceElement.getAttribute("itemLabel");
			}
			
			nsIDOMElement option = visualDocument.createElement(HTML.TAG_OPTION);
			select.appendChild(option);
		}
		
		return new VpeCreationData(select);
	}
}
