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

import org.eclipse.swt.widgets.Combo;
import org.jboss.tools.vpe.editor.template.textformating.FormatAttributeData;
import org.jboss.tools.vpe.editor.toolbar.format.css.StyleAttribute;
import org.jboss.tools.vpe.editor.toolbar.format.css.StyleProperty;
import org.jboss.tools.vpe.editor.toolbar.format.css.Token;
import org.w3c.dom.Attr;

/**
 * @author Igels
 */
public class FontNameFormatController extends ComboFormatController {

    private static final String REPLACE_VALUE = "['\"]"; //$NON-NLS-1$

    private static final String EMPTY = ""; //$NON-NLS-1$

    public static String TYPE = "FontNameFormat"; //$NON-NLS-1$

    private static String STYLE_PROPERTY_NAME = "FONT-FAMILY"; //$NON-NLS-1$

    private String defaultFont = null;

    /**
     * @param manager
     * @param comboBlockFormat
     */
    public FontNameFormatController(FormatControllerManager manager,
	    Combo comboBlockFormat, String defaultFont) {
	super(manager, comboBlockFormat);
	this.defaultFont = defaultFont;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.tools.vpe.editor.toolbar.format.IFormatController#getType()
     */
    public String getType() {
	return TYPE;
    }

    protected void setStyle(Attr styleAttribute,
	    FormatAttributeData templateData) {
	String value = createStylePropertyValue();
	if (value != null) {
	    String fontName = value.replaceAll(REPLACE_VALUE, EMPTY);
	    if (fontName.equalsIgnoreCase(defaultFont)) {
		StyleAttribute style = new StyleAttribute(styleAttribute);
		style.removeStyleProperty(STYLE_PROPERTY_NAME);
		String newStyle = style.toString().trim();
		styleAttribute.setValue(newStyle);
		return;
	    }
	    setSingleStyleProperty(styleAttribute, STYLE_PROPERTY_NAME, value);
	}
    }

    private String createStylePropertyValue() {
	if (selectionText != null && selectionText.trim().length() > 0) {
	    return "'" + selectionText.trim() + "'"; //$NON-NLS-1$//$NON-NLS-2$
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.tools.vpe.editor.toolbar.format.IFormatController#setToolbarItemEnabled(boolean
     *      enabled)
     */
    public void setToolbarItemEnabled(boolean enabled) {
		comboBlockFormat.setEnabled(enabled);
		if (enabled) {
		    Attr style = getStyleAttributeFromSelectedNode(true);
		    if (style != null) {
				StyleAttribute styleAttribute = new StyleAttribute(style);
				StyleProperty fontProperty
						= styleAttribute.getProperty(STYLE_PROPERTY_NAME);
				if (fontProperty != null) {
					Token fontPropertyValue = fontProperty.getPropertyValue();
				    if (fontPropertyValue != null) {
						String fontName
								= fontPropertyValue.getDirtyValue().trim();
						fontName = fontName.replaceAll(REPLACE_VALUE, EMPTY);
						if (getComboBlockFormat().getText().equalsIgnoreCase(
							fontName)) {
						    return;
						}
						String[] items = this.getComboBlockFormat().getItems();
						for (int i = 0; i < items.length; i++) {
						    if (items[i].equalsIgnoreCase(fontName)) {
								this.getComboBlockFormat().select(i);
								return;
						    }
						}
				    }
				}
		    }
		}
		getComboBlockFormat().select(0);
    }
}
