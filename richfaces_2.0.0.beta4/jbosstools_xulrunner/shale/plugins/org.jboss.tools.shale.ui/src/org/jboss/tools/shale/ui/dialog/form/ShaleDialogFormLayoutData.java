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

import java.util.*;
import org.jboss.tools.common.model.util.ClassLoaderUtil;
import org.jboss.tools.common.model.ui.forms.*;

public class ShaleDialogFormLayoutData implements IFormLayoutData {
	static {
		ClassLoaderUtil.init();
	}

	private final static IFormData[] FORM_LAYOUT_DEFINITIONS = new IFormData[] {
		ShaleDialogFileFormLayoutData.FILE_FORM_DEFINITION,
		ShaleDialogDialogFormLayoutData.DIALOG_FORM_DEFINITION,		
		ShaleDialogActionFormLayoutData.ACTION_FORM_DEFINITION,
		ShaleDialogTransitionFormLayoutData.TRANSITION_FORM_DEFINITION,
		ShaleDialogEndFormLayoutData.END_FORM_DEFINITION,
		ShaleDialogViewFormLayoutData.VIEW_FORM_DEFINITION,
		ShaleDialogSubdialogFormLayoutData.SUBDIALOG_FORM_DEFINITION,
	};

	private static Map FORM_LAYOUT_DEFINITION_MAP = Collections.unmodifiableMap(new ArrayToMap(FORM_LAYOUT_DEFINITIONS));
	
	private static ShaleDialogFormLayoutData INSTANCE = new ShaleDialogFormLayoutData();
	
	public static IFormLayoutData getInstance() {
		return INSTANCE;
	}
	
	private ShaleDialogFormLayoutData() {}

	public IFormData getFormData(String entityName) {
		return (IFormData)FORM_LAYOUT_DEFINITION_MAP.get(entityName);
	}

}
