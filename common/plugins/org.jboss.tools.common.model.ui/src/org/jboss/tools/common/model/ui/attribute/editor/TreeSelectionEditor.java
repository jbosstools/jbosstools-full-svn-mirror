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

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.jboss.tools.common.model.ui.widgets.IWidgetSettings;

public class TreeSelectionEditor extends SelectionEditor {

	protected DialogCellEditorEx cellEditor;
	protected TreeSelectionFieldEditor fieldEditor;
	
	public TreeSelectionEditor() {}

	public TreeSelectionEditor(IWidgetSettings settings) {
		super(settings);
	}

	public void dispose() {
		super.dispose();
		if (cellEditor!=null) cellEditor.dispose();
		cellEditor = null;
		if (fieldEditor!=null) fieldEditor.dispose();
		fieldEditor = null;
	}

	public boolean isGreedyEditor() {
		return true;
	}

	protected CellEditor createCellEditor(Composite parent) {
		cellEditor = new DialogCellEditorEx(parent, SWT.NONE);
		cellEditor.setPropertyEditor(this);
		return cellEditor;
	}

	protected ExtendedFieldEditor createFieldEditor(Composite parent) {
		fieldEditor = new TreeSelectionFieldEditor(settings);
		return fieldEditor;
	}
	
	public String getChangeButtonName() {
		return JFaceResources.getString("openBrowse");
	}

}
