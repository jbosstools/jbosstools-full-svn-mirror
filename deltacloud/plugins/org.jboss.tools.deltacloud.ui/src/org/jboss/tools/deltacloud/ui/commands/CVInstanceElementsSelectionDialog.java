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

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.ui.views.CVInstanceElement;

/**
 * A dialog that allows the user to select CVInstanceElements
 * 
 * @see CVInstanceElement
 */
public class CVInstanceElementsSelectionDialog extends ListSelectionDialog {

	private static class DeltaCloudInstanceProvider implements IStructuredContentProvider {

		@Override
		public Object[] getElements(Object deltaCloudInstances) {
			Assert.isTrue(deltaCloudInstances instanceof Collection);
			Collection<?> instances = (Collection<?>) deltaCloudInstances;
			return instances.toArray(new Object[instances.size()]);
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
			Assert.isTrue(element instanceof IAdaptable);
			DeltaCloudInstance deltaCloudInstance = (DeltaCloudInstance) ((IAdaptable) element).getAdapter(DeltaCloudInstance.class);
			if (deltaCloudInstance != null) {
				return deltaCloudInstance.getName();
			}
			return ((CVInstanceElement) element).getName();
		}
	};

	public CVInstanceElementsSelectionDialog(Shell parentShell, Collection<?> cloudViewElements, String title, String message) {
		super(parentShell
				, cloudViewElements
				, new DeltaCloudInstanceProvider()
				, new CloudElementNameProvider()
				, message);
		setTitle(title);
	}
}