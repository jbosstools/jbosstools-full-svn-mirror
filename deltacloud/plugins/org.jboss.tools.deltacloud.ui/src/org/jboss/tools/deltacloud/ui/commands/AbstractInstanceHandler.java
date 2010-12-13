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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.ui.views.PerformInstanceActionJob;
import org.jboss.tools.internal.deltacloud.ui.utils.StringUtils;
import org.jboss.tools.internal.deltacloud.ui.utils.StringUtils.IElementFormatter;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

/**
 * A base handler that instance related handler may extend
 * 
 * @author Andre Dietisheim
 */
public abstract class AbstractInstanceHandler extends AbstractHandler implements IHandler {

	protected void executeInstanceAction(DeltaCloudInstance instance, DeltaCloudInstance.Action action, DeltaCloudInstance.State expectedState,
			String title, String message) {
		if (instance != null) {
			PerformInstanceActionJob t = new PerformInstanceActionJob(
					instance.getDeltaCloud(),
					instance,
					action,
					title,
					message,
					expectedState);
			t.setUser(true);
			t.schedule();
		}
	}
	
	protected boolean isSingleInstanceSelected(ISelection selection) {
		return UIUtils.isSingleSelection(selection, DeltaCloudInstance.class);
	}
	
	@SuppressWarnings("unchecked")
	protected String getInstanceNames(ISelection selection) {
		return StringUtils.getFormattedString(((IStructuredSelection) selection).toList(),
				new IElementFormatter<Object>() {

					@Override
					public String format(Object element) {
						if (element instanceof DeltaCloudInstance) {
							return new StringBuilder()
									.append(((DeltaCloudInstance) element).getName())
									.append(",")
									.toString();
						} else {
							return null;
						}
					}
				});
	}

}
