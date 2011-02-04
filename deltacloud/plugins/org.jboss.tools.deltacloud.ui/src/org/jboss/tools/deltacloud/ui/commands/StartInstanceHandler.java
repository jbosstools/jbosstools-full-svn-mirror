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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.common.log.StatusFactory;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.views.CVMessages;
import org.jboss.tools.internal.deltacloud.ui.utils.WorkbenchUtils;

/**
 * @author Andre Dietisheim
 */
public class StartInstanceHandler extends AbstractInstanceHandler {

	private final static String STARTING_INSTANCE_TITLE = "StartingInstance.title"; //$NON-NLS-1$
	private final static String STARTING_INSTANCE_MSG = "StartingInstance.msg"; //$NON-NLS-1$
	private final static String START_INSTANCES_DIALOG_TITLE = "StartInstancesDialog.title"; //$NON-NLS-1$
	private final static String START_INSTANCES_DIALOG_MSG = "StartInstancesDialog.msg"; //$NON-NLS-1$

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		try {
			if (selection instanceof IStructuredSelection) {
				if (isSingleInstanceSelected(selection)) {
					DeltaCloudInstance cvinstance = WorkbenchUtils.getFirstAdaptedElement(selection, DeltaCloudInstance.class);
					startInstance(cvinstance);
				} else {
					startWithDialog((IStructuredSelection) selection);
				}
			}

			return Status.OK_STATUS;
		} catch (Exception e) {
			String errorMessage = CVMessages.getFormattedString("StartInstancesDialogError.msg",
					getInstanceNames(selection));
			return StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID, errorMessage, e);
		}
	}

	@SuppressWarnings("unchecked")
	private void startWithDialog(IStructuredSelection selection) {
		List<DeltaCloudInstance> deltaCloudInstances =  WorkbenchUtils.adapt((List<DeltaCloudInstance>) selection.toList(), DeltaCloudInstance.class);
		List<DeltaCloudInstance> stoppedInstances = getStoppedInstances(deltaCloudInstances);
		DeltaCloudInstanceDialog dialog = new DeltaCloudInstanceDialog(
				WorkbenchUtils.getActiveShell()
					, stoppedInstances
					, CVMessages.getString(START_INSTANCES_DIALOG_TITLE)
					, CVMessages.getString(START_INSTANCES_DIALOG_MSG));
		dialog.setInitialElementSelections(stoppedInstances);
		if (Dialog.OK == dialog.open()) {
			startInstances(dialog.getResult());
		}
	}

	private List<DeltaCloudInstance> getStoppedInstances(List<DeltaCloudInstance> deltaCloudInstances) {
		ArrayList<DeltaCloudInstance> stoppedInstances = new ArrayList<DeltaCloudInstance>();
		for (DeltaCloudInstance instance : deltaCloudInstances) {
			if (instance.isStopped()) {
				stoppedInstances.add(instance);
			}
		}
		return stoppedInstances;
	}

	private void startInstances(Object[] deltaCloudInstances) {
		for (int i = 0; i < deltaCloudInstances.length; i++) {
			Assert.isTrue(deltaCloudInstances[i] instanceof DeltaCloudInstance);
			startInstance((DeltaCloudInstance) deltaCloudInstances[i]);
		}
	}

	private void startInstance(DeltaCloudInstance instance) {
		if (instance != null) {
			executeInstanceAction(
					instance
					, DeltaCloudInstance.Action.START
					, DeltaCloudInstance.State.RUNNING
					, CVMessages.getString(STARTING_INSTANCE_TITLE)
					, CVMessages.getFormattedString(STARTING_INSTANCE_MSG, new String[] { instance.getName() }));
		}
	}
}
