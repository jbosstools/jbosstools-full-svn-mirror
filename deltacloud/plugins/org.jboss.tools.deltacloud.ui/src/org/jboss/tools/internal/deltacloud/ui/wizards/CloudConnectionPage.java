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

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.operation.IRunnableWithProgress;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.common.log.LogHelper;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.core.Driver;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;
import org.jboss.tools.internal.deltacloud.ui.common.databinding.validator.CompositeValidator;
import org.jboss.tools.internal.deltacloud.ui.common.databinding.validator.MandatoryStringValidator;
import org.jboss.tools.internal.deltacloud.ui.common.swt.JFaceUtils;
import org.jboss.tools.internal.deltacloud.ui.preferences.IPreferenceKeys;
import org.jboss.tools.internal.deltacloud.ui.preferences.StringPreferenceValue;
import org.jboss.tools.internal.deltacloud.ui.utils.ControlDecorationAdapter;
import org.jboss.tools.internal.deltacloud.ui.utils.DataBindingUtils;

/**
 * @author Jeff Jonhston
 * @author André Dietisheim
 */
public class CloudConnectionPage extends WizardPage {

	private static final int CLOUDTYPE_CHECK_DELAY = 1000;

	private static final String DESCRIPTION = "CloudConnection.desc"; //$NON-NLS-1$
	private static final String TITLE = "CloudConnection.title"; //$NON-NLS-1$
	private static final String TEST_SUCCESSFUL = "CloudConnectionTestSuccess.msg"; //$NON-NLS-1$
	private static final String TEST_FAILURE = "CloudConnectionTestFailure.msg"; //$NON-NLS-1$
	private static final String TESTING_CREDENTIALS = "CloudConnectionTestingCredentials.msg"; //$NON-NLS-1$;
	private static final String INVALID_CREDENTIALS = "CloudConnectionInvalidCredentials.msg"; //$NON-NLS-1$;
	private static final String URL_LABEL = "Url.label"; //$NON-NLS-1$
	private static final String NAME_LABEL = "Name.label"; //$NON-NLS-1$
	private static final String CLOUDTYPE_LABEL = "Type.label"; //$NON-NLS-1$
	private static final String USERNAME_LABEL = "UserName.label"; //$NON-NLS-1$
	private static final String PASSWORD_LABEL = "Password.label"; //$NON-NLS-1$
	private static final String TESTBUTTON_LABEL = "TestButton.label"; //$NON-NLS-1$
	private static final String EC2_USER_INFO = "EC2UserNameLink.text"; //$NON-NLS-1$
	private static final String EC2_PASSWORD_INFO = "EC2PasswordLink.text"; //$NON-NLS-1$
	private static final String NAME_ALREADY_IN_USE = "ErrorNameInUse.text"; //$NON-NLS-1$
	private static final String COULD_NOT_OPEN_BROWSER = "ErrorCouldNotOpenBrowser.text"; //$NON-NLS-1$
	private static final String MUST_ENTER_A_NAME = "ErrorMustNameConnection.text"; //$NON-NLS-1$
	private static final String MUST_ENTER_A_URL = "ErrorMustProvideUrl.text"; //$NON-NLS-1$;

	private CloudConnectionPageModel connectionModel;
	private CloudConnection cloudConnection;

	private Listener linkListener = new Listener() {

		public void handleEvent(Event event) {
			String urlString = event.text;
			try {
				URL url = new URL(urlString);
				PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(url);
			} catch (Exception e) {
				LogHelper.logError(Activator.getDefault(),
						WizardMessages.getFormattedString(COULD_NOT_OPEN_BROWSER, urlString), e);
			}
		}
	};

	/**
	 * jface databinding converter that converts Driver values to displayable
	 * text
	 * 
	 * @see Driver
	 */
	private static class Driver2Label extends Converter {

		private Driver2Label() {
			super(Driver.class, String.class);
		}

		@Override
		public Object convert(Object fromObject) {
			if (fromObject instanceof Driver) {
				return ((Driver) fromObject).name();
			}
			return null;
		}
	}

	/**
	 * A component that displays the result of the credentials test. Listens to
	 * clicks on the test-button (that allows you to test the credentials) and
	 * to changes in the username and password text fields.
	 * 
	 * @see CloudConnection#performTest()
	 * @see IValueChangeListener#handleValueChange(ValueChangeEvent)
	 * @see ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	private class CredentialsTestAdapter extends SelectionAdapter implements IValueChangeListener {

		private List<ControlDecoration> controlDecorations;

		public CredentialsTestAdapter(Control... controls) {
			this.controlDecorations = createDecorations(controls);
			showDecorations(false);
		}

		private List<ControlDecoration> createDecorations(Control... controls) {
			List<ControlDecoration> decorations = new ArrayList<ControlDecoration>();
			for (Control control : controls) {
				decorations.add(JFaceUtils.createDecoration(control, WizardMessages.getString(INVALID_CREDENTIALS)));
			}
			return decorations;
		}

		public void widgetSelected(SelectionEvent event) {
			try {
				getWizard().getContainer().run(true, true, onTestCredentials());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private IRunnableWithProgress onTestCredentials() {
			return new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask(WizardMessages.getString(TESTING_CREDENTIALS), 2);
					monitor.worked(1);
					final boolean success = cloudConnection.performTest();
					monitor.worked(2);
					getShell().getDisplay().syncExec(new Runnable() {

						@Override
						public void run() {
							setMessage(success);
							showDecorations(!success);
						}

					});
					monitor.done();
				}
			};
		}

		private void setMessage(boolean success) {
			if (success) {
				CloudConnectionPage.this.setMessage(WizardMessages.getString(TEST_SUCCESSFUL));
			} else {
				CloudConnectionPage.this.setErrorMessage(WizardMessages.getString(TEST_FAILURE));
			}
		}

		private void clearMessage() {
			CloudConnectionPage.this.setMessage(""); //$NON-NLS-1$
		}

		private void showDecorations(boolean visible) {
			for (ControlDecoration controlDecoration : controlDecorations) {
				if (visible) {
					controlDecoration.show();
				} else {
					controlDecoration.hide();
				}
			}
		}

		@Override
		public void handleValueChange(ValueChangeEvent event) {
			showDecorations(false);
			clearMessage();
		}
	}

	/**
	 * A validator that marks cloud names as invalid if the name the user
	 * entered is already used by another connection. It does not invalidate the
	 * name the connection had before editing.
	 * 
	 * @see IValidator
	 * @see CloudConnectionPageModel#getInitialName()
	 * @see DeltaCloudManager#findCloud(String)
	 * 
	 */
	private class CloudNameValidator implements IValidator {

		@Override
		public IStatus validate(Object value) {
			String connectionName = (String) value;
			/*
			 * keeping the same name when editing must be valid
			 */
			try {
				/* all new names must be unique */
				DeltaCloud deltaCloud = DeltaCloudManager.getDefault().findCloud(connectionName);
				if (cloudConnection.getDeltaCloud() == null) {
					if (deltaCloud == null) {
						return ValidationStatus.ok();
					}
				} else {
					if (deltaCloud == null
							|| cloudConnection.getDeltaCloud().equals(deltaCloud)) {
						return ValidationStatus.ok();
					}
				}
				return ValidationStatus
						.error(WizardMessages.getString(NAME_ALREADY_IN_USE));
			} catch (DeltaCloudException e) {
				// do nothing
			}
			return ValidationStatus.ok();
		}
	}

	private static class TrimTrailingSlashConverter extends Converter {

		public TrimTrailingSlashConverter() {
			super(String.class, String.class);
		}

		@Override
		public Object convert(Object fromObject) {
			Assert.isLegal(fromObject instanceof String);
			String url = (String) fromObject;
			if (url.charAt(url.length() - 1) == '/') {
				return url.substring(0, url.length() - 1);
			}
			return url;
		}
	}

	public CloudConnectionPage(String pageName, CloudConnection cloudConnection) {
		super(pageName);
		setDescription(WizardMessages.getString(DESCRIPTION));
		setTitle(WizardMessages.getString(TITLE));
		setImageDescriptor(SWTImagesFactory.DESC_DELTA_LARGE);
		this.connectionModel = new CloudConnectionPageModel(
				new StringPreferenceValue(IPreferenceKeys.LAST_NAME, Activator.PLUGIN_ID).get(null),
				new StringPreferenceValue(IPreferenceKeys.LAST_URL, Activator.PLUGIN_ID).get(null),
				new StringPreferenceValue(IPreferenceKeys.LAST_USERNAME, Activator.PLUGIN_ID).get(null),
				null);
		this.cloudConnection = cloudConnection;
	}

	public CloudConnectionPage(String pageName, DeltaCloud initial, CloudConnection connection)
			throws MalformedURLException, DeltaCloudException {

		this(pageName, initial.getName(), initial.getURL(), initial.getUsername(),
				initial.getPassword(), initial.getDriver(), connection);
	}

	public CloudConnectionPage(String pageName, String defaultName, String defaultUrl, String defaultUsername,
			String defaultPassword, Driver defaultDriver, CloudConnection cloudConnection) throws MalformedURLException {
		super(pageName);
		defaultName = new StringPreferenceValue(IPreferenceKeys.LAST_NAME, Activator.PLUGIN_ID).get(defaultName);
		defaultUrl = new StringPreferenceValue(IPreferenceKeys.LAST_URL, Activator.PLUGIN_ID).get(defaultUrl);
		defaultUsername = new StringPreferenceValue(IPreferenceKeys.LAST_USERNAME, Activator.PLUGIN_ID)
				.get(defaultUsername);
		this.connectionModel =
				new CloudConnectionPageModel(defaultName, defaultUrl, defaultUsername, defaultPassword, defaultDriver);
		this.cloudConnection = cloudConnection;
		setDescription(WizardMessages.getString(DESCRIPTION));
		setTitle(WizardMessages.getString(TITLE));
		setImageDescriptor(SWTImagesFactory.DESC_DELTA_LARGE);
		setPageComplete(false);
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
		bindName(dbc, nameText);

		// url
		Label urlLabel = new Label(container, SWT.NULL);
		urlLabel.setText(WizardMessages.getString(URL_LABEL));
		Point p1 = urlLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Text urlText = new Text(container, SWT.BORDER | SWT.SINGLE);

		dbc.bindValue(
				WidgetProperties.text(SWT.Modify).observe(urlText),
				BeanProperties.value(
						CloudConnectionPageModel.class, CloudConnectionPageModel.PROPERTY_URL).observe(connectionModel),
				new UpdateValueStrategy()
						.setAfterGetValidator(new MandatoryStringValidator(WizardMessages.getString(MUST_ENTER_A_URL)))
						.setConverter(new TrimTrailingSlashConverter()),
						null);

		// cloud type
		Label typeLabel = new Label(container, SWT.NULL);
		typeLabel.setText(WizardMessages.getString(CLOUDTYPE_LABEL));
		Label computedTypeLabel = new Label(container, SWT.NULL);
		Binding typeLabelBinding = bindCloudType(dbc, urlText, computedTypeLabel);

		// bind url text decoration to type
		ControlDecoration decoration = JFaceUtils.createDecoration(
				urlText, WizardMessages.getString("IllegalCloudUrl.msg"), FieldDecorationRegistry.DEC_WARNING);
		IObservableValue validationStatusProvider = typeLabelBinding.getValidationStatus();
		DataBindingUtils.addValueChangeListener(
				new ControlDecorationAdapter(decoration, (IStatus) validationStatusProvider.getValue()),
				typeLabelBinding.getValidationStatus(), urlText);

		// username
		Label usernameLabel = new Label(container, SWT.NULL);
		usernameLabel.setText(WizardMessages.getString(USERNAME_LABEL));
		Text usernameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		IObservableValue usernameObservable = WidgetProperties.text(SWT.Modify).observe(usernameText);
		dbc.bindValue(
				usernameObservable,
				BeanProperties.value(CloudConnectionPageModel.class, CloudConnectionPageModel.PROPERTY_USERNAME)
						.observe(connectionModel));
		String username =
				new StringPreferenceValue(IPreferenceKeys.LAST_USERNAME, Activator.PLUGIN_ID).get(usernameText
						.getText());
		usernameText.setText(username);

		// password
		Label passwordLabel = new Label(container, SWT.NULL);
		passwordLabel.setText(WizardMessages.getString(PASSWORD_LABEL));
		Text passwordText = new Text(container, SWT.BORDER | SWT.PASSWORD | SWT.SINGLE);
		ISWTObservableValue passwordTextObservable = WidgetProperties.text(SWT.Modify).observe(passwordText);
		dbc.bindValue(
				passwordTextObservable,
				BeanProperties.value(CloudConnectionPageModel.class, CloudConnectionPageModel.PROPERTY_PASSWORD)
						.observe(connectionModel));
		// test button
		final Button testButton = new Button(container, SWT.NULL);
		testButton.setText(WizardMessages.getString(TESTBUTTON_LABEL));
		bindTestButtonEnablement(testButton, dbc);

		CredentialsTestAdapter credentialsTestAdapter = new CredentialsTestAdapter(
				usernameText,
				passwordText);
		testButton.addSelectionListener(credentialsTestAdapter);
		DataBindingUtils.addValueChangeListener(
				credentialsTestAdapter, usernameObservable, usernameText);
		DataBindingUtils.addValueChangeListener(
				credentialsTestAdapter, passwordTextObservable, passwordText);

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
		computedTypeLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(typeLabel, 10 + centering);
		usernameLabel.setLayoutData(f);

		f = new FormData();
		f.left = new FormAttachment(computedTypeLabel, 0, SWT.LEFT);
		f.top = new FormAttachment(computedTypeLabel, 10);
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
	}

	/**
	 * Enables/Disables (credentials) test button on changes in the driver
	 * property of the model.
	 * 
	 * @param testButton
	 *            the test button to bind
	 * @param dbc
	 *            the databinding context to use
	 */
	private void bindTestButtonEnablement(final Button testButton, DataBindingContext dbc) {
		dbc.bindValue(
				WidgetProperties.enabled().observe(testButton),
				BeanProperties.value(CloudConnectionPageModel.PROPERTY_DRIVER).observe(connectionModel),
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER),
				new UpdateValueStrategy().setConverter(new Converter(Driver.class, Boolean.class) {
					@Override
					public Object convert(Object fromObject) {
						return connectionModel.isKnownCloud(fromObject);
					}
				}));
	}

	/**
	 * Binds the given url text widget to the model type property and uses a
	 * converter to transfer urls to cloud types. Furthermore it binds the given
	 * cloud type label to the result of this conversion. The binding returned
	 * is the binding that connects the url text widget to the cloud type
	 * property in the model.
	 * 
	 * @param dbc
	 *            the databinding context to use
	 * @param urlText
	 *            the url text widget
	 * @param typeLabel
	 *            the cloud type label to display the cloud type in
	 * @return
	 * @return
	 * @return the binding that was created
	 */
	private Binding bindCloudType(DataBindingContext dbc, Text urlText, final Label typeLabel) {

		// bind driver value to driver label
		Binding typeLabelBinding = dbc.bindValue(
				WidgetProperties.text().observe(typeLabel),
				BeanProperties.value(CloudConnectionPageModel.PROPERTY_DRIVER).observe(connectionModel),
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER),
				new UpdateValueStrategy()
						.setConverter(new Driver2Label())
						.setAfterGetValidator(new IValidator() {
							@Override
							public IStatus validate(Object value) {
								if (connectionModel.isKnownCloud(value)) {
									return ValidationStatus.ok();
								} else {
									return ValidationStatus.warning(WizardMessages.getString("IllegalCloudUrl.msg"));
								}
							}
						})
				);

		// set driver value when user stops typing or moves focus away
		DataBindingUtils.addValueChangeListener(
				new IValueChangeListener() {

					@Override
					public void handleValueChange(ValueChangeEvent event) {
						String url = (String) event.diff.getNewValue();
						connectionModel.setDriverByUrl(url);
					}
				},
				WidgetProperties.text(SWT.Modify).observeDelayed(CLOUDTYPE_CHECK_DELAY, urlText),
				urlText);
		return typeLabelBinding;
	}

	/**
	 * Bind the given name text wdiget to the cloud connection model. Attaches
	 * validators to the binding that enforce unique user input.
	 * 
	 * @param dbc
	 *            the databinding context to use
	 * @param nameText
	 *            the name text widget to bind
	 */
	private void bindName(DataBindingContext dbc, final Text nameText) {
		Binding nameTextBinding = dbc.bindValue(
				WidgetProperties.text(SWT.Modify).observe(nameText),
				BeanProperties.value(CloudConnectionPageModel.class, CloudConnectionPageModel.PROPERTY_NAME)
						.observe(connectionModel),
				new UpdateValueStrategy().setBeforeSetValidator(
						new CompositeValidator(
								new MandatoryStringValidator(WizardMessages.getString(MUST_ENTER_A_NAME)),
								new CloudNameValidator())),
				null);
		ControlDecorationSupport.create(nameTextBinding, SWT.LEFT | SWT.TOP);
	}

	public CloudConnectionPageModel getModel() {
		return connectionModel;
	}
}
