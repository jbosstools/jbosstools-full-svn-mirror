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
package org.jboss.tools.deltacloud.integration.wizard;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.job.AbstractInstanceJob;
import org.jboss.tools.deltacloud.integration.DeltaCloudIntegrationPlugin;
import org.jboss.tools.deltacloud.integration.Messages;
import org.jboss.tools.deltacloud.integration.rse.util.RSEUtils;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.ErrorUtils;
import org.jboss.tools.deltacloud.ui.IDeltaCloudPreferenceConstants;
import org.jboss.tools.deltacloud.ui.INewInstanceWizardPage;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * @author Rob Stryker
 */
public class RSEandASWizardPage extends WizardPage implements INewInstanceWizardPage {

	private Button createRSE;
	private Button createServer;
	private final static String CREATE_RSE_PREF_KEY = "org.jboss.tools.deltacloud.integration.wizard.RSEandASWizard.CREATE_RSE_PREF_KEY";
	private final static String CREATE_SERVER_PREF_KEY = "org.jboss.tools.deltacloud.integration.wizard.RSEandASWizard.CREATE_SERVER_PREF_KEY";

	public RSEandASWizardPage() {
		super("Create RSE Connection and Server");
		setTitle("Create RSE Connection and Server");
		setDescription("Here you can choose to create a matching RSE connection and a Server adapter");
	}

	public void createControl(Composite parent) {
		Composite c2 = new Composite(parent, SWT.NONE);
		c2.setLayout(new FormLayout());
		createRSE = new Button(c2, SWT.CHECK);
		createRSE.setText("Create RSE Connection");
		createRSE.setLayoutData(UIUtils.createFormData(0, 5, null, 0, 0, 5, 100, -5));
		createServer = new Button(c2, SWT.CHECK);
		createServer.setText("Create Server Adapter");
		createServer.setLayoutData(UIUtils.createFormData(createRSE, 5, null, 0, 0, 5, 100, -5));

		IEclipsePreferences prefs = new InstanceScope().getNode(DeltaCloudIntegrationPlugin.PLUGIN_ID);
		boolean initRSE, initServer;
		initRSE = prefs.getBoolean(CREATE_RSE_PREF_KEY, true);
		initServer = prefs.getBoolean(CREATE_SERVER_PREF_KEY, true);
		createRSE.setSelection(initRSE);
		createServer.setSelection(initServer);

		SelectionListener listener = new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				handleSelection(e.widget);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				handleSelection(e.widget);
			}
		};

		createRSE.addSelectionListener(listener);
		createServer.addSelectionListener(listener);
		setControl(c2);
	}

	private void handleSelection(Widget w) {
		if (w == createRSE) {
			if (!createRSE.getSelection()) {
				createServer.setEnabled(false);
				createServer.setSelection(false);
			} else {
				createServer.setEnabled(true);
			}
		}
	}

	public AbstractInstanceJob getPerformFinishJob(final DeltaCloudInstance instance) {
		System.out.println(instance.getHostName());
		IEclipsePreferences prefs = new InstanceScope().getNode(DeltaCloudIntegrationPlugin.PLUGIN_ID);
		prefs.putBoolean(CREATE_RSE_PREF_KEY, createRSE.getSelection());
		prefs.putBoolean(CREATE_SERVER_PREF_KEY, createServer.getSelection());
		try {
			prefs.flush();
		} catch (BackingStoreException e1) {
			// ignore
		}

		AbstractInstanceJob j = new AbstractInstanceJob(
				"Register RSE Connection", instance, INewInstanceWizardPage.NEW_INSTANCE_FAMILY) {
			public IStatus doRun(IProgressMonitor monitor) {
				return runJob(instance, monitor);
			}
		};
		return j;
	}

	private IStatus runJob(DeltaCloudInstance instance, IProgressMonitor monitor) {
		String hostname = RSEUtils.createHostName(instance);
		if (hostname != null && hostname.length() > 0 && isAutoconnect()) {
			try {
				String connectionName = RSEUtils.createConnectionName(instance);
				IHost host = RSEUtils.createHost(connectionName,
						RSEUtils.createHostName(instance),
						RSEUtils.getSSHOnlySystemType(),
						RSEUtils.getSystemRegistry());
				RSEUtils.connect(connectionName, RSEUtils.getConnectorService(host));
			} catch (Exception e) {
				return ErrorUtils.handleError(Messages.ERROR,
						NLS.bind(Messages.COULD_NOT_LAUNCH_RSE_EXPLORER2, instance.getName()),
						e, getShell());
			}
		}
		return Status.OK_STATUS;
	}

	private boolean isAutoconnect() {
		Preferences prefs = new InstanceScope().getNode(Activator.PLUGIN_ID);
		boolean autoConnect = prefs.getBoolean(IDeltaCloudPreferenceConstants.AUTO_CONNECT_INSTANCE, true);
		return autoConnect;
	}

}
