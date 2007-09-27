/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.shale.ui.wizard.newfile;

import org.jboss.tools.common.meta.action.impl.SpecialWizardSupport;
import org.jboss.tools.common.model.ui.wizard.newfile.NewFileContextEx;
import org.jboss.tools.common.model.ui.wizard.newfile.NewFileWizardEx;
import org.jboss.tools.shale.model.dialog.handlers.CreateDialogConfigFileSupport;

public class NewShaleDialogConfigFileWizard extends NewFileWizardEx {

	protected NewFileContextEx createNewFileContext() {
		return new NewShaleDialogConfigFileContext();
	}
	
	class NewShaleDialogConfigFileContext extends NewFileContextEx {
		protected SpecialWizardSupport createSupport() {
			return new CreateDialogConfigFileSupport();
		}
		protected String getActionPath() {
			return "CreateActions.CreateFiles.JSF.Shale.CreateShaleDialog";
		}
	}

}
