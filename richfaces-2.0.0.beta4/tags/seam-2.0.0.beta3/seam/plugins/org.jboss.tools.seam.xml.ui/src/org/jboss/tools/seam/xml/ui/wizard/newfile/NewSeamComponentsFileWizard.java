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
package org.jboss.tools.seam.xml.ui.wizard.newfile;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.common.model.ui.wizard.newfile.NewFileContextEx;
import org.jboss.tools.common.model.ui.wizard.newfile.NewFileWizardEx;

public class NewSeamComponentsFileWizard extends NewFileWizardEx {

	public NewSeamComponentsFileWizard() {
		setDefaultPageImageDescriptor(ImageDescriptor.createFromFile(NewSeamComponentsFileWizard.class, "SeamComponentsFile_wiz.png"));
	}
	
	protected NewFileContextEx createNewFileContext() {
		return new NewSeamComponentsFileContext();
	}
	
	class NewSeamComponentsFileContext extends NewFileContextEx {
		protected String getActionPath() {
			return "CreateActions.CreateFiles.Seam.CreateFileSeamComponents";
		}
	}

}
