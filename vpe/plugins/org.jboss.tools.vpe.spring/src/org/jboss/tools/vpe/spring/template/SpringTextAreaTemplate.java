/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.spring.template;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.NodeData;
import org.jboss.tools.vpe.editor.mapping.VpeElementData;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.VisualDomUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for <form:textarea> 
 *  
 * @author dmaliarevich
 */
public class SpringTextAreaTemplate extends VpeAbstractTemplate {

	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {
		final nsIDOMElement textarea = visualDocument.createElement(HTML.TAG_TEXTAREA);
		VisualDomUtil.copyAttributes(sourceNode, textarea);
		Element sourceElement = (Element) sourceNode;
		/*
		 * Attributes "class" and "style" should correspond to the
		 * Spring's "cssClass" and "cssStyle"
		 */
		textarea.setAttribute(
				HTML.ATTR_CLASS,
				(sourceElement.hasAttribute(SpringConstant.ATTR_CSS_CLASS) 
						? sourceElement.getAttribute(SpringConstant.ATTR_CSS_CLASS) 
						: Constants.EMPTY));
		textarea.setAttribute(
				HTML.ATTR_STYLE,
				(sourceElement.hasAttribute(SpringConstant.ATTR_CSS_STYLE) 
						? sourceElement.getAttribute(SpringConstant.ATTR_CSS_STYLE) 
						: Constants.EMPTY));
		/*
		 * Add value of the 'path' attribute to the textarea.
		 * No children are allowed for the form:textarea. 
		 */
		if (sourceElement.hasAttribute(SpringConstant.ATTR_PATH)) {
			textarea.appendChild(visualDocument.createTextNode(sourceElement
					.getAttribute(SpringConstant.ATTR_PATH)));
		}
		
		final VpeCreationData creationData = new VpeCreationData(textarea);
		return creationData;
	}

}
