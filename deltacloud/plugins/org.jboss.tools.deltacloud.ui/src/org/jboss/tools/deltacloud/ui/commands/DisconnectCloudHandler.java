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
package org.jboss.tools.deltacloud.ui.commands;

import javax.net.ssl.SSLEngineResult.Status;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.ui.views.CloudViewElement;
import org.jboss.tools.deltacloud.ui.views.DisconnectCloudsDialog;

/**
 * @author Andre Dietisheim
 */
public class DisconnectCloudHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			DisconnectCloudsDialog dialog = new DisconnectCloudsDialog(
					shell
					, ((IStructuredSelection) selection).toList());
			if (Dialog.OK == dialog.open()) {
				removeCloudViewElements(dialog.getResult());
			}
		}

		return Status.OK;
	}

	private void removeCloudViewElements(Object[] cloudViewerElements) {
		for (Object cloudViewElement : cloudViewerElements) {
			if (cloudViewElement instanceof CloudViewElement) {
				DeltaCloud deltaCloud = (DeltaCloud) ((CloudViewElement) cloudViewElement).getElement();
				DeltaCloudManager.getDefault().removeCloud(deltaCloud);
			}
		}
	}
}
