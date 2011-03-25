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
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.internal.deltacloud.ui.utils.WorkbenchUtils;
import org.jboss.tools.internal.deltacloud.ui.wizards.NewInstanceWizard;

/**
 * @author Andre Dietisheim
 */
public class CreateInstanceFromSpecificImageHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			DeltaCloudImage deltaCloudImage = WorkbenchUtils.getFirstAdaptedElement(selection, DeltaCloudImage.class);
			createInstance(deltaCloudImage, HandlerUtil.getActiveShell(event));
		}

		return Status.OK_STATUS;
	}

	private void createInstance(DeltaCloudImage deltaCloudImage, Shell shell) {
		if (deltaCloudImage != null) {
			DeltaCloud deltaCloud = deltaCloudImage.getDeltaCloud();
			IWizard wizard = new NewInstanceWizard(deltaCloud, deltaCloudImage.getId());
			WizardDialog dialog = new WizardDialog(shell, wizard);
			dialog.create();
			dialog.open();
		}
	}
}
