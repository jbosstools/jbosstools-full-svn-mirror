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
	public TagList analyze(String xmlFilePath,String[] ignoreNodeNames)
			throws FileNotFoundException, DocumentException {
		FileInputStream stream = new FileInputStream(xmlFilePath);
		return this.analyze(stream,ignoreNodeNames);
	}

	public TagList analyze(InputStream stream , String[] ignoreNodeNames) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(stream);
		Element rootElement = doc.getRootElement();
		TagList dco = new TagList();
		dco.setName("Docuement");
		dco.addRootTag( parseElement(rootElement, null , ignoreNodeNames));
		return dco;
	}
	
	public TagObject analyzeFregment(InputStream stream,String[] ignoreNodeNames) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(stream);
		Element rootElement = doc.getRootElement();
		return parseElement(rootElement, null ,ignoreNodeNames);
	}

	protected TagObject getChildTagByName(String name, TagObject tag , String[] ignoreNodeNames) {
		if(isIgnoreNode(name, ignoreNodeNames)) return null;
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
	
	private boolean isIgnoreNode(Element element , String[] ignoreNodeNames){
		return isIgnoreNode(element.getName(), ignoreNodeNames);
	}
	
//	private boolean isIgnoreNode(TagObject element , String[] ignoreNodeNames){
//		return isIgnoreNode(element.getName(), ignoreNodeNames);
//	}
	
	private boolean isIgnoreNode(String name , String[] ignoreNodeNames){
		if(ignoreNodeNames == null) return false;
		for (int i = 0; i < ignoreNodeNames.length; i++) {
			String ignore = ignoreNodeNames[i];
			if(ignore.trim().equalsIgnoreCase(name)) return true;
		}
		return false;
	}

	protected TagObject parseElement(Element element, TagObject parentTag , String[] ignoreNodeNames) {
		
		if(isIgnoreNode(element, ignoreNodeNames))
			return null;
		boolean canAdd = false;
		TagObject tag = getChildTagByName(element.getName(), parentTag , ignoreNodeNames);
		if (tag == null) {
			tag = new TagObject();
			canAdd = true;
		}
		tag.setReferenceElement(element);
		tag.setName(element.getName());
		fillProperties(element, tag ,ignoreNodeNames);
		tag.setNamespaceURI(element.getNamespaceURI());
		List<?> list = element.elements();
		for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
			Element childElement = (Element) iterator.next();
			TagObject t = parseElement(childElement, tag , ignoreNodeNames);
			if (t != null){
				t.setReferenceElement(childElement);
				tag.addChildTag(t);
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

	protected void fillProperties(Element element, TagObject tag , String[] ignoreNodeNames) {
		Iterator<?> it = element.attributeIterator();
		for (Iterator<?> iterator = it; iterator.hasNext();) {
			Attribute attr = (Attribute) iterator.next();
			String attrName = attr.getName();
			String value = attr.getValue();
			if(isIgnoreNode(attrName, ignoreNodeNames)) continue;
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
