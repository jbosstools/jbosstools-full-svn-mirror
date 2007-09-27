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

public class ShaleClayValueListenerFormLayoutData implements ShaleClayConstants {

	static IFormData VALUE_LS_FOLDER_DEFINITION = new FormData(
		"Value Change Listeners",
		"", //"Description
		"Value Change Listeners",
		new FormAttributeData[]{new FormAttributeData("jsf id", 100)},
		new String[]{VALUE_CHANGE_LISTENER_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddValueListener")
	);

	static IFormData VALUE_LS_LIST_DEFINITION = new FormData(
		"Value Change Listeners",
		"", //"Description
		new FormAttributeData[]{new FormAttributeData("jsf id", 100)},
		new String[]{VALUE_CHANGE_LISTENER_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddValueListener")
	);
	
	static IFormData[] VALUE_LISTENERS_DEFINITIONS = new IFormData[] {
		VALUE_LS_LIST_DEFINITION
	};
	
	static IFormData[] VALUE_LISTENER_DEFINITIONS = new IFormData[] {
		new FormData(
			"Value Change Listener",
			"", //"Description
			FormLayoutDataUtil.createGeneralFormAttributeData(VALUE_CHANGE_LISTENER_ENTITY)
		),
		ShaleClaySetFormLayoutData.ATTRIBUTES_FOLDER_DEFINITION,
	};

	final static IFormData VALUE_LISTENERS_FORM_DEFINITION = new FormData(
		VALUE_CHANGE_LISTENER_FOLDER_ENTITY, new String[]{null}, VALUE_LISTENERS_DEFINITIONS);

	final static IFormData VALUE_LISTENER_FORM_DEFINITION = new FormData(
		VALUE_CHANGE_LISTENER_ENTITY, new String[]{null}, VALUE_LISTENER_DEFINITIONS);

}
