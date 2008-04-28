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
package org.jboss.tools.jst.web.ui.editors.webapp.form;

import org.jboss.tools.common.model.ui.forms.*;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

public class WebAppFilterFormLayoutData {
	static String FILTER_ENTITY = "WebAppFilter"; //$NON-NLS-1$
	static String INIT_PARAM_ENTITY = "WebAppInitParam"; //$NON-NLS-1$

	static IFormData INIT_PARAM_LIST_DEFINITION = new FormData(
		WebUIMessages.INITPARAMS,
		WebAppJspConfigFormLayoutData.EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("param-name", 40), new FormAttributeData("param-value", 60)}, //$NON-NLS-1$ //$NON-NLS-2$
		new String[]{INIT_PARAM_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateInitParam") //$NON-NLS-1$
	);

	private final static IFormData[] FILTER_DEFINITIONS = new IFormData[] {
		new FormData(
			WebUIMessages.FILTER,
			WebAppJspConfigFormLayoutData.EMPTY_DESCRIPTION,
			FormLayoutDataUtil.createGeneralFormAttributeData(FILTER_ENTITY)
		),
		INIT_PARAM_LIST_DEFINITION,
		new FormData(
			WebUIMessages.ADVANCED,
			WebAppJspConfigFormLayoutData.EMPTY_DESCRIPTION,
			FormLayoutDataUtil.createAdvancedFormAttributeData(FILTER_ENTITY)
		),
	};

	final static IFormData FILTER_FORM_DEFINITION = new FormData(
		FILTER_ENTITY, new String[]{null}, FILTER_DEFINITIONS);

}
