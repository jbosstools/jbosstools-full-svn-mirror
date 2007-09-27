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
package org.jboss.tools.common.model.ui.attribute.editor;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

import org.jboss.tools.common.model.ui.widgets.IWidgetSettings;

public class UneditableEditor extends StringEditor {

	public UneditableEditor() {
	}
	
	public UneditableEditor(IWidgetSettings settings) {
		super(settings);
	}

	protected CellEditor createCellEditor(Composite parent) {
		return null;
	}

	protected ExtendedFieldEditor createFieldEditor(Composite parent) {
		return (fieldEditor = new UneditableFieldEditor(settings));
	}
}
