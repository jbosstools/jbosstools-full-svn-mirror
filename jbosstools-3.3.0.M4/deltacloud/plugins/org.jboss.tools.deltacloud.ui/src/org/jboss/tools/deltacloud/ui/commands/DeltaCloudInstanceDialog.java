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
import java.util.Collection;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.internal.deltacloud.ui.utils.WorkbenchUtils;

/**
 * A dialog that allows the user to select CVInstanceElements
 * 
 * @see DeltaCloudInstance
 */
public class DeltaCloudInstanceDialog extends ListSelectionDialog {

	private static class DeltaCloudInstanceNameProvider extends LabelProvider {
		public String getText(Object element) {
			DeltaCloudInstance instance = WorkbenchUtils.adapt(element, DeltaCloudInstance.class);
			if (instance != null) {
				return instance.getName();
			} else {
				return null;
			}
		}
	};

	public DeltaCloudInstanceDialog(Shell parentShell, Collection<DeltaCloudInstance> deltaCloudInstances, String title, String message) {
		super(parentShell
				, deltaCloudInstances
				, ArrayContentProvider.getInstance()
				, new DeltaCloudInstanceNameProvider()
				, message);
		setTitle(title);
	}
	
	public DeltaCloudInstance[] getResult2() {
		ArrayList<DeltaCloudInstance> result = new ArrayList<DeltaCloudInstance>();
		Object[] supArr = super.getResult();
		for( int i = 0; i < supArr.length; i++ ) {
			result.add((DeltaCloudInstance)supArr[i]);
		}
		return result.toArray(new DeltaCloudInstance[result.size()]);
	}
}