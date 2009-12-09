/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors.xml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Dart Peng
 * @Date Jul 25, 2008
 */
public class XMLObjectAnalyzer {
	public TagList analyze(String xmlFilePath, String[] ignoreNodeNames) throws ParserConfigurationException,
			SAXException, IOException {
		FileInputStream stream = new FileInputStream(xmlFilePath);
		TagList list = this.analyze(stream, ignoreNodeNames);
		try {
			stream.close();
		} catch (IOException e) {
		}
		return list;
	}

	public TagList analyze(Document doc, String[] ignoreNodeNames) {
		if (doc == null)
			return null;
		Element rootElement = doc.getDocumentElement();
		TagList dco = new TagList();
		dco.setName("Docuement"); //$NON-NLS-1$
		dco.addRootTag(parseElement(rootElement, null, ignoreNodeNames));
		return dco;
	}

	public TagList analyze(InputStream stream, String[] ignoreNodeNames) throws ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilder builder = createDocumentBuildingFactory();
		Document doc = builder.parse(stream);
		return analyze(doc, ignoreNodeNames);
	}

	public DocumentBuilder createDocumentBuildingFactory() throws ParserConfigurationException {
		return XMLUtils.getDOMBuilder();
	}

	public TagObject analyzeFregment(InputStream stream, String[] ignoreNodeNames) throws ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilder builder = createDocumentBuildingFactory();
		org.w3c.dom.Document doc = builder.parse(stream);
		Element rootElement = doc.getDocumentElement();
		return parseElement(rootElement, null, ignoreNodeNames);
	}

	protected TagObject getChildTagByName(String name, TagObject tag, String[] ignoreNodeNames) {
		if (isIgnoreNode(name, ignoreNodeNames))
			return null;
		if (tag == null)
			return null;
		List<?> list = tag.getXMLNodeChildren();
		for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
			TagObject tagc = (TagObject) iterator.next();
			if (tagc.getName().equals(name))
				return tagc;
		}
		return null;
	}

	private boolean isIgnoreNode(Element element, String[] ignoreNodeNames) {
		return isIgnoreNode(element.getNodeName(), ignoreNodeNames);
	}

	// private boolean isIgnoreNode(TagObject element , String[]
	// ignoreNodeNames){
	// return isIgnoreNode(element.getName(), ignoreNodeNames);
	// }

	private boolean isIgnoreNode(String name, String[] ignoreNodeNames) {
		if (ignoreNodeNames == null)
			return false;
		for (int i = 0; i < ignoreNodeNames.length; i++) {
			String ignore = ignoreNodeNames[i];
			if (ignore.trim().equalsIgnoreCase(name))
				return true;
		}
		return false;
	}

	protected TagObject parseElement(Element element, TagObject parentTag, String[] ignoreNodeNames) {

		if (isIgnoreNode(element, ignoreNodeNames))
			return null;
		boolean canAdd = false;
		TagObject tag = getChildTagByName(element.getNodeName(), parentTag, ignoreNodeNames);
		if (tag == null) {
			tag = new TagObject();
			canAdd = true;
		}
		tag.setReferenceElement(element);
		tag.setName(element.getNodeName());
		fillProperties(element, tag, ignoreNodeNames);
		tag.setNamespaceURI(element.getNamespaceURI());
		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				Element childElement = (Element) node;
				TagObject t = parseElement(childElement, tag, ignoreNodeNames);
				if (t != null) {
					t.setReferenceElement(childElement);
					tag.addChildTag(t);
				}
			}
		}
		if (canAdd)
			return tag;
		else
			return null;
	}

	protected boolean hasSameNameProperty(String proName, TagObject tag) {
		List<TagPropertyObject> pros = tag.getProperties();
		for (Iterator<TagPropertyObject> iterator = pros.iterator(); iterator.hasNext();) {
			TagPropertyObject tp = (TagPropertyObject) iterator.next();
			if (tp.getName().equals(proName))
				return true;
		}
		return false;
	}

	protected void fillProperties(Element element, TagObject tag, String[] ignoreNodeNames) {
		NamedNodeMap attrMap = element.getAttributes();
		for (int i = 0; i < attrMap.getLength(); i++) {
			Node node = attrMap.item(i);
			if (node instanceof Attr) {
				Attr attr = (Attr) node;
				String attrName = attr.getName();
				String value = attr.getValue();
				if (isIgnoreNode(attrName, ignoreNodeNames))
					continue;
				if (hasSameNameProperty(attrName, tag)) {
					continue;
				}
				TagPropertyObject pro = new TagPropertyObject();
				pro.setName(attr.getName());
				pro.setValue(value);
				pro.setNamespaceURI(attr.getNamespaceURI());
				tag.addProperty(pro);
			}
		}
	}
}
