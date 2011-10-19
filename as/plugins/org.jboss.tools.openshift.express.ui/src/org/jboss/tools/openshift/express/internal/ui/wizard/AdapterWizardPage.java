/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.express.internal.ui.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.wst.server.core.IRuntimeWorkingCopy;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.TaskModel;
import org.eclipse.wst.server.ui.internal.Messages;
import org.eclipse.wst.server.ui.internal.ServerUIPlugin;
import org.eclipse.wst.server.ui.internal.wizard.TaskWizard;
import org.eclipse.wst.server.ui.internal.wizard.WizardTaskUtil;
import org.eclipse.wst.server.ui.wizard.WizardFragment;
import org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants;
import org.jboss.ide.eclipse.as.ui.UIUtil;
import org.jboss.tools.common.ui.databinding.DataBindingUtils;
import org.jboss.tools.common.ui.databinding.InvertingBooleanConverter;
import org.jboss.tools.common.ui.ssh.SshPrivateKeysPreferences;
import org.jboss.tools.openshift.express.client.ICartridge;
import org.jboss.tools.openshift.express.client.OpenShiftException;
import org.jboss.tools.openshift.express.internal.ui.OpenShiftUIActivator;

public class AdapterWizardPage extends AbstractOpenShiftWizardPage implements IWizardPage, PropertyChangeListener {
	private Text gitUriValueText;

	private AdapterWizardPageModel model;
	private Combo suitableRuntimes;
	private IServerType serverTypeToCreate;
	private IRuntime runtimeDelegate;
	private Label domainLabel;
	private Label modeLabel;
	private Link addRuntimeLink;
	private Label runtimeLabel;
	private Button serverAdapterCheckbox;

	public AdapterWizardPage(ImportProjectWizard wizard, ImportProjectWizardModel model) {
		super(
				"Import Project",
				"Please select the destination for your local copy of the OpenShift Express repository, "
						+ "what branch to clone and setup your server adapter, ",
				"Server Adapter",
				wizard);
		this.model = new AdapterWizardPageModel(model);
		model.addPropertyChangeListener(this);
	}

	@Override
	protected void doCreateControls(Composite parent, DataBindingContext dbc) {
		GridLayoutFactory.fillDefaults().applyTo(parent);

		Group projectGroup = createCloneGroup(parent, dbc);
		GridDataFactory.fillDefaults()
				.align(SWT.LEFT, SWT.CENTER).align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(projectGroup);

		Group serverAdapterGroup = createAdapterGroup(parent, dbc);
		GridDataFactory.fillDefaults()
				.align(SWT.LEFT, SWT.CENTER).align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(serverAdapterGroup);
	}

	private Group createCloneGroup(Composite parent, DataBindingContext dbc) {
		Group cloneGroup = new Group(parent, SWT.BORDER);
		cloneGroup.setText("Git clone");
		GridDataFactory.fillDefaults()
				.align(SWT.LEFT, SWT.CENTER).align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(cloneGroup);
		GridLayoutFactory.fillDefaults().margins(6, 6).numColumns(4).applyTo(cloneGroup);

		Label gitUriLabel = new Label(cloneGroup, SWT.NONE);
		gitUriLabel.setText("Cloning From");
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(gitUriLabel);

		gitUriValueText = new Text(cloneGroup, SWT.BORDER);
		gitUriValueText.setEditable(false);
		// gitUriValueText.setBackground(cloneGroup.getBackground());
		GridDataFactory.fillDefaults().span(3, 1).align(SWT.FILL, SWT.CENTER).grab(true, false)
				.applyTo(gitUriValueText);
		dbc.bindValue(
				WidgetProperties.text(SWT.Modify).observe(gitUriValueText)
				, BeanProperties.value(AdapterWizardPageModel.PROPERTY_GIT_URI).observe(model)
				, new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER)
				, null);
		dbc.bindValue(
				WidgetProperties.enabled().observe(gitUriValueText)
				, BeanProperties.value(AdapterWizardPageModel.PROPERTY_LOADING).observe(model)
				, new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER)
				, new UpdateValueStrategy().setConverter(new InvertingBooleanConverter()));

		// bind loading state to page complete
		dbc.bindValue(
				new WritableValue(false, Boolean.class)
				, BeanProperties.value(AdapterWizardPageModel.PROPERTY_LOADING).observe(model)
				, new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER)
				, new UpdateValueStrategy().setAfterGetValidator(new IValidator() {

					@Override
					public IStatus validate(Object value) {
						if (Boolean.FALSE.equals(value)) {
							return ValidationStatus.ok();
						} else {
							return ValidationStatus.cancel("Loading...");
						}
					}
				}));

		Label repoPathLabel = new Label(cloneGroup, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(repoPathLabel);
		repoPathLabel.setText("Destination");

		Button defaultRepoPathButton = new Button(cloneGroup, SWT.CHECK);
		defaultRepoPathButton.setText("default");
		GridDataFactory.fillDefaults()
				.align(SWT.LEFT, SWT.CENTER).hint(100, SWT.DEFAULT).applyTo(defaultRepoPathButton);
		defaultRepoPathButton.addSelectionListener(onDefaultRepoPath());
		IObservableValue defaultRepoButtonSelection = WidgetProperties.selection().observe(defaultRepoPathButton);

		Text repoPathText = new Text(cloneGroup, SWT.BORDER);
		GridDataFactory.fillDefaults()
				.align(SWT.LEFT, SWT.CENTER).align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(repoPathText);
		DataBindingUtils.bindMandatoryTextField(
				repoPathText, "Location", AdapterWizardPageModel.PROPERTY_REPO_PATH, model, dbc);
		dbc.bindValue(
				defaultRepoButtonSelection
				, WidgetProperties.enabled().observe(repoPathText)
				, new UpdateValueStrategy().setConverter(new InvertingBooleanConverter())
				, new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER));

		Button browseRepoPathButton = new Button(cloneGroup, SWT.PUSH);
		browseRepoPathButton.setText("Browse");
		GridDataFactory.fillDefaults()
				.align(SWT.LEFT, SWT.CENTER).hint(100, SWT.DEFAULT).applyTo(browseRepoPathButton);
		browseRepoPathButton.addSelectionListener(onRepoPath());
		dbc.bindValue(
				defaultRepoButtonSelection
				, WidgetProperties.enabled().observe(browseRepoPathButton)
				, new UpdateValueStrategy().setConverter(new InvertingBooleanConverter())
				, new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER));

		defaultRepoButtonSelection.setValue(true);

		Label remoteNameLabel = new Label(cloneGroup, SWT.NONE);
		remoteNameLabel.setText("Remote name");
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(remoteNameLabel);

		Button defaultRemoteNameButton = new Button(cloneGroup, SWT.CHECK);
		defaultRemoteNameButton.setText("default");
		GridDataFactory.fillDefaults()
				.align(SWT.LEFT, SWT.CENTER).hint(100, SWT.DEFAULT).applyTo(defaultRemoteNameButton);
		defaultRemoteNameButton.addSelectionListener(onDefaultRemoteName());

		Text remoteNameText = new Text(cloneGroup, SWT.BORDER);
		GridDataFactory.fillDefaults()
				.span(2, 1).align(SWT.LEFT, SWT.CENTER).align(SWT.FILL, SWT.CENTER).grab(true, false)
				.applyTo(remoteNameText);
		DataBindingUtils.bindMandatoryTextField(
				remoteNameText, "Remote name", AdapterWizardPageModel.PROPERTY_REMOTE_NAME, model, dbc);

		IObservableValue defaultRemoteNameSelection = WidgetProperties.selection().observe(defaultRemoteNameButton);
		dbc.bindValue(
				defaultRemoteNameSelection
				, WidgetProperties.enabled().observe(remoteNameText)
				, new UpdateValueStrategy().setConverter(new InvertingBooleanConverter())
				, new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER));
		defaultRemoteNameSelection.setValue(true);

		Link sshPrefsLink = new Link(cloneGroup, SWT.NONE);
		sshPrefsLink
				.setText("Make sure your SSH key used with the domain is listed in <a>SSH2 Preferences</a>");
		GridDataFactory.fillDefaults()
				.span(4, 1).align(SWT.FILL, SWT.CENTER).indent(0, 10).applyTo(sshPrefsLink);
		sshPrefsLink.addSelectionListener(onSshPrefs());

		return cloneGroup;
	}

	private SelectionListener onDefaultRepoPath() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				model.resetRepositoryPath();
			}
		};
	}

	private SelectionListener onRepoPath() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(getShell());
				dialog.setMessage("Choose the location to store your repository clone to...");
				String repositoryPath = dialog.open();
				if (repositoryPath != null) {
					model.setRepositoryPath(repositoryPath);
				}
			}
		};
	}

	private SelectionListener onDefaultRemoteName() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				model.resetRemoteName();
			}
		};
	}

	private SelectionAdapter onSshPrefs() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				SshPrivateKeysPreferences.openPreferencesPage(getShell());
			}
		};
	}

	private Group createAdapterGroup(Composite parent, DataBindingContext dbc) {
		Group serverAdapterGroup = new Group(parent, SWT.BORDER);
		serverAdapterGroup.setText("JBoss Server adapter");
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 6;
		fillLayout.marginWidth = 6;
		serverAdapterGroup.setLayout(fillLayout);
		fillServerAdapterGroup(serverAdapterGroup);
		IObservableValue runtimeSelection = WidgetProperties.singleSelectionIndex().observe(suitableRuntimes);

		IObservableValue dummyObservable = new WritableValue();
		Binding comboSelectionBinding = dbc.bindValue(
				dummyObservable
				, WidgetProperties.singleSelectionIndex().observe(suitableRuntimes)
				, new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER)
				, new UpdateValueStrategy().setAfterGetValidator(new SelectedRuntimeValidator(null)));

		dbc.bindValue(
				dummyObservable
				, WidgetProperties.selection().observe(serverAdapterCheckbox)
				, new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER)
				, new UpdateValueStrategy().setAfterGetValidator(new SelectedRuntimeValidator(comboSelectionBinding)));

		runtimeSelection.setValue(null);

		return serverAdapterGroup;
	}

	protected void enableServerWidgets(boolean enabled) {
		suitableRuntimes.setEnabled(enabled);
		runtimeLabel.setEnabled(enabled);
		addRuntimeLink.setEnabled(enabled);
		// domainLabel.setEnabled(enabled);
		modeLabel.setEnabled(enabled);
	}

	private void fillServerAdapterGroup(Group serverAdapterGroup) {
		Composite c = new Composite(serverAdapterGroup, SWT.NONE);
		c.setLayout(new FormLayout());
		serverAdapterCheckbox = new Button(c, SWT.CHECK);
		serverAdapterCheckbox.setText("Create a JBoss server adapter");
		final Button serverAdapterCheckbox2 = serverAdapterCheckbox;
		serverAdapterCheckbox.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				model.getParentModel().setProperty(AdapterWizardPageModel.CREATE_SERVER,
						serverAdapterCheckbox2.getSelection());
				enableServerWidgets(serverAdapterCheckbox2.getSelection());
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		runtimeLabel = new Label(c, SWT.NONE);
		runtimeLabel.setText("Local Runtime: ");

		suitableRuntimes = new Combo(c, SWT.READ_ONLY);
		addRuntimeLink = new Link(c, SWT.NONE);
		addRuntimeLink.setText("<a>" + Messages.addRuntime + "</a>");

		domainLabel = new Label(c, SWT.NONE);
		DataBindingContext dbc = getDatabindingContext();
		dbc.bindValue(
				WidgetProperties.text().observe(domainLabel)
				, BeanProperties.value(AdapterWizardPageModel.PROPERTY_APPLICATION_URL).observe(model)
				, new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER)
				, new UpdateValueStrategy().setConverter(new Converter(String.class, String.class) {

					@Override
					public Object convert(Object fromObject) {
						String host = "";
						if (fromObject instanceof String && ((String) fromObject).length() > 0) {
							host = (String) fromObject;
						}
						return "Host: " + host;
					}

				}));
		// appLabel = new Label(c, SWT.NONE);
		modeLabel = new Label(c, SWT.NONE);

		suitableRuntimes.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateSelectedRuntimeDelegate();
			}
		});
		addRuntimeLink.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				IRuntimeType type = getValidRuntimeType();
				if (type != null)
					showRuntimeWizard(type);
			}
		});

		serverAdapterCheckbox.setLayoutData(UIUtil.createFormData2(0, 5, null, 0, 0, 5, null, 0));
		runtimeLabel.setLayoutData(UIUtil.createFormData2(serverAdapterCheckbox, 5, null, 0, 0, 5, null, 0));
		addRuntimeLink.setLayoutData(UIUtil.createFormData2(serverAdapterCheckbox, 5, null, 0, null, 0, 100, -5));
		suitableRuntimes.setLayoutData(UIUtil.createFormData2(serverAdapterCheckbox, 5, null, 0, runtimeLabel, 5,
				addRuntimeLink, -5));
		domainLabel.setLayoutData(UIUtil.createFormData2(suitableRuntimes, 5, null, 0, 0, 5, 100, 0));
		// appLabel.setLayoutData(UIUtil.createFormData2(domainLabel, 5, null,
		// 0, 0, 5, 100, 0));
		modeLabel.setLayoutData(UIUtil.createFormData2(domainLabel, 5, null, 0, 0, 5, 100, 0));
		serverAdapterCheckbox.setSelection(true);
		model.getParentModel().setProperty(AdapterWizardPageModel.CREATE_SERVER,
				serverAdapterCheckbox2.getSelection());
	}

	private void updateSelectedRuntimeDelegate() {
		if (suitableRuntimes.getSelectionIndex() != -1) {
			runtimeDelegate = ServerCore.findRuntime(suitableRuntimes.getItem(suitableRuntimes.getSelectionIndex()));
		} else {
			runtimeDelegate = null;
		}
		model.getParentModel().setProperty(AdapterWizardPageModel.RUNTIME_DELEGATE, runtimeDelegate);
	}

	private IRuntimeType getValidRuntimeType() {
		String cartridgeName = model.getParentModel().getApplicationCartridgeName();
		if (ICartridge.JBOSSAS_7.getName().equals(cartridgeName)) {
			return ServerCore.findRuntimeType(IJBossToolingConstants.AS_70);
		}
		return null;
	}

	private IServerType getServerTypeToCreate() {
		String cartridgeName = model.getParentModel().getApplicationCartridgeName();
		if (ICartridge.JBOSSAS_7.getName().equals(cartridgeName)) {
			return ServerCore.findServerType(IJBossToolingConstants.SERVER_AS_70);
		}
		return null;
	}

	private IRuntime[] getRuntimesOfType(String type) {
		ArrayList<IRuntime> validRuntimes = new ArrayList<IRuntime>();
		IRuntime[] allRuntimes = ServerCore.getRuntimes();
		for (int i = 0; i < allRuntimes.length; i++) {
			if (allRuntimes[i].getRuntimeType().getId().equals(type))
				validRuntimes.add(allRuntimes[i]);
		}
		return validRuntimes.toArray(new IRuntime[validRuntimes.size()]);
	}

	private void fillRuntimeCombo(Combo combo, IRuntime[] runtimes) {
		String[] names = new String[runtimes.length];
		for (int i = 0; i < runtimes.length; i++) {
			names[i] = runtimes[i].getName();
		}
		combo.setItems(names);
	}

	protected void onPageActivated(DataBindingContext dbc) {
		model.resetRepositoryPath();

		serverTypeToCreate = getServerTypeToCreate();
		model.getParentModel().setProperty(AdapterWizardPageModel.SERVER_TYPE, serverTypeToCreate);
		refreshValidRuntimes();
		if (suitableRuntimes.getItemCount() > 0) {
			suitableRuntimes.select(0);
			updateSelectedRuntimeDelegate();
		}
		IRuntimeType type = getValidRuntimeType();
		addRuntimeLink.setEnabled(type != null);
		modeLabel.setText("Mode: Source");
		model.getParentModel().setProperty(AdapterWizardPageModel.MODE, AdapterWizardPageModel.MODE_SOURCE);

		setPageComplete(false);
		getWizard().getContainer().updateButtons();
		onPageActivatedBackground(dbc);
	}

	protected void onPageActivatedBackground(final DataBindingContext dbc) {
		new Job("Loading remote OpenShift application") {
			public IStatus run(IProgressMonitor monitor) {
				try {
					model.loadGitUri();
					model.loadApplicationUrl();
				} catch (OpenShiftException e) {
					OpenShiftUIActivator.log(OpenShiftUIActivator.createErrorStatus(e.getMessage(), e));
				}
				return Status.OK_STATUS;
			}
		}.schedule();
	}

	protected void refreshValidRuntimes() {
		IRuntimeType type = getValidRuntimeType();
		if (type != null) {
			IRuntime[] runtimes = getRuntimesOfType(type.getId());
			fillRuntimeCombo(suitableRuntimes, runtimes);
		} else {
			suitableRuntimes.setItems(new String[0]);
		}
	}

	/* Stolen from NewManualServerComposite */
	protected int showRuntimeWizard(IRuntimeType runtimeType) {
		WizardFragment fragment = null;
		TaskModel taskModel = new TaskModel();
		final WizardFragment fragment2 = ServerUIPlugin.getWizardFragment(runtimeType.getId());
		if (fragment2 == null)
			return Window.CANCEL;

		try {
			IRuntimeWorkingCopy runtimeWorkingCopy = runtimeType.createRuntime(null, null);
			taskModel.putObject(TaskModel.TASK_RUNTIME, runtimeWorkingCopy);
		} catch (CoreException ce) {
			OpenShiftUIActivator.getDefault().getLog().log(ce.getStatus());
			return Window.CANCEL;
		}
		fragment = new WizardFragment() {
			protected void createChildFragments(List<WizardFragment> list) {
				list.add(fragment2);
				list.add(WizardTaskUtil.SaveRuntimeFragment);
			}
		};
		TaskWizard wizard2 = new TaskWizard(Messages.wizNewRuntimeWizardTitle, fragment, taskModel);
		wizard2.setForcePreviousAndNextButtons(true);
		WizardDialog dialog = new WizardDialog(getShell(), wizard2);
		int returnValue = dialog.open();
		refreshValidRuntimes();
		if (returnValue != Window.CANCEL) {
			IRuntime rt = (IRuntime) taskModel.getObject(TaskModel.TASK_RUNTIME);
			if (rt != null && rt.getName() != null && suitableRuntimes.indexOf(rt.getName()) != -1) {
				suitableRuntimes.select(suitableRuntimes.indexOf(rt.getName()));
			}
		}
		return returnValue;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (ImportProjectWizardModel.APPLICATION.equals(evt.getPropertyName())) {
			handleApplicationChanged();
		}
	}

	private void handleApplicationChanged() {
		// we need to enable or disable all the server widgets depending on
		// if we can make a server out of this
		serverTypeToCreate = getServerTypeToCreate();
		boolean canCreateServer = serverTypeToCreate != null;
		serverAdapterCheckbox.setEnabled(canCreateServer);
		serverAdapterCheckbox.setSelection(canCreateServer);
		enableServerWidgets(canCreateServer);
		refreshValidRuntimes();
		model.getParentModel().setProperty(AdapterWizardPageModel.SERVER_TYPE, serverTypeToCreate);
		model.getParentModel().setProperty(AdapterWizardPageModel.CREATE_SERVER, canCreateServer);
	}

	private class SelectedRuntimeValidator implements IValidator {

		private Binding binding;

		public SelectedRuntimeValidator(Binding binding) {
			this.binding = binding;
		}

		public IStatus validate(Object value) {
			if (!serverAdapterCheckbox.getSelection()) {
				updateBinding();
				return Status.OK_STATUS;
			}
			if (new Integer(-1).equals(suitableRuntimes.getSelectionIndex())) {
				if (suitableRuntimes.getItems() == null || suitableRuntimes.getItems().length == 0) {
					return ValidationStatus.error("Please add a new valid runtime.");
				}
				return ValidationStatus.error("Please select a runtime");
			}
			updateBinding();
			return Status.OK_STATUS;
		}

		private void updateBinding() {
			if (binding != null) {
				this.binding.updateModelToTarget();
			}
		}
	}

}
