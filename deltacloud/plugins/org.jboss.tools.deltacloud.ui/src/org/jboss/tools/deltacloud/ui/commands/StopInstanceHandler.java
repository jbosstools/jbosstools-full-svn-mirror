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
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.ui.views.CVCloudElement;
import org.jboss.tools.deltacloud.ui.views.CVInstanceElement;
import org.jboss.tools.deltacloud.ui.views.CVMessages;
import org.jboss.tools.deltacloud.ui.views.CloudViewElement;
import org.jboss.tools.deltacloud.ui.views.PerformInstanceActionThread;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

/**
 * @author Andre Dietisheim
 */
public class StopInstanceHandler extends AbstractHandler implements IHandler {

	private final static String STOPPING_INSTANCE_TITLE = "StoppingInstance.title"; //$NON-NLS-1$
	private final static String STOPPING_INSTANCE_MSG = "StoppingInstance.msg"; //$NON-NLS-1$
	private final static String STOP_INSTANCES_DIALOG_TITLE = "StopInstancesDialog.title"; //$NON-NLS-1$
	private final static String STOP_INSTANCES_DIALOG_MSG = "StopInstancesDialog.msg"; //$NON-NLS-1$
	
	public static class StopInstanceDialog extends ListSelectionDialog {

		private static class DeltaCloudInstanceProvider implements IStructuredContentProvider {

			@SuppressWarnings("unchecked")
			@Override
			public Object[] getElements(Object cvInstanceElements) {
				Assert.isTrue(cvInstanceElements instanceof Collection);
				Collection<CVInstanceElement> instances = (Collection<CVInstanceElement>) cvInstanceElements;
				return instances.toArray(new CVInstanceElement[instances.size()]);
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
		}

		private static class CloudElementNameProvider extends LabelProvider {
			public String getText(Object element) {
				return ((CVInstanceElement) element).getName();
			}
		};

		public StopInstanceDialog(Shell parentShell, Collection<?> cloudViewElements) {
			super(parentShell
					, cloudViewElements
					, new DeltaCloudInstanceProvider()
					, new CloudElementNameProvider(), CVMessages.getString(STOP_INSTANCES_DIALOG_TITLE));
			setTitle(CVMessages.getString(STOP_INSTANCES_DIALOG_MSG));
		}
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (isSingleInstance(structuredSelection)) {
				CVInstanceElement cvInstance = UIUtils.getFirstElement(selection, CVInstanceElement.class);
				stopInstance(cvInstance);
			} else {
				stopWithDialog(structuredSelection);
			}
		}

		return Status.OK_STATUS;
	}

	private boolean isSingleInstance(IStructuredSelection selection) {
		List<?> selectedItems = selection.toList();
		return selectedItems.size() == 1
				&& selectedItems.get(0) instanceof CVInstanceElement;
	}

	@SuppressWarnings("unchecked")
	private void stopWithDialog(IStructuredSelection selection) {
		StopInstanceDialog dialog = new StopInstanceDialog(
					UIUtils.getActiveShell()
					, (List<CVInstanceElement>) selection.toList());
		if (Dialog.OK == dialog.open()) {
			stopInstances(dialog.getResult());
		}
	}

	private void stopInstances(Object[] cvInstances) {
		for (int i = 0; i < cvInstances.length; i++) {
			stopInstance((CVInstanceElement) cvInstances[i]);
		}
	}

	private void stopInstance(CVInstanceElement cvInstance) {
		if (cvInstance != null) {
			DeltaCloudInstance instance = (DeltaCloudInstance) cvInstance.getElement();
			DeltaCloud cloud = getCloud(cvInstance);
			executeInstanceAction(instance, cloud);
		}
	}

	private DeltaCloud getCloud(CloudViewElement element) {
		while (!(element instanceof CVCloudElement)) {
			element = (CloudViewElement) element.getParent();
		}
		CVCloudElement cvcloud = (CVCloudElement) element;
		DeltaCloud cloud = (DeltaCloud) cvcloud.getElement();
		return cloud;
	}

	private void executeInstanceAction(DeltaCloudInstance instance, DeltaCloud cloud) {
		PerformInstanceActionThread t = new PerformInstanceActionThread(cloud, instance,
				DeltaCloudInstance.STOP,
				CVMessages.getString(STOPPING_INSTANCE_TITLE),
				CVMessages.getFormattedString(STOPPING_INSTANCE_MSG, new String[] { instance.getName() }),
				DeltaCloudInstance.STOPPED);
		t.setUser(true);
		t.schedule();
	}
}
