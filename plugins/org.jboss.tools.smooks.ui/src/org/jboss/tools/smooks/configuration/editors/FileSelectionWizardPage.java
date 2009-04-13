/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.smooks.configuration.editors.xml.AbstractFileSelectionWizardPage;

/**
 * @author Dart Peng (dpeng@redhat.com)
 * Date Apr 13, 2009
 */
public class FileSelectionWizardPage extends AbstractFileSelectionWizardPage {

	public FileSelectionWizardPage(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.configuration.editors.xml.AbstractFileSelectionWizardPage#loadedTheObject(java.lang.String)
	 */
	@Override
	protected Object loadedTheObject(String path) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	protected Composite createFileSelectionComposite(Composite parent) {
		Composite composite = super.createFileSelectionComposite(parent);
		GridData gd = new GridData();
		gd.widthHint = 0;
		fileSystemBrowseButton.setLayoutData(gd);
		this.fileSystemBrowseButton.setVisible(false);
		return composite;
	}

	@Override
	protected String processWorkSpaceFilePath(IFile file) {
		String s = file.getFullPath().toPortableString();
		return s;
	}

	
	

}
