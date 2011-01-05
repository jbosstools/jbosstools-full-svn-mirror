/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.internal.deltacloud.ui.wizards;

import java.text.MessageFormat;

import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.Driver;
import org.jboss.tools.deltacloud.ui.ErrorUtils;

/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class EditCloudConnectionWizard extends NewCloudConnectionWizard {

	private static final String MAINPAGE_NAME = "EditCloudConnection.name"; //$NON-NLS-1$

	public EditCloudConnectionWizard(DeltaCloud cloud) {
		super(WizardMessages.getString(MAINPAGE_NAME), cloud);
	}

	@Override
	public boolean performFinish() {
		String name = mainPage.getModel().getName();
		String url = mainPage.getModel().getUrl();
		String username = mainPage.getModel().getUsername();
		String password = mainPage.getModel().getPassword();
		Driver driver = mainPage.getModel().getDriver();
		try {
			initialCloud.update(name, url, username, password, driver);
		} catch (Exception e) {
			// TODO internationalize strings
			ErrorUtils.handleError("Error",
					MessageFormat.format("Could not edit cloud \"{0}\"", initialCloud.getName()),
					e, getShell());
		}
		return true;
	}
}
