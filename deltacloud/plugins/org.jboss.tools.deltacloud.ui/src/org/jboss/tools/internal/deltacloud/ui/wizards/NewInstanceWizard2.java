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
package org.jboss.tools.internal.deltacloud.ui.wizards;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.rse.core.IRSECoreRegistry;
import org.eclipse.rse.core.IRSESystemType;
import org.eclipse.rse.core.RSECorePlugin;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.core.model.SystemStartHere;
import org.eclipse.rse.core.subsystems.IConnectorService;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.IDeltaCloudPreferenceConstants;
import org.jboss.tools.deltacloud.ui.views.CVMessages;
import org.osgi.service.prefs.Preferences;

public class NewInstanceWizard2 extends Wizard {

	private final static String CREATE_INSTANCE_FAILURE_TITLE = "CreateInstanceError.title"; //$NON-NLS-1$
	private final static String CREATE_INSTANCE_FAILURE_MSG = "CreateInstanceError.msg"; //$NON-NLS-1$
	private final static String DEFAULT_REASON = "CreateInstanceErrorReason.msg"; //$NON-NLS-1$
	private final static String CONFIRM_CREATE_TITLE = "ConfirmCreate.title"; //$NON-NLS-1$
	private final static String CONFIRM_CREATE_MSG = "ConfirmCreate.msg"; //$NON-NLS-1$
	private final static String DONT_SHOW_THIS_AGAIN_MSG = "DontShowThisAgain.msg"; //$NON-NLS-1$
	private final static String STARTING_INSTANCE_MSG = "StartingInstance.msg"; //$NON-NLS-1$
	private final static String STARTING_INSTANCE_TITLE = "StartingInstance.title"; //$NON-NLS-1$
	private final static String RSE_CONNECTING_MSG = "ConnectingRSE.msg"; //$NON-NLS-1$

	
	private NewInstancePage2 mainPage;
	
	private DeltaCloud cloud;
	private DeltaCloudInstance instance;

	public NewInstanceWizard2(DeltaCloud cloud) {
		this.cloud = cloud;
	}
	
	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		mainPage = new NewInstancePage2(cloud);
		addPage(mainPage);
	}
	
	@Override
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}
	
	
	private class WatchCreateJob extends Job {
		
		private DeltaCloud cloud;
		private String instanceId;
		private String instanceName;
		
		public WatchCreateJob(String title, DeltaCloud cloud, 
				String instanceId, String instanceName) {
			super(title);
			this.cloud = cloud;
			this.instanceId = instanceId;
			this.instanceName = instanceName;
		}
		
		public IStatus run(IProgressMonitor pm) {
			if (!pm.isCanceled()){
				DeltaCloudInstance instance = null;
				try {
					pm.beginTask(WizardMessages.getFormattedString(STARTING_INSTANCE_MSG, new String[] {instanceName}), IProgressMonitor.UNKNOWN);
					pm.worked(1);
					cloud.registerActionJob(instanceId, this);
					boolean finished = false;
					while (!finished && !pm.isCanceled()) {
						instance = cloud.refreshInstance(instanceId);
						if (instance != null && !instance.getState().equals(DeltaCloudInstance.PENDING))
							break;
						Thread.sleep(400);
					}

				} catch (Exception e) {
					// do nothing
				} finally {
					cloud.replaceInstance(instance);
					cloud.removeActionJob(instanceId, this);
					String hostname = instance.getHostName();
					Preferences prefs = new InstanceScope().getNode(Activator.PLUGIN_ID);
					boolean autoConnect = prefs.getBoolean(IDeltaCloudPreferenceConstants.AUTO_CONNECT_INSTANCE, true);
					if (hostname != null && hostname.length() > 0 && autoConnect) {
						ISystemRegistry registry = SystemStartHere.getSystemRegistry();
						RSECorePlugin rsep = RSECorePlugin.getDefault();
						IRSECoreRegistry coreRegistry = rsep.getCoreRegistry();
						IRSESystemType[] sysTypes = coreRegistry.getSystemTypes();
						IRSESystemType sshType = null;			
						for (IRSESystemType sysType : sysTypes) {
							if (sysType.getId().equals(IRSESystemType.SYSTEMTYPE_SSH_ONLY_ID))
								sshType = sysType;
						}
						String connectionName = instance.getName() + " [" + instance.getId() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
						try {
							IHost host = registry.createHost(sshType, connectionName, hostname, null);
							if (host != null) {
								host.setDefaultUserId("root"); //$NON-NLS-1$
								IConnectorService[] services = host.getConnectorServices();
								if (services.length > 0) {
									final IConnectorService service = services[0];
									Job connect = new Job(CVMessages.getFormattedString(RSE_CONNECTING_MSG, connectionName)) {
										@Override
										protected IStatus run(IProgressMonitor monitor) {
											try {
												service.connect(monitor);
												return Status.OK_STATUS;
											} catch(Exception e) {
												return Status.CANCEL_STATUS;
											}
										}
									};
									connect.setUser(true);
									connect.schedule();
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							Activator.log(e);
						}
					}
					pm.done();
				}
				return Status.OK_STATUS;
			}
			else {
				pm.done();
				return Status.CANCEL_STATUS;
			}
		};
	};
	
	@Override
	public boolean performFinish() {
		String imageId = mainPage.getImageId();
		String profileId = mainPage.getHardwareProfile();
		String realmId = mainPage.getRealmId();
		String memory = mainPage.getMemoryProperty();
		String storage = mainPage.getStorageProperty();
		String keyname = mainPage.getKeyName();
		String name = null;
		
		// Save persistent settings for this particular cloud
		cloud.setLastImageId(imageId);
		cloud.setLastKeyname(keyname);
		cloud.save();

		Preferences prefs = new InstanceScope().getNode(Activator.PLUGIN_ID);
		
		try {
			name = URLEncoder.encode(mainPage.getInstanceName(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //$NON-NLS-1$
		
		boolean result = false;
		String errorMessage = WizardMessages.getString(DEFAULT_REASON);
		try {
			boolean dontShowDialog = prefs.getBoolean(IDeltaCloudPreferenceConstants.DONT_CONFIRM_CREATE_INSTANCE, false);
			if (!dontShowDialog) {
				MessageDialogWithToggle dialog =
					MessageDialogWithToggle.openOkCancelConfirm(getShell(), WizardMessages.getString(CONFIRM_CREATE_TITLE), 
							WizardMessages.getString(CONFIRM_CREATE_MSG), 
							WizardMessages.getString(DONT_SHOW_THIS_AGAIN_MSG), 
							false, null, null);
				int retCode = dialog.getReturnCode();
				boolean toggleState = dialog.getToggleState();
				if (retCode == Dialog.CANCEL)
					return true;
				// If warning turned off by user, set the preference for future usage
				if (toggleState) {
					prefs.putBoolean(IDeltaCloudPreferenceConstants.DONT_CONFIRM_CREATE_INSTANCE, true);
				}
			}
			instance = cloud.createInstance(name, imageId, realmId, profileId, keyname, memory, storage);
			if (instance != null)
				result = true;
			if (instance != null && instance.getState().equals(DeltaCloudInstance.PENDING)) {
				final String instanceId = instance.getId();
				final String instanceName = name;
				Job job = new WatchCreateJob(WizardMessages.getString(STARTING_INSTANCE_TITLE),
						cloud, instanceId, instanceName);
				job.setUser(true);
				job.schedule();
			}
		} catch (DeltaCloudException e) {
			errorMessage = e.getLocalizedMessage();
		}
		if (!result) {
			ErrorDialog.openError(this.getShell(),
					WizardMessages.getString(CREATE_INSTANCE_FAILURE_TITLE),
					WizardMessages.getFormattedString(CREATE_INSTANCE_FAILURE_MSG, new String[] {name, imageId, realmId, profileId}),
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, errorMessage));
		}
		return result;
	}

}
