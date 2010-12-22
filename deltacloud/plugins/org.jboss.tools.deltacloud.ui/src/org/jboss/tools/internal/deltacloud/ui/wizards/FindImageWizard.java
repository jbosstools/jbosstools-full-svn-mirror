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
public class FindImageWizard extends Wizard {

	private DeltaCloud cloud;
	private FindImagePage mainPage;
	private String imageId;
	
	public FindImageWizard(DeltaCloud cloud) {
		this.cloud = cloud;
	}
	
	public String getImageId() {
		return imageId;
	}
	
	@Override
	public void addPages() {
		mainPage = new FindImagePage(cloud);
		addPage(mainPage);
	}
	
	@Override
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}
	
	@Override
	public boolean performFinish() {
		imageId = mainPage.getImageId();
		return true;
	}

}
