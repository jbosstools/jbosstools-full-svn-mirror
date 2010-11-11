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

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.ui.views.CVMessages;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

/**
 * @author Andre Dietisheim
 */
public class RebootInstanceHandler extends AbstractInstanceHandler {

	private final static String REBOOTING_INSTANCE_TITLE = "RebootingInstance.title"; //$NON-NLS-1$
	private final static String REBOOTING_INSTANCE_MSG = "RebootingInstance.msg"; //$NON-NLS-1$

	private final static String REBOOT_INSTANCE_TITLE = "RebootInstancesDialog.title"; //$NON-NLS-1$
	private final static String REBOOT_INSTANCE_MSG = "RebootInstancesDialog.msg"; //$NON-NLS-1$

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			if (isSingleInstanceSelected(selection)) {
				DeltaCloudInstance cvInstance = UIUtils.getFirstElement(selection, DeltaCloudInstance.class);
				rebootInstance(cvInstance);
			} else {
				rebootWithDialog((IStructuredSelection)selection);
			}
		}

		return Status.OK_STATUS;
	}

	@SuppressWarnings("unchecked")
	private void rebootWithDialog(IStructuredSelection selection) {
		DeltaCloudInstanceDialog dialog = new DeltaCloudInstanceDialog(
					UIUtils.getActiveShell()
					, (List<DeltaCloudInstance>) selection.toList()
					, CVMessages.getString(REBOOT_INSTANCE_TITLE)
					, CVMessages.getString(REBOOT_INSTANCE_MSG));
		if (Dialog.OK == dialog.open()) {
			rebootInstances(dialog.getResult());
		}
	}
	
	private void rebootInstances(Object[] deltaCloudInstances) {
		for (int i = 0; i < deltaCloudInstances.length; i++) {
			rebootInstance((DeltaCloudInstance) deltaCloudInstances[i]);
		}
	}
	
	private void rebootInstance(DeltaCloudInstance instance) {
		if (instance != null) {
			executeInstanceAction(
					instance
					, DeltaCloudInstance.REBOOT
					, DeltaCloudInstance.RUNNING
					, CVMessages.getString(REBOOTING_INSTANCE_TITLE)
					, CVMessages.getFormattedString(REBOOTING_INSTANCE_MSG, new String[] { instance.getName() }));
		}
	}
}
