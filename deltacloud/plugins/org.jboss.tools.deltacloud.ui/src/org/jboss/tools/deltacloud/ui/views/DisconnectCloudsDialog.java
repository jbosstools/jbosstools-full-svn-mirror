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
package org.jboss.tools.deltacloud.ui.views;

import java.util.Collection;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.jboss.tools.deltacloud.core.DeltaCloud;

public class DisconnectCloudsDialog extends ListSelectionDialog {

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
