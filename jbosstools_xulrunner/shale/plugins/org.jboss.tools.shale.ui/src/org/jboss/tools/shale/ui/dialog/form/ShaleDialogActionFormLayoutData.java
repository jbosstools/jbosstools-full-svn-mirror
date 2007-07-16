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
import org.jboss.tools.shale.model.dialog.*;

public class ShaleDialogActionFormLayoutData implements ShaleDialogConstants {

	private final static IFormData[] ACTION_DEFINITIONS =new IFormData[] {
		new FormData(
			"Action",
			"", //"Description
			FormLayoutDataUtil.createGeneralFormAttributeData(ACTION_ENTITY)
		),
		ShaleDialogTransitionFormLayoutData.TRANSITION_LIST_DEFINITION
	};

	final static IFormData ACTION_FORM_DEFINITION = new FormData(
			ACTION_ENTITY, new String[]{null}, ACTION_DEFINITIONS);

}
