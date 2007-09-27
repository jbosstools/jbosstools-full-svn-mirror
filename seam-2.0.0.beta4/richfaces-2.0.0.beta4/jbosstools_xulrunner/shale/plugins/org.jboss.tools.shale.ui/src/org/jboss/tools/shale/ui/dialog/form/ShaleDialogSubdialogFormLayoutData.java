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

public class ShaleDialogSubdialogFormLayoutData implements ShaleDialogConstants {

	private final static IFormData[] SUBDIALOG_DEFINITIONS =new IFormData[] {
		new FormData(
			"Subdialog",
			"", //"Description
			FormLayoutDataUtil.createGeneralFormAttributeData(SUBDIALOG_ENTITY)
		),
		ShaleDialogTransitionFormLayoutData.TRANSITION_LIST_DEFINITION
	};

	final static IFormData SUBDIALOG_FORM_DEFINITION = new FormData(
		SUBDIALOG_ENTITY, new String[]{null}, SUBDIALOG_DEFINITIONS);

}
