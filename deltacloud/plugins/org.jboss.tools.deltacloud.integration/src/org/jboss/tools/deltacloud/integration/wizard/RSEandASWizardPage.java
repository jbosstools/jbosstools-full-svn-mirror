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
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
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
import org.jboss.tools.common.jobs.ChainedJob;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.integration.DeltaCloudIntegrationPlugin;
import org.jboss.tools.deltacloud.ui.preferences.StringPreferenceValue;
import org.jboss.tools.deltacloud.ui.wizard.INewInstanceWizardPage;
import org.jboss.tools.internal.deltacloud.ui.utils.LayoutUtils;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;
import org.osgi.service.prefs.BackingStoreException;

/**
 * @author Rob Stryker
 * @author André Dietisheim
 */
public class RSEandASWizardPage extends WizardPage implements INewInstanceWizardPage {
	private static final String SERVER_DETAILS_PROPOSAL_KEY = "server_details";
	private static final String SERVER_HOME_PROPOSAL_KEY = "server_home";
	private static final String SERVER_CONFIG_PROPOSAL_KEY = "server_config";
	private static final String SERVER_DEPLOY_PROPOSAL_KEY = "server_deploy";

	private static final String SELECTED_AUTO_LOCAL_RUNTIME_KEY = "autoruntime_selected";
	private static final String SELECTED_MANUAL_LOCAL_RUNTIME_KEY = "manualruntime_selected";

	private static final String SELECT_RUNTIME_ERROR = "Please select a local runtime. The created server will be of the same version as the local runtime.";
	private static final String DEPLOY_FOLDER_NOT_EMPTY = "The deploy folder must not be empty";
	private final static String REMOTE_DETAILS_LOC_ERROR = "You must fill in a path to fetch the server configuration from";
	private static final String SERVER_CONFIG_ERROR = "You must select a server configuration";
	private static final String SERVER_HOME_ERROR = "You must set a server home directory";

	private final static String CREATE_RSE_PREF_KEY = "org.jboss.tools.deltacloud.integration.wizard.RSEandASWizard.CREATE_RSE_PREF_KEY";
	private final static String CREATE_SERVER_PREF_KEY = "org.jboss.tools.deltacloud.integration.wizard.RSEandASWizard.CREATE_SERVER_PREF_KEY";

	private static final boolean DEFALUT_CREATE_RSE = true;
	private static final boolean DETAULT_CREATE_SERVERADAPTER = false;

	private Button createRSE, createServer;
	private Group serverDetailsGroup;
	private Button autoScanCheck, hardCodeServerDetails, deployOnlyRadio,
					addLocalRuntimeButton, autoAddLocalRuntimeButton;
	private ControlDecoration deployFolderDeco, autoLocalRuntimeDeco, serverHomeDeco, remoteDetailsLocDeco,
			localRuntimeDeco, serverConfigDeco;
	private Text remoteDetailsLoc, serverHomeText, serverConfigText, deployFolderText;
	private Label serverHome, serverConfig, localRuntimeLabel, autoLocalRuntimeLabel, deployFolder;
	private Combo autoLocalRuntimeCombo, localRuntimeCombo;
	private StringPreferenceValue selectedAutoRuntimePref = new StringPreferenceValue(SELECTED_AUTO_LOCAL_RUNTIME_KEY, DeltaCloudIntegrationPlugin.PLUGIN_ID);
	private StringPreferenceValue selectedManualRuntimePref = new StringPreferenceValue(SELECTED_MANUAL_LOCAL_RUNTIME_KEY, DeltaCloudIntegrationPlugin.PLUGIN_ID);
	
	private IHost initialHost;
	private ArrayList<IRuntime> localRuntimes = new ArrayList<IRuntime>();

	public RSEandASWizardPage() {
		super("Create RSE Connection and Server");
		setTitle("Create RSE Connection and Server");
		setDescription("Here you can choose to create a matching RSE connection and a Server adapter");
	}

	public RSEandASWizardPage(IHost host) {
		this();
		this.initialHost = host;
	}

	public void createControl(Composite parent) {
		Composite c2 = new Composite(parent, SWT.NONE);
		c2.setLayout(new FormLayout());
		createRSE = new Button(c2, SWT.CHECK);
		createRSE.setText("Create RSE Connection");
		createRSE.setLayoutData(LayoutUtils.createFormData(0, 5, null, 0, 0, 5, 100, -5));
		createServer = new Button(c2, SWT.CHECK);
		createServer.setText("Create Server Adapter");
		createServer.setLayoutData(LayoutUtils.createFormData(createRSE, 5, null, 0, 0, 5, 100, -5));

		Group g = new Group(c2, SWT.SHADOW_IN);
		serverDetailsGroup = g;
		g.setLayout(new FormLayout());
		g.setLayoutData(LayoutUtils.createFormData(createServer, 5, null, 0, 0, 5, 100, -5));
		g.setText("Server Details");

		final int INDENTATION = 40;

		autoScanCheck = new Button(g, SWT.RADIO);
		autoScanCheck.setText("Determine server details from this remote file:");
		autoScanCheck.setLayoutData(LayoutUtils.createFormData(0, 5, null, 0, 0, 5, null, 0));
		autoScanCheck.setSelection(true);

		remoteDetailsLoc = new Text(g, SWT.BORDER);
		remoteDetailsLoc.setLayoutData(LayoutUtils.createFormData(autoScanCheck, 5, null, 0, 0, INDENTATION, 100, -5));
		remoteDetailsLoc.setText("./.jboss");
		this.remoteDetailsLocDeco = UIUtils.createErrorDecoration(REMOTE_DETAILS_LOC_ERROR, remoteDetailsLoc);
		UIUtils.createPreferencesProposalAdapter(remoteDetailsLoc, SERVER_DETAILS_PROPOSAL_KEY);

		autoLocalRuntimeLabel = new Label(g, SWT.NONE);
		autoLocalRuntimeLabel.setText("Local Runtime: ");
		autoLocalRuntimeLabel.setLayoutData(
				LayoutUtils.createFormData(remoteDetailsLoc, 7, null, 0, 0, INDENTATION, null,0));

		autoAddLocalRuntimeButton = new Button(g, SWT.DEFAULT);
		autoAddLocalRuntimeButton.setText("Configure Runtimes...");
		autoAddLocalRuntimeButton.setLayoutData(LayoutUtils.createFormData(remoteDetailsLoc, 7, null, 0, null, 0, 100, -5));
		autoAddLocalRuntimeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				configureRuntimesPressed();
			}
		});
		autoLocalRuntimeCombo = new Combo(g, SWT.READ_ONLY);
		autoLocalRuntimeCombo.setLayoutData(LayoutUtils.createFormData(remoteDetailsLoc, 5, null, 0, autoLocalRuntimeLabel,
				10, autoAddLocalRuntimeButton, -5));
		this.autoLocalRuntimeDeco = UIUtils.createErrorDecoration(SELECT_RUNTIME_ERROR, autoLocalRuntimeCombo);

		hardCodeServerDetails = new Button(g, SWT.RADIO);
		hardCodeServerDetails.setText("Set remote server details manually");
		hardCodeServerDetails.setLayoutData(LayoutUtils.createFormData(autoLocalRuntimeCombo, 5, null, 0, 0, 10, null, 0));

		serverHome = new Label(g, SWT.NONE);
		serverHome.setText("JBoss Server Home: ");
		serverHome.setLayoutData(LayoutUtils.createFormData(hardCodeServerDetails, 7, null, 0, 0, INDENTATION, null, 0));
		serverHomeText = new Text(g, SWT.BORDER);
		serverHomeText.setLayoutData(LayoutUtils.createFormData(hardCodeServerDetails, 5, null, 0, serverHome, 10, 100, -5));
		serverHomeText.setText("/etc/jboss/jboss-as");
		this.serverHomeDeco = UIUtils.createErrorDecoration(SERVER_HOME_ERROR, serverHomeText);
		UIUtils.createPreferencesProposalAdapter(serverHomeText, SERVER_HOME_PROPOSAL_KEY);

		serverConfig = new Label(g, SWT.NONE);
		serverConfig.setText("Configuration: ");
		serverConfig.setLayoutData(LayoutUtils.createFormData(serverHomeText, 7, null, 0, 0, INDENTATION, null, 0));
		serverConfigText = new Text(g, SWT.BORDER);
		serverConfigText.setLayoutData(LayoutUtils.createFormData(serverHomeText, 5, null, 0, serverHome, 10, 100, -5));
		serverConfigText.setText("default");
		this.serverConfigDeco = UIUtils.createErrorDecoration(SERVER_CONFIG_ERROR, serverConfigText);
		UIUtils.createPreferencesProposalAdapter(serverConfigText, SERVER_CONFIG_PROPOSAL_KEY);

		localRuntimeLabel = new Label(g, SWT.NONE);
		localRuntimeLabel.setText("Local Runtime: ");
		localRuntimeLabel.setLayoutData(LayoutUtils.createFormData(serverConfigText, 7, null, 0, 0, INDENTATION, null, 0));

		addLocalRuntimeButton = new Button(g, SWT.DEFAULT);
		addLocalRuntimeButton.setText("Configure Runtimes...");
		addLocalRuntimeButton.setLayoutData(LayoutUtils.createFormData(serverConfigText, 7, null, 0, null, 0, 100, -5));
		addLocalRuntimeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				configureRuntimesPressed();
			}
		});
		localRuntimeCombo = new Combo(g, SWT.READ_ONLY);
		localRuntimeCombo.setLayoutData(LayoutUtils.createFormData(serverConfigText, 5, null, 0, serverHome, 10,
				addLocalRuntimeButton, -5));
		this.localRuntimeDeco = UIUtils.createErrorDecoration(SELECT_RUNTIME_ERROR, localRuntimeCombo);

		deployOnlyRadio = new Button(g, SWT.RADIO);
		deployOnlyRadio.setText("Use a deploy-only server adapter");
		deployOnlyRadio.setLayoutData(LayoutUtils.createFormData(localRuntimeCombo, 5, null, 0, 0, 5, null, 0));

		deployFolder = new Label(g, SWT.NONE);
		deployFolder.setText("Deploy Folder: ");
		deployFolder.setLayoutData(LayoutUtils.createFormData(deployOnlyRadio, 7, null, 0, 0, INDENTATION, null, 0));
		deployFolderText = new Text(g, SWT.BORDER);
		deployFolderText.setText("/path/to/deploy");
		deployFolderText.setLayoutData(LayoutUtils.createFormData(deployOnlyRadio, 5, null, 0, deployFolder, 10, 100, -5));
		this.deployFolderDeco = UIUtils.createErrorDecoration(DEPLOY_FOLDER_NOT_EMPTY, deployFolderText);
		UIUtils.createPreferencesProposalAdapter(deployFolderText, SERVER_DEPLOY_PROPOSAL_KEY);

		Label dummyLabel = new Label(g, SWT.NONE);
		dummyLabel.setLayoutData(LayoutUtils.createFormData(deployOnlyRadio, 5, null, 0, deployFolderText, 10, 100, -5));
		
		IEclipsePreferences prefs = new InstanceScope().getNode(DeltaCloudIntegrationPlugin.PLUGIN_ID);
		boolean initRSE, initServer;
		initRSE = prefs.getBoolean(CREATE_RSE_PREF_KEY, DEFALUT_CREATE_RSE);
		initServer = prefs.getBoolean(CREATE_SERVER_PREF_KEY, DETAULT_CREATE_SERVERADAPTER);
		createRSE.setSelection(initRSE);
		createServer.setSelection(initServer);
		if (initialHost != null) {
			createRSE.setEnabled(false);
			createRSE.setSelection(true);
			createServer.setSelection(true);
		}

		SelectionListener listener = new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				handleSelection(e.widget);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				handleSelection(e.widget);
			}
		};
		ModifyListener modListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				verifyPageComplete();
			}
		};
		fillRuntimeTypeCombo();
		refreshServerWidgets();
		verifyPageComplete();
		createRSE.addSelectionListener(listener);
		createServer.addSelectionListener(listener);
		autoScanCheck.addSelectionListener(listener);
		hardCodeServerDetails.addSelectionListener(listener);
		deployOnlyRadio.addSelectionListener(listener);
		remoteDetailsLoc.addModifyListener(modListener);
		serverHomeText.addModifyListener(modListener);
		serverConfigText.addModifyListener(modListener);
		deployFolderText.addModifyListener(modListener);
		autoLocalRuntimeCombo.addModifyListener(modListener);
		localRuntimeCombo.addModifyListener(modListener);
		setControl(c2);
	}

	private void fillRuntimeTypeCombo() {
		localRuntimes.clear();
		IRuntime[] rts = ServerCore.getRuntimes();
		List<String> names = new ArrayList<String>();
		for (int i = 0; i < rts.length; i++) {
			if (rts[i].getRuntimeType() == null)
				continue;
			if (rts[i].getRuntimeType().getId().startsWith("org.jboss.")
					&& !rts[i].getRuntimeType().getId().equals(IJBossToolingConstants.DEPLOY_ONLY_RUNTIME)) {
				localRuntimes.add(rts[i]);
				names.add(rts[i].getName());
			}
		}
		localRuntimeCombo.setItems((String[]) names.toArray(new String[names.size()]));
		localRuntimeCombo.select(getSelectionIndex(selectedManualRuntimePref.get(), names));
		autoLocalRuntimeCombo.setItems((String[]) names.toArray(new String[names.size()]));
		autoLocalRuntimeCombo.select(getSelectionIndex(selectedAutoRuntimePref.get(), names));
	}
	
	private int getSelectionIndex(String item, List<String> items) {
		int selectionIndex = 0;
		int listIndex = items.indexOf(item);
		if (listIndex >= 0) {
			selectionIndex = listIndex;
		}
		return selectionIndex;
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
		if (w == createServer) {
			refreshServerWidgets();
		}
		verifyPageComplete();
	}

	private void verifyPageComplete() {
		String error = null;
		deployFolderDeco.hide();
		autoLocalRuntimeDeco.hide();
		remoteDetailsLocDeco.hide();
		localRuntimeDeco.hide();
		serverHomeDeco.hide();
		serverConfigDeco.hide();
		
		if (createServer.getSelection()) {
			if (deployOnlyRadio.getSelection()) {
				if (deployFolderText.getText().equals("")) {
					error = DEPLOY_FOLDER_NOT_EMPTY;
					deployFolderDeco.show();
				}
			} else if (autoScanCheck.getSelection()) {
				if (remoteDetailsLoc.getText().equals("")) {
					remoteDetailsLocDeco.show();
					error = REMOTE_DETAILS_LOC_ERROR;
				}
				if (autoLocalRuntimeCombo.getSelectionIndex() < 0) {
					autoLocalRuntimeDeco.show();
					error = SELECT_RUNTIME_ERROR;
				}
			} else if (hardCodeServerDetails.getSelection()) {
				if (serverHomeText.getText().equals("")) {
					serverHomeDeco.show();
					error = SERVER_HOME_ERROR;
				}
				if (serverConfigText.getText().equals("")) {
					serverConfigDeco.show();
					error = SERVER_CONFIG_ERROR;
				}
				if (localRuntimeCombo.getSelectionIndex() < 0) {
					localRuntimeDeco.show();
					error = SELECT_RUNTIME_ERROR;
				}
			}
		}
		storeSelection(autoLocalRuntimeCombo.getSelectionIndex(), autoLocalRuntimeCombo.getItems(), selectedAutoRuntimePref);
		storeSelection(localRuntimeCombo.getSelectionIndex(), localRuntimeCombo.getItems(), selectedManualRuntimePref);

		setErrorMessage(error);
		setPageComplete(error == null);
	}

	private void storeSelection(int selectionIndex, String[] items, StringPreferenceValue preferenceValue) {
		if (selectionIndex < 0 || items == null || items.length == 0) {
			return;
		}

		String value = items[selectionIndex];
		preferenceValue.store(value);
	}

	private void refreshServerWidgets() {
		if (initialHost != null) {
			createRSE.setEnabled(false);
		}

		boolean enabled = createServer.getSelection();
		serverDetailsGroup.setEnabled(enabled);
		autoScanCheck.setEnabled(enabled);
		autoLocalRuntimeLabel.setEnabled(enabled);
		autoLocalRuntimeCombo.setEnabled(enabled);
		remoteDetailsLoc.setEnabled(enabled);
		hardCodeServerDetails.setEnabled(enabled);
		serverHomeText.setEnabled(enabled);
		serverConfigText.setEnabled(enabled);
		serverHome.setEnabled(enabled);
		serverConfig.setEnabled(enabled);
		deployFolder.setEnabled(enabled);
		deployFolderText.setEnabled(enabled);
		deployOnlyRadio.setEnabled(enabled);
		localRuntimeLabel.setEnabled(enabled);
		localRuntimeCombo.setEnabled(enabled);
	}

	public ChainedJob createPerformFinishJob(final DeltaCloudInstance instance) {
		IEclipsePreferences prefs = new InstanceScope().getNode(DeltaCloudIntegrationPlugin.PLUGIN_ID);
		prefs.putBoolean(CREATE_RSE_PREF_KEY, createRSE.getSelection());
		prefs.putBoolean(CREATE_SERVER_PREF_KEY, createServer.getSelection());
		try {
			prefs.flush();
		} catch (BackingStoreException e1) {
			// ignore
		}

		if (!createRSE.getSelection())
			return null;

		CreateRSEFromInstanceJob j =
				new CreateRSEFromInstanceJob(instance, INewInstanceWizardPage.NEW_INSTANCE_FAMILY);

		if (createServer.getSelection()) {
			String[] data = null;
			String type = null;
			if (deployOnlyRadio.getSelection()) {
				type = CreateServerFromRSEJob.CREATE_DEPLOY_ONLY_SERVER;
				data = new String[] { deployFolderText.getText() };
			} else if (autoScanCheck.getSelection()) {
				type = CreateServerFromRSEJob.CHECK_SERVER_FOR_DETAILS;
				int index = autoLocalRuntimeCombo.getSelectionIndex();
				String rtId = localRuntimes.get(index).getId();
				data = new String[] { remoteDetailsLoc.getText(), rtId };
			} else if (hardCodeServerDetails.getSelection()) {
				type = CreateServerFromRSEJob.SET_DETAILS_NOW;
				int index = localRuntimeCombo.getSelectionIndex();
				String rtId = localRuntimes.get(index).getId();
				data = new String[] { serverHomeText.getText(), serverConfigText.getText(), rtId };
			}
			if (type != null && data != null) {
				CreateServerFromRSEJob job2 = new CreateServerFromRSEJob(type, data, instance);
				if (initialHost != null) {
					job2.setHost(initialHost);
					return job2;
				}
				j.setNextJob(job2);
			}
		}
		return j;
	}

}
