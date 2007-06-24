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
package org.jboss.tools.shale.ui.clay.form;

import java.util.*;
import org.jboss.tools.common.model.util.ClassLoaderUtil;
import org.jboss.tools.common.model.ui.forms.*;

public class ShaleClayFormLayoutData implements IFormLayoutData {
	static {
		ClassLoaderUtil.init();
	}

	private final static IFormData[] FORM_LAYOUT_DEFINITIONS = new IFormData[] {
		ShaleClayFileFormLayoutData.FILE_FORM_DEFINITION,
		ShaleClayComponentFormLayoutData.COMPONENT_FORM_DEFINITION,

		ShaleClaySetFormLayoutData.ATTRIBUTES_FORM_DEFINITION,
		ShaleClaySetFormLayoutData.SYMBOLS_FORM_DEFINITION,
		ShaleClaySetFormLayoutData.SET_FORM_DEFINITION,
		
		ShaleClayConverterFormLayoutData.CONVERTER_FORM_DEFINITION,
		
		ShaleClayValidatorFormLayoutData.VALIDATORS_FORM_DEFINITION,
		ShaleClayValidatorFormLayoutData.VALIDATOR_FORM_DEFINITION,
		
		ShaleClayActionListenerFormLayoutData.ACTION_LISTENER_FORM_DEFINITION,
		ShaleClayActionListenerFormLayoutData.ACTION_LISTENERS_FORM_DEFINITION,
		
		ShaleClayValueListenerFormLayoutData.VALUE_LISTENER_FORM_DEFINITION,
		ShaleClayValueListenerFormLayoutData.VALUE_LISTENERS_FORM_DEFINITION,
		
		ShaleClayElementFormLayoutData.ELEMENT_FORM_DEFINITION,
		ShaleClayElementFormLayoutData.ELEMENTS_FORM_DEFINITION,
		
	};

	private static Map FORM_LAYOUT_DEFINITION_MAP = Collections.unmodifiableMap(new ArrayToMap(FORM_LAYOUT_DEFINITIONS));
	
	private static ShaleClayFormLayoutData INSTANCE = new ShaleClayFormLayoutData();
	
	public static IFormLayoutData getInstance() {
		return INSTANCE;
	}
	
	private ShaleClayFormLayoutData() {}

	public IFormData getFormData(String entityName) {
		return (IFormData)FORM_LAYOUT_DEFINITION_MAP.get(entityName);
	}

}
