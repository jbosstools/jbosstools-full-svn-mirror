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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.log.StatusFactory;

public class ErrorUtils {
	public static IStatus openErrorDialog(final String title, final String message, Exception e, final Shell shell) {
		final IStatus status = StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
		// TODO: internationalize strings
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				ErrorDialog.openError(shell, title, message, status);
			}
		});
		return status;
	}
}
