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

public class ShaleClayActionListenerFormLayoutData implements ShaleClayConstants {

	static IFormData ACTION_LS_FOLDER_DEFINITION = new FormData(
		"Action Listeners",
		"", //"Description
		"Action Listeners",
		new FormAttributeData[]{new FormAttributeData("jsf id", 100)},
		new String[]{ACTION_LISTENER_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddActionListener")
	);

	static IFormData ACTION_LS_LIST_DEFINITION = new FormData(
		"Action Listeners",
		"", //"Description
		new FormAttributeData[]{new FormAttributeData("jsf id", 100)},
		new String[]{ACTION_LISTENER_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddActionListener")
	);
	
	private static IFormData[] ACTION_LISTENERS_DEFINITIONS = new IFormData[] {
		ACTION_LS_LIST_DEFINITION
	};
	
	private static IFormData[] ACTION_LISTENER_DEFINITIONS = new IFormData[] {
		new FormData(
			"Action Listener",
			"", //"Description
			FormLayoutDataUtil.createGeneralFormAttributeData(ACTION_LISTENER_ENTITY)
		),
		ShaleClaySetFormLayoutData.ATTRIBUTES_FOLDER_DEFINITION,
	};

	final static IFormData ACTION_LISTENERS_FORM_DEFINITION = new FormData(
		ACTION_LISTENER_FOLDER_ENTITY, new String[]{null}, ACTION_LISTENERS_DEFINITIONS);

	final static IFormData ACTION_LISTENER_FORM_DEFINITION = new FormData(
		ACTION_LISTENER_ENTITY, new String[]{null}, ACTION_LISTENER_DEFINITIONS);

}
