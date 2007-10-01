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

public class ShaleDialogTransitionFormLayoutData implements ShaleDialogConstants {
	
	final static IFormData TRANSITION_LIST_DEFINITION = new FormData(
		"Transitions",
		"", //"Description
		new FormAttributeData[]{new FormAttributeData("outcome", 50, "outcome"), new FormAttributeData("target", 50, "target")},
		new String[]{TRANSITION_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddTransition")
	);

	private final static IFormData[] TRANSITION_DEFINITIONS =new IFormData[] {
		new FormData(
			"Dialog Transition",
			"", //"Description
			FormLayoutDataUtil.createGeneralFormAttributeData(TRANSITION_ENTITY)
		),
	};

	final static IFormData TRANSITION_FORM_DEFINITION = new FormData(
		TRANSITION_ENTITY, new String[]{null}, TRANSITION_DEFINITIONS);

}
