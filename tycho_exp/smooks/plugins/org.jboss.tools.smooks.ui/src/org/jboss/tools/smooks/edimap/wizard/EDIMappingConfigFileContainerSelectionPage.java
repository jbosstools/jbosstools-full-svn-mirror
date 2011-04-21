/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.edimap.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

/**
 * @author Dart (dpeng@redhat.com)
 *
 */
public class EDIMappingConfigFileContainerSelectionPage extends WizardNewFileCreationPage {

	public EDIMappingConfigFileContainerSelectionPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
		setTitle("EDI Mapping Configuration File Wizard Page");
		setDescription("Create a new EDI mapping configuration file.");
		setFileExtension("xml");
		setFileName("edi-to-xml-mapping.xml");
	}
	
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		validatePage();
	}
}
