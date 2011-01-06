/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.deltacloud.core.client.unmarshal;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author André Dietisheim
 *
 * @param <DELTACLOUDOBJECT>
 */
public abstract class AbstractDOMUnmarshaller<DELTACLOUDOBJECT> {

	private Class<DELTACLOUDOBJECT> type;
	private String tagName;

	public AbstractDOMUnmarshaller(String tagName, Class<DELTACLOUDOBJECT> type) {
		this.type = type;
		this.tagName = tagName;
	}

	public DELTACLOUDOBJECT unmarshall(InputStream inputStream, DELTACLOUDOBJECT resource) throws DeltaCloudClientException {
		try {
			Element element = getFirstElement(tagName, getDocument(inputStream));
			return unmarshall(element, resource);
		} catch (Exception e) {
			// TODO: internationalize strings
			throw new DeltaCloudClientException(
					MessageFormat.format("Could not unmarshall type \"{0}\"", type), e);
		}

	}

	protected Document getDocument(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		return documentBuilder.parse(inputStream);
	}

	public DELTACLOUDOBJECT unmarshall(Element element, DELTACLOUDOBJECT resource) throws DeltaCloudClientException {
		try {
			return doUnmarshall(element, resource);
		} catch (Exception e) {
			// TODO: internationalize strings
			throw new DeltaCloudClientException(
					MessageFormat.format("Could not unmarshall type \"{0}\"", type), e);
		}
	}

	protected abstract DELTACLOUDOBJECT doUnmarshall(Element element, DELTACLOUDOBJECT resource) throws Exception;

	protected String getFirstElementAttributeText(String elementName, String attributeId, Element element) {
		Element firstElement = getFirstElement(elementName, element);
		if (firstElement == null) {
			return null;
		}
		return firstElement.getAttribute(attributeId);
	}

	protected String getFirstElementText(String elementName, Element element) {
		Element firstElement = getFirstElement(elementName, element);
		if (firstElement == null) {
			return null;
		}
		return firstElement.getTextContent();
	}

	protected Element getFirstElement(String elementName, Element element) {
		NodeList elements = element.getElementsByTagName(elementName);
		if (elements != null
				&& elements.getLength() > 0) {
			return (Element) elements.item(0);
		}
		return null;
	}

	protected Element getFirstElement(String elementName, Document document) {
		NodeList elements = document.getElementsByTagName(elementName);
		if (elements != null
				&& elements.getLength() > 0) {
			return (Element) elements.item(0);
		}
		return null;
	}

	protected String getAttributeText(String attributeName, Element element) {
		Node attribute = element.getAttributeNode(attributeName);
		if (attribute != null) {
			return attribute.getTextContent();
		}
		return null;
	}

}
