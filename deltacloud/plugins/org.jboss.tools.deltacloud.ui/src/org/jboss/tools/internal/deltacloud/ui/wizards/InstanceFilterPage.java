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

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.IInstanceFilter;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

/**
 * @author Jeff Johnston
 */
public class InstanceFilterPage extends AbstractFilterPage {

	private final static String NAME = "InstanceFilter.name"; //$NON-NLS-1$
	private final static String TITLE = "InstanceFilter.title"; //$NON-NLS-1$
	private final static String DESC = "InstanceFilter.desc"; //$NON-NLS-1$
	private final static String FILTER_LABEL = "InstanceFilter.label"; //$NON-NLS-1$
	private final static String NAME_LABEL = "Name.label"; //$NON-NLS-1$
	private final static String ID_LABEL = "Id.label"; //$NON-NLS-1$
	private final static String ALIAS_LABEL = "Alias.label"; //$NON-NLS-1$
	private final static String OWNER_ID_LABEL = "OwnerId.label"; //$NON-NLS-1$
	private final static String IMAGE_ID_LABEL = "ImageId.label"; //$NON-NLS-1$
	private final static String KEYNAME_LABEL = "Key.label"; //$NON-NLS-1$
	private final static String REALM_LABEL = "Realm.label"; //$NON-NLS-1$
	private final static String PROFILE_LABEL = "Profile.label"; //$NON-NLS-1$

	private Text nameText;
	private ControlDecoration nameDecoration;
	private Button defaultName;
	private Text idText;
	private ControlDecoration idDecoration;
	private Button defaultId;
	private Text aliasText;
	private ControlDecoration aliasDecoration;
	private Button defaultAlias;
	private Text imageIdText;
	private ControlDecoration imageDecoration;
	private Button defaultImageId;
	private Text ownerIdText;
	private Button defaultOwnerId;
	private ControlDecoration ownerDecoration;
	private Text keyIdText;
	private ControlDecoration keyIdDecoration;
	private Button defaultKeyId;
	private Text realmText;
	private ControlDecoration realmDecoration;
	private Button defaultRealm;
	private Text profileText;
	private ControlDecoration profileDecoration;
	private Button defaultProfile;

	public InstanceFilterPage(DeltaCloud cloud) {
		super(WizardMessages.getString(NAME), WizardMessages.getString(TITLE), WizardMessages.getString(DESC), cloud);
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

	@Override
	protected Text getTextWidget(Button button) {
		Text text = null;
		if (button == defaultName) {
			text = nameText;
		} else if (button == defaultId) {
			text = idText;
		} else if (button == defaultAlias) {
			text = aliasText;
		} else if (button == defaultImageId) {
			text = imageIdText;
		} else if (button == defaultOwnerId) {
			text = ownerIdText;
		} else if (button == defaultKeyId) {
			text = keyIdText;
		} else if (button == defaultRealm) {
			text = realmText;
		} else if (button == defaultProfile) {
			text = profileText;
		}
		return text;
	}

	@Override
	protected void validate() {
		nameDecoration.hide();
		idDecoration.hide();
		aliasDecoration.hide();
		imageDecoration.hide();
		ownerDecoration.hide();
		keyIdDecoration.hide();
		realmDecoration.hide();
		profileDecoration.hide();
		
		String error = null;
		error = validate(nameText, nameDecoration, error);
		error = validate(idText, idDecoration, error);
		error = validate(aliasText, aliasDecoration, error);
		error = validate(imageIdText, imageDecoration, error);
		error = validate(ownerIdText, ownerDecoration, error);
		error = validate(keyIdText, keyIdDecoration, error);
		error = validate(realmText, realmDecoration, error);
		error = validate(profileText, profileDecoration, error);
		setPageComplete(error == null);
		setErrorMessage(error);
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		GridLayoutFactory.fillDefaults().numColumns(3).spacing(8, 4).applyTo(container);

		Label label = new Label(container, SWT.NULL);
		label.setText(WizardMessages.getString(FILTER_LABEL));
		GridDataFactory.fillDefaults().span(3, 1).align(SWT.LEFT, SWT.CENTER).indent(0, 14).hint(SWT.DEFAULT, 30)
				.applyTo(label);

		IInstanceFilter filter = getDeltaCloud().getInstanceFilter();

		Label nameLabel = createRuleLabel(WizardMessages.getString(NAME_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(nameLabel);
		this.nameText = createRuleText(filter.getNameRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(nameText);
		this.nameDecoration = UIUtils.createErrorDecoration("", nameText);
		this.defaultName = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(defaultName);

		Label aliasLabel = createRuleLabel(WizardMessages.getString(ALIAS_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(aliasLabel);
		this.aliasText = createRuleText(filter.getAliasRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(aliasText);
		this.aliasDecoration = UIUtils.createErrorDecoration("", aliasText);
		this.defaultAlias = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(defaultAlias);

		Label idLabel = createRuleLabel(WizardMessages.getString(ID_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(idLabel);
		this.idText = createRuleText(filter.getIdRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(idText);
		this.idDecoration = UIUtils.createErrorDecoration("", idText);
		this.defaultId = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(defaultId);

		Label imageIdLabel = createRuleLabel(WizardMessages.getString(IMAGE_ID_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(imageIdLabel);
		this.imageIdText = createRuleText(filter.getImageIdRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(imageIdText);
		this.imageDecoration = UIUtils.createErrorDecoration("", imageIdText);
		this.defaultImageId = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(defaultImageId);

		Label ownerIdLabel = createRuleLabel(WizardMessages.getString(OWNER_ID_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(ownerIdLabel);
		this.ownerIdText = createRuleText(filter.getOwnerIdRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(ownerIdText);
		this.ownerDecoration = UIUtils.createErrorDecoration("", ownerIdText);
		this.defaultOwnerId = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(defaultOwnerId);

		Label keyNameLabel = createRuleLabel(WizardMessages.getString(KEYNAME_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(keyNameLabel);
		this.keyIdText = createRuleText(filter.getKeyNameRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(keyIdText);
		this.keyIdDecoration = UIUtils.createErrorDecoration("", keyIdText);
		this.defaultKeyId = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(defaultKeyId);

		Label realmLabel = createRuleLabel(WizardMessages.getString(REALM_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(realmLabel);
		this.realmText = createRuleText(filter.getRealmRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(realmText);
		this.realmDecoration = UIUtils.createErrorDecoration("", realmText);
		this.defaultRealm = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(defaultRealm);

		Label profileLabel = createRuleLabel(WizardMessages.getString(PROFILE_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(profileLabel);
		this.profileText = createRuleText(filter.getProfileRule(), container);
		this.profileDecoration = UIUtils.createErrorDecoration("", profileText);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(profileText);
		this.defaultProfile = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(defaultProfile);

		setControl(container);
		setPageComplete(true);
	}
}
