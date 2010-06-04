/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.dialog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.jboss.tools.common.model.ui.ModelUIImages;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.messages.VpeUIMessages;

public class ExternalizeStringsWizard extends Wizard {
	
	public String ExternalizeStringsWizardPageName = "ExternalizeStringsWizardPage";
	public String NewFileCreationPageName = "NewFileCreationPage";
	
	VpeController vpeController = null;
	ExternalizeStringsWizardPage page1 = null;
	WizardNewFileCreationPage page2 = null;
	
	public ExternalizeStringsWizard(VpeController vpeController) {
		super();
		setHelpAvailable(false);
		setWindowTitle(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_TITLE);
		this.vpeController = vpeController; 
	}
	
	@Override
	public void addPages() {
		super.addPages();
		page1 = new ExternalizeStringsWizardPage(
				ExternalizeStringsWizardPageName, vpeController);
		page2 = new WizardNewFileCreationPage(NewFileCreationPageName,
				(IStructuredSelection) vpeController
				.getSourceEditor().getSelectionProvider().getSelection());
		page2.setTitle(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_TITLE);
		page2.setDescription(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_DESCRIPTION);
		page2.setImageDescriptor(ModelUIImages.getImageDescriptor(ModelUIImages.WIZARD_DEFAULT));
		addPage(page1);
		addPage(page2);
	}

	@Override
	public boolean canFinish() {
		return (!page1.isNewFile() && page1.isPageComplete())
				|| (page1.isNewFile() && page2.isPageComplete());
	}

	@Override
	public boolean performFinish() {
		IFile bundleFile = null;
		if (page1.isNewFile()) {
			bundleFile = page2.createNewFile();
		} else {
			bundleFile = page1.getBundleFile();
		}
		/*
		 * Exit when the file is null
		 */
		if (bundleFile == null) {
			return false;
		}
		/*
		 * Add "key=value" to the bundle
		 */
		if (bundleFile.exists()) {
			InputStream is = new ByteArrayInputStream(page1.getKeyValuePair().getBytes());
			try {
				bundleFile.appendContents(is, false, true, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		/*
		 * Replace text in the editor
		 */
		page1.replaceText();
		
		return true;
	}

}
