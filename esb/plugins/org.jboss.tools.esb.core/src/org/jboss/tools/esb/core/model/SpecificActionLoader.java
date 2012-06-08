/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.esb.core.model;

import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.common.meta.XAttribute;
import org.jboss.tools.common.meta.XChild;
import org.jboss.tools.common.meta.XMapping;
import org.jboss.tools.common.meta.XModelEntity;
import org.jboss.tools.common.meta.impl.XModelMetaDataImpl;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.impl.RegularObjectImpl;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.esb.core.ESBCorePlugin;
import org.jboss.tools.esb.core.model.converters.ConverterConstants;
import org.jboss.tools.esb.core.model.converters.IPropertyConverter;

/**
 * @author Viacheslav Kabanovich
 */
public class SpecificActionLoader implements ESBConstants {
	static final String ACTION_ENTITY = "ESBAction";
	static final String ACTIONS_FOLDER_ENTITY = "ESBActions";

	private static Map<String,String> classToEntity = new HashMap<String, String>();

	public static final SpecificActionLoader instance = new SpecificActionLoader();

	Map<String, IPropertyConverter> propertyConverters = new HashMap<String, IPropertyConverter>();

	SpecificActionLoader() {
		if(classToEntity.isEmpty()) {
			XMapping m = XModelMetaDataImpl.getInstance().getMapping("ESBSpecificActions");
			if(m != null) {
				String[] classes = m.getKeys();
				for (String c: classes) {
					String entity = m.getValue(c);
					classToEntity.put(c, entity);
				}
			}
		}
		propertyConverters.put("alias", ConverterConstants.ALIAS_CONVERTER);
		propertyConverters.put("route", ConverterConstants.ROUTE_CONVERTER);
		propertyConverters.put("send", ConverterConstants.CHANNELS_CONVERTER);
		propertyConverters.put("path", ConverterConstants.OBJECT_PATHS_CONVERTER);
		propertyConverters.put("notification", ConverterConstants.NOTIFICATION_CONVERTER);
		propertyConverters.put("notification2", ConverterConstants.NOTIFICATION_CONVERTER_2);
		propertyConverters.put("bpmVar", ConverterConstants.BPM_VAR_CONVERTER);
		propertyConverters.put("bpmParam", ConverterConstants.BPM_PARAM_CONVERTER);
		propertyConverters.put("httpclient", ConverterConstants.ENDPOINT_CONVERTER);
		propertyConverters.put("header", ConverterConstants.HEADER_CONVERTER);
		propertyConverters.put("namespace", ConverterConstants.NAMESPACES_CONVERTER);
		propertyConverters.put("fieldalias", ConverterConstants.FIELD_ALIAS_CONVERTER);
		propertyConverters.put("implicitcollection", ConverterConstants.IMPLICIT_COLLECTION_CONVERTER);
		propertyConverters.put("attributealias", ConverterConstants.ATTRIBUTE_ALIAS_CONVERTER);
		propertyConverters.put("converter", ConverterConstants.CONVERTER_CONVERTER);
		propertyConverters.put("routernamespace", ConverterConstants.ROUTER_NAMESPACES_CONVERTER);
		propertyConverters.put("arg", ConverterConstants.EJB_PARAM_CONVERTER);
	}

	public boolean isPreActionEntity(XModelObject object) {
		String entityName = object.getModelEntity().getName();
		return isPreActionEntity(entityName);		
	}

	public boolean isPreActionEntity(String entity) {
		return entity.startsWith(PREACTION_PREFIX);
	}

	public boolean isActionsFolder(String entity) {
		return entity.startsWith(ACTIONS_FOLDER_ENTITY);
	}

	private String addSuffix(String entityName, XModelObject actions) {
		for (String suff: KNOWN_SUFFIXES) {
			if(actions.getModelEntity().getChild(entityName + suff) != null) {
				return entityName + suff;
			}
		}
		return entityName;
	}

	public void convertChildrenToSpecific(XModelObject actions) {
		if(!isActionsFolder(actions.getModelEntity().getName())) return;

		boolean modified = false;

		XModelObject[] as = actions.getChildren();
		for (int i = 0; i < as.length; i++) {
			XModelObject action = convertBasicActionToSpecific(actions, as[i]);
			if(action != null) {
				as[i] = action;
				modified = true;
			}
		}
		if(modified) {
			((RegularObjectImpl)actions).replaceChildren(as);
		}
		
	}

	public XModelObject convertBasicActionToSpecific(XModelObject actions, XModelObject basic) {
		String cls = basic.getAttributeValue("class");
		if(cls == null) return null;
		String entityName = classToEntity.get(cls);
		if(entityName == null) return null;
		entityName = addSuffix(entityName, actions);
		return convertBasicActionToSpecific(basic, entityName);
	}

	public XModelObject convertBasicActionToSpecific(XModelObject basic, String entityName) {
		XModelEntity entity = basic.getModelEntity().getMetaModel().getEntity(entityName);
		if(entity == null) return null;;
		XModelObject action = basic.getModel().createModelObject(entityName, null);
		try {
			XModelObjectLoaderUtil.mergeAttributes(action, basic);
		} catch (XModelException e) {
			ESBCorePlugin.log(e);
		}
		
		copyBasicPropertiesToSpecificAtttributes(basic, action);
		
		XChild[] ce = action.getModelEntity().getChildren();
		for (int i = 0; i < ce.length; i++) {
			String childEntityName = ce[i].getName();
			if(ESBConstants.ENT_ESB_PROPERTY.equals(childEntityName)) continue;
			XModelEntity childEntity = action.getModelEntity().getMetaModel().getEntity(childEntityName);
			if(childEntity == null) continue;
			IPropertyConverter converter = getPropertyConverter(childEntity);
			if(converter != null) {
				converter.toSpecific(basic, action);
			}
		}
		
		XModelObject[] cs = basic.getChildren(ESBConstants.ENT_ESB_PROPERTY);
		for (int i = 0; i < cs.length; i++) {
			action.addChild(cs[i]);
		}
		
		return action;
	}

	public XModelObject convertSpecificActionToBasic(XModelObject action) {
		String entityName = action.getModelEntity().getName();
		if(!isPreActionEntity(entityName)) return action;

		String basicActionEntity = addSuffix(ACTION_ENTITY, action.getParent());
		
		XModelObject result = action.getModel().createModelObject(basicActionEntity, null);
		try {
			XModelObjectLoaderUtil.mergeAttributes(result, action);
		} catch (XModelException e) {
			ESBCorePlugin.log(e);
		}
		
		XModelEntity entity = action.getModelEntity();
		
		copySpecificAtttributesToBasicProperties(action, result);

		XChild[] ce = entity.getChildren();
		for (int i = 0; i < ce.length; i++) {
			String childEntityName = ce[i].getName();
			if(ESBConstants.ENT_ESB_PROPERTY.equals(childEntityName)) continue;
			XModelEntity childEntity = entity.getMetaModel().getEntity(childEntityName);
			if(childEntity == null) continue;
			IPropertyConverter converter = getPropertyConverter(childEntity);
			if(converter != null) {
				converter.toBasic(result, action);
			}
		}
		
		XModelObject[] cs = action.getChildren(ESBConstants.ENT_ESB_PROPERTY);
		for (int i = 0; i < cs.length; i++) {
			result.addChild(cs[i].copy());
		}
		return result;
	}

	IPropertyConverter getPropertyConverter(XModelEntity childEntity) {
		String converter = childEntity.getProperty("converter");
		return (converter == null) ? null : propertyConverters.get(converter);
	}

	public static void copyBasicPropertiesToSpecificAtttributes(XModelObject basic, XModelObject specific) {
		XModelEntity entity = specific.getModelEntity();
		XAttribute[] as = entity.getAttributes();
		for (int i = 0; i < as.length; i++) {
			String pre = as[i].getProperty("pre");
			if(pre == null || pre.length() == 0) continue;
			if("true".equals(pre)) {
				copyBasicPropertyToSpecificAttribute(basic, specific, as[i]);
			} else {
				//very specific cases
			}
		}
	}

	public static void copyBasicPropertyToSpecificAttribute(XModelObject basic, XModelObject specific, XAttribute a) {
		String name = a.getXMLName();
		XModelObject p = basic.getChildByPath(name);
		if(p == null) return;
		String value = p.getAttributeValue("value");
		specific.setAttributeValue(a.getName(), value);
		specific.set(a.getXMLName() + ".#comment", p.getAttributeValue("comment"));
		p.removeFromParent();
	}

	public static void copySpecificAtttributesToBasicProperties(XModelObject specific, XModelObject basic) {
		XModelEntity entity = specific.getModelEntity();
		XAttribute[] as = entity.getAttributes();
		for (int i = 0; i < as.length; i++) {
			String pre = as[i].getProperty("pre");
			if(pre == null || pre.length() == 0) continue;
			if("true".equals(pre)) {
				copySpecificAttributeToBasicProperty(specific, basic, as[i]);
			} else {
				//very specific cases
			}
		}
	}

	public static void copySpecificAttributeToBasicProperty(XModelObject specific, XModelObject basic, XAttribute a) {
		String value = specific.getAttributeValue(a.getName());
		if(value == null || value.length() == 0 || value.equals(a.getDefaultValue())) {
			if(!"always".equals(a.getProperty("save"))) return;
		}
		XModelObject p = specific.getModel().createModelObject(ESBConstants.ENT_ESB_PROPERTY, null);
		p.setAttributeValue("name", a.getXMLName());
		p.setAttributeValue("value", value);
		p.setAttributeValue("comment", specific.get(a.getXMLName() + ".#comment"));
		basic.addChild(p);
	}
}
