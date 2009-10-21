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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;

/**
 * @author Dart Peng
 * @Date Jul 25, 2008
 */
public class TagObject extends AbstractXMLObject {
	protected List<TagPropertyObject> properties = new ArrayList<TagPropertyObject>();

	public List<TagPropertyObject> getProperties() {
		return properties;
	}

	public void setProperties(List<TagPropertyObject> properties) {
		this.properties = properties;
	}

	public void addProperty(TagPropertyObject pro) {
		this.getProperties().add(pro);
		if (pro != null)
			pro.setParent(this);
		Attribute attribute = pro.getReferenceAttibute();
		Element parentElement = getReferenceElement();
		if (attribute != null && parentElement != null) {
			if (attribute.getParent() == parentElement) {
				return;
			}
			parentElement.add(attribute);
		}
	}

	public void removeProperty(TagPropertyObject pro) {
		this.getProperties().remove(pro);
		if (pro != null)
			pro.setParent(null);

		Attribute attribute = pro.getReferenceAttibute();
		Element parentElement = getReferenceElement();
		if (attribute != null && parentElement != null) {
			parentElement.remove(attribute);
		}
	}

	public void addChildTag(TagObject tag) {
		this.getXMLNodeChildren().add(tag);
		if (tag != null)
			tag.setParent(this);
		Element childElement = tag.getReferenceElement();
		Element parentElement = getReferenceElement();
		if (childElement != null && parentElement != null) {
			if (childElement.getParent() == parentElement) {
				return;
			}
			parentElement.add(childElement);
		}
	}

	public void removeChildTag(TagObject tag) {
		this.getXMLNodeChildren().remove(tag);
		if (tag != null)
			tag.setParent(null);

		Element childElement = tag.getReferenceElement();
		Element parentElement = getReferenceElement();
		if (childElement != null && parentElement != null) {
			parentElement.remove(childElement);
		}

	}

	@Override
	public List<IXMLStructuredObject> getChildren() {
		List all = new ArrayList();
		List tags = this.getXMLNodeChildren();
		List properties = this.getProperties();
		all.addAll(properties);
		all.addAll(tags);
		return all;
	}

	public String toString() {
		String blankString = "";
		int deep = -1;
		AbstractXMLObject parent = this;
		while (parent != null && !(parent instanceof TagList)) {
			deep++;
			parent = parent.getParent();
		}

		for (int i = 0; i < deep; i++) {
			blankString = blankString + "\t";
		}

		StringBuffer propertyesBuffer = new StringBuffer();
		for (Iterator<?> iterator = properties.iterator(); iterator.hasNext();) {
			TagPropertyObject pro = (TagPropertyObject) iterator.next();
			propertyesBuffer.append(" " + pro.getName() + "=\"\"");
		}
		StringBuffer buffer = null;
		if (propertyesBuffer.length() == 0) {
			buffer = new StringBuffer(blankString + "<" + getName() + ">");
		} else {
			buffer = new StringBuffer(blankString + "<" + getName() + propertyesBuffer.toString() + ">");
		}

		List<?> l = getXMLNodeChildren();
		if (!l.isEmpty()) {
			buffer.append("\n");
		}
		for (Iterator<?> iterator = l.iterator(); iterator.hasNext();) {
			TagObject tag = (TagObject) iterator.next();
			buffer.append(tag.toString());
			if (iterator.hasNext())
				buffer.append("\n");
		}

		if (l.isEmpty()) {
			buffer.append("</" + getName() + ">");
		} else {
			buffer.append("\n" + blankString + "</" + getName() + ">");
		}

		return buffer.toString();
	}

}
