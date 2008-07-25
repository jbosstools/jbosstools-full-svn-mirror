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
 
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jst.j2ee.internal.wizard.StringArrayTableWizardSection;
import org.eclipse.jst.servlet.ui.internal.wizard.StringArrayTableWizardSectionCallback;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelWizardPage;
import org.eclipse.wst.common.frameworks.internal.plugin.WTPCommonPlugin;
import org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties;
import org.jboss.tools.portlet.ui.IPortletUIConstants;

/**
 * Portlet Wizard Setting Page
 */
public class AddPortletWizardPage extends DataModelWizardPage {
	
	private Text nameText;

	public AddPortletWizardPage(IDataModel model, String pageName) {
		super(model, pageName);
		setDescription(IPortletUIConstants.ADD_PORTLET_WIZARD_PAGE_DESC);
		this.setTitle(IPortletUIConstants.NEW_PORTLET_WIZARD_PAGE_TITLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jem.util.ui.wizard.WTPWizardPage#getValidationPropertyNames()
	 */
	protected String[] getValidationPropertyNames() {
		return new String[] { DISPLAY_NAME, INIT_PARAM };
	}

	protected Composite createTopLevelComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout());
		GridData data = new GridData(GridData.FILL_BOTH);
		data.widthHint = 300;
		composite.setLayoutData(data);

		createNameDescription(composite);
		
		createModesComposite(composite);
		
		StringArrayTableWizardSectionCallback callback = new StringArrayTableWizardSectionCallback();
		StringArrayTableWizardSection initSection = new StringArrayTableWizardSection(
				composite, 
				INIT_PARAM_LABEL, 
				INIT_PARAM_TITLE, 
				ADD_BUTTON_LABEL, 
				EDIT_BUTTON_LABEL, 
				REMOVE_BUTTON_LABEL, 
				new String[] { NAME_TITLE, VALUE_TITLE, DESCRIPTION_TITLE }, 
				new String[] { NAME_LABEL, VALUE_LABEL, DESCRIPTION_LABEL }, 
				null,// WebPlugin.getDefault().getImage("initializ_parameter"),
				model, 
				INIT_PARAM);
		initSection.setCallback(callback);
		
		String text = nameText.getText();
		
		nameText.setFocus();

		IStatus projectStatus = validateProjectName();
		if (!projectStatus.isOK()) {
			setErrorMessage(projectStatus.getMessage());
			composite.setEnabled(false);
		}
	    Dialog.applyDialogFont(parent);
		return composite;
	}

	private void createModesComposite(Composite parent) {
		Group composite = new Group(parent,SWT.NULL);
		composite.setLayout(new GridLayout(3,false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setText("Portlet Modes");
		
		Button viewModeButton = new Button(composite,SWT.CHECK);
		viewModeButton.setText("View");
		synchHelper.synchCheckbox(viewModeButton, INewPortletClassDataModelProperties.VIEW_MODE, null);
		
		Button editModeButton = new Button(composite,SWT.CHECK);
		editModeButton.setText("Edit");
		synchHelper.synchCheckbox(editModeButton, INewPortletClassDataModelProperties.EDIT_MODE, null);

		Button helpModeButton = new Button(composite,SWT.CHECK);
		helpModeButton.setText("Help");
		synchHelper.synchCheckbox(helpModeButton, INewPortletClassDataModelProperties.HELP_MODE, null);

	}

	protected IStatus validateProjectName() {
		// check for empty
		if (model.getStringProperty(PROJECT_NAME) == null || model.getStringProperty(PROJECT_NAME).trim().length() == 0) {
			return WTPCommonPlugin.createErrorStatus(NO_WEB_PROJECTS);
		}
		return WTPCommonPlugin.OK_STATUS;
	}

	protected void createNameDescription(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		// name
		Label nameLabel = new Label(composite, SWT.LEFT);
		nameLabel.setText(NAME_LABEL);
		nameLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		nameText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		synchHelper.synchText(nameText, NAME, null);

		// display name
		Label displayNameLabel = new Label(composite, SWT.LEFT);
		displayNameLabel.setText(IPortletUIConstants.DISPLAY_NAME_LABEL);
		displayNameLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		Text displayNameText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		displayNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		synchHelper.synchText(displayNameText, DISPLAY_NAME, null);
		
		// title
		Label titleLabel = new Label(composite, SWT.LEFT);
		titleLabel.setText(IPortletUIConstants.TITLE_LABEL);
		titleLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		Text titleText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		titleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		synchHelper.synchText(titleText, TITLE, null);

		// description
		Label descLabel = new Label(composite, SWT.LEFT);
		descLabel.setText(DESCRIPTION_LABEL);
		descLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		Text descText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		descText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		synchHelper.synchText(descText, DESCRIPTION, null);
	}

	public String getDisplayName() {
		return nameText.getText();
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
