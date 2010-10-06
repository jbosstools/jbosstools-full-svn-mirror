/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.vpe.docbook.template;

import java.text.MessageFormat;

import org.jboss.tools.vpe.docbook.template.util.Docbook;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for <xref>
 * 
 * @author Denis Vinnichek (dvinnichek)
 */
public class DocbookXrefTemplate extends VpeAbstractTemplate {

	private static final String STR_EMPTY = ""; //$NON-NLS-1$
	private static final String STR_BLANK = " "; //$NON-NLS-1$
	private static final String OPEN_TAG = "<"; //$NON-NLS-1$
	private static final String XREF_WITH_PARAM = OPEN_TAG
			+ "xref linkend=\"{0}\"/>"; //$NON-NLS-1$

	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {

		String linkendElementId = obtainLinkendElementId(sourceNode);
		String elementText = obtainElementText(sourceNode, linkendElementId);
		String elementName = elementText.startsWith(OPEN_TAG) ? HTML.TAG_SPAN
				: HTML.TAG_A;

		final nsIDOMElement newElement = visualDocument
				.createElement(elementName);
		if (HTML.TAG_A.equals(elementName)) {
			newElement.setAttribute(HTML.ATTR_HREF, HTML.VALUE_HREF_ANCHOR
					+ linkendElementId);
		}
		newElement.appendChild(visualDocument.createTextNode(elementText));
		final VpeCreationData creationData = new VpeCreationData(newElement);
		return creationData;
	}

	private String obtainLinkendElementId(Node sourceNode) {

		String linkendElementId = null;
		Element sourceElement = (Element) sourceNode;

		if (sourceElement.hasAttribute(Docbook.ATTR_ENDTERM)) {
			linkendElementId = sourceElement.getAttribute(Docbook.ATTR_ENDTERM);
		} else if (sourceElement.hasAttribute(Docbook.ATTR_LINKEND)) {
			linkendElementId = sourceElement.getAttribute(Docbook.ATTR_LINKEND);
		}

		return linkendElementId;
	}

	private String obtainElementText(Node sourceNode, String linkendElementId) {

		if (linkendElementId == null) {
			return MessageFormat.format(XREF_WITH_PARAM, STR_EMPTY);
		}

		Element linkendElement = sourceNode.getOwnerDocument().getElementById(
				linkendElementId);

		if (linkendElement == null) {
			return MessageFormat.format(XREF_WITH_PARAM, linkendElementId);
		}

		if (linkendElement.hasAttribute(Docbook.ATTR_XREFLABEL)) {
			return linkendElement.getAttribute(Docbook.ATTR_XREFLABEL);
		}

		NodeList titleNodes = linkendElement
				.getElementsByTagName(Docbook.ELEMENT_TITLE);
		if (titleNodes.getLength() > 0) {
			Node titleNode = titleNodes.item(0);
			StringBuilder titleText = new StringBuilder();
			obtainTextFromChildNodes(titleNode, titleText);
			return titleText.toString();
		}

		if (linkendElement.getChildNodes().getLength() == 1
				&& Node.TEXT_NODE == linkendElement.getFirstChild()
						.getNodeType()) {
			return linkendElement.getFirstChild().getNodeValue();
		}

		return MessageFormat.format(XREF_WITH_PARAM, linkendElementId);
	}

	private void obtainTextFromChildNodes(Node parentNode,
			StringBuilder titleText) {

		if (Node.TEXT_NODE == parentNode.getNodeType()) {
			if (titleText.length() > 0) {
				titleText.append(STR_BLANK);
			}
			titleText.append(parentNode.getNodeValue());
			return;
		}

		NodeList childNodes = parentNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);
			obtainTextFromChildNodes(childNode, titleText);
		}
	}
}
