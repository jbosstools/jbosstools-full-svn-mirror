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

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.log.StatusFactory;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudKey;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob.CLOUDELEMENT;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.ErrorUtils;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;

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
	private final static String CREATE_KEY_MSG = "CreateKey.msg"; //$NON-NLS-1$
	private final static String CONFIRM_KEY_DELETE_TITLE = "ConfirmKeyDelete.title"; //$NON-NLS-1$
	private final static String CONFIRM_KEY_DELETE_MSG = "ConfirmKeyDelete.msg"; //$NON-NLS-1$

	private DeltaCloud cloud;
	private List keyList;
	private java.util.List<DeltaCloudKey> keys;
	private DeltaCloudKey selectedKey;

	private class UniqueKeyIdConstraint implements IInputValidator {

		@Override
		public String isValid(String keyId) {
			if (keys == null
						|| keyId == null) {
				return null;
			}

			for (DeltaCloudKey key : keys) {
				if (keyId.equals(key.getId())) {
					// TODO: internationalize string
					return "Key id is already used, please choose another id.";
				}
			}
			return null;
		}
	}

	private SelectionListener keyListListener = new SelectionAdapter() {

		@Override
		public void widgetSelected(SelectionEvent e) {
			String selectedKeyName = keyList.getItem(keyList.getSelectionIndex());
			selectedKey = getKey(selectedKeyName);
			setPageComplete(selectedKey != null);
		}
	};

	public ManageKeysPage(DeltaCloud cloud) {
		super(WizardMessages.getString(NAME));
		this.cloud = cloud;
		setDescription(WizardMessages.getString(DESC));
		setTitle(WizardMessages.getString(TITLE));
		setImageDescriptor(SWTImagesFactory.DESC_DELTA_LARGE);
		setPageComplete(false);
	}

	public DeltaCloudKey getKey() {
		return selectedKey;
	}

	@Override
	public void createControl(Composite parent) {
		setPageComplete(false);
		final Composite container = new Composite(parent, SWT.NULL);
		GridLayoutFactory.fillDefaults().numColumns(4).equalWidth(false).applyTo(container);

		keyList = new List(container, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		keyList.addSelectionListener(keyListListener);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).span(4, 5).applyTo(keyList);

		Button refreshButton = new Button(container, SWT.NULL);
		refreshButton.setText("Refresh keys");
		refreshButton.addSelectionListener(onRefreshPressed());
		GridDataFactory.fillDefaults().applyTo(refreshButton);

		Label dummyLabel = new Label(container, SWT.NULL);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(dummyLabel);

		Button createButton = new Button(container, SWT.NULL);
		// TODO: internationalize string
		createButton.setText(WizardMessages.getString(NEW));
		createButton.addSelectionListener(onNewPressed());
		GridDataFactory.fillDefaults().applyTo(createButton);

		Button deleteButton = new Button(container, SWT.NULL);
		deleteButton.setText(WizardMessages.getString(DELETE));
		deleteButton.addSelectionListener(onDeletePressed());
		GridDataFactory.fillDefaults().applyTo(deleteButton);

		setControl(container);
		asyncGetKeys(cloud);
		setPageComplete(false);
	}

	private SelectionAdapter onDeletePressed() {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				boolean confirmed = MessageDialog.openConfirm(getShell(),
						WizardMessages.getString(CONFIRM_KEY_DELETE_TITLE),
						WizardMessages.getFormattedString(CONFIRM_KEY_DELETE_MSG, selectedKey.getId()));
				if (confirmed) {
					deleteKey(selectedKey);
				}
			}
		};
	}

	private SelectionListener onRefreshPressed() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				asyncGetKeys(cloud);
			}
		};
	}

	private SelectionListener onNewPressed() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Display display = Display.getDefault();
				Shell shell = new Shell(display);
				// String directoryText = directory.getText();
				InputDialog d = new InputDialog(shell, WizardMessages.getString(CREATE_KEY_TITLE),
						WizardMessages.getString(CREATE_KEY_MSG), "", new UniqueKeyIdConstraint());
				d.setBlockOnOpen(true);
				d.create();
				if (d.open() == InputDialog.OK) {
					String keyId = d.getValue();
					createkey(keyId);
				}
			}
		};
	}

	private void asyncGetKeys(final DeltaCloud cloud) {
		// TODO: internationalize strings
		new AbstractCloudElementJob("get keys", cloud, CLOUDELEMENT.KEYS) {

			protected IStatus doRun(IProgressMonitor monitor) throws Exception {
				try {
					keys = new ArrayList<DeltaCloudKey>();
					keys.addAll(Arrays.asList(cloud.getKeys()));
					setKeysToList(keys);
					return Status.OK_STATUS;
				} catch (DeltaCloudException e) {
					// TODO: internationalize strings
					return StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID,
							MessageFormat.format("Could not get keys from cloud {0}", cloud.getName()), e);
				}
			}

		}.schedule();
	}

	private String[] toKeyIds(java.util.List<DeltaCloudKey> keys) {
		ArrayList<String> keyId = new ArrayList<String>();
		for (DeltaCloudKey key : keys) {
			keyId.add(key.getId());
		}
		return keyId.toArray(new String[keyId.size()]);
	}

	private void setKeysToList(java.util.List<DeltaCloudKey> keys) {
		final String[] keyIds = toKeyIds(keys);
		keyList.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (keyIds.length == 0) {
					keyList.setItems(new String[] { "There are no keys..." });
				} else {
					keyList.setItems(keyIds);
				}
				keyList.setEnabled(keyIds.length > 0);
			}
		});
	}

	private DeltaCloudKey getKey(String keyId) {
		if (keys == null
				|| keyId == null) {
			return null;
		}
		DeltaCloudKey matchingKey = null;
		for (DeltaCloudKey key : keys) {
			if (keyId.equals(key.getId())) {
				matchingKey = key;
				break;
			}
		}
		return matchingKey;
	}

	private void createkey(final String keyId) {
		try {
			getWizard().getContainer().run(true, true, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						DeltaCloudKey key = cloud.createKey(keyId);
						keys.add(key);
						setKeysToList(keys);
						setSelection(key);
					} catch (Exception e) {
						throw new InvocationTargetException(e);
					}
				}
			});
		} catch (Exception e) {
			// TODO: internationalize strings
			ErrorUtils.handleError(
					"Error",
					MessageFormat.format("Error", "Could not create key \"{0}\"", keyId), e,
					getShell());
		}
	}

	private void deleteKey(final DeltaCloudKey key) {
		try {
			getWizard().getContainer().run(true, true, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						cloud.deleteKey(key.getId());
						keys.remove(key);
						getShell().getDisplay().syncExec(new Runnable() {
							
							@Override
							public void run() {
								keyList.remove(key.getId());
							}
						});
					} catch (Exception e) {
						throw new InvocationTargetException(e);
					}
				}
			});
		} catch (Exception e) {
			// TODO: internationalize strings
			ErrorUtils.handleError(
					"Error",
					MessageFormat.format("Error", "Could not create key \"{0}\"", key.getId()), e,
					getShell());
		}
	}

	private void setSelection(final DeltaCloudKey key) {
		getShell().getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				keyList.setSelection(new String[] { key.getId() });
				keyList.showSelection();
			}
		});
	}

}
