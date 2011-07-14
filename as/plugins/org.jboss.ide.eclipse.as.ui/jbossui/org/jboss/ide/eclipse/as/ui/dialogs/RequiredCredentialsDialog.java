/******************************************************************************* 
* Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.ide.eclipse.as.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.as.core.server.internal.JBossServer;
import org.jboss.ide.eclipse.as.ui.Messages;
import org.jboss.ide.eclipse.as.ui.UIUtil;

/**
 * @author rob.stryker <rob.stryker@redhat.com>
 * 
 */
public class RequiredCredentialsDialog extends Dialog {
	public static final int IGNORE_ID = IDialogConstants.CLIENT_ID | 3;
	private String user, pass;
	private boolean save;
	private JBossServer jbs;
	
	public RequiredCredentialsDialog(Shell parentShell, JBossServer jbs) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.jbs = jbs;
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.RequiredCredentialsDialog_ShellText);
	}

	protected Control createDialogArea(Composite parent) {
		Composite c = (Composite) super.createDialogArea(parent);
		Composite main = new Composite(c, SWT.NONE);
		main.setLayoutData(new GridData(GridData.FILL_BOTH));
		main.setLayout(new FormLayout());

		// make widgets
		Label top = new Label(main, SWT.NONE);
		Label userLabel = new Label(main, SWT.NONE);
		Label passLabel = new Label(main, SWT.NONE);
		final Text userText = new Text(main, SWT.DEFAULT);
		final Text passText = new Text(main, SWT.DEFAULT);
		userText.setEditable(true);
		passText.setEditable(true);
		final Button saveCredentials = new Button(main, SWT.CHECK);
		
		UIUtil u = new UIUtil();
		top.setLayoutData(u.createFormData(0,5,null,0,0,5,100,-5));
		userLabel.setLayoutData(u.createFormData(top, 10, null, 0, 0,5, 100, -5));
		userText.setLayoutData(u.createFormData(userLabel, 5, null, 0, 0,5, 100, -5));
		passLabel.setLayoutData(u.createFormData(userText, 10, null, 0, 0,5, 100, -5));
		passText.setLayoutData(u.createFormData(passLabel, 5, null, 0, 0,5, 100, -5));
		saveCredentials.setLayoutData(u.createFormData(passText, 10, null, 0, 0,5, 100, -5));
		
		top.setText(Messages.credentials_warning);
		userLabel.setText(Messages.swf_Username);
		passLabel.setText(Messages.swf_Password);
		saveCredentials.setText(Messages.credentials_save);
		
		ModifyListener listener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				user = userText.getText();
				pass = passText.getText();
				save = saveCredentials.getSelection();
			}
		};
		SelectionListener listener2 = new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			public void widgetSelected(SelectionEvent e) {
				user = userText.getText();
				pass = passText.getText();
				save = saveCredentials.getSelection();
			}
		};
		userText.addModifyListener(listener);
		passText.addModifyListener(listener);
		saveCredentials.addSelectionListener(listener2);
		
		// defaults
		userText.setText(jbs.getUsername());
		userText.setSelection(0, jbs.getUsername() == null ? 0 : jbs.getUsername().length());
		passText.setText(jbs.getPassword());
		// save by default
		saveCredentials.setSelection(true);
		return c;
	}
	
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.RequiredCredentialsDialog_IgnoreButton, false);
	}

	
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}
	
	/**
	 * @return whether to save
	 */
	public boolean getSave() {
		return save;
	}
}
