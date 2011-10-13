/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.internal.deltacloud.ui.wizards;

import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateSetStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.common.log.StatusFactory;
import org.jboss.tools.common.ui.WizardUtils;
import org.jboss.tools.common.ui.ssh.SshPrivateKeysPreferences;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudKey;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob.CLOUDELEMENT;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.ErrorUtils;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;
import org.jboss.tools.internal.deltacloud.ui.common.databinding.validator.ObjectNotNullToBoolean;
import org.jboss.tools.internal.deltacloud.ui.utils.LayoutUtils;

/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class ManageKeysPage extends WizardPage {

	private final static String NAME = "ManageKeys.name"; //$NON-NLS-1$
	private final static String TITLE = "ManageKeys.title"; //$NON-NLS-1$
	private final static String DESC = "ManageKeys.desc"; //$NON-NLS-1$
	private final static String NEW = "NewButton.label"; //$NON-NLS-1$
	private final static String DELETE = "DeleteButton.label"; //$NON-NLS-1$
	private final static String CREATE_KEY_TITLE = "CreateKey.title"; //$NON-NLS-1$
	private final static String CONFIRM_KEY_DELETE_TITLE = "ConfirmKeyDelete.title"; //$NON-NLS-1$
	private final static String CONFIRM_KEY_DELETE_MSG = "ConfirmKeyDelete.msg"; //$NON-NLS-1$

	private ManageKeysPageModel model;
	private DataBindingContext dbc;

	private class Key2IdConverter extends Converter {

		public Key2IdConverter() {
			super(Object.class, String.class);
		}

		@Override
		public Object convert(Object fromObject) {
			if (fromObject == null) {
				return null;
			}
			Assert.isTrue(fromObject instanceof DeltaCloudKey);
			return ((DeltaCloudKey) fromObject).getId();
		}

	}

	private class Id2KeyConverter extends Converter {

		public Id2KeyConverter() {
			super(String.class, DeltaCloudKey.class);
		}

		@Override
		public Object convert(Object fromObject) {
			if (fromObject == null) {
				return null;
			}
			Assert.isTrue(fromObject instanceof String);
			return model.getKey((String) fromObject);
		}
	}

	public ManageKeysPage(DeltaCloud cloud) {
		super(WizardMessages.getString(NAME));
		this.model = new ManageKeysPageModel(cloud);
		setDescription(WizardMessages.getString(DESC));
		setTitle(WizardMessages.getString(TITLE));
		setImageDescriptor(SWTImagesFactory.DESC_DELTA_LARGE);
		setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {
		this.dbc = new DataBindingContext();
		// WizardPageSupport.create(this, dbc);
		bindWizardComplete(dbc);

		Composite container = new Composite(parent, SWT.NULL);
		GridLayoutFactory.fillDefaults()
				.numColumns(4).equalWidth(false).applyTo(container);

		Label keyListLabel = new Label(container, SWT.NULL);
		keyListLabel.setText("Keys available on the server:");
		GridDataFactory.fillDefaults().span(4, 1).applyTo(keyListLabel);

		List keyList = createKeyList(dbc, container);
		GridDataFactory.fillDefaults()
				.align(SWT.FILL, SWT.FILL).grab(true, true).span(4, 8).hint(100, 200).applyTo(keyList);

		Button refreshButton = new Button(container, SWT.NULL);
		refreshButton.setText("Refresh keys");
		refreshButton.addSelectionListener(onRefreshPressed());
		GridDataFactory.fillDefaults().applyTo(refreshButton);

		Label dummyLabel = new Label(container, SWT.NULL);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(dummyLabel);

		Button createButton = createNewButton(container, dbc);
		GridDataFactory.fillDefaults().applyTo(createButton);

		Button deleteButton = createDeleteButton(container, dbc);
		GridDataFactory.fillDefaults().applyTo(deleteButton);

		setControl(container);
		refreshKeys();
	}

	private Button createNewButton(Composite container, DataBindingContext dbc) {
		Button newButton = new Button(container, SWT.NULL);
		newButton.setText(WizardMessages.getString(NEW));
		newButton.addSelectionListener(onNewPressed());
		return newButton;
	}

	private Button createDeleteButton(Composite container, DataBindingContext dbc) {
		Button deleteButton = new Button(container, SWT.NULL);
		deleteButton.setText(WizardMessages.getString(DELETE));
		deleteButton.addSelectionListener(onDeletePressed());
		// bind enablement
		dbc.bindValue(
				WidgetProperties.enabled().observe(deleteButton),
				BeanProperties.value(ManageKeysPageModel.PROP_SELECTED_KEY).observe(model),
				new UpdateValueStrategy(UpdateSetStrategy.POLICY_NEVER),
				new UpdateValueStrategy().setConverter(new ObjectNotNullToBoolean()));
		return deleteButton;
	}

	private void bindWizardComplete(DataBindingContext dbc) {
		dbc.bindValue(
				BeanProperties.value("pageComplete").observe(this),
				BeanProperties.value(ManageKeysPageModel.PROP_SELECTED_KEY).observe(model),
				new UpdateValueStrategy(UpdateSetStrategy.POLICY_NEVER),
				new UpdateValueStrategy().setConverter(new ObjectNotNullToBoolean()));
	}

	@Override
	public void setPageComplete(boolean complete) {
		super.setPageComplete(complete);
	}

	private List createKeyList(DataBindingContext dbc, Composite container) {
		final List keyList = new List(container, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		// bind items
		dbc.bindList(
				WidgetProperties.items().observe(keyList),
				BeanProperties.list(ManageKeysPageModel.PROP_KEYS).observe(model),
				new UpdateListStrategy(UpdateSetStrategy.POLICY_NEVER),
				new UpdateListStrategy().setConverter(new Key2IdConverter()));
		// bind selected key
		dbc.bindValue(
				WidgetProperties.selection().observe(keyList),
				BeanProperties.value(ManageKeysPageModel.PROP_SELECTED_KEY).observe(model),
				new UpdateValueStrategy().setConverter(new Id2KeyConverter()),
				new UpdateValueStrategy().setConverter(new Key2IdConverter()));

		keyList.addListener(SWT.MouseDoubleClick, onKeyDoubleclick());

		return keyList;
	}

	private SelectionAdapter onDeletePressed() {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				DeltaCloudKey key = model.getSelectedKey();
				boolean confirmed = MessageDialog.openConfirm(getShell(),
						WizardMessages.getString(CONFIRM_KEY_DELETE_TITLE),
						WizardMessages.getFormattedString(CONFIRM_KEY_DELETE_MSG, key.getId()));
				if (confirmed) {
					deleteSelectedKey();
				}
			}
		};
	}

	private SelectionListener onRefreshPressed() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshKeys();
			}
		};
	}

	private Listener onKeyDoubleclick() {
		return new Listener() {

			@Override
			public void handleEvent(Event event) {
				WizardUtils.nextPageOrFinish(ManageKeysPage.this);
			}
		};
	}
	
	private void refreshKeys() {
		Job job = new AbstractCloudElementJob("Get keys", model.getCloud(), CLOUDELEMENT.KEYS) {

			protected IStatus doRun(IProgressMonitor monitor) throws Exception {
				try {
					model.refreshKeys();
					return Status.OK_STATUS;
				} catch (DeltaCloudException e) {
					return StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID,
							MessageFormat.format("Could not get keys from cloud {0}", getCloud().getName()), e);
				}
			}
		};
		try {
			WizardUtils.runInWizard(job, getContainer(), dbc);
		} catch (Exception e) {
			// ignore since the job will report its failure
		}
	}

	private SelectionListener onNewPressed() {
		return new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				java.util.List<DeltaCloudKey> keys = model.getKeys();
				String[] keys2 = new String[keys.size()];
				for( int i = 0; i < keys2.length; i++ ) {
					keys2[i] = keys.get(i).getId();
				}
				AddKeyDialog dialog = new AddKeyDialog(getShell(), keys2);
				if(dialog.open() == Dialog.OK) {
					String keyId = dialog.getKeyId();
					DeltaCloudKey key = createKey(keyId);
					if( dialog.shouldPersist() ) {
						storeKeyLocally(key, dialog.getFolder());
					}
				}
			}
		};
	}

	private class AddKeyDialog extends TitleAreaDialog {
		private String[] existingKeys;
		private Text pemText, nameText;
		private Button persist;
		private String pem, name;
		private boolean persistBool;
		public AddKeyDialog(Shell parentShell, String[] existingKeys) {
			super(parentShell);
			this.existingKeys = existingKeys;
		}
		
		protected Control createDialogArea(Composite parent) {
			// create the top level composite for the dialog area
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayoutData(new GridData(GridData.FILL_BOTH));
			composite.setFont(parent.getFont());
			composite.setLayout(new FormLayout());
			Label titleBarSeparator = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
			titleBarSeparator.setLayoutData(LayoutUtils.createFormData(0,0,null,0,0,0,100,0));

			setTitle(WizardMessages.getString(CREATE_KEY_TITLE));
			Label nameLabel = new Label(composite, SWT.NONE);
			nameLabel.setText("Key ID: ");
			nameLabel.setLayoutData(LayoutUtils.createFormData(0,7,null,0,0,5,null,0));
			nameText = new Text(composite, SWT.BORDER);
			nameText.setLayoutData(LayoutUtils.createFormData(0,5,null,0,nameLabel, 5, 100, -5));
			
			persist = new Button(composite, SWT.CHECK);
			persist.setText("Save key in folder: ");
			persist.setLayoutData(LayoutUtils.createFormData(nameText, 8, null, 0, 0,5,null,0));
			
			Button browse = new Button(composite, SWT.DEFAULT);
			browse.setText("Browse...");
			browse.setLayoutData(LayoutUtils.createFormData(nameText, 5, null, 0, null, 0, 100, -5));
			browse.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.NONE);
					dialog.setFilterPath(model.getKeyStorePath());
					dialog.setText("Choose a directory to store new keys");
					String keyStorePath = dialog.open();
					pemText.setText(keyStorePath);
				}
			});
			
			pemText = new Text(composite, SWT.BORDER);
			pemText.setLayoutData(LayoutUtils.createFormData(nameText,5,null,0,persist, 5, browse, -5));

			init();
			
			ModifyListener listener = new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					verify();
				}
			};
			
			nameText.addModifyListener(listener);
			pemText.addModifyListener(listener);
			persist.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					verify();
				}
			});
			
			return composite;
		}
		
		private void init() {
			persist.setSelection(true);
			pemText.setText(model.getInitialKeyStorePath());
			verify();
		}
		
		private void verify() {
			persistBool = persist.getSelection();
			pem = pemText.getText();
			name = nameText.getText();
			File pemFolder = new File(pem);
			if( !pemFolder.exists()) {
				setErrorMessage("Key folder does not exist.");
			} else if( !pemFolder.canWrite()) {
				setErrorMessage("Key folder is not writable.");
			} else if( Arrays.asList(existingKeys).contains(name)){
				setErrorMessage("Key id is already used, please choose another id.");
			} else {
				setErrorMessage(null);
			}
		}
		
		public String getKeyId() {
			return name;
		}
		
		public String getFolder() {
			return pem;
		}
		
		public boolean shouldPersist() {
			return persistBool;
		}
	}
	
	private DeltaCloudKey createKey(final String keyId) {
		final DeltaCloudKey[] keyHolder = new DeltaCloudKey[1];
		try {
			Job job = new AbstractCloudElementJob("Create new key", model.getCloud(), CLOUDELEMENT.KEYS) {

				protected IStatus doRun(IProgressMonitor monitor) throws Exception {
					try {
						keyHolder[0] = model.createKey(keyId);
						return Status.OK_STATUS;
					} catch (DeltaCloudException e) {
						return StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID,
								MessageFormat.format("Could not create key \"{0}\"", keyId), e);
					}
				}
			};
			WizardUtils.runInWizard(job, getContainer(), dbc);
		} catch (Exception e) {
			// ignore
		}
		return keyHolder[0];
	}

	private void storeKeyLocally(DeltaCloudKey key, String pemFolder) {
		try {
			if (key != null) {
				model.storeKeyLocally(key, pemFolder);
			}
		} catch (Exception e) {
			ErrorUtils.handleError("Error", MessageFormat.format(
					"Could not store key \"{0}\" locally and add it to the ssh private keys", key.getName()),
					e, getShell());
		}
	}

	private void deleteSelectedKey() {
		try {
			Job job = new AbstractCloudElementJob(
					MessageFormat.format("Delete key {0}",model.getSelectedKey().getName()), 
					model.getCloud(), 
					CLOUDELEMENT.KEYS) {

				protected IStatus doRun(IProgressMonitor monitor) throws Exception {
					try {
						final DeltaCloudKey key = model.deleteSelectedKey();
						final boolean[] isConfirmDelete = new boolean[] { false };
						if (PemFileManager.exists(key.getName(), SshPrivateKeysPreferences.getSshKeyDirectory())) {
							getShell().getDisplay().syncExec(new Runnable() {

								@Override
								public void run() {
									isConfirmDelete[0] = MessageDialog.openConfirm(
											getShell(),
											MessageFormat.format("Delete key {0} locally?",key.getName()),
											MessageFormat.format(
													"Shall the key \"{0}\" be deleted from local key store?",
													key.getName()));
								}
							});
							if (isConfirmDelete[0]) {
								model.removeKeyLocally(key);
							}
						}
						return Status.OK_STATUS;
					} catch (DeltaCloudException e) {
						return StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID,
									MessageFormat.format("Could not delete key", model.getSelectedKey().getId()), e);
					}
				}
			};
			WizardUtils.runInWizard(job, getContainer(), dbc);
//			// WORKAROUND (wizard runnable service always reenables OK-button)
//			setPageComplete(isPageComplete());
		} catch (Exception e) {
			// ignore
		}
	}

	public DeltaCloudKey getKey() {
		return model.getSelectedKey();
	}
}
