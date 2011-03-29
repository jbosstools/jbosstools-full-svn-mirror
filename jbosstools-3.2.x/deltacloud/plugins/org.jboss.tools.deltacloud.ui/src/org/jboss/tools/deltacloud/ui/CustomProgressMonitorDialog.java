/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.ui;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class CustomProgressMonitorDialog extends ProgressMonitorDialog {

	private Button checkbox;
	private boolean checkboxValue;
	
	private String okLabel;
	private String checkboxLabel;
	
	private SelectionListener listener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			checkboxValue = b.getSelection();
		}
	};
	
	public boolean getCheckboxValue() {
		return checkboxValue;
	}
	
	public CustomProgressMonitorDialog(Shell parent, String okLabel,
			String checkboxLabel, boolean checkboxValue) {
		super(parent);
		this.okLabel = okLabel;
		this.checkboxLabel = checkboxLabel;
		this.checkboxValue = checkboxValue;
	}
	
	protected void okPressed() {
		// We need to cheat...we need to cancel the monitoring task
		// so we can successfully close the dialog
		cancelPressed();
		setReturnCode(OK);
	}
	
	protected void createButtonsForButtonBar(Composite parent) {
		createCancelButton(parent);
		createButton(parent, IDialogConstants.OK_ID, okLabel, false);
	}

	protected Control createDialogArea(Composite parent) {
		super.createDialogArea(parent);
		checkbox = new Button(parent, SWT.CHECK);
		checkbox.setText(checkboxLabel);
		checkbox.setSelection(checkboxValue);
		checkbox.addSelectionListener(listener);
		return parent;
	}

}
