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
package org.jboss.tools.shale.ui.dialog.form;

import org.jboss.tools.common.model.ui.forms.*;

public class ShaleDialogFileFormLayoutData {
	static String FILE_ENTITY = "FileShaleDialog";

	private final static IFormData[] FILE_DEFINITIONS =new IFormData[] {
		new FormData(
			"Dialog Config File",
			"", //"Description
			FormLayoutDataUtil.createGeneralFormAttributeData(FILE_ENTITY)
		),
		new FormData(
			"Dialogs",
			"", //"Description
			new FormAttributeData[]{new FormAttributeData("name", 100, "name")},
			new String[]{"ShaleDialog"},
			FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddDialog")
		),
		new FormData(
			"Advanced",
			"", //"Description
			FormLayoutDataUtil.createAdvancedFormAttributeData(FILE_ENTITY)
		),
	};

	final static IFormData FILE_FORM_DEFINITION = new FormData(
		FILE_ENTITY, new String[]{null}, FILE_DEFINITIONS);

}
