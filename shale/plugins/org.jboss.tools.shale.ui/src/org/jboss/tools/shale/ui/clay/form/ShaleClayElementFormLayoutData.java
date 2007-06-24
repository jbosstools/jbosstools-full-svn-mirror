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

import org.jboss.tools.common.model.ui.forms.FormAttributeData;
import org.jboss.tools.common.model.ui.forms.FormData;
import org.jboss.tools.common.model.ui.forms.FormLayoutDataUtil;
import org.jboss.tools.common.model.ui.forms.IFormData;
import org.jboss.tools.shale.model.clay.*;

public class ShaleClayElementFormLayoutData implements ShaleClayConstants {
	
	private final static IFormData ELEMENT_LIST_DEFINITION = new FormData(
		"Elements",
		"", //"Description
		new FormAttributeData[]{new FormAttributeData("jsf id", 100)},
		new String[]{ELEMENT_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddElement")
	);

	final static IFormData ELEMENT_FOLDER_DEFINITION = new FormData(
		"Elements",
		"", //"Description
		"Elements",
		new FormAttributeData[]{new FormAttributeData("jsf id", 100)},
		new String[]{ELEMENT_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddElement")
	);

	private final static IFormData[] ELEMENT_DEFINITIONS = new IFormData[] {
		new FormData(
			"Element",
			"", //"Description
			FormLayoutDataUtil.createGeneralFormAttributeData(ELEMENT_ENTITY)
		),
		ShaleClaySetFormLayoutData.ATTRIBUTES_FOLDER_DEFINITION,
		ShaleClaySetFormLayoutData.SYMBOLS_FOLDER_DEFINITION,
		ShaleClayConverterFormLayoutData.CONVERTER_CHILD_DEFINITION,
		ShaleClayValidatorFormLayoutData.VALIDATORS_FOLDER_DEFINITION,
		ShaleClayActionListenerFormLayoutData.ACTION_LS_FOLDER_DEFINITION,
		ShaleClayValueListenerFormLayoutData.VALUE_LS_FOLDER_DEFINITION,
		ELEMENT_FOLDER_DEFINITION,
		new FormData(
			"Advanced",
			"", //"Description
			FormLayoutDataUtil.createAdvancedFormAttributeData(ELEMENT_ENTITY)
		),
	};

	private static IFormData[] ELEMENTS_DEFINITIONS = new IFormData[] {
		ELEMENT_LIST_DEFINITION
	};
	
	final static IFormData ELEMENT_FORM_DEFINITION = new FormData(
		ELEMENT_ENTITY, new String[]{null}, ELEMENT_DEFINITIONS);

	final static IFormData ELEMENTS_FORM_DEFINITION = new FormData(
		ELEMENT_FOLDER_ENTITY, new String[]{null}, ELEMENTS_DEFINITIONS);
}
