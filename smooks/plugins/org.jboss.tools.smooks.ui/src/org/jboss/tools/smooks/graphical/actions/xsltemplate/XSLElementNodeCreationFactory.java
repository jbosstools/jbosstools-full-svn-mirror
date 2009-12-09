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
package org.jboss.tools.smooks.graphical.actions.xsltemplate;

import org.eclipse.gef.requests.CreationFactory;
import org.jboss.tools.smooks.configuration.editors.xml.AbstractXMLObject;
import org.jboss.tools.smooks.configuration.editors.xml.TagPropertyObject;
import org.jboss.tools.smooks.configuration.editors.xml.XSLModelAnalyzer;
import org.jboss.tools.smooks.configuration.editors.xml.XSLTagObject;

/**
 * @author Dart
 * 
 */
public class XSLElementNodeCreationFactory implements CreationFactory {

	public static final int TYPE_ELEMENT = 1;

	public static final int TYPE_ATTRIBUTE = 2;

	private String name;

	private String nameSpaceURI;

	private String nameSpacePrefix;

	private int type;

	private String value;

	public XSLElementNodeCreationFactory(String name, String nameSpace, String prefix, String value, int type) {
		this.name = name;
		this.nameSpaceURI = nameSpace;
		this.nameSpacePrefix = prefix;
		this.value = value;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.requests.CreationFactory#getNewObject()
	 */
	public Object getNewObject() {
		AbstractXMLObject obj = null;
		if(type == TYPE_ATTRIBUTE){
			obj = new TagPropertyObject();
			
			((TagPropertyObject)obj).setValue(value);
		}
		if(type == TYPE_ELEMENT){
			obj = new XSLTagObject();
		}
		obj.setName(name);
		obj.setNameSpacePrefix(nameSpacePrefix);
		obj.setNamespaceURI(nameSpaceURI);
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.requests.CreationFactory#getObjectType()
	 */
	public Object getObjectType() {
		return null;
	}

	public static XSLElementNodeCreationFactory newXSLTypeElementCreationFactory(String name) {
		return new XSLElementNodeCreationFactory(name, XSLModelAnalyzer.XSL_NAME_SPACE, "xsl", null, TYPE_ELEMENT); //$NON-NLS-1$
	}
	
	public static XSLElementNodeCreationFactory newXSLTypeAttributeCreationFactory(String name) {
		return new XSLElementNodeCreationFactory(name, XSLModelAnalyzer.XSL_NAME_SPACE, "xsl", null, TYPE_ATTRIBUTE); //$NON-NLS-1$
	}
	
	public static XSLElementNodeCreationFactory newNormalAttributeCreationFactory(String name) {
		return new XSLElementNodeCreationFactory(name, null, null, null, TYPE_ATTRIBUTE);
	}

	public static XSLElementNodeCreationFactory newNormalElementCreationFactory(String name) {
		return new XSLElementNodeCreationFactory(name, null, null, null, TYPE_ELEMENT);
	}

}
