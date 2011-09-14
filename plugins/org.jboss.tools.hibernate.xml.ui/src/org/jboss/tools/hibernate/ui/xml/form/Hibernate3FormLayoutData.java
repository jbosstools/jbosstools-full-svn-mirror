/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.hibernate.ui.xml.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jboss.tools.common.meta.XModelEntity;
import org.jboss.tools.common.meta.impl.XModelMetaDataImpl;
import org.jboss.tools.common.model.ui.forms.ArrayToMap;
import org.jboss.tools.common.model.ui.forms.FormAttributeData;
import org.jboss.tools.common.model.ui.forms.FormData;
import org.jboss.tools.common.model.ui.forms.IFormData;
import org.jboss.tools.common.model.ui.forms.IFormLayoutData;
import org.jboss.tools.common.model.ui.forms.ModelFormLayoutData;

public class Hibernate3FormLayoutData implements IFormLayoutData {

	private final static IFormData[] FORM_LAYOUT_DEFINITIONS =
		new IFormData[] {
			Hibernate3FileFormLayoutData.FILE_FORM_DEFINITION,

			Hibernate3ClassFormLayoutData.CLASS_DEFINITION,
			Hibernate3ClassFormLayoutData.SUBCLASS_DEFINITION,
			Hibernate3ClassFormLayoutData.JOIN_DEFINITION,
			Hibernate3ClassFormLayoutData.JOINED_SUBCLASS_DEFINITION,
			Hibernate3ClassFormLayoutData.SUBCLASSES_FOLDER_DEFINITION,
			Hibernate3ClassFormLayoutData.ALL_SUBCLASSES_FOLDER_DEFINITION,
			Hibernate3ClassFormLayoutData.JOINED_SUBCLASSES_FOLDER_DEFINITION,
			
			Hibernate3MapFormLayoutData.MAP_DEFINITION,
			Hibernate3ListFormLayoutData.LIST_DEFINITION,
			Hibernate3ListFormLayoutData.ARRAY_DEFINITION,
			Hibernate3SetFormLayoutData.SET_DEFINITION,
			Hibernate3SetFormLayoutData.BAG_DEFINITION,

			Hibernate3AnyFormLayoutData.ANY_DEFINITION,
			Hibernate3PropertyFormLayoutData.PROPERTY_DEFINITION,
			Hibernate3ManyToOneFormLayoutData.MANY_TO_ONE_DEFINITION,
			Hibernate3OneToOneFormLayoutData.ONE_TO_ONE_DEFINITION,
			Hibernate3MetaFormLayoutData.META_FOLDER_DEFINITION,
			Hibernate3MetaFormLayoutData.TUPLIZER_FOLDER_DEFINITION,
			Hibernate3ComponentFormLayoutData.COMPONENT_DEFINITION,
			Hibernate3ComponentFormLayoutData.DYNAMIC_COMPONENT_DEFINITION,
			
			Hibernate3ElementFormLayoutData.ELEMENT_DEFINITION,
			Hibernate3ElementFormLayoutData.MANY_TO_MANY_DEFINITION,
			Hibernate3ElementFormLayoutData.MANY_TO_ANY_DEFINITION,
			Hibernate3CompositeElementFormLayoutData.ELEMENT_DEFINITION,
			Hibernate3CompositeElementFormLayoutData.NESTED_ELEMENT_DEFINITION,
			
			Hibernate3IdFormLayoutData.COMPOSITE_ID_DEFINITION,
			Hibernate3IdFormLayoutData.COMPOSITE_INDEX_DEFINITION,
			Hibernate3IdFormLayoutData.COLLECTION_ID_DEFINITION,
			Hibernate3KeyFormLayoutData.KEY_DEFINITION,
			Hibernate3KeyFormLayoutData.KEY_MANY_TO_ONE_DEFINITION,
			Hibernate3KeyFormLayoutData.KEY_PROPERTY_DEFINITION,
			Hibernate3KeyFormLayoutData.MAP_KEY_DEFINITION,
			Hibernate3KeyFormLayoutData.MAP_KEY_MANY_TO_MANY_DEFINITION,
			Hibernate3KeyFormLayoutData.COMPOSITE_MAP_KEY_DEFINITION,
			Hibernate3KeyFormLayoutData.INDEX_DEFINITION,
			Hibernate3KeyFormLayoutData.LIST_INDEX_DEFINITION,
			
			Hibernate3FormulaFormLayoutData.FORMULA_DEFINITION,
						
			new FormData(
				Messages.Hibernate3FormLayoutData_Types,
				"", //"Description //$NON-NLS-1$
				"Hibernate3TypedefFolder", //$NON-NLS-1$
				new FormAttributeData[]{
						new FormAttributeData("name", 30, Messages.Hibernate3FormLayoutData_Name),  //$NON-NLS-1$
						new FormAttributeData("class", 70, Messages.Hibernate3FormLayoutData_Class)}, //$NON-NLS-1$
				new String[]{"Hibernate3Typedef"}, //$NON-NLS-1$
				Hibernate3FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddTypedef") //$NON-NLS-1$
			),
			new FormData(
				Messages.Hibernate3FormLayoutData_Imports,
				"", //"Description //$NON-NLS-1$
				"Hibernate3ImportFolder", //$NON-NLS-1$
				new FormAttributeData[]{
						new FormAttributeData("class", 60, Messages.Hibernate3FormLayoutData_Class),  //$NON-NLS-1$
						new FormAttributeData("rename", 40, Messages.Hibernate3FormLayoutData_Rename)}, //$NON-NLS-1$
				new String[]{"Hibernate3Import"}, //$NON-NLS-1$
				Hibernate3FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddTypedef") //$NON-NLS-1$
			),
			new FormData(
				Messages.Hibernate3FormLayoutData_Classes,
				"", //"Description //$NON-NLS-1$
				"Hibernate3ClassFolder", //$NON-NLS-1$
				new FormAttributeData[]{new FormAttributeData("name", 100, Messages.Hibernate3FormLayoutData_ClassName)}, //$NON-NLS-1$
				Hibernate3FormLayoutDataUtil.getChildEntitiesWithAttribute("Hibernate3ClassFolder", Messages.Hibernate3FormLayoutData_Name), //$NON-NLS-1$
				Hibernate3FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddAnyClass") //$NON-NLS-1$
			),
			new FormData(
				Messages.Hibernate3FormLayoutData_Queries,
				"", //"Description //$NON-NLS-1$
				"Hibernate3QueryFolder", //$NON-NLS-1$
				new FormAttributeData[]{
						new FormAttributeData("name", 30, Messages.Hibernate3FormLayoutData_Name),  //$NON-NLS-1$
						new FormAttributeData("query", 70, Messages.Hibernate3FormLayoutData_Query)}, //$NON-NLS-1$
				new String[]{"Hibernate3Query", "Hibernate3SQLQuery"}, //$NON-NLS-1$ //$NON-NLS-2$
				Hibernate3FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddAnyQuery") //$NON-NLS-1$
			),
			
			Hibernate3FilterFormLayoutData.FILTER_FOLDER_DEFINITION,
			Hibernate3FilterFormLayoutData.FILTERDEF_FOLDER_DEFINITION,

			Hibernate3SQLQueryFormLayoutData.SQL_QUERY_DEFINITION,
			Hibernate3SQLQueryFormLayoutData.RESULT_SET_DEFINITION,
			Hibernate3FilterFormLayoutData.FILTERDEF_DEFINITION,
			Hibernate3FilterFormLayoutData.TYPEDEF_DEFINITION,

			Hibernate3FormLayoutDataUtil.createAllChildrenFormData(Messages.Hibernate3FormLayoutData_Properties, 
					"Hibernate3AttributesCFolder", null, "name", "CreateActions.AddAttribute"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			Hibernate3FormLayoutDataUtil.createAllChildrenFormData(Messages.Hibernate3FormLayoutData_Properties, 
					"Hibernate3AttributesFolder",  null, "name", "CreateActions.AddAttribute"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			Hibernate3FormLayoutDataUtil.createAllChildrenFormData(Messages.Hibernate3FormLayoutData_Properties, 
					"Hibernate3AttributesJFolder", null, "name", "CreateActions.AddAttribute"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			Hibernate3FormLayoutDataUtil.createAllChildrenFormData(Messages.Hibernate3FormLayoutData_Properties, 
					"Hibernate3AttributesPFolder", null, "name", "CreateActions.AddAttribute"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			Hibernate3FormLayoutDataUtil.createAllChildrenFormData(Messages.Hibernate3FormLayoutData_Properties, 
					"Hibernate3AttributesNestedFolder", null, "name", "CreateActions.AddAttribute"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			
			Hibernate3DatabaseObjectFormLayoutData.DATABASE_OBJECT_CD_DEFINITION,
			Hibernate3DatabaseObjectFormLayoutData.DATABASE_OBJECT_DEF_DEFINITION,
			Hibernate3DatabaseObjectFormLayoutData.DATABASE_FOLDER_DEFINITION,
			
			
			HibConfig3FileFormLayoutData.FILE_FORM_DEFINITION,
			HibConfig3SessionFormLayoutData.SESSION_FACTORY_FORM_DEFINITION,
			HibConfig3PropertyFormLayoutData.PROPERTY_FOLDER_DEFINITION,
			HibConfig3MappingFormLayoutData.MAPPING_FOLDER_DEFINITION,
			HibConfig3CacheFormLayoutData.CACHE_FOLDER_DEFINITION,
			HibConfig3EventFormLayoutData.EVENT_FOLDER_DEFINITION,
			HibConfig3EventFormLayoutData.EVENT_DEFINITION,
			HibConfig3EventFormLayoutData.LISTENER_FOLDER_DEFINITION,
			
	};
	
	private static Map FORM_LAYOUT_DEFINITION_MAP = Collections.synchronizedMap(new ArrayToMap(FORM_LAYOUT_DEFINITIONS));
	
	static Hibernate3FormLayoutData INSTANCE = new Hibernate3FormLayoutData();
	
	public static IFormLayoutData getInstance() {
		return INSTANCE;
	}
	
	public Hibernate3FormLayoutData() {}

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

		IFormData a = ModelFormLayoutData.createAdvancedFormData(entityName);
		if(a != null) list.add(a);
		IFormData[] ds = list.toArray(new IFormData[0]);
		IFormData data = new FormData(entityName, new String[]{null}, ds);
		return data;
	}

}
