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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

/**
 * @author Dart
 * 
 */
public class XSLModelAnalyzer {

	public static final String XSL_NAME_SPACE = "http://www.w3.org/1999/XSL/Transform";

	public static boolean isXSLElement(Element element) {
		if (element == null)
			return false;
		String nameSpace = element.getNamespaceURI();
		return isXSLNameSpaceURI(nameSpace);
	}

	public static boolean isXSLTagObject(AbstractXMLObject node) {
		if (node == null)
			return false;
		String nameSpace = node.getNamespaceURI();
		return isXSLNameSpaceURI(nameSpace);
	}

	public static boolean isXSLNameSpaceURI(String uri) {
		if (uri != null) {
			uri = uri.trim();
			if (XSL_NAME_SPACE.equals(uri)) {
				return true;
			}
			if("http://www.w3.org/TR/WD-xsl".equals(uri)){
				return true;
			}
		}
		return false;
	}

	public TagObject parse(IFile file) throws DocumentException, CoreException {
		return parse(file.getContents());
	}

	public TagObject parse(String filePath) throws FileNotFoundException, DocumentException {
		File file = new File(filePath);
		return parse(new FileInputStream(file));
	}

	public TagObject parse(InputStream inputStream, String[] ignoreXSLElementnames) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		Element rootElement = document.getRootElement();
		Namespace namespace = rootElement.getNamespaceForURI("http://milyn.codehaus.org/Smooks");
		TagObject tag = (TagObject) parseElement(rootElement, ignoreXSLElementnames, namespace);
		return tag;
	}

	public TagObject parse(InputStream inputStream) throws DocumentException {
		return parse(inputStream, new String[] { "output", "value-of" });
	}

	private boolean isIgnoreXSLTag(Element element, String[] ignoreXSLElementNames) {
		if (isXSLElement(element) && ignoreXSLElementNames != null) {
			for (int i = 0; i < ignoreXSLElementNames.length; i++) {
				String name = ignoreXSLElementNames[i];
				String en = element.getName();
				if (en != null) {
					en = en.trim();
				}
				if (name.equalsIgnoreCase(en)) {
					return true;
				}
			}
		}
		return false;
	}

	private TagObject createTagObject(Element element, boolean createAttributes, Namespace namespace) {
		XSLTagObject tag = new XSLTagObject();
		tag.setNameSpacePrefix(element.getNamespacePrefix());
		tag.setName(element.getName());
		tag.setNamespaceURI(element.getNamespaceURI());
		tag.setReferenceElement(element);
		if (namespace != null) {
			tag.setSmooksPrix(namespace.getPrefix());
		}
		List<?> attributes = element.attributes();
		if (createAttributes) {
			for (Iterator<?> iterator = attributes.iterator(); iterator.hasNext();) {
				Attribute attribute = (Attribute) iterator.next();
				TagPropertyObject pro = new TagPropertyObject();
				pro.setName(attribute.getName());
				pro.setNamespaceURI(attribute.getNamespaceURI());
				pro.setNameSpacePrefix(attribute.getNamespacePrefix());
				pro.setValue(attribute.getValue());
				pro.setReferenceAttibute(attribute);
				((TagObject) tag).addProperty(pro);
			}
		}

		return tag;
	}

	private AbstractXMLObject parseElement(Element rootElement, String[] ignoreXSLElementNames, Namespace namespace) {
		if (rootElement != null) {
			AbstractXMLObject tag = null;
			if (isIgnoreXSLTag(rootElement, ignoreXSLElementNames)) {
				tag = new TagList();
			} else {
				tag = createTagObject(rootElement, true, namespace);
			}

			List<?> childrenElements = rootElement.elements();
			for (Iterator<?> iterator = childrenElements.iterator(); iterator.hasNext();) {
				Element element = (Element) iterator.next();
				TagObject xt = null;
				if (isIgnoreXSLTag(element, ignoreXSLElementNames)) {
					xt = createTagObject(element, true, namespace);
				}
				AbstractXMLObject t = parseElement(element, ignoreXSLElementNames, namespace);
				if (t != null) {
					if (tag instanceof TagList) {
						if (t instanceof XSLTagObject) {
							((TagList) tag).addRootTag((TagObject) t);
							((XSLTagObject) tag).addRelatedIgnoreXSLTagObject(xt);
						}
						if (t instanceof TagList) {
							List<TagObject> tagObjectList = ((TagList) t).getRootTagList();
							for (Iterator<?> iterator2 = tagObjectList.iterator(); iterator2.hasNext();) {
								TagObject tagObject = (TagObject) iterator2.next();
								((TagList) tag).addRootTag(tagObject);
							}
						}
					}
					if (tag instanceof XSLTagObject) {
						((XSLTagObject) tag).addRelatedIgnoreXSLTagObject(xt);
						if (t instanceof TagObject) {
							((TagObject) tag).addChildTag((TagObject) t);
						}
						if (t instanceof TagList) {
							List<TagObject> tagObjectList = ((TagList) t).getRootTagList();
							for (Iterator<?> iterator2 = tagObjectList.iterator(); iterator2.hasNext();) {
								TagObject tagObject = (TagObject) iterator2.next();
								((TagObject) tag).addChildTag(tagObject);
							}
						}
					}
				}
			}

			return tag;
		}

		return null;
	}
}
