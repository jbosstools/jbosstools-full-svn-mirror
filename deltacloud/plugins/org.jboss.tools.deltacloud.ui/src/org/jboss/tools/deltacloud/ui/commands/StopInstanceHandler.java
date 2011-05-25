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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.common.log.StatusFactory;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.views.CVMessages;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;
import org.jboss.tools.internal.deltacloud.ui.utils.WorkbenchUtils;
import org.osgi.service.prefs.Preferences;

/**
 * @author Andre Dietisheim
 */
public class StopInstanceHandler extends AbstractInstanceHandler {

	private static final String DONT_CONFIRM_STOP_INSTANCE = "dont_confirm_stop_instance"; //$NON-NLS-1$
	
	private final static String STOPPING_INSTANCE_TITLE = "StoppingInstance.title"; //$NON-NLS-1$
	private final static String STOPPING_INSTANCE_MSG = "StoppingInstance.msg"; //$NON-NLS-1$
	private final static String STOP_INSTANCES_DIALOG_TITLE = "StopInstancesDialog.title"; //$NON-NLS-1$
	private final static String STOP_INSTANCES_DIALOG_MSG = "StopInstancesDialog.msg"; //$NON-NLS-1$

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		try {
			if (selection instanceof IStructuredSelection) {
				if (isSingleInstanceSelected(selection)) {
					DeltaCloudInstance instance = UIUtils.getFirstAdaptedElement(selection, DeltaCloudInstance.class);
					stopInstance(instance);
				} else {
					stopWithDialog((IStructuredSelection) selection);
				}
			}
			return Status.OK_STATUS;
		} catch (Exception e) {
			String errorMessage = CVMessages.getFormattedString("StopInstancesDialogError.msg",
					getInstanceNames(selection));
			return StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID, errorMessage, e);
		}
	}

	@SuppressWarnings("unchecked")
	private void stopWithDialog(IStructuredSelection selection) {
		List<DeltaCloudInstance> deltaCloudInstances = UIUtils.adapt((List<DeltaCloudInstance>) selection.toList(),
				DeltaCloudInstance.class);
		List<DeltaCloudInstance> stoppableInstances = getStoppableInstances(deltaCloudInstances);
		DeltaCloudInstanceDialog dialog = new DeltaCloudInstanceDialog(
					UIUtils.getActiveShell()
					, stoppableInstances
					, CVMessages.getString(STOP_INSTANCES_DIALOG_TITLE)
					, CVMessages.getString(STOP_INSTANCES_DIALOG_MSG));
		dialog.setInitialElementSelections(stoppableInstances);
		if (Dialog.OK == dialog.open()) {
			stopInstances(dialog.getResult());
		}
	}

	private List<DeltaCloudInstance> getStoppableInstances(List<DeltaCloudInstance> deltaCloudInstances) {
		ArrayList<DeltaCloudInstance> stoppedInstances = new ArrayList<DeltaCloudInstance>();
		for (DeltaCloudInstance instance : deltaCloudInstances) {
			if (instance.canStop()) {
				stoppedInstances.add(instance);
			}
		}
		return stoppedInstances;
	}

	private void stopInstances(Object[] deltaCloudInstances) {
		if (askUserToConfirm()) {
			for (int i = 0; i < deltaCloudInstances.length; i++) {
				doStopInstance((DeltaCloudInstance) deltaCloudInstances[i]);
			}
		}
	}

	private void stopInstance(DeltaCloudInstance instance) {
		if (instance != null) {
			if (askUserToConfirm()) {
				doStopInstance(instance);
			}
		}
	}

	private void doStopInstance(DeltaCloudInstance instance) {
		if (instance != null) {
			executeInstanceAction(
						instance
						, DeltaCloudInstance.Action.STOP
						, DeltaCloudInstance.State.STOPPED
						, CVMessages.getString(STOPPING_INSTANCE_TITLE)
						, CVMessages.getFormattedString(STOPPING_INSTANCE_MSG, new String[] { instance.getAlias() }));
		}
	}
	
	private boolean askUserToConfirm() {
		return openConfirmationDialog(
						CVMessages.getString("StopInstancesConfirm.title"),
						CVMessages.getString("StopInstancesConfirm.msg"),
						CVMessages.getString("StopInstancesConfirmDontWarn.msg"),
						DONT_CONFIRM_STOP_INSTANCE,
						Activator.PLUGIN_ID,
						WorkbenchUtils.getActiveShell());
	}

	private static boolean openConfirmationDialog(String title, String message, String dontShowAgainMessage,
			String preferencesKey, String pluginId, Shell shell) {
		boolean confirmed = true;
		Preferences prefs = new InstanceScope().getNode(pluginId);
		boolean dontShowDialog =
				prefs.getBoolean(preferencesKey, false);
		if (!dontShowDialog) {
			MessageDialogWithToggle dialog =
					MessageDialogWithToggle.openOkCancelConfirm(shell, title, message, dontShowAgainMessage,
							false, null, null);
			confirmed = dialog.getReturnCode() == Dialog.OK;
			boolean toggleState = dialog.getToggleState();
			// If warning turned off by user, set the preference for future
			// usage
			if (toggleState) {
				prefs.putBoolean(preferencesKey, true);
			}
		}
		return confirmed;
	}
}
