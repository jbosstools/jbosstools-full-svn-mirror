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
import org.jboss.tools.deltacloud.core.DeltaCloudKey;

/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class ManageKeysWizard extends Wizard {

	private DeltaCloud cloud;
	private ManageKeysPage mainPage;
	private DeltaCloudKey key;

	public ManageKeysWizard(DeltaCloud cloud) {
		this.cloud = cloud;
	}

	public DeltaCloudKey getKey() {
		return key;
	}

	@Override
	public void addPages() {
		mainPage = new ManageKeysPage(cloud);
		addPage(mainPage);
	}

	@Override
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}

	@Override
	public boolean performFinish() {
		this.key = mainPage.getKey();
		return true;
	}

	@Override
	public boolean needsProgressMonitor() {
		return true;
	}

}
