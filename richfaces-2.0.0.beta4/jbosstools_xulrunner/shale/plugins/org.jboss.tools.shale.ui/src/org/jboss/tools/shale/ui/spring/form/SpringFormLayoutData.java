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
package org.jboss.tools.shale.ui.spring.form;

import java.util.Collections;
import java.util.Map;

import org.jboss.tools.common.model.util.ClassLoaderUtil;
import org.jboss.tools.common.model.ui.forms.ArrayToMap;
import org.jboss.tools.common.model.ui.forms.IFormData;
import org.jboss.tools.common.model.ui.forms.IFormLayoutData;

public class SpringFormLayoutData implements IFormLayoutData {
	static {
		ClassLoaderUtil.init();
	}

	private final static IFormData[] FORM_LAYOUT_DEFINITIONS = new IFormData[] {
		SpringMapFormLayoutData.MAP_FORM_DEFINITION,
		SpringMapFormLayoutData.SET_FORM_DEFINITION,
		SpringMapFormLayoutData.LIST_FORM_DEFINITION,
				
	};

	private static Map FORM_LAYOUT_DEFINITION_MAP = Collections.unmodifiableMap(new ArrayToMap(FORM_LAYOUT_DEFINITIONS));
	
	private static SpringFormLayoutData INSTANCE = new SpringFormLayoutData();

	public static IFormLayoutData getInstance() {
		return INSTANCE;
	}
	
	private SpringFormLayoutData() {}
	
	public IFormData getFormData(String entityName) {
		return (IFormData)FORM_LAYOUT_DEFINITION_MAP.get(entityName);
	}

}
