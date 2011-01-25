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

import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.ui.ErrorUtils;

/**
 * @author Jeff Johnston
 */
public class ImageFilterWizard extends AbstractDeltaCloudWizard {

	private ImageFilterPage mainPage;

	public ImageFilterWizard(DeltaCloud cloud) {
		super(cloud);
	}

	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		mainPage = new ImageFilterPage(getDeltaCloud());
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
		String archRule = mainPage.getArchRule();
		String descRule = mainPage.getDescRule();

		try {
			getDeltaCloud().updateImageFilter(
					nameRule + ";" + //$NON-NLS-1$
					idRule + ";" + //$NON-NLS-1$
					archRule + ";" + //$NON-NLS-1$
					descRule);
		} catch (Exception e) {
			// TODO: internationalize strings
			ErrorUtils.handleError(
					"Error",
					"Cloud not get update filters on cloud " + getDeltaCloud().getName(), e, getShell());
		}

		return true;
	}

}
