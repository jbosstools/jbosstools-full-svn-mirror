/*******************************************************************************
* Copyright (c) 2007-2008 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributor:
*     Red Hat, Inc. - initial API and implementation
******************************************************************************/
package org.jboss.tools.vpe.html.template;

import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMText;
import org.w3c.dom.Node;

/**
 * @author mareshkau
 * Template for comment node
 */
public class HtmlCommentTemplate extends VpeAbstractTemplate {
	

	private static final String doubleHyphen = "--"; //$NON-NLS-1$
    private static final String COMMENT_STYLE = "font-style:italic; color:green"; //$NON-NLS-1$
	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#create(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMDocument)
	 */
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {
	    nsIDOMNode visualNode;  
		if (Constants.YES_STRING.equals(VpePreference.SHOW_COMMENTS_VALUE)) {
			visualNode = visualDocument.createElement(HTML.TAG_DIV);
			((nsIDOMElement)visualNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID)).setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE, COMMENT_STYLE);
		    String value = sourceNode.getNodeValue();
		    nsIDOMText text = visualDocument.createTextNode(value);
		    ((nsIDOMElement)visualNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID)).appendChild(text);

	     } else {
	    	 visualNode = visualDocument.createComment(removeDoubleHyphens(sourceNode.getNodeValue()));
	     }
		return new VpeCreationData(visualNode);
	}
	
	//a part of https://jira.jboss.org/jira/browse/JBIDE-5143 fix
	
	private String removeDoubleHyphens(String value){
		StringBuilder stringBuilder = new StringBuilder(""); //$NON-NLS-1$
		while (value.indexOf(doubleHyphen)>-1) {
			stringBuilder.append(value.substring(0, value.indexOf(doubleHyphen)));
			value = value.substring(value.indexOf(doubleHyphen)+doubleHyphen.length());
		}
		stringBuilder.append(value);
		return stringBuilder.toString();
	}

}
