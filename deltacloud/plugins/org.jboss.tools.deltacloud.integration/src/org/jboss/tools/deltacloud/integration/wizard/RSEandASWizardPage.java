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

import java.util.ArrayList;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.ui.ServerUIUtil;
import org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.job.AbstractInstanceJob;
import org.jboss.tools.deltacloud.integration.DeltaCloudIntegrationPlugin;
import org.jboss.tools.deltacloud.ui.INewInstanceWizardPage;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;
import org.osgi.service.prefs.BackingStoreException;

/**
 * @author Rob Stryker
 */
public class RSEandASWizardPage extends WizardPage implements INewInstanceWizardPage {

	private Button createRSE, createServer;
	private Group serverDetailsGroup;
	private Button autoScanCheck, hardCodeServerDetails, deployOnlyRadio, addLocalRuntimeButton;
	private Text remoteDetailsLoc, serverHomeText, serverConfigText, deployFolderText;
	private Label serverHome, serverConfig, localRuntimeLabel, deployFolder;
	private Combo localRuntimeCombo;
	
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

		Group g = new Group(c2, SWT.SHADOW_IN);
		serverDetailsGroup = g;
		g.setLayout(new FormLayout());
		g.setLayoutData(UIUtils.createFormData(createServer,5,null,0,0,5,100,-5));
		g.setText("Server Details");
		
		final int INDENTATION = 40;
		
		autoScanCheck = new Button(g, SWT.RADIO);
		autoScanCheck.setText("Determine server details from this remote file:");
		autoScanCheck.setLayoutData(UIUtils.createFormData(0,5,null,0,0,5,null,0));
		
		remoteDetailsLoc = new Text(g, SWT.BORDER);
		remoteDetailsLoc.setLayoutData(UIUtils.createFormData(autoScanCheck,5,null,0,0,INDENTATION,100,-5));
		remoteDetailsLoc.setText("/etc/sysconfig/jboss-eap");
		
		
		hardCodeServerDetails = new Button(g, SWT.RADIO);
		hardCodeServerDetails.setText("Set remote server details manually");
		hardCodeServerDetails.setLayoutData(UIUtils.createFormData(remoteDetailsLoc,5,null,0,0,5,null,0));

		serverHome = new Label(g, SWT.NONE);
		serverHome.setText("JBoss Server Home: ");
		serverHome.setLayoutData(UIUtils.createFormData(hardCodeServerDetails, 7, null, 0, 0, INDENTATION, null, 0 ));
		serverHomeText = new Text(g, SWT.BORDER);
		serverHomeText.setLayoutData(UIUtils.createFormData(hardCodeServerDetails, 5, null, 0, serverHome, 5, 100, -5));
		serverHomeText.setText("/etc/jboss/jboss-as");

		serverConfig = new Label(g, SWT.NONE);
		serverConfig.setText("Configuration: ");
		serverConfig.setLayoutData(UIUtils.createFormData(serverHomeText, 7, null, 0, 0, INDENTATION, null, 0 ));
		serverConfigText = new Text(g, SWT.BORDER);
		serverConfigText.setLayoutData(UIUtils.createFormData(serverHomeText, 5, null, 0, serverHome, 5, 100, -5));
		serverConfigText.setText("default");
		
		localRuntimeLabel = new Label(g, SWT.NONE);
		localRuntimeLabel.setText("Local Runtime: ");
		localRuntimeLabel.setLayoutData(UIUtils.createFormData(serverConfigText, 7, null, 0, 0, INDENTATION, null, 0 ));
		
		addLocalRuntimeButton = new Button(g, SWT.DEFAULT);
		addLocalRuntimeButton.setText("Configure Runtimes...");
		addLocalRuntimeButton.setLayoutData(UIUtils.createFormData(serverConfigText, 7, null, 0, null, 0, 100, -5));
		addLocalRuntimeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				configureRuntimesPressed();
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		localRuntimeCombo = new Combo(g, SWT.NONE);
		localRuntimeCombo.setLayoutData(UIUtils.createFormData(serverConfigText, 5, null, 0, serverHome, 5, addLocalRuntimeButton, -5));
		
		
		deployOnlyRadio = new Button(g, SWT.RADIO);
		deployOnlyRadio.setText("Use a deploy-only server adapter");
		deployOnlyRadio.setLayoutData(UIUtils.createFormData(localRuntimeCombo,5,null,0,0,5,null,0));
		
		deployFolder = new Label(g, SWT.NONE);
		deployFolder.setText("Deploy Folder: ");
		deployFolder.setLayoutData(UIUtils.createFormData(deployOnlyRadio, 7, null, 0, 0, INDENTATION, null, 0 ));
		deployFolderText = new Text(g, SWT.BORDER);
		deployFolderText.setText("/path/to/deploy");
		deployFolderText.setLayoutData(UIUtils.createFormData(deployOnlyRadio, 5, null, 0, deployFolder, 5, 100, -5));
		deployOnlyRadio.setSelection(true);
		
		
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
		
		fillRuntimeTypeCombo();
		refreshServerWidgets();
		createRSE.addSelectionListener(listener);
		createServer.addSelectionListener(listener);
		setControl(c2);
	}

	private ArrayList<IRuntime> localRuntimes = new ArrayList<IRuntime>();
	private void fillRuntimeTypeCombo() {
		localRuntimes.clear();
		IRuntime[] rts = ServerCore.getRuntimes();
		ArrayList<String> names = new ArrayList<String>();
		for( int i = 0; i < rts.length; i++ ) {
			if( rts[i].getRuntimeType().getId().startsWith("org.jboss.") 
					&& !rts[i].getRuntimeType().getId().equals(IJBossToolingConstants.DEPLOY_ONLY_RUNTIME)) {
				localRuntimes.add(rts[i]);
				names.add(rts[i].getName());
			}
		}
		localRuntimeCombo.setItems((String[]) names.toArray(new String[names.size()]));
	}
	
	protected void configureRuntimesPressed() {
		ServerUIUtil.showNewRuntimeWizard(addLocalRuntimeButton.getShell(), null, null);
		fillRuntimeTypeCombo();
	}
	
	private void handleSelection(Widget w) {
		if (w == createRSE) {
			if (!createRSE.getSelection()) {
				createServer.setEnabled(false);
				createServer.setSelection(false);
				refreshServerWidgets();
			} else {
				createServer.setEnabled(true);
			}
		}
		if( w == createServer ) {
			refreshServerWidgets();
		}
	}

	private void refreshServerWidgets() {
		boolean enabled = createServer.getSelection();
		boolean enabled2 = false;
		serverDetailsGroup.setEnabled(enabled);
		autoScanCheck.setEnabled(enabled2);
		remoteDetailsLoc.setEnabled(enabled2);
		hardCodeServerDetails.setEnabled(enabled);
		serverHomeText.setEnabled(enabled);
		serverConfigText.setEnabled(enabled);
		serverHome.setEnabled(enabled);
		serverConfig.setEnabled(enabled);
		deployFolder.setEnabled(enabled);
		deployFolderText.setEnabled(enabled);
		deployOnlyRadio.setEnabled(enabled);
		System.out.println(deployFolderText.getEditable());
	}
	
	public AbstractInstanceJob getPerformFinishJob(final DeltaCloudInstance instance) {
		IEclipsePreferences prefs = new InstanceScope().getNode(DeltaCloudIntegrationPlugin.PLUGIN_ID);
		prefs.putBoolean(CREATE_RSE_PREF_KEY, createRSE.getSelection());
		prefs.putBoolean(CREATE_SERVER_PREF_KEY, createServer.getSelection());
		try {
			prefs.flush();
		} catch (BackingStoreException e1) {
			// ignore
		}

		CreateRSEFromInstanceJob j = 
			new CreateRSEFromInstanceJob(instance, INewInstanceWizardPage.NEW_INSTANCE_FAMILY);

		if( createServer.getSelection()) {
			String[] data = null;
			String type = null;
			if( deployOnlyRadio.getSelection()) {
				type = CreateServerFromRSEJob.CREATE_DEPLOY_ONLY_SERVER;
				data = new String[]{deployFolderText.getText()};
			} else if( autoScanCheck.getSelection()) {
				type = CreateServerFromRSEJob.CHECK_SERVER_FOR_DETAILS;
				data = new String[]{remoteDetailsLoc.getText()};
			} else if( hardCodeServerDetails.getSelection()) {
				type = CreateServerFromRSEJob.SET_DETAILS_NOW;
				int index = localRuntimeCombo.getSelectionIndex();
				String rtId = localRuntimes.get(index).getId();
				data = new String[]{serverHomeText.getText(), serverConfigText.getText(), rtId};
			}
			if( type != null && data != null ) {
				CreateServerFromRSEJob job2 = new CreateServerFromRSEJob(type, data, instance.getImageId());
				j.setNextJob(job2);
			}
		}
		return j;
	}
	
}
