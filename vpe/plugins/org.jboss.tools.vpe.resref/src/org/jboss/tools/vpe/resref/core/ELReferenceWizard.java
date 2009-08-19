package org.jboss.tools.vpe.resref.core;

import org.jboss.tools.common.resref.core.ResourceReference;

public class ELReferenceWizard extends ReferenceWizard {
	
	protected ResourceReference[] resrefList = null;
	
	public ELReferenceWizard(Object fileLocation, String windowTitle,
			ResourceReference[] resrefList) {
		super(fileLocation, windowTitle);
		this.resrefList = resrefList;
	}

	@Override
	protected void createPage() {
		page = new ELReferenceWizardPage(Messages.EL_WIZARD_PAGE_NAME,
				Messages.VRD_ADD_EL_PREFERENCE, ReferenceWizardPage
						.getImageDescriptor(), fileLocation);
		page.setDescription(Messages.VRD_ADD_EL_PREFERENCE_MESSAGE);
	}

	public ResourceReference[] getResrefList() {
		return resrefList;
	}

	public void setResrefList(ResourceReference[] resrefList) {
		this.resrefList = resrefList;
	}
	
}
