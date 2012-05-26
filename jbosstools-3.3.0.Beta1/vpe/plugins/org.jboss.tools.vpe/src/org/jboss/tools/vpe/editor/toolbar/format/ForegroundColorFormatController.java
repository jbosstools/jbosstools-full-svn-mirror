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
import org.jboss.tools.vpe.messages.VpeUIMessages;

/**
 * @author Igels
 */
public class ForegroundColorFormatController extends ColorFormatController {

	public static String TYPE = "ForegroundColorFormat"; //$NON-NLS-1$
	public static String COLOR_DIALOG_TITLE = VpeUIMessages.SET_FOREGROUND_COLOR;

	private static String STYLE_PROPERTY_NAME = "COLOR"; //$NON-NLS-1$

	/**
	 * @param manager
	 * @param toolItem
	 */
	public ForegroundColorFormatController(FormatControllerManager manager,	ToolItem toolItem) {
		super(manager, toolItem);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.ColorFormatController#getColorDialogTitle()
	 */
	protected String getColorDialogTitle() {
		return COLOR_DIALOG_TITLE;
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
}