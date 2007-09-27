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
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.jboss.tools.vpe.editor.context.VpePageContext;

public class VpeCommentCreator extends VpeAbstractCreator implements VpeOutputComment {
	public static final String SIGNATURE_VPE_COMMENT = ":vpe:comment";
	private static final String COMMENT_STYLE = "font-style:italic; color:green";
	private static final String COMMENT_PREFIX = "";
	private static final String COMMENT_SUFFIX = "";

	VpeCommentCreator(Element element, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		dependencyMap.setCreator(this, SIGNATURE_VPE_COMMENT);
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, Document visualDocument, Element visualElement, Map visualNodeMap) {
		if(!"yes".equals(VpePreference.SHOW_COMMENTS.getValue())) {
			return null;
		}
		Element div = visualDocument.createElement("div");
		div.setAttribute("style", COMMENT_STYLE);
		String value = COMMENT_PREFIX + sourceNode.getNodeValue() + COMMENT_SUFFIX;
		Node text = visualDocument.createTextNode(value);
		div.appendChild(text);
//		visualNodeMap.put(this, new VisualElements(div, text));
		visualNodeMap.put(this, div);
		return new VpeCreatorInfo(div);
	}

	public void setOutputCommentValue(VpePageContext pageContext, Comment sourceComment, Map visualNodeMap) {
		String commentValue = "";
		Element div = (Element)visualNodeMap.get(this);
		if (div != null) {
			NodeList children = div.getChildNodes();
			if (children != null) {
				int len = children.getLength();
				for (int i = 0; i < len; i++) {
					Node text = children.item(i);
					if (text.getNodeType() == Node.TEXT_NODE) {
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