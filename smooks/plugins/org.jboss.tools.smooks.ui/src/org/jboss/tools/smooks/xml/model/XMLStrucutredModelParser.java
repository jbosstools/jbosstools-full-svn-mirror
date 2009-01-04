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

import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.StructuredDataContentModel;
import org.jboss.tools.smooks.ui.gef.model.StructuredDataModel;
import org.jboss.tools.smooks.ui.modelparser.IStructuredModelParser;

/**
 * 
 * @author Dart Peng
 * @Date Jul 25, 2008
 */
public class XMLStrucutredModelParser implements IStructuredModelParser {

	public AbstractStructuredDataModel parse(Object customModel) {
		AbstractStructuredDataModel model = null;
		// for AbstractXMLObject (come from xml file fragment)
		if(customModel instanceof TagList){
			model = new StructuredDataModel();
			model.setLabelName(((TagList)customModel).getName());
		}
		
		if(customModel instanceof TagObject){
			model = new StructuredDataContentModel();
			model.setLabelName(((TagObject)customModel).getName());
		}
		if(customModel instanceof TagPropertyObject){
			model = new StructuredDataContentModel();
			model.setLabelName(((TagPropertyObject)customModel).getName());
			model.setTypeString(((TagPropertyObject)customModel).getType());
		}
		
		// for XSD (come from XML Schema file)
		if (customModel instanceof XSDSchema) {
			model = new StructuredDataModel();
			StructuredDataModel sm = (StructuredDataModel) model;
			sm.setLabelName("Schema");
		}
		if (customModel instanceof XSDElementDeclaration) {
			model = new StructuredDataContentModel();
			model.setLabelName(((XSDElementDeclaration) customModel)
					.getAliasName());
			model
					.setTypeString(getElementTypeString((XSDElementDeclaration) customModel));
		}
		if (customModel instanceof XSDAttributeDeclaration) {
			model = new StructuredDataContentModel();
			model.setLabelName(((XSDAttributeDeclaration) customModel)
					.getAliasName());
			model.setTypeString(((XSDAttributeDeclaration) customModel)
					.getTypeDefinition().getQName());
		}
		if (model != null) {
			model.setReferenceEntityModel(customModel);
		}
		return model;
	}

	protected boolean isSimpleElement(XSDElementDeclaration element) {
		return this.getSimpleType(element) != null;
	}

	protected String getElementTypeString(XSDElementDeclaration element) {
		if (isSimpleElement(element)) {
			XSDSimpleTypeDefinition simple = getSimpleType(element);
			return simple.getQName();
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param element
	 * @return
	 */
	protected XSDSimpleTypeDefinition getSimpleType(
			XSDElementDeclaration element) {
		XSDTypeDefinition type = element.getType();
		return type.getSimpleType();
	}

}
