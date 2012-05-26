package org.jboss.tools.portlet.ui.internal.project.facet;

import org.eclipse.jst.common.project.facet.core.libprov.LibraryInstallDelegate;
import org.eclipse.jst.common.project.facet.ui.libprov.LibraryProviderFrameworkUi;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelWizardPage;
import org.eclipse.wst.common.project.facet.ui.IFacetWizardPage;
import org.eclipse.wst.common.project.facet.ui.IWizardContext;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.ui.Messages;

/**
 * @author snjeza
 */
public class PortletFacetInstallPage extends DataModelWizardPage implements
		IFacetWizardPage {

	public PortletFacetInstallPage() {
		super(DataModelFactory.createDataModel(new AbstractDataModelProvider() {
		}), "jboss.portal.facet.install.page"); //$NON-NLS-1$
		setTitle(Messages.PortletFacetInstallPage_JBoss_Portlet_Capabilities);
		setDescription(Messages.PortletFacetInstallPage_Add_JBoss_Portlet_capabilities_to_this_Web_Project);
	}

	@Override
	protected Composite createTopLevelComposite(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		GridData gd = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gd);
		LibraryInstallDelegate librariesInstallDelegate= (LibraryInstallDelegate) getDataModel().getProperty( IPortletConstants.PORTLET_LIBRARY_PROVIDER_DELEGATE );
		Control librariesComposite= LibraryProviderFrameworkUi.createInstallLibraryPanel( composite, librariesInstallDelegate,
	                                                            Messages.PortletFacetInstallPage_PortletImplementationLibrariesFrame );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		librariesComposite.setLayoutData( gd );
	
		return composite;
	}

	@Override
	protected String[] getValidationPropertyNames() {
		return new String[] { IPortletConstants.PORTLET_LIBRARY_PROVIDER_DELEGATE };
	}

	public void setConfig(Object config) {
		model.removeListener(this);
		
		model = (IDataModel) config;
		model.addListener(this);
	}

	@Override
	public void dispose() {
		model.removeListener(this);
		super.dispose();
	}

	public void setWizardContext(IWizardContext context) {

	}

	public void transferStateToConfig() {

	}

}
