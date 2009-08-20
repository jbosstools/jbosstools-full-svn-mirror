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

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.vpe.resref.Activator;

public abstract class ReferenceWizard extends Wizard {
	
	protected ResourceReference resref = null;
	protected Object fileLocation = null;

	/*
	 * Should be initialized in #createPage() method by implementator.
	 */
	ReferenceWizardPage page = null;
	
	public ReferenceWizard(Object fileLocation, String windowTitle) {
		super();
		this.fileLocation = fileLocation;
		createPage();
		setWindowTitle(windowTitle);
	}

	public void createPageControls(Composite pageContainer, Object fileLocation) {
		super.createPageControls(pageContainer);
		this.fileLocation = fileLocation;
	}

	@Override
	public void addPages() {
		/*
		 * Set values from resref to the page.
		 */
		if (null == page) {
			Activator.getDefault().logError(
					Messages.WIZARD_PAGE_SHOULD_BE_INITIALIZED);
		} else {
			page.setScope(resref.getScope());
			page.setLocation(resref.getLocation());
			page.setProperties(resref.getProperties());
			page.setResref(resref);
			addPage(page);
		}
	}
	
	@Override
	public boolean performCancel() {
		return true;
	}

	@Override
	public boolean performFinish() {
		resref.setLocation(page.getLocation());
		resref.setProperties(page.getProperties());
		resref.setScope(page.getSelectedScope());
		return true;
	}

	public ResourceReference getResref() {
		return resref;
	}

	public void setResref(ResourceReference resref) {
		this.resref = resref;
	}

	/**
	 * Must return created wizard page.
	 * 
	 * @return the page
	 */
	protected abstract void createPage();	
	
}
