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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.jboss.tools.common.model.ui.widgets.IWidgetSettings;

public class TableStructuredEditor extends StructuredEditor {
	
	public static final String ADD_ACTION = "ADD_ACTION"; //$NON-NLS-1$
	public static final String REMOVE_ACTION = "REMOVE_ACTION"; //$NON-NLS-1$
	public static final String EDIT_ACTION = "EDIT_ACTION"; //$NON-NLS-1$
	public static final String UP_ACTION = "UP_ACTION"; //$NON-NLS-1$
	public static final String DOWN_ACTION = "DOWN_ACTION"; //$NON-NLS-1$
	public static final String DOUBLE_CLICK__ACTION = "DOUBLE_CLICK__ACTION"; //$NON-NLS-1$
	

	protected DialogCellEditorEx cellEditor;
	protected TableStructuredFieldEditor fieldEditor;

	public TableStructuredEditor() {}

	public TableStructuredEditor(IWidgetSettings settings) {
		super(settings);
	}

	public void dispose() {
		super.dispose();
		if (cellEditor!=null) cellEditor.dispose();
		cellEditor = null;
		if (fieldEditor!=null) fieldEditor.dispose();
		fieldEditor = null;
	}

	protected CellEditor createCellEditor(Composite parent) {
		cellEditor = new DialogCellEditorEx(parent, SWT.NONE);
		cellEditor.setPropertyEditor(this);
		return cellEditor;
	}
	
	protected ExtendedFieldEditor createFieldEditor(Composite parent) {
		fieldEditor = new TableStructuredFieldEditor(settings);
		fieldEditor.setPropertyEditor(this);
		return fieldEditor;
	}

	public boolean isGreedyEditor() {
		return true;
	}

}
