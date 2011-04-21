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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolItem;
import org.w3c.dom.Attr;

import org.jboss.tools.vpe.editor.template.textformating.FormatAttributeData;

/**
 * @author Igels
 */
abstract public class ColorFormatController extends ToolItemFormatController {

	protected String color;

	/**
	 * @param manager
	 * @param toolItem
	 */
	public ColorFormatController(FormatControllerManager manager, ToolItem toolItem) {
		super(manager, toolItem);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		ColorDialog cd = new ColorDialog(toolItem.getParent().getShell(),
				SWT.APPLICATION_MODAL);
		cd.setText(getColorDialogTitle());
		cd.setRGB(new RGB(255, 255, 255));
		RGB newColor = cd.open();
		if (newColor == null) {
			return;
		}
		StringBuffer buf = new StringBuffer();
		String c = Integer.toHexString(newColor.red);
		if(c.length()<2) {
			buf.append("0"); //$NON-NLS-1$
		}
		buf.append(c);
		c = Integer.toHexString(newColor.green);
		if(c.length()<2) {
			buf.append("0"); //$NON-NLS-1$
		}
		buf.append(c);
		c = Integer.toHexString(newColor.blue);
		if(c.length()<2) {
			buf.append("0"); //$NON-NLS-1$
		}
		buf.append(c);
		color = buf.toString();
		super.handleEvent(event);
	}

	abstract protected String getColorDialogTitle();

	protected String createStylePropertyValue() {
		if(color!=null && color.trim().length()>0) {
			return "#" + color; //$NON-NLS-1$
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.IFormatController#setToolbarItemSelection()
	 */
	public void setToolbarItemSelection() {
		// Do nothing
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.AttributeFormatController#isStyleSet(org.w3c.dom.Attr, org.jboss.tools.vpe.editor.template.textformating.FormatAttributeData)
	 */
	protected boolean isStyleSet(Attr attribute, FormatAttributeData templateData) {
		// Do nothing
		return false;
	}
}