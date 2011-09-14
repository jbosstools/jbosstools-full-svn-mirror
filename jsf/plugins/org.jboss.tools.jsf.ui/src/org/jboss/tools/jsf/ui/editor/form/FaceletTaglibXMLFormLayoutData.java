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
package org.jboss.tools.jsf.ui.editor.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jboss.tools.common.meta.XModelEntity;
import org.jboss.tools.common.meta.impl.XModelMetaDataImpl;
import org.jboss.tools.common.model.ui.forms.ArrayToMap;
import org.jboss.tools.common.model.ui.forms.FormAttributeData;
import org.jboss.tools.common.model.ui.forms.FormData;
import org.jboss.tools.common.model.ui.forms.FormLayoutDataUtil;
import org.jboss.tools.common.model.ui.forms.IFormData;
import org.jboss.tools.common.model.ui.forms.IFormLayoutData;
import org.jboss.tools.common.model.ui.forms.ModelFormLayoutData;
import org.jboss.tools.jsf.facelet.model.FaceletTaglibConstants;

/**
 * @author Viacheslav Kabanovich
 */
public class FaceletTaglibXMLFormLayoutData implements IFormLayoutData, FaceletTaglibConstants {
	
	public static String EMPTY_DESCRIPTION = ""; //$NON-NLS-1$

	private final static IFormData[] FORM_LAYOUT_DEFINITIONS = new IFormData[] {		
	};

	public static IFormData ATTRIBUTE_LIST = new FormData(
		"Attributes",
		"", //"Description //$NON-NLS-1$
		new FormAttributeData[]{new FormAttributeData("name", 100)}, //$NON-NLS-1$
		new String[]{"FaceletTaglibAttribute20"}, //$NON-NLS-1$
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddAttribute") //$NON-NLS-1$
	);

	private static Map<String,IFormData> FORM_LAYOUT_DEFINITION_MAP = Collections.synchronizedMap(new ArrayToMap(FORM_LAYOUT_DEFINITIONS));
	
	private static FaceletTaglibXMLFormLayoutData INSTANCE = new FaceletTaglibXMLFormLayoutData();
	
	public static IFormLayoutData getInstance() {
		return INSTANCE;
	}
	
	private FaceletTaglibXMLFormLayoutData() {}

	public IFormData getFormData(String entityName) {
		IFormData data = (IFormData)FORM_LAYOUT_DEFINITION_MAP.get(entityName);
		if(data == null) {
			data = generateDefaultFormData(entityName);
		}
		return data;
	}
	
	private IFormData generateDefaultFormData(String entityName) {
		IFormData data = null;
		XModelEntity entity = XModelMetaDataImpl.getInstance().getEntity(entityName);
		if(entity != null) {
			data = generateDefaultFormData(entity);
		}
		if(data != null) {
			FORM_LAYOUT_DEFINITION_MAP.put(entityName, data);
		}
		return data;		
	}
	
	private IFormData generateDefaultFormData(XModelEntity entity) {
		String entityName = entity.getName();
		List<IFormData> list = new ArrayList<IFormData>();
		IFormData g = ModelFormLayoutData.createGeneralFormData(entity);
		if(g != null) list.add(g);
//		if(entityName.startsWith(PREACTION_PREFIX)) {
//			if(entity.getChild(ENT_ESB_ROUTE_TO) != null) {
//				list.add(ESBListsFormLayoutData.ESB_ROUTE_LIST_DEFINITION);
//			}
//			//do nothing; when specific children exist use specific forms
//		} else if(entity.getChild(ENT_ESB_PROPERTY) != null) {
//			list.add(ESBListsFormLayoutData.ESB_PROPERTY_LIST_DEFINITION);
//		}
		if(entity.getChild("AnyElement") != null) {
			list.add(ModelFormLayoutData.TAG_LIST);
		}
		if(entityName.equals("FaceletTaglibTag20")) {
			list.add(ATTRIBUTE_LIST);
		}
		IFormData a = ModelFormLayoutData.createAdvancedFormData(entityName);
		if(a != null) list.add(a);
		IFormData[] ds = list.toArray(new IFormData[0]);
		IFormData data = new FormData(entityName, new String[]{null}, ds);
		return data;
	}

}
