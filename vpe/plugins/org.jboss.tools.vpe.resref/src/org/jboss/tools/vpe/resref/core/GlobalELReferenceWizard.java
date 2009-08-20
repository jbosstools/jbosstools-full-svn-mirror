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

import org.jboss.tools.common.resref.core.ResourceReference;

public class GlobalELReferenceWizard extends ELReferenceWizard {

	public GlobalELReferenceWizard(Object fileLocation, String windowTitle,
			ResourceReference[] resrefList) {
		super(fileLocation, windowTitle, resrefList);
	}

	@Override
	protected void createPage() {
		page = new GlobalElReferenceWizardPage(Messages.GLOBAL_EL_WIZARD_PAGE_NAME,
				Messages.VRD_ADD_EL_PREFERENCE, ReferenceWizardPage
						.getImageDescriptor(), fileLocation);
		page.setDescription(Messages.ADD_GLOBAL_EL_PREFERENCE_MESSAGE);
	}
	
}
