/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.model.util;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.jboss.tools.smooks.model.ParamType;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.SmooksPackage;

/**
 * @author Dart Peng
 * 
 */

public class SmooksModelUtils {

	public static EStructuralFeature ATTRIBUTE_PROPERTY = ExtendedMetaData.INSTANCE
			.demandFeature(null, "property", false);

	public static EStructuralFeature ATTRIBUTE_SELECTOR = ExtendedMetaData.INSTANCE
			.demandFeature(null, "selector", false);

	public static EStructuralFeature ATTRIBUTE_TYPE = ExtendedMetaData.INSTANCE
			.demandFeature(null, "type", false);

	public static EStructuralFeature ELEMENT_BINDING = ExtendedMetaData.INSTANCE
			.demandFeature("http://www.milyn.org/xsd/smooks-1.0.xsd",
					"binding", true);

	public static AnyType addBindingTypeToParamType(ParamType param,
			String property, String selector, String type, String uri) {
		AnyType binding = createBindingType(property, selector, type, uri);
		param.getMixed().add(ELEMENT_BINDING, binding);
		return binding;
	}

	public static String getParmaText(String paramName,
			ResourceConfigType resourceConfigType) {
		List plist = resourceConfigType.getParam();
		for (Iterator iterator = plist.iterator(); iterator.hasNext();) {
			ParamType p = (ParamType) iterator.next();
			if (paramName.equals(p.getName())) {
				return getAnyTypeText(p);
			}
		}
		return null;
	}
	
	public static  String getAttributeValueFromAnyType(AnyType anyType,EStructuralFeature attribute) {
		String value = (String) anyType.getAnyAttribute().get(attribute, false);
		return value;
	}
	
	public static String getAnyTypeText(AnyType anyType){
		Object value = anyType.getMixed().get(
				XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT,
				true);
		if (value != null) {
			if (value instanceof List && !((List)value).isEmpty()) {
				return ((List) value).get(0).toString().trim();
			}
//			return value.toString();
		}
		return null;
	}

	public static AnyType createBindingType(String property, String selector,
			String type, String uri) {
		if (uri == null) {
			uri = SmooksPackage.eNS_URI;
		}

		AnyType binding = (AnyType) EcoreUtil
				.create(XMLTypePackage.Literals.ANY_TYPE);
		if (property != null) {
			binding.getAnyAttribute().add(ATTRIBUTE_PROPERTY, property);
		}

		if (selector != null) {
			binding.getAnyAttribute().add(ATTRIBUTE_SELECTOR, selector);
		}
		if (type != null) {
			binding.getAnyAttribute().add(ATTRIBUTE_TYPE, false);
		}
		return binding;
	}

	public static void appendTextToSmooksType(AnyType smooksModel, String text) {
		smooksModel.getMixed().add(
				XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT, text);
	}
	
	public static void setTextToSmooksType(AnyType smooksModel, String text){
		cleanTextToSmooksType(smooksModel);
		appendTextToSmooksType(smooksModel,text);
	}
	
	public static void cleanTextToSmooksType(AnyType smooksModel) {
		Object obj = smooksModel.getMixed().get(
				XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT,true);
		if(obj instanceof List){
			((List)obj).clear();
		}
	}
}
