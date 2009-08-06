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
import org.eclipse.swt.widgets.Event;
import org.w3c.dom.Attr;

import org.jboss.tools.vpe.editor.template.textformating.FormatAttributeData;

/**
 * @author Igels
 */
abstract public class ComboFormatController extends AttributeFormatController {

	protected Combo comboBlockFormat;
	protected int selectionIndex;
	protected String selectionText;

	public ComboFormatController(FormatControllerManager manager, Combo comboBlockFormat) {
		super(manager);
		this.comboBlockFormat = comboBlockFormat;
		setToolbarItemEnabled(false);
	}

	public Combo getComboBlockFormat() {
		return comboBlockFormat;
	}

	public void handleEvent(Event event) {
		selectionIndex = ((Combo)event.widget).getSelectionIndex();
		selectionText = ((Combo)event.widget).getText();
		super.handleEvent(event);
    }

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.IFormatController#setToolbarItemEnabled(boolean enabled)
	 */
	public void setToolbarItemEnabled(boolean enabled) {
		comboBlockFormat.setEnabled(enabled);
		setToolbarItemSelection(manager.didControllerNotifySelectionChange());
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.AttributeFormatController#setToolbarItemSelection(boolean)
	 */
	protected void setToolbarItemSelection(boolean selected) {
		if(!selected) {
			comboBlockFormat.deselectAll();
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.AttributeFormatController#isStyleSet(org.w3c.dom.Attr, org.jboss.tools.vpe.editor.template.textformating.FormatAttributeData)
	 */
	protected boolean isStyleSet(Attr attribute, FormatAttributeData templateData) {
		return false;
	}

	/**
	 * @return Returns the selectionIndex.
	 */
	public int getSelectionIndex() {
		return selectionIndex;
	}

	/**
	 * @return Returns the selectionText.
	 */
	public String getSelectionText() {
		return selectionText;
	}
}