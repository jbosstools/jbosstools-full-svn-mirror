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

public class ShaleClayFileFormLayoutData implements ShaleClayConstants {

	private final static IFormData[] FILE_DEFINITIONS = new IFormData[] {
		new FormData(
			"Clay Config File",
			"", //"Description
			FormLayoutDataUtil.createGeneralFormAttributeData(ENT_SHALE_CLAY)
		),
		ShaleClayComponentFormLayoutData.COMPONENT_LIST_DEFINITION,
	};

	final static IFormData FILE_FORM_DEFINITION = new FormData(
		ENT_SHALE_CLAY, new String[]{null}, FILE_DEFINITIONS);

}
