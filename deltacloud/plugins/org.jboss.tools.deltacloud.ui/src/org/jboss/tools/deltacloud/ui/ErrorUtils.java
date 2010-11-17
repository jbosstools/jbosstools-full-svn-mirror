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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.log.StatusFactory;
import org.jboss.tools.deltacloud.core.DeltaCloudMultiException;

public class ErrorUtils {
	public static IStatus openErrorDialog(final String title, final String message, Throwable e, final Shell shell) {
		IStatus status = createStatus(e);
		openErrorDialog(title, status, shell);
		return status;
	}

	private static IStatus createStatus(Throwable e) {
		if (e instanceof DeltaCloudMultiException) {
			return createMultiStatus(e.getMessage(), e, ((DeltaCloudMultiException) e).getThrowables());
		} else {
			return StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
		}
	}

	private static IStatus openErrorDialog(final String title, final IStatus status, final Shell shell) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				ErrorDialog.openError(shell, title, status.getMessage(), status);
			}
		});
		return status;
	}

	private static IStatus createMultiStatus(String message, Throwable throwable, Collection<Throwable> throwables) {
		List<IStatus> states = new ArrayList<IStatus>(throwables.size());
		for(Throwable childThrowable : throwables) {
			IStatus status = StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID, childThrowable.getMessage(), childThrowable);
			states.add(status);
		}
		return StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID, message, throwable, states.toArray(new IStatus[states.size()]));
	}
}
