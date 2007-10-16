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
import org.jboss.tools.common.model.ui.forms.IFormData;

/**
 * @author glory
 */
public class Hibernate3AnyFormLayoutData {
	static String ANY_ENTITY = "Hibernate3Any";
	static String META_VALUE_ENTITY = "Hibernate3MetaValue";
	
	static IFormData META_VALUE_LIST_DEFINITION = new FormData(
		"Meta Values",
		"", //"Description
		new FormAttributeData[]{new FormAttributeData("value", 50), new FormAttributeData("class", 50)},
		new String[]{META_VALUE_ENTITY},
		Hibernate3FormLayoutDataUtil.createDefaultFormActionData("CreateActions.AddMetaValue")
	);

	final static IFormData[] ANY_DEFINITIONS = new IFormData[] {
		new FormData(
			"Any",
			"", //"Description
			Hibernate3FormLayoutDataUtil.createGeneralFormAttributeData(ANY_ENTITY)
		),
		META_VALUE_LIST_DEFINITION,
		Hibernate3ColumnFormLayoutData.COLUMN_LIST_DEFINITION,
		Hibernate3MetaFormLayoutData.META_LIST_DEFINITION,
		new FormData(
			"Advanced",
			"", //"Description
			Hibernate3FormLayoutDataUtil.createAdvancedFormAttributeData(ANY_ENTITY)
		),
	};

	static IFormData ANY_DEFINITION = new FormData(
		ANY_ENTITY, new String[]{null}, ANY_DEFINITIONS
	);

}
