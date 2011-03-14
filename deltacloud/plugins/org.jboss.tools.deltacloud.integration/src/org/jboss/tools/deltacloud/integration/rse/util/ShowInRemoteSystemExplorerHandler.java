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
package org.jboss.tools.deltacloud.integration.rse.util;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.integration.DeltaCloudIntegrationPlugin;
import org.jboss.tools.deltacloud.integration.Messages;
import org.jboss.tools.internal.deltacloud.ui.utils.WorkbenchUtils;

/**
 * @author Andre Dietisheim
 */
public class ShowInRemoteSystemExplorerHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			DeltaCloudInstance instance = WorkbenchUtils.getFirstAdaptedElement(selection, DeltaCloudInstance.class);
			try {
				String connectionName = RSEUtils.createConnectionName(instance);
				IHost host = RSEUtils.createHost(
						"jboss",
						connectionName, 
						RSEUtils.createRSEHostName(instance),
						RSEUtils.getSSHOnlySystemType(), 
						RSEUtils.getSystemRegistry());
				Job connectJob = RSEUtils.connect(connectionName, RSEUtils.getConnectorService(host));
				RSEUtils.showRemoteSystemExplorer(connectJob);
			} catch (Exception e) {
				return new Status(IStatus.ERROR, DeltaCloudIntegrationPlugin.PLUGIN_ID,
						Messages.COULD_NOT_LAUNCH_RSE_EXPLORER2, e);
			}
		}

		return Status.OK_STATUS;
	}
}
