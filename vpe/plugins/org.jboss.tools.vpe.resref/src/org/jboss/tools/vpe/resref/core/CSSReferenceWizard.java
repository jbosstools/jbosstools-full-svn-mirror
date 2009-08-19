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
