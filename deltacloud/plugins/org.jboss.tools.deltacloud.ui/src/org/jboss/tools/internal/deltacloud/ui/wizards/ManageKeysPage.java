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

import java.text.MessageFormat;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateSetStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
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
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudKey;
import org.jboss.tools.deltacloud.ui.ErrorUtils;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;
import org.jboss.tools.internal.deltacloud.ui.common.databinding.validator.BoundObjectPresentConverter;

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

	private List keyList;
	private ManageKeysPageModel model;

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

	private class UniqueKeyIdConstraint implements IInputValidator {

		@Override
		public String isValid(String keyId) {
			if (keyId == null) {
				return null;
			}

			for (DeltaCloudKey key : model.getKeys()) {
				if (keyId.equals(key.getId())) {
					// TODO: internationalize string
					return "Key id is already used, please choose another id.";
				}
			}
			return null;
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

	public DeltaCloudKey getKey() {
		return model.getSelectedKey();
	}

	@Override
	public void createControl(Composite parent) {
		DataBindingContext dbc = new DataBindingContext();
		// WizardPageSupport.create(this, dbc);

		bindWizardComplete(dbc);
		Composite container = new Composite(parent, SWT.NULL);
		GridLayoutFactory.fillDefaults().numColumns(4).equalWidth(false).applyTo(container);

		this.keyList = createKeyList(dbc, container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).span(4, 5).applyTo(keyList);

		Button refreshButton = new Button(container, SWT.NULL);
		// TODO: Internationalize strings
		refreshButton.setText("Refresh keys");
		refreshButton.addSelectionListener(onRefreshPressed());
		GridDataFactory.fillDefaults().applyTo(refreshButton);

		Label dummyLabel = new Label(container, SWT.NULL);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(dummyLabel);

		Button createButton = new Button(container, SWT.NULL);
		createButton.setText(WizardMessages.getString(NEW));
		createButton.addSelectionListener(onNewPressed());
		GridDataFactory.fillDefaults().applyTo(createButton);

		Button deleteButton = createDeleteButton(container, dbc);
		GridDataFactory.fillDefaults().applyTo(deleteButton);

		setControl(container);
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
				new UpdateValueStrategy().setConverter(new BoundObjectPresentConverter()));
		return deleteButton;
	}

	private void bindWizardComplete(DataBindingContext dbc) {
		dbc.bindValue(
				BeanProperties.value("pageComplete").observe(this),
				BeanProperties.value(ManageKeysPageModel.PROP_SELECTED_KEY).observe(model),
				new UpdateValueStrategy(UpdateSetStrategy.POLICY_NEVER),
				new UpdateValueStrategy().setConverter(new BoundObjectPresentConverter()));
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
		dbc.bindValue(WidgetProperties.selection().observe(keyList),
				BeanProperties.value(ManageKeysPageModel.PROP_SELECTED_KEY).observe(model),
				new UpdateValueStrategy().setConverter(new Id2KeyConverter()),
				new UpdateValueStrategy().setConverter(new Key2IdConverter()));
		bindKeyListEnablement(keyList, dbc);
		return keyList;
	}

	private void bindKeyListEnablement(final List keyList, DataBindingContext dbc) {
		dbc.bindValue(WidgetProperties.enabled().observe(keyList),
				BeanProperties.value(ManageKeysPageModel.PROP_KEYS).observe(model),
				new UpdateValueStrategy(UpdateSetStrategy.POLICY_NEVER),
				new UpdateValueStrategy().setConverter(new Converter(java.util.List.class, Boolean.class) {

					@SuppressWarnings("rawtypes")
					@Override
					public Object convert(Object fromObject) {
						if (fromObject == null) {
							return false;
						}
						return ((java.util.List) fromObject).size() > 0;
					}
				}));
		// BeanProperties.value(ManageKeysPageModel.PROP_KEYS).observe(model).addValueChangeListener(
		// new IValueChangeListener() {
		//
		// @Override
		// public void handleValueChange(ValueChangeEvent event) {
		// @SuppressWarnings("rawtypes")
		// boolean keysPresent = ((java.util.List)
		// event.diff.getNewValue()).size() > 0;
		// keyList.setEnabled(keysPresent);
		// }
		// });
	}

	private SelectionAdapter onDeletePressed() {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				boolean confirmed = MessageDialog.openConfirm(getShell(),
						WizardMessages.getString(CONFIRM_KEY_DELETE_TITLE),
						WizardMessages.getFormattedString(CONFIRM_KEY_DELETE_MSG, model.getSelectedKey().getId()));
				if (confirmed) {
					deleteKey();
				}
			}
		};
	}

	private SelectionListener onRefreshPressed() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				model.refreshKeys();
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
					createKey(keyId);
				}
			}
		};
	}

	private void createKey(final String keyId) {
		try {
			model.createKey(keyId);
		} catch (Exception e) {
			// TODO: internationalize strings
			ErrorUtils.handleError(
					"Error",
					MessageFormat.format("Error", "Could not create key \"{0}\"", keyId), e,
					getShell());
		}
	}

	private void deleteKey() {
		try {
			model.deleteSelectedKey();
		} catch (Exception e) {
			// TODO: internationalize strings
			ErrorUtils.handleError(
					"Error",
					MessageFormat.format("Error", "Could not create key \"{0}\"", model.getSelectedKey().getId()), e,
					getShell());
		}
	}
}
