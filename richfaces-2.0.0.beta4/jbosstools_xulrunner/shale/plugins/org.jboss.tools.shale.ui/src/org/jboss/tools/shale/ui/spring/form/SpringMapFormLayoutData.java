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
package org.jboss.tools.shale.ui.spring.form;

import org.jboss.tools.common.model.ui.forms.*;
import org.jboss.tools.shale.model.spring.*;

public class SpringMapFormLayoutData implements SpringBeansConstants {
	
	private final static IFormData ENTRIES_LIST_DEFINITION = new FormData(
		"Entries",
		"", //"Description
		new FormAttributeData[]{new FormAttributeData("key presentation", 100, "entry key")},
		new String[]{ENTRY_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddEntry")
	);

	private final static IFormData LIST_ELEMENTS_DEFINITION = new FormData(
		"Elements",
		"", //"Description
		new FormAttributeData[]{new FormAttributeData("presentation", 100, "element")},
		new String[]{"SpringBeansElementBean", "SpringBeansRef", "SpringBeansIDRef", "SpringBeansValue", "SpringBeansNull", "SpringBeansList", "SpringBeansSet", "SpringBeansMap", "SpringBeansProps"},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddElement")
	);

	private final static IFormData[] createCollectionDefinitions(String name, String entity) {
		return new IFormData[] {
			new FormData(
				name,
				"", //"Description
				FormLayoutDataUtil.createGeneralFormAttributeData(entity)
			),
			LIST_ELEMENTS_DEFINITION
		};
	}

	private final static IFormData[] MAP_DEFINITIONS = new IFormData[] {
		new FormData(
		"Map",
		"", //"Description
		FormLayoutDataUtil.createGeneralFormAttributeData(MAP_ENTITY)
		),
		ENTRIES_LIST_DEFINITION
	};
	
	private final static IFormData[] SET_DEFINITIONS = createCollectionDefinitions("Map", MAP_ENTITY);
	private final static IFormData[] LIST_DEFINITIONS = createCollectionDefinitions("Map", MAP_ENTITY);

	final static IFormData MAP_FORM_DEFINITION = new FormData(
		MAP_ENTITY, new String[]{null}, MAP_DEFINITIONS);

	final static IFormData SET_FORM_DEFINITION = new FormData(
		SET_ENTITY, new String[]{null}, SET_DEFINITIONS);

	final static IFormData LIST_FORM_DEFINITION = new FormData(
		LIST_ENTITY, new String[]{null}, LIST_DEFINITIONS);

}
