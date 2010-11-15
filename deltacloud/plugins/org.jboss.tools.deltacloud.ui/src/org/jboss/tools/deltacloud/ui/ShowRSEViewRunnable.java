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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.common.log.StatusFactory;

public class ShowRSEViewRunnable implements Runnable {

	private static final String VIEW_REMOTESYSEXPLORER_ID = "org.eclipse.rse.ui.view.systemView";

	private String instanceName;

	public ShowRSEViewRunnable(String instanceName) {
		this.instanceName = instanceName;
	}

	@Override
	public void run() {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage()
						.showView(VIEW_REMOTESYSEXPLORER_ID);
		} catch (PartInitException e) {
			IStatus status = StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
			// TODO: internationalize strings
			ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						"Error",
						"Could not launch remote system explorer for instance \"" + instanceName + "\"",
					status);
		}
	}
}
