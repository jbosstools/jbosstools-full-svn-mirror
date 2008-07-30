/******************************************************************************* 
* Copyright (c) 2007 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*     Red Hat, Inc. - initial API and implementation
******************************************************************************/

package org.jboss.tools.vpe.html.template;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.TextUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Node;

/**
 * @author mareshkau
 * Template for text nodes
 */
public class HtmlTextTemplate extends VpeAbstractTemplate {
	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#create(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMDocument)
	 */
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {
		String sourceText = sourceNode.getNodeValue();
	    
		if (sourceText.trim().length() <= 0) {
	    	return new VpeCreationData(visualDocument.createTextNode(sourceText));
	    }
	    String visualText = TextUtil.visualText(sourceText);

	    nsIDOMNode visualNewTextNode = visualDocument
	        .createTextNode(visualText);
	    //TODO Max Areshkau think may be we shouldn't use span
	    nsIDOMElement element = visualDocument.createElement(HTML.TAG_SPAN);
	    element.appendChild(visualNewTextNode);
		
	    return new VpeCreationData(element);
	}

}
