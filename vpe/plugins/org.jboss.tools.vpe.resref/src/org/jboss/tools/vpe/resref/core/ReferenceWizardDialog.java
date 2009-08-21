/*******************************************************************************
 * Copyright (c) 2007-2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.resref.core;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.resref.core.ResourceReference;

public class ReferenceWizardDialog extends WizardDialog {

	protected ResourceReference resref = null;
	
	public ReferenceWizardDialog(Shell parentShell, ReferenceWizard newWizard, ResourceReference resref) {
		super(parentShell, newWizard);
		this.resref = resref;
		setHelpAvailable(false);
	}
	
	@Override
	public int open() {
		/*
		 * Get Resource Reference wizard.
		 */
		ReferenceWizard wizard = (ReferenceWizard) getWizard();
		
		/*
		 * Read values from resref when editing
		 * and send it to the wizard.
		 */
		wizard.setResref(resref);
		
		/*
		 * Open the dialog
		 */
		int returnCode = super.open();
		
		/*
		 * If Finish pressed - store new values in the resref
		 * and than it'll be saved in the appropriate ResourceReferenceComposite
		 */
		if (Dialog.OK == returnCode) {
			resref = wizard.getResref();
		}
		
		return returnCode;
	}
	
}
