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

import org.jboss.tools.common.model.ui.forms.*;
import org.jboss.tools.shale.model.clay.*;

public class ShaleClayValidatorFormLayoutData implements ShaleClayConstants {

	static IFormData VALIDATORS_FOLDER_DEFINITION = new FormData(
		"Validators",
		"", //"Description
		"Validators",
		new FormAttributeData[]{new FormAttributeData("jsf id", 100)},
		new String[]{VALIDATOR_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddValidator")
	);

	static IFormData VALIDATORS_LIST_DEFINITION = new FormData(
		"Validators",
		"", //"Description
		new FormAttributeData[]{new FormAttributeData("jsf id", 100)},
		new String[]{VALIDATOR_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddValidator")
	);
	
	static IFormData[] VALIDATORS_DEFINITIONS = new IFormData[] {
		VALIDATORS_LIST_DEFINITION
	};
	
	static IFormData[] VALIDATOR_DEFINITIONS = new IFormData[] {
		new FormData(
			"Validator",
			"", //"Description
			FormLayoutDataUtil.createGeneralFormAttributeData(VALIDATOR_ENTITY)
		),
	};

	final static IFormData VALIDATORS_FORM_DEFINITION = new FormData(
		VALIDATOR_FOLDER_ENTITY, new String[]{null}, VALIDATORS_DEFINITIONS);

	final static IFormData VALIDATOR_FORM_DEFINITION = new FormData(
			VALIDATOR_ENTITY, new String[]{null}, VALIDATOR_DEFINITIONS);

}
