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

import java.util.HashMap;

import org.eclipse.swt.widgets.Combo;
import org.w3c.dom.Attr;

import org.jboss.tools.vpe.editor.template.textformating.FormatAttributeData;
import org.jboss.tools.vpe.editor.toolbar.format.css.StyleAttribute;
import org.jboss.tools.vpe.editor.toolbar.format.css.StyleProperty;

/**
 * @author Igels
 */
public class FontSizeFormatController extends ComboFormatController {

	public static String TYPE = "FontSizeFormat";

	private static String STYLE_PROPERTY_NAME = "FONT-SIZE";

	private static HashMap SIZES = new HashMap();
	static {
		SIZES.put("1", "xx-small");
		SIZES.put("2", "x-small");
		SIZES.put("3", "small");
		SIZES.put("4", "medium");
		SIZES.put("5", "large");
		SIZES.put("6", "x-large");
		SIZES.put("7", "xx-large");
	}

	/**
	 * @param manager
	 * @param comboBlockFormat
	 */
	public FontSizeFormatController(FormatControllerManager manager, Combo comboBlockFormat) {
		super(manager, comboBlockFormat);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.IFormatController#getType()
	 */
	public String getType() {
		return TYPE;
	}

	protected void setStyle(Attr styleAttribute, FormatAttributeData templateData) {
		String value = createStylePropertyValue();
		if(value!=null) {
			setSingleStyleProperty(styleAttribute, STYLE_PROPERTY_NAME, value);
		}
	}

	private String createStylePropertyValue() {
		if(selectionText!=null && selectionText.trim().length()>0) {
			return (String)SIZES.get(selectionText.trim());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.IFormatController#setToolbarItemEnabled(boolean enabled)
	 */
	public void setToolbarItemEnabled(boolean enabled) {
		comboBlockFormat.setEnabled(enabled);
		if(enabled) {
			Attr style = getStyleAttributeFromSelectedNode(true);
			if(style!=null) {
				StyleAttribute styleAttribute = new StyleAttribute(style);
				StyleProperty fontProperty = styleAttribute.getProperty(STYLE_PROPERTY_NAME);
				if(fontProperty!=null) {
					String fontSize = fontProperty.getFirstSinglePropertyValue();
					if(fontSize!=null) {
						String[] items = this.getComboBlockFormat().getItems();
						for (int i = 0; i < items.length; i++) {
							if(SIZES.get(items[i]).equals(fontSize)) {
								if(this.getComboBlockFormat().getSelectionIndex()!=i) {
									this.getComboBlockFormat().select(i);
								}
								return;
							}
						}
					}
				}
			}
		}
		getComboBlockFormat().deselectAll();
	}
}