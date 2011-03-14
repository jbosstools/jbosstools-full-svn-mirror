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

import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.osgi.util.NLS;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.services.clientserver.messages.SystemOperationFailedException;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFileSubSystem;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.job.AbstractInstanceJob;
import org.jboss.tools.deltacloud.integration.DeltaCloudIntegrationPlugin;
import org.jboss.tools.deltacloud.integration.Messages;
import org.jboss.tools.deltacloud.integration.rse.util.RSEUtils;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.IDeltaCloudPreferenceConstants;
import org.osgi.service.prefs.Preferences;

import com.jcraft.jsch.JSchException;

public class CreateRSEFromInstanceJob extends AbstractInstanceJob {
	/** the timeout for trying to connect to the new instance */
	private static final long CONNECT_TIMEOUT = 3 * 60 * 1000;

	private Job nextJob2 = null;

	public CreateRSEFromInstanceJob(DeltaCloudInstance instance, String family) {
		super(MessageFormat.format("Create RSE {0}", instance.getAlias()), instance, family);
	}

	public IStatus doRun(IProgressMonitor monitor) throws CoreException {
		return runRSEJob(getInstance(), monitor);
	}

	public void setNextJob(Job job) {
		super.setNextJob(job);
		this.nextJob2 = job;
	}

	private IStatus runRSEJob(DeltaCloudInstance instance, IProgressMonitor monitor) throws CoreException {
		if (monitor.isCanceled()) {
			return Status.CANCEL_STATUS;
		}

		String hostname = RSEUtils.createRSEHostName(instance);
		if (hostname == null || hostname.length() <= 0) {
			return Status.CANCEL_STATUS;
		}

		if (isAutoconnect()) {
			try {
				monitor.beginTask(MessageFormat.format("Create RSE to server {0}", hostname), 100);
				String connectionName = RSEUtils.createConnectionName(instance);
				IHost host = RSEUtils.createHost(
						"jboss",
						connectionName,
						RSEUtils.createRSEHostName(instance),
						RSEUtils.getSSHOnlySystemType(),
						RSEUtils.getSystemRegistry());
				if (nextJob2 != null && nextJob2 instanceof CreateServerFromRSEJob) {
					((CreateServerFromRSEJob) nextJob2).setHost(host);
				}
				monitor.worked(10);
				IStatus credentials =
						triggerCredentialsDialog(host, new SubProgressMonitor(monitor, 10));
				if (credentials.isOK())
					return RSEUtils.connect(RSEUtils.getConnectorService(host), CONNECT_TIMEOUT,
							new SubProgressMonitor(monitor, 80));
				return credentials;
			} catch (Exception e) {
				throw new CoreException(
						new Status(IStatus.ERROR, DeltaCloudIntegrationPlugin.PLUGIN_ID,
								NLS.bind(Messages.COULD_NOT_LAUNCH_RSE_EXPLORER2, instance.getName())));
			}
		}
		return Status.OK_STATUS;
	}

	private IStatus triggerCredentialsDialog(IHost host, IProgressMonitor monitor) {
		try {
			monitor.setTaskName(MessageFormat.format("Initiating connection to {0}...", host.getName()));
			IRemoteFileSubSystem system = RSEUtils.findRemoteFileSubSystem(host);
			system.connect(monitor, true /* force credentials dialog */);
		} catch (Exception e) {
			if (e instanceof OperationCanceledException)
				return Status.CANCEL_STATUS;
			if (e instanceof SystemOperationFailedException) {
				Exception f = ((SystemOperationFailedException) e).getRemoteException();
				if (f != null && f instanceof JSchException) {
					// User selected No on accept hostkey
					if (f.getMessage().contains("reject HostKey:"))
						return Status.CANCEL_STATUS;
				}
			}
		} finally {
			monitor.done();
		}
		return Status.OK_STATUS;
	}

	private boolean isAutoconnect() {
		Preferences prefs = new InstanceScope().getNode(Activator.PLUGIN_ID);
		boolean autoConnect = prefs.getBoolean(IDeltaCloudPreferenceConstants.AUTO_CONNECT_INSTANCE, true);
		return autoConnect;
	}
}
