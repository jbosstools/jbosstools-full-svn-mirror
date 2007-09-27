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

/**
 * @author igels
 */
abstract public class ToolItemFormatController extends AttributeFormatController {

	protected ToolItem toolItem;

	/**
	 * @param manager
	 * @param toolItem
	 */
	public ToolItemFormatController(FormatControllerManager manager, ToolItem toolItem) {
		super(manager);
		this.toolItem = toolItem;
		setToolbarItemEnabled(false);
	}

	/**
	 * @see org.jboss.tools.vpe.editor.toolbar.format.IFormatController#setToolbarItemEnabled(boolean enabled)
	 */
	public void setToolbarItemEnabled(boolean enabled) {
		toolItem.setEnabled(enabled);
		if(enabled) {
			setToolbarItemSelection();
		} else {
			setToolbarItemSelection(false);
		}
	}

	public void setToolbarItemSelection(boolean selected) {
		toolItem.setSelection(selected);
	}
}