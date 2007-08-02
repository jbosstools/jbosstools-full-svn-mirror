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

public class ShaleClayConverterFormLayoutData implements ShaleClayConstants {

	static IFormData CONVERTER_CHILD_DEFINITION = new FormData(
		"Converter",
		"", //"Description
		"Converter",
		FormLayoutDataUtil.createGeneralFormAttributeData(CONVERTER_ENTITY)
	);

	private static IFormData[] CONVERTER_DEFINITIONS = new IFormData[] {
		new FormData(
			"Converter",
			"", //"Description
			FormLayoutDataUtil.createGeneralFormAttributeData(CONVERTER_ENTITY)
		),
	};

	final static IFormData CONVERTER_FORM_DEFINITION = new FormData(
		CONVERTER_ENTITY, new String[]{null}, CONVERTER_DEFINITIONS);

}
