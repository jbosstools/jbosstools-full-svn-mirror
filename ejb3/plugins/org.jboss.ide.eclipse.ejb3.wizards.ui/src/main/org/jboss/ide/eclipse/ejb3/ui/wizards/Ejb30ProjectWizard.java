package org.jboss.ide.eclipse.ejb3.ui.wizards;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectTemplate;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.web.ui.internal.wizards.NewProjectDataModelFacetWizard;
import org.jboss.ide.eclipse.ejb3.core.facet.Ejb30FacetProjectCreationDataModelProvider;
import org.osgi.framework.Bundle;

public class Ejb30ProjectWizard extends NewProjectDataModelFacetWizard
		implements INewWizard {

	public Ejb30ProjectWizard(IDataModel model){
		super(model);
		setWindowTitle("JBIDE EJB 3.0");
	}
	
	public Ejb30ProjectWizard(){
		super();
		setWindowTitle("JBIDE EJB 3.0");
	}
	
	protected IDataModel createDataModel() {
		return DataModelFactory.createDataModel(new Ejb30FacetProjectCreationDataModelProvider());
	}

	protected IFacetedProjectTemplate getTemplate() {
		return ProjectFacetsManager.getTemplate("template.jboss.ejb30"); //$NON-NLS-1$
	}

	protected IWizardPage createFirstPage() {
		return new Ejb30ProjectFirstPage(model, "first.page"); //$NON-NLS-1$
	}
	
	// TODO: change
	protected ImageDescriptor getDefaultPageImageDescriptor() {
		final Bundle bundle = Platform.getBundle("org.eclipse.jst.ejb.ui"); //$NON-NLS-1$
		final URL url = bundle.getEntry("icons/full/wizban/ejbproject_wiz.gif"); //$NON-NLS-1$
		return ImageDescriptor.createFromURL(url);
	}
	
	// Let's you switch to another perspective
	protected String getFinalPerspectiveID() {
		return null;
	}

}
