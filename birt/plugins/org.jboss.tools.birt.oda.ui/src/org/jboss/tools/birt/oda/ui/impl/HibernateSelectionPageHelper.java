/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 ******************************************************************************/
package org.jboss.tools.birt.oda.ui.impl;

import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.KnownConfigurations;
import org.jboss.tools.birt.oda.IOdaFactory;
import org.jboss.tools.birt.oda.ui.Activator;

/**
 * Helper class for Hibernate wizard and property page
 * 
 * @author snjeza
 * 
 */
public class HibernateSelectionPageHelper {

	private static final String DEFAULT_MAX_ROWS = "100";
	private WizardPage wizardPage;
	private PreferencePage propertyPage;
	private Combo configurationCombo;
	private Text maxRows;
	private Button testButton;

	public HibernateSelectionPageHelper(WizardPage page) {
		this.wizardPage = page;
	}

	public HibernateSelectionPageHelper(PreferencePage page) {
		propertyPage = page;
	}

	void createCustomControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.verticalSpacing = 10;
		layout.marginBottom = 10;
		composite.setLayout(layout);

		GridData gridData;

		Label configurationLabel = new Label(composite, SWT.RIGHT);
		configurationLabel.setText("Configuration:");

		configurationCombo = new Combo(composite, SWT.NONE);
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		configurationCombo.setLayoutData(gridData);

		ConsoleConfiguration[] configurations = KnownConfigurations
				.getInstance().getConfigurations();
		String[] configurationNames = new String[configurations.length];
		for (int i = 0; i < configurations.length; i++) {
			configurationNames[i] = configurations[i].getName();
		}
		configurationCombo.setItems(configurationNames);

		Label maxFetchLabel = new Label(composite, SWT.NONE);
		maxFetchLabel.setText("Max results:");

		maxRows = new Text(composite, SWT.BORDER);
		maxRows.setLayoutData(gridData);

		configurationCombo.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validateData();
			}

		});

		new Label(composite, SWT.NONE);
		testButton = new Button(composite, SWT.PUSH);
		testButton.setText("Test Connection...");

		testButton.setLayoutData(new GridData(GridData.CENTER));
		testButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				testConnection();
			}

		});

		validateData();

	}

	private void setMessage(String message, int type) {
		if (wizardPage != null)
			wizardPage.setMessage(message, type);
		else if (propertyPage != null)
			propertyPage.setMessage(message, type);
	}

	private void setMessage(String message) {
		if (wizardPage != null)
			wizardPage.setMessage(message);
		else if (propertyPage != null)
			propertyPage.setMessage(message);
	}

	private void validateData() {
		if (configurationCombo.getText().trim().length() > 0) {
			setMessage("Edit the selected data source");
			testButton.setEnabled(true);
			setPageComplete(true);
		} else {
			setMessage("Configuration must not be empty",
					IMessageProvider.ERROR);
			testButton.setEnabled(false);
			setPageComplete(false);
		}
	}

	public void initCustomControl(Properties profileProps) {
		if (profileProps != null) {
			String confName = profileProps
					.getProperty(IOdaFactory.CONFIGURATION);
			if (confName != null) {
				configurationCombo.setText(confName);
			}
			String maxResult = profileProps
					.getProperty(IOdaFactory.MAX_ROWS);
			maxRows.setText(maxResult);
		} else {
			if (configurationCombo.getItemCount() > 0) {
				configurationCombo.select(0);
			}
			maxRows.setText(DEFAULT_MAX_ROWS);
		}
	}

	protected void testConnection() {
		String configurationName = configurationCombo.getText();
		ConsoleConfiguration[] configurations = KnownConfigurations
				.getInstance().getConfigurations();
		ConsoleConfiguration consoleConfiguration = null;
		String title = "Test connection";
		for (int i = 0; i < configurations.length; i++) {
			if (configurations[i].getName().equals(configurationName)) {
				consoleConfiguration = configurations[i];
				break;
			}
		}
		if (consoleConfiguration != null) {
			try {
				SessionFactory sessionFactory = consoleConfiguration
						.getSessionFactory();
				if (sessionFactory == null) {
					consoleConfiguration.build();
					consoleConfiguration.buildSessionFactory();
					sessionFactory = consoleConfiguration.getSessionFactory();
				}
				MessageDialog.openInformation(getShell(), title,
						"Connection successfull.");
			} catch (HibernateException e) {
				String message = e.getLocalizedMessage();
				IStatus status = new Status(Status.ERROR, Activator.PLUGIN_ID,
						message, e);
				Activator.getDefault().getLog().log(status);
				ErrorDialog.openError(getShell(), title, message, status);
			}
		} else {
			MessageDialog.openConfirm(getShell(), title,
					"Invalid configuration '" + configurationName + "'.");

		}

	}

	private Shell getShell() {
		if (wizardPage != null)
			return wizardPage.getShell();
		if (propertyPage != null)
			return propertyPage.getShell();
		return null;
	}

	public String getConfiguration() {
		return configurationCombo.getText();
	}

	public String getMaxRows() {
		return maxRows.getText();
	}

	Properties collectCustomProperties(Properties props) {
		if (props == null)
			props = new Properties();

		props
				.setProperty(IOdaFactory.CONFIGURATION,
						getConfiguration());
		props.setProperty(IOdaFactory.MAX_ROWS, getMaxRows());
		return props;
	}

	private void setPageComplete(boolean complete) {
		if (wizardPage != null)
			wizardPage.setPageComplete(complete);
		else if (propertyPage != null)
			propertyPage.setValid(complete);
	}
}
