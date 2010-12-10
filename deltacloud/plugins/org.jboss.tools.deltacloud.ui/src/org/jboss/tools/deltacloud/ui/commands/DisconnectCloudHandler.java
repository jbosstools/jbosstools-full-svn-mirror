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

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.ui.ErrorUtils;
import org.jboss.tools.deltacloud.ui.views.CVMessages;
import org.jboss.tools.deltacloud.ui.views.cloud.CloudItem;
import org.jboss.tools.deltacloud.ui.views.cloud.DeltaCloudViewItem;

/**
 * @author Andre Dietisheim
 */
public class DisconnectCloudHandler extends AbstractHandler implements IHandler {

	public static class DisconnectCloudsDialog extends ListSelectionDialog {

		private static final String CONFIRM_CLOUD_DELETE_TITLE = "ConfirmCloudDelete.title"; //$NON-NLS-1$
		private static final String CONFIRM_CLOUD_DELETE_MSG = "ConfirmCloudDelete.msg"; //$NON-NLS-1$

		private static class DeltaCloudItemProvider implements IStructuredContentProvider {

			@SuppressWarnings("unchecked")
			@Override
			public Object[] getElements(Object cloudViewElements) {
				Assert.isTrue(cloudViewElements instanceof Collection);
				Collection<DeltaCloud> deltaClouds = (Collection<DeltaCloud>) cloudViewElements;
				return deltaClouds.toArray(new DeltaCloud[deltaClouds.size()]);
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
				return ((DeltaCloud) element).getName();
			}
		};

		public DisconnectCloudsDialog(Shell parentShell, Collection<?> cloudViewElements) {
			super(parentShell
					, cloudViewElements
					, new DeltaCloudItemProvider()
					, new CloudElementNameProvider(), CVMessages.getString(CONFIRM_CLOUD_DELETE_MSG));
			setTitle(CVMessages.getString(CONFIRM_CLOUD_DELETE_TITLE));
		}
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			List<?> selectedElements = ((IStructuredSelection) selection).toList();
			DeltaCloud deltaCloud = getFirstSelectedCloud(selectedElements);
			try {
				if (selectedElements != null
						&& selectedElements.size() == 1
						&& deltaCloud != null) {
					// TODO internationalize strings
					if (MessageDialog.openConfirm(
							HandlerUtil.getActiveShell(event),
							"Confirm disconnect",
							MessageFormat.format("Are you sure that you want to disconnect cloud \"{0}\"?",
									deltaCloud.getName()))) {
						removeDeltaCloud(deltaCloud);
					}
				} else {
					removeWithDialog(shell, selectedElements);
				}
			} catch (DeltaCloudException e) {
				// TODO internationalize strings
				ErrorUtils.handleError("Error",
						MessageFormat.format("Could not disconnect cloud {0}", deltaCloud.getName()), e, shell);
			}
		}

		return Status.OK_STATUS;
	}

	private void removeWithDialog(Shell shell, List<?> selectedElements) throws DeltaCloudException {
		Collection<?> clouds = getSelectedClouds(selectedElements);
		DisconnectCloudsDialog dialog = new DisconnectCloudsDialog(
				shell
				, clouds);
		dialog.setInitialSelections(clouds.toArray());
		if (Dialog.OK == dialog.open()) {
			removeDeltaClouds(dialog.getResult());
		}
	}

	private DeltaCloud getFirstSelectedCloud(List<?> selectedElements) {
		DeltaCloud deltaCloud = null;
		if (selectedElements.size() > 0) {
			Object object = selectedElements.get(0);
			if (object instanceof CloudItem) {
				Object element = ((CloudItem) object).getModel();
				if (element instanceof DeltaCloud) {
					deltaCloud = (DeltaCloud) element;
				}
			}
		}
		return deltaCloud;
	}

	private Collection<DeltaCloud> getSelectedClouds(List<?> selectedElements) {
		Set<DeltaCloud> selectedClouds = new HashSet<DeltaCloud>();
		for (Object element : selectedElements) {
			DeltaCloud deltaCloud = getDeltaCloud(element);
			if (deltaCloud != null) {
				selectedClouds.add(deltaCloud);
			}
		}
		return selectedClouds;
	}

	private DeltaCloud getDeltaCloud(Object item) {
		if (!(item instanceof DeltaCloudViewItem)) {
			return null;
		}

		DeltaCloud cloud = getDeltaCloud((DeltaCloudViewItem) item);

		if (cloud == null) {
			return null;
		}
		return cloud;
	}

	private DeltaCloud getDeltaCloud(DeltaCloudViewItem element) {
		if (element == null) {
			return null;
		}
		Object cloud = element.getModel();
		if (cloud instanceof DeltaCloud) {
			return (DeltaCloud) cloud;
		}

		return getDeltaCloud((DeltaCloudViewItem) element.getParent());
	}

	private void removeDeltaClouds(Object[] deltaClouds) throws DeltaCloudException {
		for (Object deltaCloud : deltaClouds) {
			removeDeltaCloud((DeltaCloud) deltaCloud);
		}
	}

	private void removeDeltaCloud(DeltaCloud deltaCloud) throws DeltaCloudException {
		DeltaCloudManager.getDefault().removeCloud((DeltaCloud) deltaCloud);
	}
}
