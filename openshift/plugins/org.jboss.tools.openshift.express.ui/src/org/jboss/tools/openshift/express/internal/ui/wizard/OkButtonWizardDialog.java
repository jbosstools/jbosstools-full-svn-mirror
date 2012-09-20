/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.express.internal.ui.wizard;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Andre Dietisheim
 */
public class OkButtonWizardDialog extends WizardDialog {

	public OkButtonWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
	}
	
	protected Control createButtonBar(Composite parent) {
		Control control = super.createButtonBar(parent);
		getButton(IDialogConstants.FINISH_ID).setText("OK");
		getButton(CANCEL).setVisible(false);
		return control;
	}
}
