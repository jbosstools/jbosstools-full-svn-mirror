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

import java.util.Collections;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.dom.DOMDocumentFactory;

/**
 * @author Dart Peng
 * @Date Jul 25, 2008
 */
public class TagPropertyObject extends AbstractXMLObject {

	protected String type = "string"; //$NON-NLS-1$

	protected String value = null;

	protected Attribute referenceAttibute;

	public String getValue() {
		Attribute attribute = getReferenceAttibute();
		if (attribute != null) {
			return attribute.getValue();
		}
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		Attribute attribute = getReferenceAttibute();
		if (attribute != null) {
			attribute.setValue(value);
		}
	}

	/**
	 * @return the referenceAttibute
	 */
	public Attribute getReferenceAttibute() {
		return referenceAttibute;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.editors.xml.AbstractXMLObject#getName
	 * ()
	 */
	@Override
	public String getName() {
		Attribute attribute = getReferenceAttibute();
		if (attribute != null) {
			return attribute.getName();
		} else {
			return super.getName();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.xml.AbstractXMLObject#
	 * getNameSpacePrefix()
	 */
	@Override
	public String getNameSpacePrefix() {
		Attribute attribute = getReferenceAttibute();
		if (attribute != null) {
			return attribute.getNamespacePrefix();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.xml.AbstractXMLObject#
	 * getNamespaceURI()
	 */
	@Override
	public String getNamespaceURI() {
		Attribute attribute = getReferenceAttibute();
		if (attribute != null) {
			return attribute.getNamespaceURI();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.editors.xml.AbstractXMLObject#setName
	 * (java.lang.String)
	 */
	@Override
	public void setName(String name) {
		if(name.equals(getName())){
			return;
		}
		super.setName(name);
		Attribute attribute = getReferenceAttibute();
		if (attribute != null) {
			String namespace = attribute.getNamespaceURI();
			String namespacePrefix = attribute.getNamespacePrefix();
			Element container = attribute.getParent();
			String value = attribute.getValue();
			container.remove(attribute);
			Attribute newAttribute = DOMDocumentFactory.getInstance().createAttribute(container,
					new QName(name, new Namespace(namespacePrefix, namespace)), value);
			container.add(newAttribute);
			this.setReferenceAttibute(newAttribute);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.xml.AbstractXMLObject#
	 * setNameSpacePrefix(java.lang.String)
	 */
	@Override
	public void setNameSpacePrefix(String nameSpacePrefix) {
		Attribute attribute = getReferenceAttibute();
		if (attribute != null) {
			attribute.setNamespace(new Namespace(nameSpacePrefix, attribute.getNamespaceURI()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.xml.AbstractXMLObject#
	 * setNamespaceURI(java.lang.String)
	 */
	@Override
	public void setNamespaceURI(String namespaceURI) {
		Attribute attribute = getReferenceAttibute();
		if (attribute != null) {
			attribute.setNamespace(new Namespace(attribute.getNamespacePrefix(), namespaceURI));
		}
	}

	/**
	 * @param referenceAttibute
	 *            the referenceAttibute to set
	 */
	public void setReferenceAttibute(Attribute referenceAttibute) {
		this.referenceAttibute = referenceAttibute;
	}

	@Override
	public boolean isAttribute() {
		return true;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public Object getID() {
		return "@" + super.getID(); //$NON-NLS-1$
	}

	@Override
	public List<AbstractXMLObject> getXMLNodeChildren() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public void setChildren(List<AbstractXMLObject> children) {
	}
}
