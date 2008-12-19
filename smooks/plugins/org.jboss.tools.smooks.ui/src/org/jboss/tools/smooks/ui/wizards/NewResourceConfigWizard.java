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
package org.jboss.tools.smooks.ui.wizards;

import org.eclipse.jface.wizard.Wizard;

/**
 * @author Dart Peng<br>
 * Date : Sep 18, 2008
 */
public class NewResourceConfigWizard extends Wizard {
	protected NewResourceConfigWizardPage page = null;
	
	private NewResourceConfigKey selectedKey = null;
	
	
	@Override
	public void addPages() {
		if(page == null){
			page = new NewResourceConfigWizardPage("New ResourceConfig");
		}
		this.addPage(page);
		super.addPages();
	}



	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		this.setSelectedKey(page.getSelectedKey());
		return true;
	}



	public NewResourceConfigKey getSelectedKey() {
		return selectedKey;
	}



	public void setSelectedKey(NewResourceConfigKey selectedKey) {
		this.selectedKey = selectedKey;
	}

}
