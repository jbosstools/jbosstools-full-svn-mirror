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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.jboss.tools.smooks.model.ParamType;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.SmooksPackage;

/**
 * @author Dart Peng
 * 
 */

public class SmooksModelUtils {
	
	public static final String TYPE_XSL = "xsl";
	
	public static final String[] TEMPLATE_TYPES = new String[] { "xsl","ftl" };
	
	public static final String BEAN_CLASS = "beanClass";

	public static final String BEAN_ID = "beanId";

	public static final String BINDINGS = "bindings";

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

	public static List<Object> getBindingListFromResourceConfigType(
			ResourceConfigType resourceConfig) {
		List<ParamType> paramList = resourceConfig.getParam();
		for (Iterator<ParamType> iterator = paramList.iterator(); iterator
				.hasNext();) {
			ParamType param = iterator.next();
			if ("bindings".equals(param.getName())) {
				if (param.eContents().isEmpty())
					continue;
				List<Object> bindingList = (List<Object>) param.getMixed().get(
						SmooksModelUtils.ELEMENT_BINDING, false);
				return bindingList;
			}
		}
		return Collections.EMPTY_LIST;
	}
	
	public static boolean isBeanPopulatorResource(ResourceConfigType type) {
		ResourceType resource = type.getResource();
		if (resource == null)
			return false;
		String value = resource.getStringValue();
		if(value != null) value = value.trim();
		if (SmooksModelConstants.BEAN_POPULATOR.equals(value)) {
			return true;
		}
		return false;
	}
	
	public static void setPropertyValueToAnyType(Object value,EStructuralFeature attribute,AnyType anyType){
		anyType.getAnyAttribute().set(attribute, value);
	}
	
	public static AnyType getBindingViaProperty(ResourceConfigType resourceConfig , String property){
		List bindingList = getBindingListFromResourceConfigType(resourceConfig);
		for (Iterator iterator = bindingList.iterator(); iterator.hasNext();) {
			AnyType binding = (AnyType) iterator.next();
			String pro = getAttributeValueFromAnyType(binding, ATTRIBUTE_PROPERTY);
			if(pro != null) pro = pro.trim();
			if(property.equals(pro)){
				return binding;
			}
		}
		return null;
	}
	
	public static boolean isInnerFileContents(ResourceConfigType resourceConfig){
		ResourceType resource = resourceConfig.getResource();
		if(resource == null) return false;
		String type = resource.getType();
		if(type != null) type = type.trim();
		for (int i = 0; i < TEMPLATE_TYPES.length; i++) {
			String type1 = TEMPLATE_TYPES[i];
			if(type1.equalsIgnoreCase(type)) return true;
		}
		return false;
	}

	public static boolean isDateTypeSelector(ResourceConfigType type) {
		ResourceType resource = type.getResource();
		if (resource == null)
			return false;
		String value = resource.getStringValue();
		if(value != null) value = value.trim();
		for (int i = 0; i < SmooksModelConstants.DECODER_CLASSES.length; i++) {
			String decoderClass = SmooksModelConstants.DECODER_CLASSES[i];
			if(decoderClass.equals(value)){
				return true;
			}
		}
		return false;
	}

	public static String getTransformType(ResourceConfigType resourceConfig) {
		ParamType typeParam = null;
		if (resourceConfig == null)
			return "";
		if (isTransformTypeResourceConfig(resourceConfig)) {
			List paramList = resourceConfig.getParam();
			for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
				ParamType param = (ParamType) iterator.next();
				String name = param.getName();
				if (name != null)
					name = name.trim();
				if (SmooksModelConstants.STREAM_FILTER_TYPE.equals(name)) {
					typeParam = param;
					break;
				}
			}
			if (typeParam != null) {
				return SmooksModelUtils.getAnyTypeText(typeParam);
			}
		}
		return "";
	}

	public static void setTransformType(ResourceConfigType resourceConfig,
			String type) {
		if (type == null)
			type = "";
		if (isTransformTypeResourceConfig(resourceConfig)) {
			List paramList = resourceConfig.getParam();
			for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
				ParamType param = (ParamType) iterator.next();
				if (SmooksModelConstants.STREAM_FILTER_TYPE.equals(param
						.getName())) {
					cleanTextToSmooksType(param);
					setTextToAnyType(param, type);
				}
			}
		}
	}

	public static boolean isFilePathResourceConfig(
			ResourceConfigType resourceConfig) {
		ResourceType resource = resourceConfig.getResource();
		if (resource != null) {
			String value = resource.getStringValue();
			if (value != null) {
				if (value.startsWith("\\")) {
					return true;
				}
				if (value.startsWith("/")) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isTransformTypeResourceConfig(
			ResourceConfigType resourceConfig) {
		String selector = resourceConfig.getSelector();
		if (selector != null)
			selector = selector.trim();
		if (!SmooksModelConstants.GLOBAL_PARAMETERS.equals(selector)) {
			return false;
		}

		if (resourceConfig.getParam().isEmpty()) {
			return false;
		} else {
			List paramList = resourceConfig.getParam();
			for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
				ParamType p = (ParamType) iterator.next();
				String paramName = p.getName();
				if (paramName != null)
					paramName = paramName.trim();
				if (SmooksModelConstants.STREAM_FILTER_TYPE.equals(paramName)) {
					return true;
				}
			}
			return false;
		}
	}

	public static void setParamText(String paramName, String value,
			ResourceConfigType resourceConfigType) {
		List<ParamType> list = resourceConfigType.getParam();
		ParamType param = null;
		for (Iterator<ParamType> iterator = list.iterator(); iterator.hasNext();) {
			ParamType paramType = (ParamType) iterator.next();
			if (paramType.getName().equalsIgnoreCase(paramName)) {
				param = paramType;
				break;
			}
		}
		if (param == null) {
			param = SmooksFactory.eINSTANCE.createParamType();
			resourceConfigType.getParam().add(param);
		}
		param.setName(paramName);
		setTextToAnyType(param, value);
	}

	public static String getParmaText(String paramName,
			ResourceConfigType resourceConfigType) {
		List plist = resourceConfigType.getParam();
		for (Iterator iterator = plist.iterator(); iterator.hasNext();) {
			ParamType p = (ParamType) iterator.next();
			if (paramName.equalsIgnoreCase(p.getName())) {
				return getAnyTypeText(p);
			}
		}
		return null;
	}

	public static String getAttributeValueFromAnyType(AnyType anyType,
			EStructuralFeature attribute) {
		String value = (String) anyType.getAnyAttribute().get(attribute, false);
		return value;
	}

	public static String getAnyTypeText(AnyType anyType) {
		Object value = anyType.getMixed().get(
				XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT, true);
		if (value != null) {
			if (value instanceof List && !((List) value).isEmpty()) {
				return ((List) value).get(0).toString().trim();
			}
			// return value.toString();
		}
		return null;
	}

	public static String getAnyTypeCDATA(AnyType anyType) {
		Object value = anyType.getMixed().get(
				XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__CDATA, true);
		if (value != null) {
			if (value instanceof List && !((List) value).isEmpty()) {
				return ((List) value).get(0).toString().trim();
			}
			// return value.toString();
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

	public static void appendCDATAToSmooksType(AnyType smooksModel, String text) {
		smooksModel.getMixed().add(
				XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__CDATA, text);
	}

	public static void setTextToAnyType(AnyType smooksModel, String text) {
		cleanTextToSmooksType(smooksModel);
		appendTextToSmooksType(smooksModel, text);
	}

	public static void setCDATAToAnyType(AnyType smooksModel, String text) {
		cleanCDATAToSmooksType(smooksModel);
		appendCDATAToSmooksType(smooksModel, text);
	}

	public static void cleanTextToSmooksType(AnyType smooksModel) {
		Object obj = smooksModel.getMixed().get(
				XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT, true);
		if (obj instanceof List) {
			((List) obj).clear();
		}
	}

	public static void cleanCDATAToSmooksType(AnyType smooksModel) {
		Object obj = smooksModel.getMixed().get(
				XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__CDATA, true);
		if (obj instanceof List) {
			((List) obj).clear();
		}
	}
}
