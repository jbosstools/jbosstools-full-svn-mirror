package org.jboss.tools.portlet.ui.internal.wizard;

import static org.eclipse.jst.j2ee.internal.web.operations.INewServletClassDataModelProperties.INIT_PARAM;
import static org.eclipse.jst.j2ee.internal.web.operations.INewWebClassDataModelProperties.DESCRIPTION;
import static org.eclipse.jst.j2ee.internal.web.operations.INewWebClassDataModelProperties.DISPLAY_NAME;

import static org.eclipse.jst.j2ee.internal.web.operations.INewWebClassDataModelProperties.USE_EXISTING_CLASS;
import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.ADD_BUTTON_LABEL;
import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.DESCRIPTION_LABEL;
import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.DESCRIPTION_TITLE;
import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.EDIT_BUTTON_LABEL;
import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.INIT_PARAM_LABEL;
import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.INIT_PARAM_TITLE;
import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.NAME_LABEL;
import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.NAME_TITLE;
import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.NO_WEB_PROJECTS;
import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.REMOVE_BUTTON_LABEL;
import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.VALUE_LABEL;
import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.VALUE_TITLE;
import static org.eclipse.wst.common.componentcore.internal.operation.IArtifactEditOperationDataModelProperties.PROJECT_NAME;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.NAME;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.TITLE;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.INSTANCE_NAME;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.WINDOW_NAME;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.IF_EXISTS;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.PAGE_REGION;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.PARENT_PORTAL;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.PORTLET_HEIGHT;

import static org.jboss.tools.portlet.ui.IPortletUIConstants.INSTANCE_NAME_LABEL;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.WINDOW_NAME_LABEL;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.IF_EXISTS_LABEL;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.PAGE_REGION_LABEL;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.PARENT_PORTAL_LABEL;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.PORTLET_HEIGHT_LABEL;
 
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jst.j2ee.internal.wizard.StringArrayTableWizardSection;
import org.eclipse.jst.servlet.ui.internal.wizard.StringArrayTableWizardSectionCallback;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.componentcore.internal.operation.IArtifactEditOperationDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelWizardPage;
import org.eclipse.wst.common.frameworks.internal.plugin.WTPCommonPlugin;
import org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties;
import org.jboss.tools.portlet.ui.IPortletUIConstants;

/**
 * JBoss Portlet Wizard Setting Page
 * 
 * @author snjeza
 */
public class AddJBossPortletWizardPage extends DataModelWizardPage {
	
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
		
		// if exists
		Label ifExistsLabel = new Label(composite, SWT.LEFT);
		ifExistsLabel.setText(IF_EXISTS_LABEL);
		ifExistsLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		Combo ifExistsCombo = new Combo(composite,SWT.NONE);
		ifExistsCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		ifExistsCombo.setItems(new String[] {"overwrite","keep"});
		synchHelper.synchCombo(ifExistsCombo, IF_EXISTS, null);
		
		// instance name
		Label instanceNameLabel = new Label(composite, SWT.LEFT);
		instanceNameLabel.setText(INSTANCE_NAME_LABEL);
		instanceNameLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		Text instanceNameText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		instanceNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		synchHelper.synchText(instanceNameText, INSTANCE_NAME, null);

		// window name
		Label windowNameLabel = new Label(composite, SWT.LEFT);
		windowNameLabel.setText(WINDOW_NAME_LABEL);
		windowNameLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		Text windowNameText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		windowNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		synchHelper.synchText(windowNameText, WINDOW_NAME, null);
		
		// parent reference
		Label parentReferenceLabel = new Label(composite, SWT.LEFT);
		parentReferenceLabel.setText(PARENT_PORTAL_LABEL);
		parentReferenceLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		Text parentReferenceText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		parentReferenceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		synchHelper.synchText(parentReferenceText, PARENT_PORTAL, null);

		// region
		Label regionLabel = new Label(composite, SWT.LEFT);
		regionLabel.setText(PAGE_REGION_LABEL);
		regionLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		Text regionText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		regionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		synchHelper.synchText(regionText, PAGE_REGION, null);
		
		// height
		Label heightLabel = new Label(composite, SWT.LEFT);
		heightLabel.setText(PORTLET_HEIGHT_LABEL);
		heightLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		Text heightText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		heightText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		synchHelper.synchText(heightText, PORTLET_HEIGHT, null);
		
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
	
}
