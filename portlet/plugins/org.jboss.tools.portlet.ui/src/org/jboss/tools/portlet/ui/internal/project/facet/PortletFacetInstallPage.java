package org.jboss.tools.portlet.ui.internal.project.facet;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelWizardPage;
import org.eclipse.wst.common.project.facet.ui.IFacetWizardPage;
import org.eclipse.wst.common.project.facet.ui.IWizardContext;
import org.jboss.tools.portlet.ui.Messages;
import org.jboss.tools.portlet.ui.PortletUIActivator;

/**
 * @author snjeza
 */
public class PortletFacetInstallPage extends DataModelWizardPage implements
		IFacetWizardPage {

	private IDialogSettings dialogSettings;

	public PortletFacetInstallPage() {
		super(DataModelFactory.createDataModel(new AbstractDataModelProvider() {
		}), "jboss.portal.facet.install.page"); //$NON-NLS-1$
		setTitle(Messages.PortletFacetInstallPage_JBoss_Portlet_Capabilities);
		setDescription(Messages.PortletFacetInstallPage_Add_JBoss_Portlet_capabilities_to_this_Web_Project);
		dialogSettings = PortletUIActivator.getDefault().getDialogSettings();
	}

	@Override
	protected Composite createTopLevelComposite(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = new Composite(parent, SWT.NONE);
		return composite;
	}

	@Override
	protected String[] getValidationPropertyNames() {
		return new String[0];
	}

	public void setConfig(Object config) {
		model.removeListener(this);
		synchHelper.dispose();

		model = (IDataModel) config;
		model.addListener(this);
		synchHelper = initializeSynchHelper(model);
	}

	public void setWizardContext(IWizardContext context) {
		// TODO Auto-generated method stub

	}

	public void transferStateToConfig() {
		// TODO Auto-generated method stub

	}

}
