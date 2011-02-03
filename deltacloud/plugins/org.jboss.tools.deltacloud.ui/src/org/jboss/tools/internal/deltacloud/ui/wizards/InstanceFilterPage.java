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

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.ICloudElementFilter;
import org.jboss.tools.deltacloud.core.IFieldMatcher;
import org.jboss.tools.deltacloud.core.IInstanceFilter;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;

/**
 * @author Jeff Johnston
 */
public class InstanceFilterPage extends WizardPage {

	private final static String NAME = "InstanceFilter.name"; //$NON-NLS-1$
	private final static String TITLE = "InstanceFilter.title"; //$NON-NLS-1$
	private final static String DESC = "InstanceFilter.desc"; //$NON-NLS-1$
	private final static String FILTER_LABEL = "InstanceFilter.label"; //$NON-NLS-1$
	private final static String EMPTY_RULE = "ErrorFilterEmptyRule.msg"; //$NON-NLS-1$
	private final static String INVALID_SEMICOLON = "ErrorFilterSemicolon.msg"; //$NON-NLS-1$
	private final static String NAME_LABEL = "Name.label"; //$NON-NLS-1$
	private final static String ID_LABEL = "Id.label"; //$NON-NLS-1$
	private final static String ALIAS_LABEL = "Alias.label"; //$NON-NLS-1$
	private final static String OWNER_ID_LABEL = "OwnerId.label"; //$NON-NLS-1$
	private final static String IMAGE_ID_LABEL = "ImageId.label"; //$NON-NLS-1$
	private final static String KEYNAME_LABEL = "Key.label"; //$NON-NLS-1$
	private final static String REALM_LABEL = "Realm.label"; //$NON-NLS-1$
	private final static String PROFILE_LABEL = "Profile.label"; //$NON-NLS-1$
	private final static String DEFAULT_LABEL = "DefaultButton.label"; //$NON-NLS-1$

	private DeltaCloud cloud;
	private Text nameText;
	private Text idText;
	private Text aliasText;
	private Text imageIdText;
	private Text ownerIdText;
	private Text keyIdText;
	private Text realmText;
	private Text profileText;

	private Button defaultName;
	private Button defaultId;
	private Button defaultAlias;
	private Button defaultImageId;
	private Button defaultOwnerId;
	private Button defaultKeyId;
	private Button defaultRealm;
	private Button defaultProfile;

	public InstanceFilterPage(DeltaCloud cloud) {
		super(WizardMessages.getString(NAME));
		this.cloud = cloud;
		setDescription(WizardMessages.getString(DESC));
		setTitle(WizardMessages.getString(TITLE));
		setImageDescriptor(SWTImagesFactory.DESC_DELTA_LARGE);
		setPageComplete(false);
	}

	public String getNameRule() {
		return nameText.getText();
	}

	public String getIdRule() {
		return idText.getText();
	}

	public String getAliasRule() {
		return aliasText.getText();
	}

	public String getImageIdRule() {
		return imageIdText.getText();
	}

	public String getOwnerIdRule() {
		return ownerIdText.getText();
	}

	public String getKeyNameRule() {
		return keyIdText.getText();
	}

	public String getRealmRule() {
		return realmText.getText();
	}

	public String getProfileRule() {
		return profileText.getText();
	}

	private ModifyListener Listener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			// TODO Auto-generated method stub
			validate();
		}
	};

	private SelectionAdapter buttonListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button) e.widget;
			Text text = getTextWidget(b);
			if (text != null) {
				text.setText(ICloudElementFilter.ALL_MATCHER_EXPRESSION);
			}
		}

		private Text getTextWidget(Button button) {
			Text text = null;
			if (button == defaultName) {
				text = nameText;
			}
			else if (button == defaultId) {
				text = idText;
			}
			else if (button == defaultAlias) {
				text = aliasText;
			}
			else if (button == defaultImageId) {
				text = imageIdText;
			}
			else if (button == defaultOwnerId) {
				text = ownerIdText;
			}
			else if (button == defaultKeyId) {
				text = keyIdText;
			}
			else if (button == defaultRealm) {
				text = realmText;
			}
			else if (button == defaultProfile) {
				text = profileText;
			}
			return text;
		}
	};

	private void validate() {
		boolean complete = true;
		boolean error = false;

		if (nameText.getText().length() == 0 ||
				idText.getText().length() == 0 ||
				aliasText.getText().length() == 0 ||
				imageIdText.getText().length() == 0 ||
				ownerIdText.getText().length() == 0 ||
				keyIdText.getText().length() == 0 ||
				realmText.getText().length() == 0 ||
				profileText.getText().length() == 0) {

			setErrorMessage(WizardMessages.getString(EMPTY_RULE));
			error = true;
		} else if (nameText.getText().contains(";") ||
				idText.getText().contains(";") ||
				aliasText.getText().contains(";") ||
				imageIdText.getText().contains(";") ||
				ownerIdText.getText().contains(";") ||
				keyIdText.getText().contains(";") ||
				realmText.getText().contains(";") ||
				profileText.getText().contains(";")) {
			setErrorMessage(WizardMessages.getString(INVALID_SEMICOLON));
			error = true;
		}

		if (!error)
			setErrorMessage(null);
		setPageComplete(complete && !error);
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		GridLayoutFactory.fillDefaults().numColumns(3).spacing(8, 4).applyTo(container);
		
		Label label = new Label(container, SWT.NULL);
		label.setText(WizardMessages.getString(FILTER_LABEL));
		GridDataFactory.fillDefaults().span(3, 1).align(SWT.LEFT, SWT.CENTER).indent(0, 14).hint(SWT.DEFAULT, 30).applyTo(label);
		
		IInstanceFilter filter = cloud.getInstanceFilter();

		Label nameLabel = createRuleLabel(WizardMessages.getString(NAME_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(nameLabel);
		this.nameText = createRuleText(filter.getNameRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(nameText);
		this.defaultName = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(defaultName);

		Label aliasLabel = createRuleLabel(WizardMessages.getString(ALIAS_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(aliasLabel);
		this.aliasText = createRuleText(filter.getAliasRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(aliasText);
		this.defaultAlias = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(defaultAlias);

		Label idLabel = createRuleLabel(WizardMessages.getString(ID_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(idLabel);
		this.idText = createRuleText(filter.getIdRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(idText);
		this.defaultId = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(defaultId);

		Label imageIdLabel = createRuleLabel(WizardMessages.getString(IMAGE_ID_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(imageIdLabel);
		this.imageIdText = createRuleText(filter.getImageIdRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(imageIdText);
		this.defaultImageId = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(defaultImageId);

		Label ownerIdLabel = createRuleLabel(WizardMessages.getString(OWNER_ID_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(ownerIdLabel);
		this.ownerIdText = createRuleText(filter.getOwnerIdRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(ownerIdText);
		this.defaultOwnerId = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(defaultOwnerId);

		Label keyNameLabel = createRuleLabel(WizardMessages.getString(KEYNAME_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(keyNameLabel);
		this.keyIdText = createRuleText(filter.getKeyNameRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(keyIdText);
		this.defaultKeyId = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(defaultKeyId);

		Label realmLabel = createRuleLabel(WizardMessages.getString(REALM_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(realmLabel);
		this.realmText = createRuleText(filter.getKeyNameRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(realmText);
		this.defaultRealm = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(defaultRealm);

		Label profileLabel = createRuleLabel(WizardMessages.getString(PROFILE_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(profileLabel);
		this.profileText = createRuleText(filter.getProfileRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(profileText);
		this.defaultProfile = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(defaultProfile);

		setControl(container);
		setPageComplete(true);
	}

	private Label createRuleLabel(String text, Composite container) {
		Label label = new Label(container, SWT.NULL);
		label.setText(text);
		return label;
	}

	private Button createDefaultRuleButton(final Composite container) {
		Button button = new Button(container, SWT.NULL);
		button.setText(WizardMessages.getString(DEFAULT_LABEL));
		button.addSelectionListener(buttonListener);
		return button;
	}

	private Text createRuleText(IFieldMatcher rule, final Composite container) {
		Assert.isNotNull(rule, "Rule may not be null");
		
		Text text = new Text(container, SWT.BORDER | SWT.SINGLE);
		text.setText(rule.toString());
		text.addModifyListener(Listener);
		return text;
	}
}
