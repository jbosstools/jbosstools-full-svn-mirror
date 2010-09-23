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
public class BulletsFormatController extends ToolItemFormatController {

	public static String TYPE = "BulletsFormat"; //$NON-NLS-1$

	/**
	 * @param manager
	 * @param toolItem
	 */
	public BulletsFormatController(FormatControllerManager manager, ToolItem toolItem) {
		super(manager, toolItem);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.IFormatController#getType()
	 */
	public String getType() {
		return TYPE;
	}

	protected void setStyle(Attr styleAttribute, FormatAttributeData templateData) {
		// TODO
	}

	public void setToolbarItemSelection() {
		// TODO
	}

	protected boolean isStyleSet(Attr attribute, FormatAttributeData templateData) {
		// TODO
		return false;
	}
}