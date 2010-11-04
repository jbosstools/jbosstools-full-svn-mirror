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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.Status;
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
					, getSelectedClouds(selection));
			if (Dialog.OK == dialog.open()) {
				removeCloudViewElements(dialog.getResult());
			}
		}

		return Status.OK_STATUS;
	}

	private Collection<DeltaCloud> getSelectedClouds(ISelection selection) {
		Set<DeltaCloud> selectedClouds = new HashSet<DeltaCloud>();
		List<?> selectedElements = ((IStructuredSelection) selection).toList();
		for (Object element : selectedElements) {
			DeltaCloud deltaCloud = getDeltaCloud(element);
			if (deltaCloud != null) {
				selectedClouds.add(deltaCloud);
			}
		}
		return selectedClouds;
	}

	private DeltaCloud getDeltaCloud(Object item) {
		if (!(item instanceof CloudViewElement)) {
			return null;
		}

		DeltaCloud cloud = getDeltaCloud((CloudViewElement) item);

		if (cloud == null) {
			return null;
		}
		return cloud;
	}

	private DeltaCloud getDeltaCloud(CloudViewElement element) {
		if (element == null) {
			return null;
		}
		Object cloud = element.getElement();
		if (cloud instanceof DeltaCloud) {
			return (DeltaCloud) cloud;
		}

		return getDeltaCloud((CloudViewElement) element.getParent());
	}

	private void removeCloudViewElements(Object[] deltaClouds) {
		for (Object deltaCloud : deltaClouds) {
			DeltaCloudManager.getDefault().removeCloud((DeltaCloud) deltaCloud);
		}
	}
}
