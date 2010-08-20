package org.jboss.tools.portlet.ui.internal.wizard;

import static org.eclipse.jst.j2ee.internal.web.operations.INewWebClassDataModelProperties.USE_EXISTING_CLASS;
import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.NO_WEB_PROJECTS;
import static org.eclipse.wst.common.componentcore.internal.operation.IArtifactEditOperationDataModelProperties.PROJECT_NAME;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.ADD_JBOSS_APP;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.ADD_JBOSS_PORTLET;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.ADD_PORTLET;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.CONFIGURE_GATEIN_PARAMETERS;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.COPY_JSF_TEMPLATES;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.IF_EXISTS;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.INITIAL_WINDOW_STATE;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.INSTANCE_NAME;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.IS_JSF_PORTLET;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.IS_SEAM_PORTLET;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.JBOSS_APP;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.PAGE_NAME;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.PAGE_REGION;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.PARENT_PORTAL;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.PORTLET_HEIGHT;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.WINDOW_NAME;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.ADD_JBOSS_APP_LABEL;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.ADD_JBOSS_PORTLET_LABEL;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.ADD_PORTLET_LABEL;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.CONFIGURE_GATEIN_PARAMETERS_LABEL;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.COPY_JSF_TEMPLATES_LABEL;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.IF_EXISTS_LABEL;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.INITIAL_WINDOW_STATE_LABEL;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.INSTANCE_NAME_LABEL;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.JBOSS_APP_LABEL;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.PAGE_NAME_LABEL;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.PAGE_REGION_LABEL;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.PARENT_PORTAL_LABEL;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.PORTLET_HEIGHT_LABEL;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.WINDOW_NAME_LABEL;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelWizardPage;
import org.eclipse.wst.common.frameworks.internal.plugin.WTPCommonPlugin;
import org.jboss.tools.portlet.ui.IPortletUIConstants;
import org.jboss.tools.portlet.ui.PortletUIActivator;

/**
 * JBoss Portlet Wizard Setting Page
 * 
 * @author snjeza
 */
public class AddJBossPortletWizardPage extends DataModelWizardPage {
	
	private Combo ifExistsCombo;
	private Text instanceNameText;
	private Text pageNameText;
	private Text windowNameText;
	private Text parentReferenceText;
	private Text regionText;
	private Text heightText;
	private Combo initialWindowStateCombo;
	private Button addPortlet;

	public AddJBossPortletWizardPage(IDataModel model, String pageName) {
		super(model, pageName);
		setDescription(IPortletUIConstants.ADD_JBOSS_PORTLET_WIZARD_PAGE_DESC);
		setTitle(IPortletUIConstants.ADD_JBOSS_PORTLET_WIZARD_PAGE_TITLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jem.util.ui.wizard.WTPWizardPage#getValidationPropertyNames()
	 */
	protected String[] getValidationPropertyNames() {
		return new String[] { INSTANCE_NAME, WINDOW_NAME };
	}

	protected Composite createTopLevelComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout());
		GridData data = new GridData(GridData.FILL_BOTH);
		data.widthHint = 300;
		composite.setLayoutData(data);

		createComponents(composite);
		
		IStatus projectStatus = validateProjectName();
		if (!projectStatus.isOK()) {
			setErrorMessage(projectStatus.getMessage());
			composite.setEnabled(false);
		}
	    Dialog.applyDialogFont(parent);
		return composite;
	}

	protected IStatus validateProjectName() {
		// check for empty
		if (model.getStringProperty(PROJECT_NAME) == null || model.getStringProperty(PROJECT_NAME).trim().length() == 0) {
			return WTPCommonPlugin.createErrorStatus(NO_WEB_PROJECTS);
		}
		return WTPCommonPlugin.OK_STATUS;
	}

	protected void createComponents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		boolean isJBossPortal = PortletUIActivator.isJBossPortalRuntime(model);
		model.setBooleanProperty(ADD_PORTLET, isJBossPortal);
		boolean isGateIn = PortletUIActivator.isGateIn(model);
		if (isGateIn) {
			model.setBooleanProperty(ADD_JBOSS_APP, false);
			model.setBooleanProperty(ADD_JBOSS_PORTLET, false);
		}
		GridData gd;
		if (isJBossPortal) {
			addPortlet = new Button(composite,SWT.CHECK);
			addPortlet.setText(ADD_PORTLET_LABEL);
			gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
			gd.horizontalSpan = 2;
			addPortlet.setLayoutData(gd);
			synchHelper.synchCheckbox(addPortlet, ADD_PORTLET, null);
		
			// if exists
			Label ifExistsLabel = new Label(composite, SWT.LEFT);
			ifExistsLabel.setText(IF_EXISTS_LABEL);
			ifExistsLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
			ifExistsCombo = new Combo(composite,SWT.READ_ONLY);
			ifExistsCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
			ifExistsCombo.setItems(new String[] {"overwrite","keep"}); //$NON-NLS-1$ //$NON-NLS-2$
			synchHelper.synchCombo(ifExistsCombo, IF_EXISTS, null);
		
			// instance name
			Label instanceNameLabel = new Label(composite, SWT.LEFT);
			instanceNameLabel.setText(INSTANCE_NAME_LABEL);
			instanceNameLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
			instanceNameText = new Text(composite, SWT.SINGLE | SWT.BORDER);
			instanceNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			synchHelper.synchText(instanceNameText, INSTANCE_NAME, null);

			// window name
			Label pageNameLabel = new Label(composite, SWT.LEFT);
			pageNameLabel.setText(PAGE_NAME_LABEL);
			pageNameLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
			pageNameText = new Text(composite, SWT.SINGLE | SWT.BORDER);
			pageNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			synchHelper.synchText(pageNameText, PAGE_NAME, null);
		
			// window name
			Label windowNameLabel = new Label(composite, SWT.LEFT);
			windowNameLabel.setText(WINDOW_NAME_LABEL);
			windowNameLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
			windowNameText = new Text(composite, SWT.SINGLE | SWT.BORDER);
			windowNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			synchHelper.synchText(windowNameText, WINDOW_NAME, null);
		
			// parent reference
			Label parentReferenceLabel = new Label(composite, SWT.LEFT);
			parentReferenceLabel.setText(PARENT_PORTAL_LABEL);
			parentReferenceLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
			parentReferenceText = new Text(composite, SWT.SINGLE | SWT.BORDER);
			parentReferenceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			synchHelper.synchText(parentReferenceText, PARENT_PORTAL, null);

			// region
			Label regionLabel = new Label(composite, SWT.LEFT);
			regionLabel.setText(PAGE_REGION_LABEL);
			regionLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
			regionText = new Text(composite, SWT.SINGLE | SWT.BORDER);
			regionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			synchHelper.synchText(regionText, PAGE_REGION, null);
		
			// height
			Label heightLabel = new Label(composite, SWT.LEFT);
			heightLabel.setText(PORTLET_HEIGHT_LABEL);
			heightLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
			heightText = new Text(composite, SWT.SINGLE | SWT.BORDER);
			heightText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			synchHelper.synchText(heightText, PORTLET_HEIGHT, null);
		
			// initial window state
			Label initialWindowStateLabel = new Label(composite, SWT.LEFT);
			initialWindowStateLabel.setText(INITIAL_WINDOW_STATE_LABEL);
			initialWindowStateLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
			initialWindowStateCombo = new Combo(composite,SWT.READ_ONLY);
			initialWindowStateCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
			initialWindowStateCombo.setItems(new String[] {"maximized","minimized","normal"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			synchHelper.synchCombo(initialWindowStateCombo, INITIAL_WINDOW_STATE, null);
		
			addPortlet.addSelectionListener(new SelectionAdapter() {

					public void widgetSelected(SelectionEvent e) {
						enableJBossProperties();
					}
			});
			enableJBossProperties();
		
			if (isJSFPortlet() || isSeamPortlet()) {
				final Button addJBossApp = new Button(composite, SWT.CHECK);
				addJBossApp.setText(ADD_JBOSS_APP_LABEL);
				gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
				gd.horizontalSpan = 2;
				addJBossApp.setLayoutData(gd);
				synchHelper.synchCheckbox(addJBossApp, ADD_JBOSS_APP, null);
			
				// JBoss Application Name
				Label jbossAppLabel = new Label(composite, SWT.LEFT);
				jbossAppLabel.setText(JBOSS_APP_LABEL);
				jbossAppLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
				final Text jbossAppText = new Text(composite, SWT.SINGLE | SWT.BORDER);
				jbossAppText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				synchHelper.synchText(jbossAppText, JBOSS_APP, null);
			
				addJBossApp.addSelectionListener(new SelectionAdapter() {

					public void widgetSelected(SelectionEvent e) {
						boolean enable = addJBossApp.getSelection();
						jbossAppText.setEnabled(enable);
					
					}
				});
			
				addJBossApp.setSelection(isJBossPortal);
				jbossAppText.setEnabled(addJBossApp.getSelection());
			
				final Button addJBossPortlet = new Button(composite, SWT.CHECK);
				addJBossPortlet.setText(ADD_JBOSS_PORTLET_LABEL);
				gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
				gd.horizontalSpan = 2;
				addJBossPortlet.setLayoutData(gd);
				synchHelper.synchCheckbox(addJBossPortlet, ADD_JBOSS_PORTLET, null);
			}
		}
			
		final Button copyJSFTemplates = new Button(composite, SWT.CHECK);
		copyJSFTemplates.setText(COPY_JSF_TEMPLATES_LABEL);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 2;
		copyJSFTemplates.setLayoutData(gd);
		synchHelper.synchCheckbox(copyJSFTemplates, COPY_JSF_TEMPLATES, null);
			
		model.setBooleanProperty(CONFIGURE_GATEIN_PARAMETERS, isGateIn);
			
		final Button configureGateIn = new Button(composite, SWT.CHECK);
		configureGateIn.setText(CONFIGURE_GATEIN_PARAMETERS_LABEL);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 2;
		configureGateIn.setLayoutData(gd);
		synchHelper.synchCheckbox(configureGateIn, CONFIGURE_GATEIN_PARAMETERS, null);
			
		
	}
	
	public boolean canFlipToNextPage() {
		if (model.getBooleanProperty(USE_EXISTING_CLASS))
			return false;
		return super.canFlipToNextPage();
	}
	
	@Override
	protected boolean showValidationErrorsOnEnter() {
		return true;
	}
	
	protected boolean isJSFPortlet() {
		return model.getBooleanProperty(IS_JSF_PORTLET);
	}
	
	protected boolean isSeamPortlet() {
		return model.getBooleanProperty(IS_SEAM_PORTLET);
	}

	private void enableJBossProperties() {
		boolean enable = addPortlet.getSelection();
		ifExistsCombo.setEnabled(enable);
		instanceNameText.setEnabled(enable);
		windowNameText.setEnabled(enable);
		parentReferenceText.setEnabled(enable);
		regionText.setEnabled(enable);
		heightText.setEnabled(enable);
		initialWindowStateCombo.setEnabled(enable);
		pageNameText.setEnabled(enable);
	}
}
