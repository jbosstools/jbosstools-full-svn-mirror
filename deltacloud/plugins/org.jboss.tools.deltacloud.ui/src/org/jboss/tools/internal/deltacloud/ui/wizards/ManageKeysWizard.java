/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.internal.deltacloud.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jboss.tools.deltacloud.core.DeltaCloud;

/**
 * @author Jeff Johnston
 */
public class ManageKeysWizard extends Wizard {

	private DeltaCloud cloud;
	private String fileExtension;
	private ManageKeysPage mainPage;
	private String keyname;

	public ManageKeysWizard(DeltaCloud cloud, String fileExtension) {
		this.cloud = cloud;
		this.fileExtension = fileExtension;
	}

	public String getKeyName() {
		return keyname;
	}

	@Override
	public void addPages() {
		mainPage = new ManageKeysPage(cloud, fileExtension);
		addPage(mainPage);
	}

	@Override
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}

	@Override
	public boolean performFinish() {
		String currFile = mainPage.getCurrFile();
		keyname = currFile.substring(0, currFile.length() - fileExtension.length());
		return true;
	}

}
