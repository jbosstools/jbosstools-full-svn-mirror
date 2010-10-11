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
import java.text.MessageFormat;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.jboss.tools.common.log.LogHelper;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;
import org.jboss.tools.deltacloud.ui.common.databinding.validator.CompositeValidator;
import org.jboss.tools.deltacloud.ui.common.databinding.validator.MandatoryStringValidator;

public class CloudConnectionPage extends WizardPage {

	private static final String DESCRIPTION = "NewCloudConnection.desc"; //$NON-NLS-1$
	private static final String TITLE = "NewCloudConnection.title"; //$NON-NLS-1$
	private static final String URL_LABEL = "Url.label"; //$NON-NLS-1$
	private static final String NAME_LABEL = "Name.label"; //$NON-NLS-1$
	private static final String USERNAME_LABEL = "UserName.label"; //$NON-NLS-1$
	private static final String PASSWORD_LABEL = "Password.label"; //$NON-NLS-1$
	private static final String TESTBUTTON_LABEL = "TestButton.label"; //$NON-NLS-1$
	private static final String EC2_USER_INFO = "EC2UserNameLink.text"; //$NON-NLS-1$
	private static final String EC2_PASSWORD_INFO = "EC2PasswordLink.text"; //$NON-NLS-1$
	private static final String NAME_ALREADY_IN_USE = "ErrorNameInUse.text"; //$NON-NLS-1$
	private static final String COULD_NOT_OPEN_BROWSER = "ErrorCouldNotOpenBrowser.text"; //$NON-NLS-1$
	private static final String TEST_SUCCESSFUL = "NewCloudConnectionTest.success"; //$NON-NLS-1$
	private static final String TEST_FAILURE = "NewCloudConnectionTest.failure"; //$NON-NLS-1$

	private String defaultName = ""; //$NON-NLS-1$
	private String defaultUrl = ""; //$NON-NLS-1$
	private String defaultUsername = ""; //$NON-NLS-1$
	private String defaultPassword = ""; //$NON-NLS-1$
	private String defaultType = ""; //$NON-NLS-1$
	
	private CloudConnectionModel connectionModel;
	private CloudConnection cloudConnection;
	
	private Listener linkListener = new Listener() {

		public void handleEvent(Event event) {
			String urlString = event.text;
			try {
				URL url = new URL(urlString);
				PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(url);
			} catch (Exception e) {
				LogHelper.logError(Activator.getDefault(), WizardMessages.getFormattedString(COULD_NOT_OPEN_BROWSER, urlString), e);
			}
		}
	};

	public CloudConnectionPage(String pageName, CloudConnection cloudConnection) {
		super(pageName);
		setDescription(WizardMessages.getString(DESCRIPTION));
		setTitle(WizardMessages.getString(TITLE));
		setImageDescriptor(SWTImagesFactory.DESC_DELTA_LARGE);
		this.connectionModel = new CloudConnectionModel();
		this.cloudConnection = cloudConnection;
	}

	public CloudConnectionPage(String pageName, String defaultName, String defaultUrl,
			String defaultUsername, String defaultPassword, String defaultType,
			CloudConnection cloudConnection) {
		super(pageName);
		this.defaultName = defaultName;
		this.defaultUrl = defaultUrl;
		this.defaultUsername = defaultUsername;
		this.defaultPassword = defaultPassword;
		this.defaultType = defaultType;
		this.connectionModel = new CloudConnectionModel();
		this.cloudConnection = cloudConnection;
		setDescription(WizardMessages.getString(DESCRIPTION));
		setTitle(WizardMessages.getString(TITLE));
		setImageDescriptor(SWTImagesFactory.DESC_DELTA_LARGE);
		setPageComplete(false);
	}

	public CloudConnectionModel getModel() {
		return connectionModel;
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

		// name
		Label nameLabel = new Label(container, SWT.NULL);
		nameLabel.setText(WizardMessages.getString(NAME_LABEL));
		Text nameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		nameText.setText(defaultName);
		bindName(dbc, nameText);

		// url
		Label urlLabel = new Label(container, SWT.NULL);
		urlLabel.setText(WizardMessages.getString(URL_LABEL));
		Point p1 = urlLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Text urlText = new Text(container, SWT.BORDER | SWT.SINGLE);
		urlText.setText(defaultUrl);
		dbc.bindValue(
				WidgetProperties.text(SWT.Modify).observeDelayed(500, urlText),
				BeanProperties.value(CloudConnectionModel.class, CloudConnectionModel.PROPERTY_URL)
				.observe(connectionModel));

		// cloud type
		Label typeLabel = new Label(container, SWT.NULL);
		typeLabel.setText(defaultType);
		UpdateValueStrategy url2TypeStrategy = new UpdateValueStrategy();
		url2TypeStrategy.setConverter(new CloudConnectionModel.CloudTypeConverter());
		url2TypeStrategy.setBeforeSetValidator(new CloudConnectionModel.CloudTypeValidator());
		Binding urlBinding = bindCloudTypeLabel(dbc, urlText, typeLabel, url2TypeStrategy);

		Label usernameLabel = new Label(container, SWT.NULL);
		usernameLabel.setText(WizardMessages.getString(USERNAME_LABEL));

		DataBindingContext credentialsDbc = new DataBindingContext();

		// username
		Text usernameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		usernameText.setText(defaultUsername);
		credentialsDbc.bindValue(
				WidgetProperties.text(SWT.Modify).observe(usernameText),
				BeanProperties.value(CloudConnectionModel.class, CloudConnectionModel.PROPERTY_USERNAME).observe(
						connectionModel));

		// password
		Label passwordLabel = new Label(container, SWT.NULL);
		passwordLabel.setText(WizardMessages.getString(PASSWORD_LABEL));
		Text passwordText = new Text(container, SWT.BORDER | SWT.PASSWORD | SWT.SINGLE);
		passwordText.setText(defaultPassword);
		credentialsDbc.bindValue(
				WidgetProperties.text(SWT.Modify).observe(passwordText),
				BeanProperties.value(CloudConnectionModel.class, CloudConnectionModel.PROPERTY_PASSWORD).observe(
						connectionModel));
		// test button
		final Button testButton = new Button(container, SWT.NULL);
		testButton.setText(WizardMessages.getString(TESTBUTTON_LABEL));
		testButton.setEnabled(false);
		bindTestButton(urlBinding, testButton);

		// ec2 user link
		Link ec2userLink = new Link(container, SWT.NULL);
		ec2userLink.setText(WizardMessages.getString(EC2_USER_INFO));
		ec2userLink.addListener(SWT.Selection, linkListener);

		// ec2 pw link
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

	private void bindTestButton(Binding urlBinding, final Button testButton) {
		urlBinding.getValidationStatus().addValueChangeListener(new IValueChangeListener() {

			@Override
			public void handleValueChange(ValueChangeEvent event) {
				IStatus status = (IStatus) event.diff.getNewValue();
				testButton.setEnabled(status.isOK());
			}
		});
		
		testButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				 boolean successful = cloudConnection.performTest();
				 if (successful) {
				 setMessage(WizardMessages.getString(TEST_SUCCESSFUL));
				 } else {
				 setErrorMessage(WizardMessages.getString(TEST_FAILURE));
				 }
			}
		});
	}

	private Binding bindCloudTypeLabel(DataBindingContext dbc, Text urlText, final Label typeLabel,
			UpdateValueStrategy url2TypeStrategy) {
		Binding urlBinding = dbc.bindValue(
				WidgetProperties.text(SWT.Modify).observeDelayed(100, urlText),
				BeanProperties.value(CloudConnectionModel.PROPERTY_TYPE).observe(connectionModel),
				url2TypeStrategy,
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER));
		urlBinding.getValidationStatus().addValueChangeListener(new IValueChangeListener() {

			@Override
			public void handleValueChange(ValueChangeEvent event) {
				IStatus status = (IStatus) event.diff.getNewValue();
				if (status.isOK()) {
					typeLabel.setText(connectionModel.getType());
				} else {
					typeLabel.setText("");
				}
			}
		});
		ControlDecorationSupport.create(urlBinding, SWT.LEFT | SWT.TOP);
		return urlBinding;
	}

	private void bindName(DataBindingContext dbc, final Text nameText) {
		Binding nameTextBinding = dbc.bindValue(
				WidgetProperties.text(SWT.Modify).observe(nameText),
				BeanProperties.value(CloudConnectionModel.class, CloudConnectionModel.PROPERTY_NAME)
						.observe(connectionModel),
				new UpdateValueStrategy().setBeforeSetValidator(
						new CompositeValidator(
								new MandatoryStringValidator("name must be defined"),
								new IValidator() {

									@Override
									public IStatus validate(Object value) {
										if (nameText.getText() != null && DeltaCloudManager.getDefault().findCloud(nameText.getText()) != null) {
											return ValidationStatus.error(NAME_ALREADY_IN_USE);
										} else {
											return ValidationStatus.ok();
										}
									}
								})),
				null);
		ControlDecorationSupport.create(nameTextBinding, SWT.LEFT | SWT.TOP);
	}
}
