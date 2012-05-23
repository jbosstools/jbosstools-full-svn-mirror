/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.express.internal.ui.job;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.ui.DelegatingProgressMonitor;
import org.jboss.tools.openshift.express.internal.ui.OpenShiftUIActivator;
import org.jboss.tools.openshift.express.internal.ui.messages.OpenShiftExpressUIMessages;
import org.jboss.tools.openshift.express.internal.ui.utils.Logger;

import com.openshift.client.IApplication;
import com.openshift.client.OpenShiftException;
import com.openshift.client.OpenShiftTimeoutException;

/**
 * @author Andre Dietisheim
 */
public class WaitForApplicationJob extends AbstractDelegatingMonitorJob {

//	private static final int APP_REACHABLE_TIMEOUT = 180 * 1000;
	private static final int APP_REACHABLE_TIMEOUT = 1 * 1000;
	private IApplication application;
	private Shell shell;

	public WaitForApplicationJob(IApplication application, Shell shell) {
		super(NLS.bind(OpenShiftExpressUIMessages.WAITING_FOR_REACHABLE, application.getName()));
		this.shell = shell;
		this.application = application;
	}

	@Override
	protected IStatus doRun(DelegatingProgressMonitor monitor) {
		Logger.debug(OpenShiftExpressUIMessages.WAITING_FOR_REACHABLE);
		try {
			while (!application.waitForAccessible(APP_REACHABLE_TIMEOUT)) {
				if (!openKeepWaitingDialog()) {
					return OpenShiftUIActivator.createCancelStatus(NLS.bind(
							OpenShiftExpressUIMessages.APPLICATION_NOT_ANSWERING, application.getName()));
				}
			}
		} catch (OpenShiftException e) {
			return OpenShiftUIActivator.createErrorStatus(NLS.bind(
					"Could not wait for application \"{0}\" to become reachable", application.getName()), e);
		}
		return Status.OK_STATUS;
	}

	protected boolean openKeepWaitingDialog() {
		final AtomicBoolean keepWaiting = new AtomicBoolean(false);
		shell.getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				MessageDialog dialog =
						new MessageDialog(shell
								, NLS.bind("Waiting for application {0}", application.getName())
								, shell.getDisplay().getSystemImage(SWT.ICON_QUESTION)
								, NLS.bind(OpenShiftExpressUIMessages.APPLICATION_NOT_ANSWERING_CONTINUE_WAITING,
										application.getName())
								, MessageDialog.QUESTION
								, new String[] { OpenShiftExpressUIMessages.BTN_KEEP_WAITING,
										OpenShiftExpressUIMessages.BTN_CLOSE_WIZARD }
								, MessageDialog.QUESTION);
				// style &= SWT.SHEET;
				// dialog.setShellStyle(dialog.getShellStyle() | style);
				keepWaiting.set(dialog.open() == IDialogConstants.OK_ID);
			}
		});
		return keepWaiting.get();
	}
}
