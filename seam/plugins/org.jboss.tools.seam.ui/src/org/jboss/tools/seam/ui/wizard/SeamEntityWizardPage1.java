/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.seam.ui.wizard;

/**
 * @author eskimo
 *
 */
public class SeamEntityWizardPage1 extends SeamBaseWizardPage {

	/**
	 * 
	 */
	public SeamEntityWizardPage1() {
		super("seam.new.entity.page1","Seam Entity", null);
		setMessage("Select the name of the new Seam Entity. A new Seam Entity Bean with key Seam/EJB3 " +
				"annotations and wxample attributes will be created.");
	}
	
	/**
	 * 
	 */
	protected void createEditors() {
		addEditor(SeamWizardFactory.createSeamProjectSelectionFieldEditor(SeamWizardUtils.getSelectedProjectName()));
		addEditor(SeamWizardFactory.createSeamEntityClasNameFieldEditor());
		addEditor(SeamWizardFactory.createSeamMasterPageNameFieldEditor());
		addEditor(SeamWizardFactory.createSeamPageNameFieldEditor());
	}
}
