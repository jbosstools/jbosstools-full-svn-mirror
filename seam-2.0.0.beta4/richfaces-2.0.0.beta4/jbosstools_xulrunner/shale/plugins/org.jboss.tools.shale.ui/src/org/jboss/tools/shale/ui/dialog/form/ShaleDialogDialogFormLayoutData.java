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
import org.jboss.tools.shale.model.dialog.ShaleDialogConstants;

public class ShaleDialogDialogFormLayoutData implements ShaleDialogConstants {

	final static IFormData STATE_LIST_DEFINITION = new FormData(
		"States",
		"", //"Description
		new FormAttributeData[]{new FormAttributeData("element type", 30, "state"), new FormAttributeData("name", 70, "name")},
		new String[]{ACTION_ENTITY, SUBDIALOG_ENTITY, VIEW_ENTITY, END_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddState")
	);

	private final static IFormData[] DIALOG_DEFINITIONS =new IFormData[] {
		new FormData(
			"Dialog",
			"", //"Description
			FormLayoutDataUtil.createGeneralFormAttributeData(DIALOG_ENTITY)
		),
		ShaleDialogTransitionFormLayoutData.TRANSITION_LIST_DEFINITION,
		STATE_LIST_DEFINITION
	};

	final static IFormData DIALOG_FORM_DEFINITION = new FormData(
		DIALOG_ENTITY, new String[]{null}, DIALOG_DEFINITIONS);

}
