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
import org.jboss.tools.vpe.editor.toolbar.format.css.SinglePropertyValue;
import org.jboss.tools.vpe.editor.toolbar.format.css.StyleAttribute;
import org.jboss.tools.vpe.editor.toolbar.format.css.StyleProperty;

/**
 * @author Igels
 */
public class UnderlineFormatController extends ToolItemFormatController {

	public static String TYPE = "UnderlineFormat"; //$NON-NLS-1$

	private static String STYLE_PROPERTY_NAME = "TEXT-DECORATION"; //$NON-NLS-1$
	private static String STYLE_PROPERTY_UNDERLINE_VALUE = "underline"; //$NON-NLS-1$
	private static String STYLE_PROPERTY_NORMAL_VALUE = "none"; //$NON-NLS-1$

	/**
	 * @param manager
	 * @param toolItem
	 */
	public UnderlineFormatController(FormatControllerManager manager, ToolItem toolItem) {
		super(manager, toolItem);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.IFormatController#getType()
	 */
	public String getType() {
		return TYPE;
	}

//	protected boolean isStyleSet(Attr styleAttribute) {
//		return isStyleSet(STYLE_PROPERTY_NAME, STYLE_PROPERTY_VALUE, styleAttribute);
//	}

	protected void setStyle(Attr styleAttribute, FormatAttributeData templateData) {
		StyleAttribute style = new StyleAttribute(styleAttribute);
		StyleProperty property = style.getProperty(STYLE_PROPERTY_NAME);
		if(templateData.getParentFormatData().isSetDefault()) {
			if(property==null) {
				style.addStyleProperty(STYLE_PROPERTY_NAME, STYLE_PROPERTY_NORMAL_VALUE);
			} else {
				SinglePropertyValue value = property.getSinglePropertyValue(STYLE_PROPERTY_NORMAL_VALUE);
				if(value!=null) {
					style.removeStyleProperty(STYLE_PROPERTY_NAME);
				} else {
					style.removeStyleProperty(STYLE_PROPERTY_NAME);
					style.addStyleProperty(STYLE_PROPERTY_NAME, STYLE_PROPERTY_NORMAL_VALUE);
				}
			}
		} else {
			if(property==null) {
				style.addStyleProperty(STYLE_PROPERTY_NAME, STYLE_PROPERTY_UNDERLINE_VALUE);
			} else {
				SinglePropertyValue value = property.getSinglePropertyValue(STYLE_PROPERTY_UNDERLINE_VALUE);
				if(value!=null) {
					style.removeStyleProperty(STYLE_PROPERTY_NAME, STYLE_PROPERTY_UNDERLINE_VALUE);
				} else {
					style.addStyleProperty(STYLE_PROPERTY_NAME, STYLE_PROPERTY_UNDERLINE_VALUE);
				}
			}
		}

		String value = style.toString().trim();
		if(value.length()==0) {
			styleAttribute.getOwnerElement().removeAttributeNode(styleAttribute);
		} else {
			styleAttribute.setValue(value);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.AttributeFormatController#isStyleSet(org.w3c.dom.Attr, org.jboss.tools.vpe.editor.template.textformating.FormatAttributeData)
	 */
	protected boolean isStyleSet(Attr attribute, FormatAttributeData templateData) {
		boolean isSetDefault = templateData.getParentFormatData().isSetDefault();
		String value = isSetDefault?STYLE_PROPERTY_NORMAL_VALUE:STYLE_PROPERTY_UNDERLINE_VALUE;
		boolean isStyleSet = isStyleSet(STYLE_PROPERTY_NAME, value, attribute);
		if(isSetDefault) {
			return !isStyleSet;
		} else {
			return isStyleSet;
		}
	}
}