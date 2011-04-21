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
package org.jboss.tools.common.model.ui.attribute.editor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.common.meta.XAttribute;
import org.jboss.tools.common.meta.action.XAttributeData;
import org.jboss.tools.common.meta.key.WizardKeys;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.extension.ExtensionPointUtil;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.common.model.ui.widgets.DefaultSettings;
import org.jboss.tools.common.model.ui.widgets.IWidgetSettings;

public class PropertyEditorFactory {
	public static final String ATTRIBUTE_EDITOR_EXT_POINT = "org.jboss.tools.common.model.ui.attributeEditor";
	
	private static Map<String,Class<?>> classes = new HashMap<String,Class<?>>();
	private static IWidgetSettings settings = new DefaultSettings();

	public PropertyEditorFactory() {}

	public static PropertyEditor createPropertyEditor(Object adapter, XAttribute attribute, XModelObject modelObject) {
		return createPropertyEditor(adapter, attribute, attribute.isRequired());
	}
	
	public static PropertyEditor createPropertyEditor(Object adapter, XAttribute attribute, XAttributeData attributeData) {
		return createPropertyEditor(adapter, attribute, attributeData.getMandatoryFlag());
	}
	
	private static PropertyEditor createPropertyEditor(Object adapter, XAttribute attribute, boolean required) {
		return createPropertyEditor(adapter, attribute, required, settings);
	}

	public static PropertyEditor createPropertyEditor(Object adapter, XAttribute attribute, XModelObject modelObject, IWidgetSettings settings) {
		return createPropertyEditor(adapter, attribute, attribute.isRequired(), settings);
	}
	
	public static PropertyEditor createPropertyEditor(Object adapter, XAttribute attribute, XAttributeData attributeData, IWidgetSettings settings) {
		return createPropertyEditor(adapter, attribute, attributeData.getMandatoryFlag(), settings);
	}
	
	private static PropertyEditor createPropertyEditor(Object adapter, XAttribute attribute, boolean required, IWidgetSettings settings) {
		PropertyEditor propertyEditor=null;
		try {
			propertyEditor = (PropertyEditor)getEditorClass(attribute).newInstance();
			propertyEditor.setSettings(settings);
		} catch (IllegalAccessException e) {
			ModelUIPlugin.getPluginLog().logError(e);
		} catch (InstantiationException e) {
			ModelUIPlugin.getPluginLog().logError(e);
		}
		if (propertyEditor == null) {
			propertyEditor = new StringEditor(settings); 
		}
		String labelText = WizardKeys.getAttributeDisplayName(attribute, true);
		String editorName = attribute.getEditor().getName();
		if(!"CheckBox".equals(editorName) && !"ListRadio".equals(editorName)) {
			//Note: If later there appear more cases of editors that do not need ':' 
			//it will be better to add a property to extension point for field editors.
			labelText += ":";
		}
		if(required) labelText += "*";
		propertyEditor.setLabelText(labelText);
		
		propertyEditor.setInput(adapter);
		return propertyEditor;
	}

	private static Class<?> getEditorClass(XAttribute attribute) {
		return getEditorClass(attribute.getEditor().getName());
	}
	
	static Set<String> defaultEditorIds = new HashSet<String>();
	
	private static Class<?> getEditorClass(String id) {
		Class<?> c = classes.get(id);
		if(c != null) return c;
		c = StringEditor.class;
		try {
			c = ExtensionPointUtil.findClassByElementId(ATTRIBUTE_EDITOR_EXT_POINT, id).getClass();			
		} catch (CoreException e) {
			if(!defaultEditorIds.contains(id)) {
				defaultEditorIds.add(id);
				ModelUIPlugin.getPluginLog().logInfo("PropertyEditorFactory: Default editor used for " + id);
			}		
		}
		classes.put(id, c);		
		return c;
	}
}
