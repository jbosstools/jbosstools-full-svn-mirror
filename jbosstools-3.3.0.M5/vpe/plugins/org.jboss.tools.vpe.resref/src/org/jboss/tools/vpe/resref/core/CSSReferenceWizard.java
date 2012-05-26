/*******************************************************************************
 * Copyright (c) 2007-2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.resref.core;

public class CSSReferenceWizard extends ReferenceWizard {

	public CSSReferenceWizard(Object fileLocation, String windowTitle) {
		super(fileLocation, windowTitle);
	}
	
	@Override
	protected void createPage() {
		page = new CSSReferenceWizardPage(
				Messages.CSS_WIZARD_PAGE_NAME, Messages.VRD_ADD_CSS_PREFERENCE,
				ReferenceWizardPage.getImageDescriptor(), fileLocation);
		page.setDescription(Messages.VRD_ADD_CSS_PREFERENCE_MESSAGE);
	}
	
}
