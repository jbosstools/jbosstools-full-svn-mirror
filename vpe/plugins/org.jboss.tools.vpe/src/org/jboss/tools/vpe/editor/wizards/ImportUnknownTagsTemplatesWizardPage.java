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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardResourceImportPage;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.resref.core.ReferenceWizardPage;


/**
 * Page for importing unknown tags templates.
 * 
 * @author dmaliarevich
 */
public class ImportUnknownTagsTemplatesWizardPage extends
		WizardResourceImportPage {

	/**
	 * Constructor
	 * 
	 * @param name
	 * @param selection
	 */
	public ImportUnknownTagsTemplatesWizardPage(String name,
			IStructuredSelection selection) {
		super(name, selection);
		setTitle(VpeUIMessages.IMPORT_UNKNOWN_TAGS_PAGE_TITLE);
		setDescription(VpeUIMessages.IMPORT_UNKNOWN_TAGS_PAGE_DESCRIPTION);
		setImageDescriptor(ReferenceWizardPage.getImageDescriptor());
	}

	@Override
	protected void createSourceGroup(Composite parent) {
		/*
		 * Create nothing.
		 */
	}

	@Override
	protected ITreeContentProvider getFileProvider() {
		return null;
	}

	@Override
	protected ITreeContentProvider getFolderProvider() {
		return null;
	}

	

}
