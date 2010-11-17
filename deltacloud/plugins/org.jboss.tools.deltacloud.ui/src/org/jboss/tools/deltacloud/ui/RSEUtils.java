/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.deltacloud.ui;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.rse.core.IRSECoreRegistry;
import org.eclipse.rse.core.IRSESystemType;
import org.eclipse.rse.core.RSECorePlugin;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.core.model.SystemStartHere;
import org.eclipse.rse.core.subsystems.IConnectorService;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.common.log.StatusFactory;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.ui.commands.AbstractCloudJob;
import org.jboss.tools.deltacloud.ui.views.CVMessages;

public class RSEUtils {

	private final static String RSE_CONNECTING_MSG = "ConnectingRSE.msg"; //$NON-NLS-1$

	public static IRSESystemType getSSHOnlySystemType() {
		IRSESystemType sshType = null;
		RSECorePlugin rsep = RSECorePlugin.getDefault();
		IRSECoreRegistry coreRegistry = rsep.getCoreRegistry();
		IRSESystemType[] sysTypes = coreRegistry.getSystemTypes();
		for (IRSESystemType sysType : sysTypes) {
			if (sysType.getId().equals(IRSESystemType.SYSTEMTYPE_SSH_ONLY_ID))
				sshType = sysType;
		}
		return sshType;
	}

	public static ISystemRegistry getSystemRegistry() {
		return SystemStartHere.getSystemRegistry();
	}

	public static String createConnectionName(DeltaCloudInstance instance) {
		return instance.getName() + " [" + instance.getId() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static String createHostName(DeltaCloudInstance instance) {
		if (instance == null) {
			return null;
		}
		return instance.getHostName();
	}

	public static IHost createHost(String connectionName, String hostname) throws Exception {
		ISystemRegistry registry = getSystemRegistry();
		IRSESystemType sshOnlySystemType = getSSHOnlySystemType();
		// TODO: Internationalize string
		Assert.isTrue(sshOnlySystemType != null,
				"Remote System Explorer could not initialize SSH subsystem: ssh type not found");
		IHost host = registry.createHost(sshOnlySystemType, connectionName, hostname, null);
		host.setDefaultUserId("root"); //$NON-NLS-1$
		return host;
	}

	public static IConnectorService getConnectorService(IHost host) {
		IConnectorService[] services = host.getConnectorServices();
		if (services == null || services.length <= 0) {
			return null;
		}
		return services[0];
	}

	public static void connect(final String instanceName, final IConnectorService service, String connectionName) {
		// TODO: internationalize strings
		Assert.isLegal(service != null, "Remote System Explorer could not connect: connector service not found.");
		Job connect = new AbstractCloudJob(CVMessages.getFormattedString(RSE_CONNECTING_MSG, connectionName)) {
			@Override
			protected IStatus doRun(IProgressMonitor monitor) {
				try {
					monitor.worked(1);
					service.connect(monitor);
					Display.getDefault().asyncExec(new ShowRSEViewRunnable(instanceName));
					monitor.done();
					return Status.OK_STATUS;
				} catch (Exception e) {
					return StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
				}
			}
		};
		connect.setUser(true);
		connect.schedule();
	}

	public static void launchRemoteSystemExplorer(String instanceName, String connectionName, IHost host) throws Exception {
		if (host != null) {
			IConnectorService service = RSEUtils.getConnectorService(host);
			RSEUtils.connect(instanceName, service, connectionName);
		} else {
			// Assume failure is due to name already in use
			Display.getDefault().asyncExec(new ShowRSEViewRunnable(instanceName));
		}
	}
}
