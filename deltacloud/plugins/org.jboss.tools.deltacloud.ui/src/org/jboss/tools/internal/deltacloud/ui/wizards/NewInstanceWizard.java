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
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.rse.core.model.IHost;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.core.job.InstanceStateJob;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.ErrorUtils;
import org.jboss.tools.deltacloud.ui.IDeltaCloudPreferenceConstants;
import org.jboss.tools.deltacloud.ui.RSEUtils;
import org.osgi.service.prefs.Preferences;

public class NewInstanceWizard extends Wizard {

	private final static String CREATE_INSTANCE_FAILURE_TITLE = "CreateInstanceError.title"; //$NON-NLS-1$
	private final static String CREATE_INSTANCE_FAILURE_MSG = "CreateInstanceError.msg"; //$NON-NLS-1$
	private final static String CONFIRM_CREATE_TITLE = "ConfirmCreate.title"; //$NON-NLS-1$
	private final static String CONFIRM_CREATE_MSG = "ConfirmCreate.msg"; //$NON-NLS-1$
	private final static String DONT_SHOW_THIS_AGAIN_MSG = "DontShowThisAgain.msg"; //$NON-NLS-1$
	private final static String STARTING_INSTANCE_TITLE = "StartingInstance.title"; //$NON-NLS-1$

	protected NewInstancePage mainPage;
	protected DeltaCloud cloud;
	protected DeltaCloudInstance instance;
	/**
	 * Initial image, may be null
	 */
	private DeltaCloudImage image;

	public NewInstanceWizard(DeltaCloud cloud) {
		this.cloud = cloud;
	}

	public NewInstanceWizard(DeltaCloud cloud, DeltaCloudImage image) {
		this(cloud);
		this.image = image;
	}

	@Override
	public void addPages() {
		mainPage = new NewInstancePage(cloud);
		if (image != null)
			mainPage.setImage(image);
		addPage(mainPage);
	}

	@Override
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}

	private class WatchCreateJob extends InstanceStateJob {

		public WatchCreateJob(DeltaCloudInstance instance) {
			super(WizardMessages.getString(STARTING_INSTANCE_TITLE), instance, DeltaCloudInstance.State.RUNNING);
			setUser(true);
		}

		@Override
		public IStatus doRun(IProgressMonitor monitor) {
			// DeltaCloudInstance instance = null;
			try {
				super.doRun(monitor);
			} catch (Exception e) {
				// do nothing
			} finally {
				// cloud.replaceInstance(instance);
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
						return ErrorUtils.handleError("Error",
								"Could not launch remote system explorer for instance \""
										+ instance.getName() + "\"", e, getShell());
					}
				}
			}
			return Status.OK_STATUS;
		}

		private boolean isAutoconnect() {
			Preferences prefs = new InstanceScope().getNode(Activator.PLUGIN_ID);
			boolean autoConnect = prefs.getBoolean(IDeltaCloudPreferenceConstants.AUTO_CONNECT_INSTANCE, true);
			return autoConnect;
		}
	};

	@Override
	public boolean performFinish() {
		String imageId = mainPage.getImageId().trim();
		String profileId = mainPage.getHardwareProfile();
		String realmId = mainPage.getRealmId();
		String memory = mainPage.getMemoryProperty();
		String storage = mainPage.getStorageProperty();
		String keyname = mainPage.getKeyName();
		String name = getName();

		// Save persistent settings for this particular cloud
		cloud.setLastImageId(imageId);
		cloud.setLastKeyname(keyname);

		Preferences prefs = new InstanceScope().getNode(Activator.PLUGIN_ID);

		boolean result = false;
		Exception e = null;
		try {
			DeltaCloudManager.getDefault().saveClouds();
			boolean dontShowDialog = prefs.getBoolean(IDeltaCloudPreferenceConstants.DONT_CONFIRM_CREATE_INSTANCE,
					false);
			if (!dontShowDialog) {
				MessageDialogWithToggle dialog =
						MessageDialogWithToggle.openOkCancelConfirm(getShell(),
								WizardMessages.getString(CONFIRM_CREATE_TITLE),
								WizardMessages.getString(CONFIRM_CREATE_MSG),
								WizardMessages.getString(DONT_SHOW_THIS_AGAIN_MSG),
								false, null, null);
				int retCode = dialog.getReturnCode();
				boolean toggleState = dialog.getToggleState();
				if (retCode == Dialog.CANCEL)
					return true;
				// If warning turned off by user, set the preference for future
				// usage
				if (toggleState) {
					prefs.putBoolean(IDeltaCloudPreferenceConstants.DONT_CONFIRM_CREATE_INSTANCE, true);
				}
			}
			instance = cloud.createInstance(name, imageId, realmId, profileId, keyname, memory, storage);
			if (instance != null) {
				result = true;
			}
			if (instance != null && instance.getState().equals(DeltaCloudInstance.State.PENDING)) {
				new WatchCreateJob(instance).schedule();
			}
		} catch (DeltaCloudException ex) {
			e = ex;
		}
		if (!result) {
			ErrorUtils.handleError(
					WizardMessages.getString(CREATE_INSTANCE_FAILURE_TITLE), 
					WizardMessages.getFormattedString(CREATE_INSTANCE_FAILURE_MSG, new String[] { name, imageId, realmId, profileId }), 
					e, getShell());
		}
		return result;
	}

	private String getName() {
		try {
			return URLEncoder.encode(mainPage.getInstanceName(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO: implement proper handling
			return "";
		} //$NON-NLS-1$
	}

}
