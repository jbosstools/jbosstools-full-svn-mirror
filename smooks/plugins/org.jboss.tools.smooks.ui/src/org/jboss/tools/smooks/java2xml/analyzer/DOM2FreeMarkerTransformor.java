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
package org.jboss.tools.smooks.java2xml.analyzer;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.jboss.tools.smooks.java2xml.utils.XMLConstants;

/**
 * @author Dart Peng
 * @Date : Sep 25, 2008
 */
public class DOM2FreeMarkerTransformor {

	public String transformDOM(Document document) {
		Element element = document.getRootElement();
		StringBuffer buffer = new StringBuffer();
		transform(element, buffer);
		return buffer.toString();
	}

	protected void transform(Element element, StringBuffer buffer) {
		String name = element.getName();
		if (XMLConstants.REPLACE_FREEMARKER_FOR_EXPRESS_ELEMENT_NAME
				.equals(name)) {
			String desc = element
					.attributeValue(XMLConstants.REPLACE_FREEMARKER_FOR_EXPRESS_ELEMENT_ATTRIBUTE);
			name = "#" + desc;
		}
		String attributesString = "";
		if(!isFreeMarkerExpression(name)){
			attributesString = buildAttributeString(element);
		}
		List children = element.elements();
		if (children.isEmpty()) {
			buffer.append("<" + name + " " +attributesString + "/>");
			return;
		} else {
			buffer.append("<" + name + " " +attributesString + "/>");
			for (Iterator iterator = children.iterator(); iterator.hasNext();) {
				Element child = (Element) iterator.next();
				transform(child, buffer);
			}
			if(isFreeMarkerExpression(name)){
				name = name.trim();
				int index = name.indexOf(" ");
				if(index != -1){
					name = name.substring(0,index);
				}
			}
			buffer.append("</" + name + ">");
		}
	}
	
	private String buildAttributeString(Element element) {
		List list = element.attributes();
		StringBuffer buffer = new StringBuffer("");
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Attribute att = (Attribute) iterator.next();
			String name = att.getName();
			String value = att.getValue();
			if(value == null) value = "";
			if(name != null){
				buffer.append(name);
				buffer.append("=");
				buffer.append("\"");
				buffer.append(value);
				buffer.append("\"");
				buffer.append(" ");
			}
		}
		return buffer.toString();
	}

	private boolean isFreeMarkerExpression(String str){
		return str.startsWith("#");
	}

}
