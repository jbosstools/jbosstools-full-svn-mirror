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
package org.jboss.tools.vpe.editor.wizards;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.jboss.tools.vpe.editor.template.VpeAnyData;
import org.jboss.tools.vpe.messages.VpeUIMessages;

public class ExportUserTagsTemplatesWizard extends Wizard {

	private ExportUserTagsTemplatesWizardPage mainPage;
	private List<VpeAnyData>  currentList;
	
	/**
	 * Constructor
	 */
	public ExportUserTagsTemplatesWizard(List<VpeAnyData>  currentList) {
		setWindowTitle(VpeUIMessages.EXPORT_USER_TAGS_PAGE_TITLE);
		this.currentList = currentList;
	}
	
	@Override
	public void addPages() {
		super.addPages();
		mainPage = new ExportUserTagsTemplatesWizardPage(
				VpeUIMessages.EXPORT_USER_TAGS_TEMPLATES_WIZARD_PAGE,
				currentList);
		addPage(mainPage);
	}

	@Override
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}

	@Override
	public boolean performFinish() {
		return mainPage.finish();
	}

}
