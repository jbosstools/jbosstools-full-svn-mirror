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
package org.jboss.tools.smooks.xml.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author Dart Peng
 * @Date Jul 25, 2008
 */
public class XMLObjectAnalyzer {
	public DocumentObject analyze(String xmlFilePath)
			throws FileNotFoundException, DocumentException {
		FileInputStream stream = new FileInputStream(xmlFilePath);
		return this.analyze(stream);
	}

	public DocumentObject analyze(InputStream stream) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(stream);
		Element rootElement = doc.getRootElement();
		DocumentObject dco = new DocumentObject();
		dco.setName("Docuement");
		dco.setRootTag( parseElement(rootElement, null));
		return dco;
	}

	protected TagObject getChildTagByName(String name, TagObject tag) {
		if (tag == null)
			return null;
		List list = tag.getChildren();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			TagObject tagc = (TagObject) iterator.next();
			if (tagc.getName().equals(name))
				return tagc;
		}
		return null;
	}

	protected TagObject parseElement(Element element, TagObject parentTag) {
		boolean canAdd = false;
		TagObject tag = getChildTagByName(element.getName(), parentTag);
		if (tag == null) {
			tag = new TagObject();
			canAdd = true;
		}
		tag.setName(element.getName());
		fillProperties(element, tag);
		tag.setNamespaceURL(element.getNamespaceURI());
		List list = element.elements();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Element childElement = (Element) iterator.next();
			TagObject t = parseElement(childElement, tag);
			if (t != null)
				tag.addChildTag(t);
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

	protected void fillProperties(Element element, TagObject tag) {
		Iterator it = element.attributeIterator();
		for (Iterator iterator = it; iterator.hasNext();) {
			Attribute attr = (Attribute) iterator.next();
			if (hasSameNameProperty(attr.getName(), tag)) {
				continue;
			}
			TagPropertyObject pro = new TagPropertyObject();
			pro.setName(attr.getName());
			pro.setNamespaceURL(attr.getNamespaceURI());
			tag.addProperty(pro);
		}
	}
}
