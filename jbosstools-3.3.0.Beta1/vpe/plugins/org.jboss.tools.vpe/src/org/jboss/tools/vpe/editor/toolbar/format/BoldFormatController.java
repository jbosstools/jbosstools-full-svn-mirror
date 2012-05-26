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
package org.jboss.tools.vpe.editor.toolbar.format;

import org.eclipse.swt.widgets.ToolItem;
import org.w3c.dom.Attr;

import org.jboss.tools.vpe.editor.template.textformating.FormatAttributeData;

/**
 * @author Igels
 */
public class BoldFormatController extends ToolItemFormatController {

	public static String TYPE = "BoldFormat"; //$NON-NLS-1$

	private static String STYLE_PROPERTY_NAME = "FONT-WEIGHT"; //$NON-NLS-1$
	private static String STYLE_PROPERTY_BOLD_VALUE = "bold"; //$NON-NLS-1$
	private static String STYLE_PROPERTY_NORMAL_VALUE = "normal"; //$NON-NLS-1$

	/**
	 * @param manager
	 * @param toolItem
	 */
	public BoldFormatController(FormatControllerManager manager, ToolItem toolItem) {
		super(manager, toolItem);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.IFormatController#getType()
	 */
	public String getType() {
		return TYPE;
	}

	protected void setStyle(Attr styleAttribute, FormatAttributeData templateData) {
		String setValue = templateData.getParentFormatData().isSetDefault()?STYLE_PROPERTY_NORMAL_VALUE:STYLE_PROPERTY_BOLD_VALUE;
		invertSingleStyleProperty(styleAttribute, STYLE_PROPERTY_NAME, setValue);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.AttributeFormatController#isStyleSet(org.w3c.dom.Attr, org.jboss.tools.vpe.editor.template.textformating.FormatAttributeData)
	 */
	protected boolean isStyleSet(Attr attribute, FormatAttributeData templateData) {
		boolean isSetDefault = templateData.getParentFormatData().isSetDefault();
		String value = isSetDefault?STYLE_PROPERTY_NORMAL_VALUE:STYLE_PROPERTY_BOLD_VALUE;
		boolean isStyleSet = isStyleSet(STYLE_PROPERTY_NAME, value, attribute);
		if(isSetDefault) {
			return !isStyleSet;
		} else {
			return isStyleSet;
		}
	}
}