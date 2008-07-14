/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * I had to copy this class from debug.ui because
 * there were no getters or setters provided in the
 * FieldSummary class. 
 * 
 * Assholes. 
 */
package org.jboss.ide.eclipse.archives.ui.dialogs;

import java.io.File;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.IVariableProvider;
import org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspaceVFS;
import org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspaceVariableManager;
import org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspaceVariableManager.DefaultVariableProvider;

public class ArchivesVariableDialog extends Dialog {
	private String title;
	protected Composite panel;
	private String name;
	private String value;
	private boolean canChangeName = true;
	public ArchivesVariableDialog(Shell shell) {
		super(shell);
		this.title = "Archives Variable";
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.name = "";
		this.value = "";
	}

	public ArchivesVariableDialog(Shell shell, String name, String value) {
		this(shell);
		this.name = name;
		this.value = value;
		canChangeName = false;
	}
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
	}

	protected Control createButtonBar(Composite parent) {
		Control bar = super.createButtonBar(parent);
		validateFields();
		return bar;
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite)super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		panel = new Composite(container, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		panel.setLayout(layout);
		panel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		createNameField("Name", name);
		createValueField("Value", value);
		Dialog.applyDialogFont(container);
		return container;
	}

	protected void createNameField(String labelText, String initialValue) { 
		Label label = new Label(panel, SWT.NONE);
		label.setText(labelText);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		
		final Text text = new Text(panel, SWT.SINGLE | SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// make sure rows are the same height on both panels.
		label.setSize(label.getSize().x, text.getSize().y); 
		
		if (initialValue != null) {
			text.setText(initialValue);
		}
		
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				name = text.getText();
				validateFields();
			}
		});
		if( !canChangeName )
			text.setEnabled(false);
	}
	
	protected void createValueField(String labelText, String initialValue) {
		Label label = new Label(panel, SWT.NONE);
		label.setText(labelText);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		
		Composite comp = new Composite(panel, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight=0;
		layout.marginWidth=0;
		comp.setLayout(layout);
		comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		final Text text = new Text(comp, SWT.SINGLE | SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 200;
		text.setLayoutData(data);

		// make sure rows are the same height on both panels.
		label.setSize(label.getSize().x, text.getSize().y); 
		
		if (initialValue != null) {
			text.setText(initialValue);
		}

		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				value=text.getText();
				validateFields();
			}
		});

		Button button = createButton(comp, IDialogConstants.IGNORE_ID, "Browse", false); 
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(getShell());
				String currentWorkingDir = text.getText();
				if (!currentWorkingDir.trim().equals("")) {
					File path = new File(currentWorkingDir);
					if (path.exists()) {
						dialog.setFilterPath(currentWorkingDir);
					}			
				}
				
				String selectedDirectory = dialog.open();
				if (selectedDirectory != null) {
					text.setText(selectedDirectory);
				}		
			}
		});
	}
	
	public void validateFields() {
		boolean valid = true;
		if( "".equals(name)) valid = false;
		if( "".equals(value)) valid = false;
		WorkspaceVariableManager mgr = ((WorkspaceVFS)ArchivesCore.getInstance().getVFS()).getVariableManager();
		IVariableProvider loc = mgr.getVariableLocation(name);
		// already declared
		if( loc != null && DefaultVariableProvider.ID.equals(loc.getId()) && canChangeName)
			valid = false;
		getButton(IDialogConstants.OK_ID).setEnabled(valid);
	}
	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
