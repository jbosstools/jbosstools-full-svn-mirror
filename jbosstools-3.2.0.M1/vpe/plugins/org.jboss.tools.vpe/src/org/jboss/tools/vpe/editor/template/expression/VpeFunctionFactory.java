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
package org.jboss.tools.vpe.editor.template.expression;

import java.util.HashMap;
import java.util.Map;
import org.jboss.tools.vpe.VpePlugin;

public class VpeFunctionFactory {
	private static final String FUNC_JSF_VALUE = "jsfvalue";//$NON-NLS-1$
	private static final String FUNC_JSF2_RESOURCE = "jsf2resource";//$NON-NLS-1$
	private static final String FUNC_NAME = "name";//$NON-NLS-1$
	private static final String FUNC_NOT = "not";//$NON-NLS-1$
	private static final String FUNC_IIF = "iif";//$NON-NLS-1$
	private static final String FUNC_SRC = "src";//$NON-NLS-1$
	private static final String FUNC_HREF = "href";//$NON-NLS-1$
	private static final String FUNC_PARENT_NAME = "parentname";//$NON-NLS-1$
	private static final String FUNC_HAS_IN_PARENTS = "hasinparents";//$NON-NLS-1$
	private static final String FUNC_TAG_STRING = "tagstring";//$NON-NLS-1$
	private static final String FUNC_TAG_TEXT = "tagtext";//$NON-NLS-1$
	private static final String FUNC_ATTR_PRESENT = "attrpresent";//$NON-NLS-1$
	private static final String FUNC_PARENT_ATTR_VALUE = "parentattrvalue";//$NON-NLS-1$
	private static final String FUNC_HAS_CHILDREN = "haschildren";//$NON-NLS-1$
	private static final String FUNC_HAS_CONTENT = "hascontent";//$NON-NLS-1$

	private static Map<String,Class<?>> clsMap = new HashMap<String,Class<?>>();

	static VpeFunction getFunction(String name) {
		Class<?> cls = clsMap.get(name);
		if (cls == null) {
			cls = createCls(name);
			if (cls != null) {
				clsMap.put(name, cls);
			} else {
				return null;
			}
		}
		try {
			return (VpeFunction)cls.newInstance();
		} catch(IllegalAccessException e) {
			VpePlugin.getPluginLog().logError(e);
		} catch (InstantiationException e) {
			VpePlugin.getPluginLog().logError(e);
		}
		return null;
	}
	
	private static Class<?> createCls(String name) {
		if (FUNC_JSF_VALUE.equals(name)) {
			return VpeFunctionJsfValue.class;
		} else if (FUNC_JSF2_RESOURCE.equals(name)) {
			return VpeFunctionJsf2Resource.class;
		} else if (FUNC_NAME.equals(name)) {
			return VpeFunctionName.class;
		} else if (FUNC_NOT.equals(name)) {
			return VpeFunctionNot.class;
		} else if (FUNC_IIF.equals(name)) {
			return VpeFunctionIif.class;
		} else if (FUNC_SRC.equals(name)) {
			return VpeFunctionSrc.class;
		} else if (FUNC_HREF.equals(name)) {
			return VpeFunctionHref.class;
		} else if (FUNC_PARENT_NAME.equals(name)) {
			return VpeFunctionParentName.class;
		} else if (FUNC_HAS_IN_PARENTS.equals(name)) {
			return VpeFunctionHasInParents.class;
		} else if (FUNC_TAG_STRING.equals(name)) {
			return VpeFunctionTagString.class;
		} else if (FUNC_TAG_TEXT.equals(name)) {
			return VpeFunctionTagText.class;
		} else if (FUNC_ATTR_PRESENT.equals(name)) {
			return VpeFunctionAttrPresent.class;
		} else if (FUNC_PARENT_ATTR_VALUE.equals(name)) {
			return VpeFunctionParentAttrValue.class;
		} else if (FUNC_HAS_CHILDREN.equals(name)) {
			return VpeFunctionHasChildren.class;
		} else if (FUNC_HAS_CONTENT.equals(name)) {
			return VpeFunctionHasContent.class;
		} else if(VpeFunctionTldVersionCheck.FUNCTION_NAME.equals(name)) {
			return VpeFunctionTldVersionCheck.class;
		}
		return null;
	}
}
