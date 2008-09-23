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
package org.jboss.tools.vpe.ui.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.common.model.util.XMLUtil;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class TestDomUtil {

	public static String ID_ATTRIBUTE = "id";

	public static Document getDocument(File file) throws FileNotFoundException {
		// create reader
		FileReader reader = new FileReader(file);

		// return document
		return XMLUtil.getDocument(reader);
	}

	public static Document getDocument(String content)
			throws FileNotFoundException {
		// create reader
		StringReader reader = new StringReader(content);

		// return document
		return XMLUtil.getDocument(reader);
	}

	/**
	 * 
	 * @param document
	 * @param elementId
	 * @return
	 */
	public static Element getElemenById(Document document, String elementId) {

		Element element = document.getDocumentElement();

		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if ((child.getNodeType() == Node.ELEMENT_NODE)
					&& elementId.equals(((Element) child)
							.getAttribute(ID_ATTRIBUTE)))
				return (Element) child;

		}

		return null;

	}

	/**
	 * 
	 * @param element
	 * @return
	 */
	public static Element getFirstChildElement(Element element) {

		if (element != null) {
			NodeList children = element.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);

				if (child.getNodeType() == Node.ELEMENT_NODE)
					return (Element) child;

			}
		}
		return null;

	}

	/**
	 * 
	 * @param vpeNode
	 * @param schemeNode
	 * @return
	 */
	public static boolean compareNodes(nsIDOMNode vpeNode, Node schemeNode) {

		// compare node's features
		if ((schemeNode.getNodeType() != vpeNode.getNodeType())
				|| (!schemeNode.getNodeName().equalsIgnoreCase(
						vpeNode.getNodeName()))
				|| ((schemeNode.getNodeValue() != null) && (!schemeNode
						.getNodeValue().trim().equalsIgnoreCase(
								vpeNode.getNodeValue().trim()))))
			return false;

		// compare node's attributes
		if (schemeNode.getNodeType() == Node.ELEMENT_NODE) {

			NamedNodeMap attributes = schemeNode.getAttributes();
			nsIDOMElement vpeElement = (nsIDOMElement) vpeNode
					.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
			if (attributes != null) {
				for (int i = 0; i < attributes.getLength(); i++) {
					Attr attr = (Attr) attributes.item(i);

					if ((!vpeElement.hasAttribute(attr.getName()))
							|| (!attr.getNodeValue().trim().equals(
									vpeElement.getAttributeNode(attr.getName())
											.getNodeValue().trim())))
						return false;
				}
			}
		}

		// compare children
		nsIDOMNodeList vpeChildren = vpeNode.getChildNodes();
		NodeList schemeChildren = schemeNode.getChildNodes();
		int realCount = 0;
		for (int i = 0; i < schemeChildren.getLength(); i++) {

			Node schemeChild = schemeChildren.item(i);

			// leave out empty text nodes in test dom model
			if ((schemeChild.getNodeType() == Node.TEXT_NODE)
					&& ((schemeChild.getNodeValue() == null) || (schemeChild
							.getNodeValue().trim().length() == 0)))
				continue;

			nsIDOMNode vpeChild = vpeChildren.item(realCount++);

			// leave out empty text nodes in vpe dom model
			while (((vpeChild.getNodeType() == Node.TEXT_NODE) && ((vpeChild
					.getNodeValue() == null) || (vpeChild.getNodeValue().trim()
					.length() == 0)))) {
				vpeChild = vpeChildren.item(realCount++);

			}

			if (!compareNodes(vpeChild, schemeChild))
				return false;

		}

		return true;

	}

	/**
	 * get ids of tests
	 * 
	 * @param testDocument
	 * @return
	 */
	public static List<String> getTestIds(Document testDocument) {
		Element rootElement = testDocument.getDocumentElement();
		List<String> ids = new ArrayList<String>();
		NodeList children = rootElement.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE)
				ids.add(((Element) child).getAttribute(ID_ATTRIBUTE));

		}
		return ids;
	}
}
