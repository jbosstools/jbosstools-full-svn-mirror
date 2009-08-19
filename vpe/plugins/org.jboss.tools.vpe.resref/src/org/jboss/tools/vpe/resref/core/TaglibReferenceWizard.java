package org.jboss.tools.vpe.resref.core;

public class TaglibReferenceWizard extends ReferenceWizard {

	
	
	public TaglibReferenceWizard(Object fileLocation, String windowTitle) {
		super(fileLocation, windowTitle);
	}

	@Override
	protected void createPage() {
		page = new TaglibReferenceWizardPage(Messages.TAGLIB_WIZARD_PAGE_NAME,
				Messages.VRD_ADD_TAGLIB_PREFERENCE, ReferenceWizardPage
						.getImageDescriptor(), fileLocation);
		page.setDescription(Messages.VRD_ADD_TAGLIB_PREFERENCE_MESSAGE);
	}

}
