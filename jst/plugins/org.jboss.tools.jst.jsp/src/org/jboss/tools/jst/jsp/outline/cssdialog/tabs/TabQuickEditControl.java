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
package org.jboss.tools.jst.jsp.outline.cssdialog.tabs;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.StyleAttributes;

/**
 * Class for creating control in Quick edit tab
 * 
 * @author Evgeny Zheleznyakov
 */
public class TabQuickEditControl extends BaseTabControl {

	/**
	 * Constructor for creating controls
	 * 
	 * @param composite
	 *            The parent composite for tab
	 * @param comboMap
	 * @param styleAttributes
	 *            the StyleAttributes object
	 */
	public TabQuickEditControl(Composite sc, StyleAttributes styleAttributes,
			DataBindingContext bindingContext) {
		super(bindingContext, styleAttributes, sc, SWT.NONE);

		addContent();
	}

	/**
	 * Update data method.
	 */
	protected void updateData() {

		Control[] controls = this.getChildren();
		if (controls != null) {
			for (int i = 0; i < controls.length; i++) {
				if (!controls[i].isDisposed()) {
					controls[i].dispose();
				}
			}
		}

		addContent();
		this.layout();
	}

	/**
	 * Initialize method.s
	 */
	private void addContent() {

		ArrayList<String> listKeys = new ArrayList<String>(getStyleAttributes()
				.keySet());

		if (listKeys.size() == 0) {
			Label label = new Label(this, SWT.CENTER);
			label.setText(JstUIMessages.CSS_NO_EDITED_PROPERTIES);
		}

		Collections.sort(listKeys);

		for (String key : listKeys) {

			String value = getStyleAttributes().getAttribute(key);
			if (value != null && value.length() > 0) {
				addLabel(this, getLabel(key));
				createControl(this, key);
			}
		}
	}

	@Override
	public void tabSelected() {
		updateData();
	}

	private String getLabel(String key) {
		return key;
	}

}