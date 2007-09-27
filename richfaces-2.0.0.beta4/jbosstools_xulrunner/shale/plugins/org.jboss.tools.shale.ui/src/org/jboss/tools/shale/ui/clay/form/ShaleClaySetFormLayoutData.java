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

public class ShaleClaySetFormLayoutData implements ShaleClayConstants {

	static IFormData createSetListDefinition(String name) {
		return new FormData(
			name,
			"", //"Description
			new FormAttributeData[]{new FormAttributeData("name", 100)},
			new String[]{SET_ENTITY},
			FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddSet")
		);
	}

	static IFormData createSetFolderDefinition(String name) {
		return new FormData(
			name,
			"", //"Description
			name,
			new FormAttributeData[]{new FormAttributeData("name", 100)},
			new String[]{SET_ENTITY},
			FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddSet")
		);
	}

	static IFormData ATTRIBUTES_FOLDER_DEFINITION =
		createSetFolderDefinition("Attributes");

	static IFormData SYMBOLS_FOLDER_DEFINITION = 
		createSetFolderDefinition("Symbols");

	private final static IFormData[] ATTRIBUTES_DEFINITIONS = new IFormData[] {
		createSetListDefinition("Attributes")
	};
	private final static IFormData[] SYMBOLS_DEFINITIONS = new IFormData[] {
		createSetListDefinition("Symbols")
	};
	
	private final static IFormData[] SET_DEFINITIONS = new IFormData[] {
		new FormData(
			"Set",
			"", //"Description
			FormLayoutDataUtil.createGeneralFormAttributeData(SET_ENTITY)
		),
	};

	final static IFormData ATTRIBUTES_FORM_DEFINITION = new FormData(
		ATTRIBUTES_ENTITY, new String[]{null}, ATTRIBUTES_DEFINITIONS);

	final static IFormData SYMBOLS_FORM_DEFINITION = new FormData(
		SYMBOLS_ENTITY, new String[]{null}, SYMBOLS_DEFINITIONS);

	final static IFormData SET_FORM_DEFINITION = new FormData(
		SET_ENTITY, new String[]{null}, SET_DEFINITIONS);

}
