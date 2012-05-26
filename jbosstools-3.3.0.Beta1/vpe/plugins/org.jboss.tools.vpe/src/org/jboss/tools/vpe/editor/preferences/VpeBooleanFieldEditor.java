/*******************************************************************************
 * Copyright (c) 2007-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class VpeBooleanFieldEditor extends BooleanFieldEditor {


	public VpeBooleanFieldEditor() {
		super();
	}

	public VpeBooleanFieldEditor(String name, String label, Composite parent) {
		super(name, label, parent);
	}

	public VpeBooleanFieldEditor(String name, String labelText, int style,
			Composite parent) {
		super(name, labelText, style, parent);
	}

	@Override
	protected void createControl(Composite parent) {
		GridLayout layout = new GridLayout();
        layout.numColumns = getNumberOfControls();
        layout.marginWidth = 5;
        layout.marginHeight = 5;
        layout.horizontalSpacing = HORIZONTAL_GAP;
        parent.setLayout(layout);
        doFillIntoGrid(parent, layout.numColumns);
	}
	
}
