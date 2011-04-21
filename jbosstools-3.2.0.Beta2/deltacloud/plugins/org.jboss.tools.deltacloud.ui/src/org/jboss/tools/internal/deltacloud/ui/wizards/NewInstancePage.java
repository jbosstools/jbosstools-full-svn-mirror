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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudHardwareProfile;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.DeltaCloudRealm;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.ErrorUtils;
import org.jboss.tools.deltacloud.ui.IDeltaCloudPreferenceConstants;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;
import org.osgi.service.prefs.Preferences;

public class NewInstancePage extends WizardPage {

	private final static String NAME = "NewInstance.name"; //$NON-NLS-1$
	private final static String DESCRIPTION = "NewInstance.desc"; //$NON-NLS-1$
	private final static String TITLE = "NewInstance.title"; //$NON-NLS-1$

	private static final String NAME_LABEL = "Name.label"; //$NON-NLS-1$
	private static final String IMAGE_LABEL = "Image.label"; //$NON-NLS-1$
	private static final String ARCH_LABEL = "Arch.label"; //$NON-NLS-1$
	private static final String HARDWARE_LABEL = "Profile.label"; //$NON-NLS-1$
	private static final String REALM_LABEL = "Realm.label"; //$NON-NLS-1$
	private static final String KEY_LABEL = "Key.label"; //$NON-NLS-1$
	private static final String MANAGE_BUTTON_LABEL = "ManageButton.label"; //$NON-NLS-1$
	private static final String PROPERTIES_LABEL = "Properties.label"; //$NON-NLS-1$

	private static final String NONE_RESPONSE = "None.response"; //$NON-NLS-1$
	@SuppressWarnings("unused")
	private static final String NAME_ALREADY_IN_USE = "ErrorNameInUse.text"; //$NON-NLS-1$

	private DeltaCloud cloud;
	private DeltaCloudImage image;
	private ArrayList<DeltaCloudHardwareProfile> profiles;

	private Text nameText;
	private Text keyText;
	private Combo hardware;
	private Button keyManage;
	private Control realmCombo;
	private String[] profileIds;
	private ProfileComposite currPage;
	private ProfileComposite[] profilePages;
	private List<DeltaCloudRealm> realms;

	private ModifyListener textListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			validate();
		}
	};

	private ModifyListener comboListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			int index = hardware.getSelectionIndex();
			currPage.setVisible(false);
			currPage = profilePages[index];
			currPage.setVisible(true);
		}
	};

	private SelectionListener manageListener = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {
			Shell shell = getShell();
			ManageKeysWizard wizard = new ManageKeysWizard(cloud, ".pem"); //$NON-NLS-1$
			WizardDialog dialog = new CustomWizardDialog(shell, wizard,
					IDialogConstants.OK_LABEL);
			dialog.create();
			dialog.open();
			String keyname = wizard.getKeyName();
			if (keyname != null)
				keyText.setText(keyname);
		}

	};

	public NewInstancePage(DeltaCloud cloud, DeltaCloudImage image) {
		super(WizardMessages.getString(NAME));
		this.cloud = cloud;
		this.image = image;
		getPossibleProfiles();
		setDescription(WizardMessages.getString(DESCRIPTION));
		setTitle(WizardMessages.getString(TITLE));
		setImageDescriptor(SWTImagesFactory.DESC_DELTA_LARGE);
		setPageComplete(false);
	}

	public String getHardwareProfile() {
		return hardware.getText();
	}

	public String getRealmId() {
		if (realmCombo instanceof Combo) {
			int index = ((Combo) realmCombo).getSelectionIndex();
			return realms.get(index).getId();
		} else {
			return null;
		}
	}

	public String getCpuProperty() {
		return currPage.getCPU();
	}

	public String getStorageProperty() {
		return currPage.getStorage();
	}

	public String getMemoryProperty() {
		return currPage.getMemory();
	}

	public String getInstanceName() {
		return nameText.getText();
	}

	public String getKeyName() {
		if (keyText == null) {
			return null;
		} else {
			return keyText.getText();
		}
	}

	private void validate() {
		boolean complete = true;
		boolean errorFree = true;

		setMessage(null);

		String name = nameText.getText();
		if (name.length() == 0) {
			complete = false;
		}

		if (cloud.getType().equals(DeltaCloud.EC2_TYPE)) {
			String keyname = keyText.getText();
			if (keyname.length() == 0)
				complete = false;
			else {
				Preferences prefs = new InstanceScope().getNode(Activator.PLUGIN_ID);
				try {
					prefs.put(IDeltaCloudPreferenceConstants.LAST_EC2_KEYNAME, keyname);
				} catch (Exception e) {
					// ignore
				}
			}
		}

		if (errorFree)
			setErrorMessage(null);
		setPageComplete(complete & errorFree);
	}

	private void getPossibleProfiles() {
		profiles = new ArrayList<DeltaCloudHardwareProfile>();
		try {
			DeltaCloudHardwareProfile[] allProfiles = cloud.getProfiles();
			for (DeltaCloudHardwareProfile p : allProfiles) {
				if (p.getArchitecture() == null || image.getArchitecture().equals(p.getArchitecture())) {
					profiles.add(p);
				}
			}
		} catch (DeltaCloudException e) {
			// TODO internationalize strings
			ErrorUtils.handleError("Error",
					MessageFormat.format("Could not get profiles from cloud {0}", cloud.getName()), e, getShell());
		}
	}

	private String[] getProfileIds(Composite container) {
		String[] ids = new String[profiles.size()];
		profilePages = new ProfileComposite[profiles.size()];
		for (int i = 0; i < profiles.size(); ++i) {
			DeltaCloudHardwareProfile p = profiles.get(i);
			ids[i] = p.getId();
			profilePages[i] = new ProfileComposite(p, container);
			profilePages[i].setVisible(false);
		}
		currPage = profilePages[0];
		return ids;
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		container.setLayout(layout);

		Label dummyLabel = new Label(container, SWT.NULL);

		Label imageLabel = new Label(container, SWT.NULL);
		imageLabel.setText(WizardMessages.getString(IMAGE_LABEL));

		Label imageId = new Label(container, SWT.NULL);
		imageId.setText(image.getName());

		Label archLabel = new Label(container, SWT.NULL);
		archLabel.setText(WizardMessages.getString(ARCH_LABEL));

		Label arch = new Label(container, SWT.NULL);
		arch.setText(image.getArchitecture());

		Label nameLabel = new Label(container, SWT.NULL);
		nameLabel.setText(WizardMessages.getString(NAME_LABEL));

		Label realmLabel = new Label(container, SWT.NULL);
		realmLabel.setText(WizardMessages.getString(REALM_LABEL));

		nameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		nameText.addModifyListener(textListener);

		realms = getRealms();
		createRealmsControl(container, getRealmNames(realms));

		Label hardwareLabel = new Label(container, SWT.NULL);
		hardwareLabel.setText(WizardMessages.getString(HARDWARE_LABEL));

		hardware = new Combo(container, SWT.READ_ONLY);
		Group groupContainer = new Group(container, SWT.BORDER);
		groupContainer.setText(WizardMessages.getString(PROPERTIES_LABEL));
		FormLayout groupLayout = new FormLayout();
		groupLayout.marginHeight = 0;
		groupLayout.marginWidth = 0;
		groupContainer.setLayout(groupLayout);

		profileIds = getProfileIds(groupContainer);

		if (profileIds.length > 0) {
			hardware.setItems(profileIds);
			hardware.setText(profileIds[0]);
			profilePages[0].setVisible(true);
			hardware.addModifyListener(comboListener);
		}

		Point p1 = nameLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point p2 = nameText.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int centering = (p2.y - p1.y + 1) / 2;

		FormData f = new FormData();
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(100, 0);
		dummyLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(dummyLabel, 8 + centering);
		f.left = new FormAttachment(0, 0);
		nameLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(dummyLabel, 8);
		f.left = new FormAttachment(hardwareLabel, 5);
		f.right = new FormAttachment(100, 0);
		nameText.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(nameText, 8);
		f.left = new FormAttachment(0, 0);
		imageLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(nameText, 8);
		f.left = new FormAttachment(hardwareLabel, 5);
		f.right = new FormAttachment(100, 0);
		imageId.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(imageLabel, 8);
		f.left = new FormAttachment(0, 0);
		archLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(imageLabel, 8);
		f.left = new FormAttachment(hardwareLabel, 5);
		arch.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(arch, 8 + centering);
		f.left = new FormAttachment(0, 0);
		realmLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(arch, 8);
		f.left = new FormAttachment(hardwareLabel, 5);
		f.right = new FormAttachment(100, 0);
		realmCombo.setLayoutData(f);

		Control control = realmCombo;

		if (cloud.getType().equals(DeltaCloud.EC2_TYPE)) {
			Label keyLabel = new Label(container, SWT.NULL);
			keyLabel.setText(WizardMessages.getString(KEY_LABEL));

			keyText = new Text(container, SWT.BORDER | SWT.SINGLE);
			Preferences prefs = new InstanceScope().getNode(Activator.PLUGIN_ID);
			String defaultKeyname = prefs.get(IDeltaCloudPreferenceConstants.LAST_EC2_KEYNAME, "");
			keyText.setText(defaultKeyname);
			keyText.addModifyListener(textListener);

			keyManage = new Button(container, SWT.NULL);
			keyManage.setText(WizardMessages.getString(MANAGE_BUTTON_LABEL));
			keyManage.addSelectionListener(manageListener);
			Point p3 = keyManage.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			int centering2 = (p3.y - p2.y + 1) / 2;

			f = new FormData();
			f.top = new FormAttachment(realmCombo, 8 + centering + centering2);
			f.left = new FormAttachment(0, 0);
			keyLabel.setLayoutData(f);

			f = new FormData();
			int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
			Point minSize = keyManage.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
			f.width = Math.max(widthHint, minSize.x);
			f.top = new FormAttachment(realmCombo, 8);
			f.right = new FormAttachment(realmCombo, 0, SWT.RIGHT);
			keyManage.setLayoutData(f);

			f = new FormData();
			f.top = new FormAttachment(realmCombo, 8 + centering2);
			f.left = new FormAttachment(hardwareLabel, 5);
			f.right = new FormAttachment(keyManage, -10);
			keyText.setLayoutData(f);

			control = keyText;
		}

		f = new FormData();
		f.top = new FormAttachment(control, 8 + centering);
		f.left = new FormAttachment(0, 0);
		hardwareLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(control, 8);
		f.left = new FormAttachment(hardwareLabel, 5);
		f.right = new FormAttachment(100, 0);
		hardware.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(hardware, 10);
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(100, 0);
		f.bottom = new FormAttachment(100, 0);
		groupContainer.setLayoutData(f);

		setControl(container);
	}

	private List<String> getRealmNames(List<DeltaCloudRealm> realms) {
		List<String> realmNames = new ArrayList<String>();
		for (DeltaCloudRealm realm : realms) {
			realmNames.add(
					new StringBuilder()
							.append(realm.getId())
							.append("   [") //$NON-NLS-1$
							.append(realm.getName())
							.append("]") //$NON-NLS-1$ 
							.toString());
		}
		return realmNames;
	}

	private List<DeltaCloudRealm> getRealms() {
		List<DeltaCloudRealm> realms = new ArrayList<DeltaCloudRealm>();
		try {
			realms = Arrays.asList(cloud.getRealms());
		} catch (DeltaCloudException e) {
			ErrorUtils.handleError("Error",
					MessageFormat.format("Could not get realms from cloud {0}", cloud.getName()), e, getShell());
		}
		return realms;
	}

	/**
	 * Creates the control that shall display the available realms. It creates
	 * either a combo, if there are realms available, or a label if none are
	 * available.
	 * 
	 * @param parent
	 *            the container
	 * @param realmNames
	 *            the realm names
	 */
	private void createRealmsControl(final Composite parent, List<String> realmNames) {
		if (realmNames.size() > 0) {
			Combo combo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
			combo.setItems(realmNames.toArray(new String[realmNames.size()]));
			combo.setText(realmNames.get(0));
			realmCombo = combo;
		} else {
			Label label = new Label(parent, SWT.NULL);
			label.setText(WizardMessages.getString(NONE_RESPONSE));
			realmCombo = label;
		}
	}
}
