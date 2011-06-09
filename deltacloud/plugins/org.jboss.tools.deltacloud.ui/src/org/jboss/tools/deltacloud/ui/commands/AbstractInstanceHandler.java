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

import org.apache.deltacloud.client.utils.StringUtils;
import org.apache.deltacloud.client.utils.StringUtils.IElementFormatter;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.DeltaCloudResourceAction;
import org.jboss.tools.deltacloud.core.job.InstanceActionJob;
import org.jboss.tools.internal.deltacloud.ui.utils.WorkbenchUtils;

/**
 * A base handler that instance related handler may extend
 * 
 * @author Andre Dietisheim
 */
public abstract class AbstractInstanceHandler extends AbstractHandler implements IHandler {

	protected void executeInstanceAction(DeltaCloudInstance instance, DeltaCloudResourceAction action,
			DeltaCloudInstance.State expectedState, String title, String message) {
		if (instance != null) {
			new InstanceActionJob(message, instance, action, expectedState)
					.schedule();
		}
	}

	protected boolean isSingleInstanceSelected(ISelection selection) {
		return WorkbenchUtils.isSingleSelection(selection, DeltaCloudInstance.class);
	}

	@SuppressWarnings("unchecked")
	protected String getInstanceNames(ISelection selection) {
		return StringUtils.getFormattedString(((IStructuredSelection) selection).toList(),
				new IElementFormatter<Object>() {

					@Override
					public String format(Object element) {
						if (element instanceof DeltaCloudInstance) {
							return new StringBuilder()
									.append(((DeltaCloudInstance) element).getAlias())
									.append(",")
									.toString();
						} else {
							return null;
						}
					}
				});
	}

}
