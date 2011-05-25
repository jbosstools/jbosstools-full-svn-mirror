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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.deltacloud.core.DeltaCloud;
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
	private Text keyNameText;
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
		return keyNameText.getText();
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
	
	private SelectionAdapter ButtonListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			if (b == defaultName)
				nameText.setText("*"); //$NON-NLS-1$
			else if (b == defaultId)
				idText.setText("*"); //$NON-NLS-1$
			else if (b == defaultAlias) 
				aliasText.setText("*"); //$NON-NLS-1$
			else if (b == defaultImageId)
				imageIdText.setText("*"); //$NON-NLS-1$
			else if (b == defaultOwnerId)
				ownerIdText.setText("*"); //$NON-NLS-1$
			else if (b == defaultKeyId)
				keyNameText.setText("*"); //$NON-NLS-1$
			else if (b == defaultRealm)
				realmText.setText("*"); //$NON-NLS-1$
			else if (b == defaultProfile)
				profileText.setText("*"); //$NON-NLS-1$			
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
				keyNameText.getText().length() == 0 ||
				realmText.getText().length() == 0 ||
				profileText.getText().length() == 0) {
			
			setErrorMessage(WizardMessages.getString(EMPTY_RULE));
			error = true;
		} else if (nameText.getText().contains(";") ||
				idText.getText().contains(";") ||
				aliasText.getText().contains(";") ||
				imageIdText.getText().contains(";") ||
				ownerIdText.getText().contains(";") ||
				keyNameText.getText().contains(";") ||
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
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		container.setLayout(layout);		

		Label label = new Label(container, SWT.NULL);
		label.setText(WizardMessages.getString(FILTER_LABEL));
		
		Label nameLabel = new Label(container, SWT.NULL);
		nameLabel.setText(WizardMessages.getString(NAME_LABEL));
		
		nameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		nameText.setText(cloud.getInstanceFilter().getNameRule().toString());
		nameText.addModifyListener(Listener);
		
		defaultName = new Button(container, SWT.NULL);
		defaultName.setText(WizardMessages.getString(DEFAULT_LABEL));
		defaultName.addSelectionListener(ButtonListener);

		Label idLabel = new Label(container, SWT.NULL);
		idLabel.setText(WizardMessages.getString(ID_LABEL));

		idText = new Text(container, SWT.BORDER | SWT.SINGLE);
		idText.setText(cloud.getInstanceFilter().getIdRule().toString());
		idText.addModifyListener(Listener);

		defaultId = new Button(container, SWT.NULL);
		defaultId.setText(WizardMessages.getString(DEFAULT_LABEL));
		defaultId.addSelectionListener(ButtonListener);

		Label aliasLabel = new Label(container, SWT.NULL);
		aliasLabel.setText(WizardMessages.getString(ALIAS_LABEL));

		aliasText = new Text(container, SWT.BORDER | SWT.SINGLE);
		aliasText.setText(cloud.getInstanceFilter().getAliasRule().toString());
		aliasText.addModifyListener(Listener);

		defaultAlias = new Button(container, SWT.NULL);
		defaultAlias.setText(WizardMessages.getString(DEFAULT_LABEL));
		defaultAlias.addSelectionListener(ButtonListener);

		Label imageIdLabel = new Label(container, SWT.NULL);
		imageIdLabel.setText(WizardMessages.getString(IMAGE_ID_LABEL));
		
		imageIdText = new Text(container, SWT.BORDER | SWT.SINGLE);
		imageIdText.setText(cloud.getInstanceFilter().getImageIdRule().toString());
		imageIdText.addModifyListener(Listener);
		
		defaultImageId = new Button(container, SWT.NULL);
		defaultImageId.setText(WizardMessages.getString(DEFAULT_LABEL));
		defaultImageId.addSelectionListener(ButtonListener);

		Label ownerIdLabel = new Label(container, SWT.NULL);
		ownerIdLabel.setText(WizardMessages.getString(OWNER_ID_LABEL));

		ownerIdText = new Text(container, SWT.BORDER | SWT.SINGLE);
		ownerIdText.setText(cloud.getInstanceFilter().getOwnerIdRule().toString());
		ownerIdText.addModifyListener(Listener);
		
		defaultOwnerId = new Button(container, SWT.NULL);
		defaultOwnerId.setText(WizardMessages.getString(DEFAULT_LABEL));
		defaultOwnerId.addSelectionListener(ButtonListener);

		Label keyNameLabel = new Label(container, SWT.NULL);
		keyNameLabel.setText(WizardMessages.getString(KEYNAME_LABEL));

		keyNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		keyNameText.setText(cloud.getInstanceFilter().getKeyNameRule().toString());
		keyNameText.addModifyListener(Listener);
		
		defaultKeyId = new Button(container, SWT.NULL);
		defaultKeyId.setText(WizardMessages.getString(DEFAULT_LABEL));
		defaultKeyId.addSelectionListener(ButtonListener);

		Label realmLabel = new Label(container, SWT.NULL);
		realmLabel.setText(WizardMessages.getString(REALM_LABEL));

		realmText = new Text(container, SWT.BORDER | SWT.SINGLE);
		realmText.setText(cloud.getInstanceFilter().getRealmRule().toString());
		realmText.addModifyListener(Listener);
		
		defaultRealm = new Button(container, SWT.NULL);
		defaultRealm.setText(WizardMessages.getString(DEFAULT_LABEL));
		defaultRealm.addSelectionListener(ButtonListener);
		
		Label profileLabel = new Label(container, SWT.NULL);
		profileLabel.setText(WizardMessages.getString(PROFILE_LABEL));

		profileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		profileText.setText(cloud.getInstanceFilter().getProfileRule().toString());
		profileText.addModifyListener(Listener);
		
		defaultProfile = new Button(container, SWT.NULL);
		defaultProfile.setText(WizardMessages.getString(DEFAULT_LABEL));
		defaultProfile.addSelectionListener(ButtonListener);
		
		Point p1 = label.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point p2 = nameText.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point p3 = defaultName.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int centering = (p2.y - p1.y + 1) / 2;
		int centering2 = (p3.y - p2.y + 1) / 2;

		FormData f = new FormData();
		f.top = new FormAttachment(0);
		label.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(label, 11 + centering + centering2);
		f.left = new FormAttachment(0, 0);
		nameLabel.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(label, 11);
		f.right = new FormAttachment(100);
		defaultName.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(label, 11 + centering2);
		f.left = new FormAttachment(profileLabel, 5);
		f.right = new FormAttachment(defaultName, -10);
		nameText.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(nameLabel, 11 + centering + centering2);
		f.left = new FormAttachment(0, 0);
		idLabel.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(nameLabel, 11);
		f.right = new FormAttachment(100);
		defaultId.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(nameLabel, 11 + centering2);
		f.left = new FormAttachment(profileLabel, 5);
		f.right = new FormAttachment(defaultId, -10);
		idText.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(idLabel, 11 + centering + centering2);
		f.left = new FormAttachment(0, 0);
		aliasLabel.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(idLabel, 11);
		f.right = new FormAttachment(100);
		defaultAlias.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(idLabel, 11 + centering2);
		f.left = new FormAttachment(profileLabel, 5);
		f.right = new FormAttachment(defaultAlias, -10);
		aliasText.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(aliasLabel, 11 + centering + centering2);
		f.left = new FormAttachment(0, 0);
		imageIdLabel.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(aliasLabel, 11);
		f.right = new FormAttachment(100);
		defaultImageId.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(aliasLabel, 11 + centering2);
		f.left = new FormAttachment(profileLabel, 5);
		f.right = new FormAttachment(defaultImageId, -10);
		imageIdText.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(imageIdLabel, 11 + centering + centering2);
		f.left = new FormAttachment(0, 0);
		ownerIdLabel.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(imageIdLabel, 11);
		f.right = new FormAttachment(100);
		defaultOwnerId.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(imageIdLabel, 11 + centering2);
		f.left = new FormAttachment(profileLabel, 5);
		f.right = new FormAttachment(defaultOwnerId, -10);
		ownerIdText.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(ownerIdLabel, 11 + centering + centering2);
		f.left = new FormAttachment(0, 0);
		keyNameLabel.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(ownerIdLabel, 11);
		f.right = new FormAttachment(100);
		defaultKeyId.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(ownerIdLabel, 11 + centering2);
		f.left = new FormAttachment(profileLabel, 5);
		f.right = new FormAttachment(defaultKeyId, -10);
		keyNameText.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(keyNameLabel, 11 + centering + centering2);
		f.left = new FormAttachment(0, 0);
		realmLabel.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(keyNameLabel, 11);
		f.right = new FormAttachment(100);
		defaultRealm.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(keyNameLabel, 11 + centering2);
		f.left = new FormAttachment(profileLabel, 5);
		f.right = new FormAttachment(defaultRealm, -10);
		realmText.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(realmLabel, 11 + centering + centering2);
		f.left = new FormAttachment(0, 0);
		profileLabel.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(realmLabel, 11);
		f.right = new FormAttachment(100);
		defaultProfile.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(realmLabel, 11 + centering2);
		f.left = new FormAttachment(profileLabel, 5);
		f.right = new FormAttachment(defaultProfile, -10);
		profileText.setLayoutData(f);

		setControl(container);
		setPageComplete(true);
	}

}
