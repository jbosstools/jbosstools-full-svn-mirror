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

import org.eclipse.swt.widgets.Display;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.ui.ErrorUtils;

/**
 * @author Jeff Johnston
 */
public class InstanceFilterWizard extends AbstractDeltaCloudWizard {

	private InstanceFilterPage mainPage;

	public InstanceFilterWizard(DeltaCloud cloud) {
		super(cloud);
	}

	@Override
	public void addPages() {
		setWindowTitle(WizardMessages.getString("InstanceFilter.title"));
		mainPage = new InstanceFilterPage(getDeltaCloud());
		addPage(mainPage);
	}

	@Override
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}

	@Override
	public boolean performFinish() {
		String nameRule = mainPage.getNameRule();
		String idRule = mainPage.getIdRule();
		String aliasRule = mainPage.getAliasRule();
		String imageIdRule = mainPage.getImageIdRule();
		String ownerIdRule = mainPage.getOwnerIdRule();
		String keyNameRule = mainPage.getKeyNameRule();
		String realmRule = mainPage.getRealmRule();
		String profileRule = mainPage.getProfileRule();

		try {
			getDeltaCloud().updateInstanceFilter(
					nameRule, idRule, aliasRule, imageIdRule, ownerIdRule, keyNameRule, realmRule, profileRule);
		} catch (Exception e) {
			// TODO: internationalize strings
			ErrorUtils.handleError(
					"Error",
					"Could not update filters", e, Display.getDefault().getActiveShell());
		}

		return true;
	}

}
