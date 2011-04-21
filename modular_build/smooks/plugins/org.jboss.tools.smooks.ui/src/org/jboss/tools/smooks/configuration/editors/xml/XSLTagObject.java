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

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 * @author Dart
 * 
 */
public class XSLTagObject extends TagObject {

	private List<TagObject> relatedIgnoreXSLTagObjects = new ArrayList<TagObject>();

	private String smooksPrix = null;

	/**
	 * @return the smooksPrix
	 */
	public String getSmooksPrix() {
		return smooksPrix;
	}

	/**
	 * @param smooksPrix
	 *            the smooksPrix to set
	 */
	public void setSmooksPrix(String smooksPrix) {
		this.smooksPrix = smooksPrix;
	}

	public void addRelatedIgnoreXSLTagObject(TagObject tag) {
		if (tag == null)
			return;
		relatedIgnoreXSLTagObjects.add(tag);
	}

	public void removeRelatedIgnoreXSLTagObject(TagObject tag) {
		relatedIgnoreXSLTagObjects.remove(tag);
	}

	/**
	 * @return the relatedIgnoreXSLTagObjects
	 */
	public List<TagObject> getRelatedIgnoreXSLTagObjects() {
		return relatedIgnoreXSLTagObjects;
	}

	public String getSelectValue() {
		List<TagPropertyObject> properties = this.getProperties();
		for (Iterator<?> iterator = properties.iterator(); iterator.hasNext();) {
			TagPropertyObject tagPropertyObject = (TagPropertyObject) iterator.next();
			if (tagPropertyObject.getName().equalsIgnoreCase("select")) { //$NON-NLS-1$
				return tagPropertyObject.getValue();
			}
		}
		return null;
	}

	public void removeSelectProperty() {
		TagPropertyObject tagPropertyObject = null;
		List<TagPropertyObject> properties = this.getProperties();
		for (Iterator<?> iterator = properties.iterator(); iterator.hasNext();) {
			TagPropertyObject tagPropertyObject1 = (TagPropertyObject) iterator.next();
			if (tagPropertyObject1.getName().equalsIgnoreCase("select")) { //$NON-NLS-1$
				tagPropertyObject = tagPropertyObject1;
				break;
			}
		}
		if (tagPropertyObject != null) {
			Element parentElement = this.getReferenceElement();
			Attr att = tagPropertyObject.getReferenceAttibute();
			parentElement.removeAttributeNode(att);
			this.removeProperty(tagPropertyObject);
		}
	}

	public void removeMatchProperty() {
		TagPropertyObject tagPropertyObject = null;
		List<TagPropertyObject> properties = this.getProperties();
		for (Iterator<?> iterator = properties.iterator(); iterator.hasNext();) {
			TagPropertyObject tagPropertyObject1 = (TagPropertyObject) iterator.next();
			if (tagPropertyObject1.getName().equalsIgnoreCase("match")) { //$NON-NLS-1$
				tagPropertyObject = tagPropertyObject1;
				break;
			}
		}
		if (tagPropertyObject != null) {
			Element parentElement = this.getReferenceElement();
			Attr att = tagPropertyObject.getReferenceAttibute();
			parentElement.removeAttributeNode(att);
			this.removeProperty(tagPropertyObject);
		}
	}

	public void setSelectValue(String value) {
		if (value == null) {
			removeSelectProperty();
			return;
		}
		TagPropertyObject tagPropertyObject = null;
		List<TagPropertyObject> properties = this.getProperties();
		for (Iterator<?> iterator = properties.iterator(); iterator.hasNext();) {
			TagPropertyObject tagPropertyObject1 = (TagPropertyObject) iterator.next();
			if (tagPropertyObject1.getName().equalsIgnoreCase("select")) { //$NON-NLS-1$
				tagPropertyObject = tagPropertyObject1;
				break;
			}
		}
		if (tagPropertyObject == null) {
			tagPropertyObject = new TagPropertyObject();
			Element parentElement = this.getReferenceElement();
			Attr attribute = parentElement.getOwnerDocument().createAttribute("select");//.createAttribute(parentElement, new QName("select"), value); //$NON-NLS-1$
			attribute.setNodeValue(value);
			tagPropertyObject.setReferenceAttibute(attribute);
			parentElement.appendChild(attribute);
			this.addProperty(tagPropertyObject);

		}
		tagPropertyObject.setValue(value);
	}

	public void setMatchValue(String value) {
		TagPropertyObject tagPropertyObject = null;
		List<TagPropertyObject> properties = this.getProperties();
		for (Iterator<?> iterator = properties.iterator(); iterator.hasNext();) {
			TagPropertyObject tagPropertyObject1 = (TagPropertyObject) iterator.next();
			if (tagPropertyObject1.getName().equalsIgnoreCase("match")) { //$NON-NLS-1$
				tagPropertyObject = tagPropertyObject1;
				break;
			}
		}
		if (tagPropertyObject == null) {
			tagPropertyObject = new TagPropertyObject();
			Element parentElement = this.getReferenceElement();
			Attr attribute = parentElement.getOwnerDocument().createAttribute("match");//.createAttribute(parentElement, new QName("select"), value); //$NON-NLS-1$
			attribute.setNodeValue(value);
			parentElement.appendChild(attribute);
			tagPropertyObject.setReferenceAttibute(attribute);
			this.addProperty(tagPropertyObject);
		}
		tagPropertyObject.setValue(value);
	}

	public boolean isChoiceElement() {
		return "choose".equalsIgnoreCase(this.getName()) && isXSLTag(); //$NON-NLS-1$
	}

	public boolean isIfElement() {
		return "if".equalsIgnoreCase(this.getName()) && isXSLTag(); //$NON-NLS-1$
	}

	public boolean isSortElement() {
		return "sort".equalsIgnoreCase(this.getName()) && isXSLTag(); //$NON-NLS-1$
	}

	public boolean isForeachElement() {
		return "for-each".equalsIgnoreCase(this.getName()) && isXSLTag(); //$NON-NLS-1$
	}

	public boolean isValueOfElement() {
		return "value-of".equalsIgnoreCase(this.getName()) && isXSLTag(); //$NON-NLS-1$
	}

	public boolean isTemplateElement() {
		return "template".equalsIgnoreCase(this.getName()) && isXSLTag(); //$NON-NLS-1$
	}

	public boolean isApplyTemplatesElement() {
		return "apply-templates".equalsIgnoreCase(this.getName()) && isXSLTag(); //$NON-NLS-1$
	}

	public String getMatchValue() {
		List<TagPropertyObject> properties = this.getProperties();
		for (Iterator<?> iterator = properties.iterator(); iterator.hasNext();) {
			TagPropertyObject tagPropertyObject = (TagPropertyObject) iterator.next();
			if (tagPropertyObject.getName().equalsIgnoreCase("match")) { //$NON-NLS-1$
				return tagPropertyObject.getValue();
			}
		}
		return null;
	}

	public boolean isXSLTag() {
		return XSLModelAnalyzer.isXSLTagObject(this);
	}

	public boolean isStyleSheetElement() {
		return "stylesheet".equalsIgnoreCase(this.getName()) && isXSLTag(); //$NON-NLS-1$
	}

}
