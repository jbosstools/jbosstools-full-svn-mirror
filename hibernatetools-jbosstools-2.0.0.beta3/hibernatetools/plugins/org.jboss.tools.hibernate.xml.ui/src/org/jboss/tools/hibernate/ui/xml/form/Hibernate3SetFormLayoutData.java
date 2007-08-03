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
package org.jboss.tools.hibernate.ui.xml.form;

import org.jboss.tools.common.model.ui.forms.FormAttributeData;
import org.jboss.tools.common.model.ui.forms.FormData;
import org.jboss.tools.common.model.ui.forms.IFormAttributeData;
import org.jboss.tools.common.model.ui.forms.IFormData;

public class Hibernate3SetFormLayoutData {
	static String SET_ENTITY = "Hibernate3Set";
	static String BAG_ENTITY = "Hibernate3Bag";

	final static IFormData[] SET_DEFINITIONS = new IFormData[] {
		new FormData(
			"Set",
			"", //"Description
			Hibernate3FormLayoutDataUtil.createGeneralFormAttributeData(SET_ENTITY)
		),
		new FormData(
			"Key",
			"",
			"key",
			Hibernate3FormLayoutDataUtil.createGeneralFormAttributeData("Hibernate3Key")
		),
		new FormData(
			"Element",
			"", //"Description
			new IFormAttributeData[]{
				new FormAttributeData("element", 100, "Element Kind")
			}
		),
		Hibernate3MetaFormLayoutData.META_LIST_DEFINITION,
		Hibernate3SQLQueryFormLayoutData.SYNCHRONIZES_LIST_DEFINITION,
		Hibernate3FilterFormLayoutData.FILTER_LIST_DEFINITION,
		new FormData(
			"Advanced",
			"", //"Description
			Hibernate3FormLayoutDataUtil.createAdvancedFormAttributeData(SET_ENTITY)
		),
	};

	final static IFormData[] BAG_DEFINITIONS = new IFormData[] {
		new FormData(
			"Bag",
			"", //"Description
			Hibernate3FormLayoutDataUtil.createGeneralFormAttributeData(BAG_ENTITY)
		),
		new FormData(
			"Key",
			"",
			"key",
			Hibernate3FormLayoutDataUtil.createGeneralFormAttributeData("Hibernate3Key")
		),
		new FormData(
			"Element",
			"", //"Description
			new IFormAttributeData[]{
				new FormAttributeData("element", 100, "Element Kind")
			}
		),
		Hibernate3MetaFormLayoutData.META_LIST_DEFINITION,
		Hibernate3SQLQueryFormLayoutData.SYNCHRONIZES_LIST_DEFINITION,
		Hibernate3FilterFormLayoutData.FILTER_LIST_DEFINITION,
		new FormData(
			"Advanced",
			"", //"Description
			Hibernate3FormLayoutDataUtil.createAdvancedFormAttributeData(BAG_ENTITY)
		),
	};

	static IFormData SET_DEFINITION = new FormData(
		SET_ENTITY, new String[]{null}, SET_DEFINITIONS
	);

	static IFormData BAG_DEFINITION = new FormData(
		BAG_ENTITY, new String[]{null}, BAG_DEFINITIONS
	);

}
