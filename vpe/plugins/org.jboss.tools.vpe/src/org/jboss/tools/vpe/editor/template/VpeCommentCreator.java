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

import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.interfaces.nsIDOMText;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VpeCommentCreator extends VpeAbstractCreator implements VpeOutputComment {
	public static final String SIGNATURE_VPE_COMMENT = ":vpe:comment"; //$NON-NLS-1$
	private static final String COMMENT_STYLE = "font-style:italic; color:green"; //$NON-NLS-1$
	private static final String COMMENT_PREFIX = ""; //$NON-NLS-1$
	private static final String COMMENT_SUFFIX = ""; //$NON-NLS-1$

	VpeCommentCreator(Element element, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		dependencyMap.setCreator(this, SIGNATURE_VPE_COMMENT);
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, nsIDOMElement visualElement, Map visualNodeMap) {
		if(!"yes".equals(VpePreference.SHOW_COMMENTS.getValue())) { //$NON-NLS-1$
			return null;
		}
		nsIDOMElement div = visualDocument.createElement(HTML.TAG_DIV);
		div.setAttribute("style", COMMENT_STYLE); //$NON-NLS-1$
		String value = COMMENT_PREFIX + sourceNode.getNodeValue() + COMMENT_SUFFIX;
		nsIDOMText text = visualDocument.createTextNode(value);
		div.appendChild(text);
		visualNodeMap.put(this, div);
		return new VpeCreatorInfo(div);
	}

	public void setOutputCommentValue(VpePageContext pageContext, Comment sourceComment, Map visualNodeMap) {
		String commentValue = ""; //$NON-NLS-1$
		nsIDOMElement div = (nsIDOMElement)visualNodeMap.get(this);
		if (div != null) {
			nsIDOMNodeList children = div.getChildNodes();
			if (children != null) {
				long len = children.getLength();
				for (long i = 0; i < len; i++) {
					nsIDOMNode text = children.item(i);
					if (text.getNodeType() == nsIDOMNode.TEXT_NODE) {
						String value = text.getNodeValue();
						if (value.length() > 0) {
							commentValue = value;
							break;
						}
					}
				}
			}
		}
		sourceComment.setNodeValue(commentValue);
	}
}