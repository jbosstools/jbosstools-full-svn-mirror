/*******************************************************************************
 * Copyright (c) 2012 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.preferences;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;

public class VpeColorSelector extends ColorSelector {

	private Text colorText; 
	
	public VpeColorSelector(Composite parent) {
		super(parent);
		colorText = new Text(parent, SWT.NONE);
		colorText.setEditable(false);
		colorText.setTextLimit(9);
	}
	
	@Override
	protected void updateColorImage() {
		super.updateColorImage();
		colorText.setText(VpeStyleUtil.rgbToString(getColorValue()));
	}

	public Text getColorText() {
		return colorText;
	}
	
}
