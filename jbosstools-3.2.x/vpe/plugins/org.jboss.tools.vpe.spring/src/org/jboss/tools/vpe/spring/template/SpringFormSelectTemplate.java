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

import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.VisualDomUtil;
import org.jboss.tools.vpe.spring.template.util.Spring;
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
	public static final Map<String, String> COMMON_SPRING_FORM_ATTRIBUTES_MAP
			= new HashMap<String, String>() {{
		put(Spring.ATTR_ID, HTML.ATTR_ID);
		put(Spring.ATTR_CSS_STYLE, HTML.ATTR_STYLE);
		put(Spring.ATTR_CSS_CLASS, HTML.ATTR_CLASS);
		put(HTML.ATTR_SIZE, HTML.ATTR_SIZE);
	}};
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#create(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMDocument)
	 */
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {
		Element sourceElement = (Element) sourceNode;
		nsIDOMElement select = visualDocument.createElement(HTML.TAG_SELECT);
		
		VisualDomUtil.copyAttributes(sourceElement, select, COMMON_SPRING_FORM_ATTRIBUTES_MAP);
		
		
		if (Spring.VALUE_TRUE.equals(sourceElement.getAttribute(Spring.ATTR_DISABLED))) {
			select.setAttribute(HTML.ATTR_DISABLED, HTML.ATTR_DISABLED);
		}
		
		if (!Spring.VALUE_FALSE.equals(sourceElement.getAttribute(Spring.ATTR_MULTIPLE))) {
			select.setAttribute(HTML.ATTR_MULTIPLE, HTML.ATTR_MULTIPLE);
		}
		
		if (sourceElement.hasAttribute(Spring.ATTR_ITEMS)) {
			// an inner 'option' tag has to be generated
			String optionBody = sourceElement.getAttribute(Spring.ATTR_ITEMS);
			if (sourceElement.hasAttribute(Spring.ATTR_ITEM_LABEL)) {
				optionBody += '.' + sourceElement.getAttribute(Spring.ATTR_ITEM_LABEL);
			}
			
			nsIDOMElement option = visualDocument.createElement(HTML.TAG_OPTION);
			option.appendChild(visualDocument.createTextNode(optionBody));
			select.appendChild(option);
		}
		
		return new VpeCreationData(select);
	}
}
