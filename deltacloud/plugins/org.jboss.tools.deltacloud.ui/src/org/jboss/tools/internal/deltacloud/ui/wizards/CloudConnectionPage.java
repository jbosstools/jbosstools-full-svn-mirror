/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.internal.deltacloud.ui.wizards;

import java.net.URL;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;
import org.jboss.tools.deltacloud.ui.common.databinding.validator.MandatoryStringValidator;

public class CloudConnectionPage extends WizardPage {

	private static final String DESCRIPTION = "NewCloudConnection.desc"; //$NON-NLS-1$
	private static final String TITLE = "NewCloudConnection.title"; //$NON-NLS-1$
	private static final String URL_LABEL = "Url.label"; //$NON-NLS-1$
	private static final String NAME_LABEL = "Name.label"; //$NON-NLS-1$
	private static final String USERNAME_LABEL = "UserName.label"; //$NON-NLS-1$
	private static final String TYPE_LABEL = "Type.label"; //$NON-NLS-1$
	private static final String PASSWORD_LABEL = "Password.label"; //$NON-NLS-1$
	private static final String TESTBUTTON_LABEL = "TestButton.label"; //$NON-NLS-1$
	private static final String EC2_USER_INFO = "EC2UserNameLink.text"; //$NON-NLS-1$
	private static final String EC2_PASSWORD_INFO = "EC2PasswordLink.text"; //$NON-NLS-1$
	private static final String NAME_ALREADY_IN_USE = "ErrorNameInUse.text"; //$NON-NLS-1$

	private static final String TEST_SUCCESSFUL = "NewCloudConnectionTest.success"; //$NON-NLS-1$
	private static final String TEST_FAILURE = "NewCloudConnectionTest.failure"; //$NON-NLS-1$

	private CloudConnection wizard;

	// private Button testButton;

	private Text nameText;
	private Text urlText;
	private Label typeLabel;
	private Text usernameText;
	private Text passwordText;

	private String name;
	private String url;
	private String username;
	private String password;
	private String cloudTypeLabel;

	private String defaultName = "";
	private String defaultURL = "";
	private String defaultUsername = "";
	private String defaultPassword = "";

	private CloudConnectionModel connectionModel;

	private Listener linkListener = new Listener() {

		public void handleEvent(Event event) {
			try {
				URL url = new URL(event.text);
				PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(url);
			} catch (Exception e) {
				Activator.log(e);
			}
		}

	};

	private SelectionListener buttonListener = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {
			// boolean successful = false;
			// if (getURLValid()) {
			// successful = wizard.performTest();
			// }
			// if (successful) {
			// setMessage(WizardMessages.getString(TEST_SUCCESSFUL));
			// } else {
			// setErrorMessage(WizardMessages.getString(TEST_FAILURE));
			// }
		}

	};

	public CloudConnectionPage(String pageName, CloudConnection wizard) {
		super(pageName);
		this.wizard = wizard;
		setDescription(WizardMessages.getString(DESCRIPTION));
		setTitle(WizardMessages.getString(TITLE));
		setImageDescriptor(SWTImagesFactory.DESC_DELTA_LARGE);
		// setPageComplete(false);
		this.connectionModel = new CloudConnectionModel();
	}

	public CloudConnectionPage(String pageName, String defaultName, String defaultURL,
			String defaultUsername, String defaultPassword, String defaultCloudType,
			CloudConnection wizard) {
		super(pageName);
		this.wizard = wizard;
		this.defaultName = defaultName;
		this.defaultURL = defaultURL;
		this.defaultUsername = defaultUsername;
		this.defaultPassword = defaultPassword;
		this.cloudTypeLabel = defaultCloudType;
		setDescription(WizardMessages.getString(DESCRIPTION));
		setTitle(WizardMessages.getString(TITLE));
		setImageDescriptor(SWTImagesFactory.DESC_DELTA_LARGE);
		setPageComplete(false);
	}

	public String getName() {
		return connectionModel.getName();
	}

	public String getURL() {
		return connectionModel.getUrl();
	}

	public String getUsername() {
		return connectionModel.getUsername();
	}

	public String getPassword() {
		return connectionModel.getPassword();
	}

	public String getType() {
		return connectionModel.getCloudType();
	}

	@Override
	public void createControl(Composite parent) {
		DataBindingContext dbc = new DataBindingContext();
		WizardPageSupport.create(this, dbc);

		final Composite container = new Composite(parent, SWT.NULL);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		container.setLayout(layout);

		Label dummyLabel = new Label(container, SWT.NULL);

		Label nameLabel = new Label(container, SWT.NULL);
		nameLabel.setText(WizardMessages.getString(NAME_LABEL));
		nameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		nameText.setText(defaultName);
		Binding nameTextBinding = dbc.bindValue(
				WidgetProperties.text(SWT.Modify).observe(nameText),
				BeanProperties.value(CloudConnectionModel.class, CloudConnectionModel.PROPERTY_NAME)
						.observe(connectionModel),
				new UpdateValueStrategy().setBeforeSetValidator(new MandatoryStringValidator("name must be defined")),
				null);
		ControlDecorationSupport.create(nameTextBinding, SWT.LEFT|SWT.TOP);

		Label urlLabel = new Label(container, SWT.NULL);
		urlLabel.setText(WizardMessages.getString(URL_LABEL));
		Point p1 = urlLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		urlText = new Text(container, SWT.BORDER | SWT.SINGLE);
		Binding urlTextBinding = dbc.bindValue(
				WidgetProperties.text(SWT.Modify).observeDelayed(500, urlText),
				BeanProperties.value(CloudConnectionModel.class, CloudConnectionModel.PROPERTY_URL)
						.observe(connectionModel));
		
		final Label typeLabel = new Label(container, SWT.NULL);
		UpdateValueStrategy url2TypeStrategy = new UpdateValueStrategy();
		url2TypeStrategy.setConverter(new CloudConnectionModel.CloudTypeConverter());
		url2TypeStrategy.setBeforeSetValidator(new CloudConnectionModel.CloudTypeValidator());
		Binding urlBinding = dbc.bindValue(
				WidgetProperties.text(SWT.Modify).observeDelayed(100, urlText),
				BeanProperties.value(CloudConnectionModel.PROPERTY_CLOUDTYPE).observe(connectionModel),
				url2TypeStrategy,
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER));
		urlBinding.getValidationStatus().addValueChangeListener(new IValueChangeListener() {

			@Override
			public void handleValueChange(ValueChangeEvent event) {
				IStatus status = (IStatus) event.diff.getNewValue();
				if (status.isOK()) {
					typeLabel.setText(connectionModel.getCloudType());
				} else {
					typeLabel.setText("");
				}
			}
		});
		ControlDecorationSupport.create(urlBinding, SWT.LEFT|SWT.TOP);

		Label usernameLabel = new Label(container, SWT.NULL);
		usernameLabel.setText(WizardMessages.getString(USERNAME_LABEL));

		DataBindingContext credentialsDbc = new DataBindingContext();

		usernameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		credentialsDbc.bindValue(
				WidgetProperties.text(SWT.Modify).observe(usernameText),
				BeanProperties.value(CloudConnectionModel.class, CloudConnectionModel.PROPERTY_USERNAME).observe(
						connectionModel));

		Label passwordLabel = new Label(container, SWT.NULL);
		passwordLabel.setText(WizardMessages.getString(PASSWORD_LABEL));

		passwordText = new Text(container, SWT.BORDER | SWT.PASSWORD | SWT.SINGLE);
		credentialsDbc.bindValue(
				WidgetProperties.text(SWT.Modify).observe(passwordText),
				BeanProperties.value(CloudConnectionModel.class, CloudConnectionModel.PROPERTY_PASSWORD).observe(
						connectionModel));

		final Button testButton = new Button(container, SWT.NULL);
		testButton.setText(WizardMessages.getString(TESTBUTTON_LABEL));
		testButton.setEnabled(false);
		urlBinding.getValidationStatus().addValueChangeListener(new IValueChangeListener() {

			@Override
			public void handleValueChange(ValueChangeEvent event) {
				IStatus status = (IStatus) event.diff.getNewValue();
				testButton.setEnabled(status.isOK());
			}
		});
		testButton.addSelectionListener(buttonListener);

		Link ec2userLink = new Link(container, SWT.NULL);
		ec2userLink.setText(WizardMessages.getString(EC2_USER_INFO));
		ec2userLink.addListener(SWT.Selection, linkListener);

		Link ec2pwLink = new Link(container, SWT.NULL);
		ec2pwLink.setText(WizardMessages.getString(EC2_PASSWORD_INFO));
		ec2pwLink.addListener(SWT.Selection, linkListener);

		FormData f = new FormData();
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(100, 0);
		dummyLabel.setLayoutData(f);

		Point p2 = urlText.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int centering = (p2.y - p1.y + 1) / 2;

		f = new FormData();
		f.top = new FormAttachment(dummyLabel, 8 + centering);
		nameLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(dummyLabel, 8);
		f.left = new FormAttachment(usernameLabel, 5);
		f.right = new FormAttachment(100, 0);
		nameText.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(nameText, 5 + centering);
		urlLabel.setLayoutData(f);

		f = new FormData();
		f.left = new FormAttachment(nameText, 0, SWT.LEFT);
		f.top = new FormAttachment(nameText, 5);
		f.right = new FormAttachment(100, 0);
		urlText.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(urlText, 5 + centering);
		typeLabel.setLayoutData(f);

		f = new FormData();
		f.left = new FormAttachment(urlText, 0, SWT.LEFT);
		f.top = new FormAttachment(urlText, 5 + centering);
		f.right = new FormAttachment(100, 0);
		typeLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(typeLabel, 10 + centering);
		usernameLabel.setLayoutData(f);

		f = new FormData();
		f.left = new FormAttachment(typeLabel, 0, SWT.LEFT);
		f.top = new FormAttachment(typeLabel, 10);
		f.right = new FormAttachment(100, -70);
		usernameText.setLayoutData(f);

		f = new FormData();
		f.left = new FormAttachment(usernameText, 0, SWT.LEFT);
		f.top = new FormAttachment(usernameText, 5);
		ec2userLink.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(ec2userLink, 5 + centering);
		passwordLabel.setLayoutData(f);

		f = new FormData();
		f.left = new FormAttachment(usernameText, 0, SWT.LEFT);
		f.top = new FormAttachment(ec2userLink, 5);
		f.right = new FormAttachment(100, -70);
		passwordText.setLayoutData(f);

		f = new FormData();
		int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		Point minSize = testButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		f.width = Math.max(widthHint, minSize.x);
		f.left = new FormAttachment(usernameText, 10);
		f.top = new FormAttachment(usernameText, 0);
		f.right = new FormAttachment(100, 0);
		testButton.setLayoutData(f);

		f = new FormData();
		f.left = new FormAttachment(passwordText, 0, SWT.LEFT);
		f.top = new FormAttachment(passwordText, 5);
		ec2pwLink.setLayoutData(f);

		setControl(container);
		// validate();
	}
}
