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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudHardwareProfile;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.DeltaCloudRealm;
import org.jboss.tools.deltacloud.ui.ErrorUtils;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;
import org.jboss.tools.internal.deltacloud.ui.common.databinding.validator.MandatoryStringValidator;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

/**
 * @author Jeff Jonston
 */
public class NewInstancePage extends WizardPage {

	private static final int IMAGE_CHECK_DELAY = 500;

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
	private static final String FIND_BUTTON_LABEL = "FindButton.label"; //$NON-NLS-1$
	private static final String PROPERTIES_LABEL = "Properties.label"; //$NON-NLS-1$
	private static final String MUST_ENTER_A_NAME = "ErrorMustProvideName.text"; //$NON-NLS-1$	
	private static final String MUST_ENTER_A_KEYNAME = "ErrorMustProvideKeyName.text"; //$NON-NLS-1$	
	private static final String MUST_ENTER_IMAGE_ID = "ErrorMustProvideImageId.text"; //$NON-NLS-1$	
	private static final String NONE_RESPONSE = "None.response"; //$NON-NLS-1$
	private static final String LOADING_VALUE = "Loading.value"; //$NON-NLS-1$

	private NewInstanceModel model;

	private DeltaCloud cloud;
	private DeltaCloudImage image;
	private Label arch;
	private Text nameText;
	private Text imageText;
	private Text keyText;
	private Combo hardware;
	private Button keyManage;
	private Button findImage;
	private Combo realmCombo;
	private ProfileComposite currPage;
	private Map<String, ProfileComposite> profilePages;
	private DeltaCloudHardwareProfile[] allProfiles;
	private List<DeltaCloudRealm> realms;

	private Group groupContainer;

	private ModifyListener comboListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			int index = hardware.getSelectionIndex();
			String id = index > -1 ? hardware.getItem(hardware.getSelectionIndex()) : null;
			if (currPage != null)
				currPage.setVisible(false);
			if (id != null) {
				currPage = profilePages.get(id);
				currPage.setVisible(true);
			}
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

	private SelectionListener findListener = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {
			Shell shell = getShell();
			FindImageWizard wizard = new FindImageWizard(cloud);
			WizardDialog dialog = new CustomWizardDialog(shell, wizard,
					IDialogConstants.OK_LABEL);
			dialog.create();
			dialog.open();
			String imageId = wizard.getImageId();
			if (imageId != null)
				imageText.setText(imageId);
		}

	};

	public NewInstancePage(DeltaCloud cloud) {
		super(WizardMessages.getString(NAME));
		this.cloud = cloud;
		String defaultKeyname = cloud.getLastKeyname();
		model = new NewInstanceModel("", //$NON-NLS-1$ 
				"", //$NON-NLS-1$
				"", //$NON-NLS-1$
				"", //$NON-NLS-1$
				defaultKeyname, ""); //$NON-NLS-1$
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
		return keyText.getText();
	}

	public String getImageId() {
		return imageText.getText();
	}

	public void setImage(DeltaCloudImage image) {
		this.image = image;
	}

	public void clearProfiles() {
		hardware.removeModifyListener(comboListener);
		hardware.removeAll();
		if (currPage != null) {
			currPage.setVisible(false);
		}
		hardware.setEnabled(false);
		hardware.addModifyListener(comboListener);
	}

	private DeltaCloudHardwareProfile[] getProfiles() {
		List<DeltaCloudHardwareProfile> profiles = new ArrayList<DeltaCloudHardwareProfile>();
		try {
			DeltaCloudHardwareProfile[] allProfiles = cloud.getProfiles();
			for (DeltaCloudHardwareProfile p : allProfiles) {
				profiles.add(p);
			}
		} catch (DeltaCloudException e) {
			// TODO internationalize strings
			ErrorUtils.handleError("Error",
					MessageFormat.format("Could not get profiles from cloud {0}", cloud.getName()), e, getShell());
		}
		return profiles.toArray(new DeltaCloudHardwareProfile[profiles.size()]);
	}

	public void filterProfiles() {
		if (allProfiles == null)
			return;

		ArrayList<DeltaCloudHardwareProfile> profiles = new ArrayList<DeltaCloudHardwareProfile>();
		for (DeltaCloudHardwareProfile p : allProfiles) {
			if (p.getArchitecture() == null || image == null || image.getArchitecture().equals(p.getArchitecture())) {
				profiles.add(p);
			}
		}
		String[] ids = new String[profiles.size()];
		for (int i = 0; i < profiles.size(); ++i) {
			DeltaCloudHardwareProfile p = profiles.get(i);
			ids[i] = p.getId();
		}
		if (ids.length > 0) {
			hardware.removeModifyListener(comboListener);
			hardware.setItems(ids);
			hardware.setText(ids[0]);
			currPage = profilePages.get(ids[0]);
			currPage.setVisible(true);
			hardware.setEnabled(true);
			hardware.addModifyListener(comboListener);
		}
	}

	@Override
	public void createControl(Composite parent) {
		DataBindingContext dbc = new DataBindingContext();
		WizardPageSupport.create(this, dbc);
		Composite container = createWidgets(parent);
		bindWidgets(dbc);

		launchFetchRealms();
		launchFetchProfiles();

		// We have to set the image id here instead of in the constructor
		// of the model because the image id triggers other items to fill
		// in their values such as the architecture and hardware profiles.
		String defaultImage = cloud.getLastImageId();
		model.setImageId(defaultImage);
		setControl(container);

		// lastly, if there's already an image set, use it
		if (image != null) {
			imageText.setText(image.getId());
			filterProfiles();
		}
	}

	private void bindWidgets(DataBindingContext dbc) {
		bindText(dbc, nameText, NewInstanceModel.PROPERTY_NAME, MUST_ENTER_A_NAME);
		dbc.bindValue(
				WidgetProperties.text(SWT.Modify).observeDelayed(IMAGE_CHECK_DELAY, imageText),
				BeanProperties.value(NewInstanceModel.class, NewInstanceModel.PROPERTY_IMAGE_ID)
						.observe(model),
				new UpdateValueStrategy().setAfterGetValidator(new MandatoryStringValidator(
						WizardMessages.getString(MUST_ENTER_IMAGE_ID))),
				null);
		bindArchLabel(dbc, imageText, arch, this);

		IObservableValue realmObservable = WidgetProperties.text().observe(realmCombo);
		dbc.bindValue(
				realmObservable,
				BeanProperties.value(NewInstanceModel.class, NewInstanceModel.PROPERTY_REALM).observe(
						model));

		IObservableValue hardwareObservable = WidgetProperties.text().observe(hardware);
		dbc.bindValue(
				hardwareObservable,
				BeanProperties.value(NewInstanceModel.class, NewInstanceModel.PROPERTY_PROFILE).observe(
						model));
		bindText(dbc, keyText, NewInstanceModel.PROPERTY_KEYNAME, MUST_ENTER_A_KEYNAME);
	}

	private Composite createWidgets(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		container.setLayout(layout);

		Label dummyLabel = new Label(container, SWT.NULL);

		Label nameLabel = new Label(container, SWT.NULL);
		nameLabel.setText(WizardMessages.getString(NAME_LABEL));
		nameText = new Text(container, SWT.BORDER | SWT.SINGLE);

		Label imageLabel = new Label(container, SWT.NULL);
		imageLabel.setText(WizardMessages.getString(IMAGE_LABEL));
		imageText = new Text(container, SWT.BORDER | SWT.SINGLE);

		Label realmLabel = new Label(container, SWT.NULL);
		realmLabel.setText(WizardMessages.getString(REALM_LABEL));

		// createRealmsControl(container, getRealmNames(realms));
		createRealmsControl(container);

		findImage = new Button(container, SWT.NULL);
		findImage.setText(WizardMessages.getString(FIND_BUTTON_LABEL));
		findImage.addSelectionListener(findListener);

		Label archLabel = new Label(container, SWT.NULL);
		archLabel.setText(WizardMessages.getString(ARCH_LABEL));
		arch = new Label(container, SWT.NULL);

		Label hardwareLabel = new Label(container, SWT.NULL);
		hardwareLabel.setText(WizardMessages.getString(HARDWARE_LABEL));

		hardware = new Combo(container, SWT.READ_ONLY);
		hardware.setEnabled(false);
		hardware.setItems(new String[] { WizardMessages.getString(LOADING_VALUE) });
		hardware.select(0);

		groupContainer = new Group(container, SWT.BORDER);
		groupContainer.setText(WizardMessages.getString(PROPERTIES_LABEL));
		FormLayout groupLayout = new FormLayout();
		groupLayout.marginHeight = 0;
		groupLayout.marginWidth = 0;
		groupContainer.setLayout(groupLayout);
		hardware.setEnabled(false);

		// add invisible dummy widget to guarantee a min size
		dummyLabel = new Label(groupContainer, SWT.NONE);
		dummyLabel.setText("\n\n\n\n\n");
		FormData dummyData = UIUtils.createFormData(0, 0, 0, 150, null, 0, null, 0);
		dummyLabel.setLayoutData(dummyData);
		dummyLabel.setVisible(false);

		keyManage = new Button(container, SWT.NULL);
		keyManage.setText(WizardMessages.getString(MANAGE_BUTTON_LABEL));
		keyManage.addSelectionListener(manageListener);
		if (cloud.getType().equals(DeltaCloud.MOCK_TYPE))
			keyManage.setEnabled(false);

		Point p1 = nameLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point p2 = nameText.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point p3 = findImage.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int centering = (p2.y - p1.y + 1) / 2;
		int centering2 = (p3.y - p2.y + 1) / 2;

		FormData f = UIUtils.createFormData(null, 0, null, 0, 0, 0, 100, 0);
		dummyLabel.setLayoutData(f);

		f = UIUtils.createFormData(dummyLabel, 8 + centering, null, 0, 0, 0, null, 0);
		nameLabel.setLayoutData(f);

		f = UIUtils.createFormData(dummyLabel, 8, null, 0, hardwareLabel, 5, 100, 0);
		nameText.setLayoutData(f);

		f = UIUtils.createFormData(nameText, 8 + centering + centering2, null, 0, 0, 0, null, 0);
		imageLabel.setLayoutData(f);

		int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		Point minSize1 = findImage.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		Point minSize2 = keyManage.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		int buttonWidth = Math.max(widthHint, minSize1.x);
		buttonWidth = Math.max(buttonWidth, minSize2.x);

		f = new FormData();
		f.top = new FormAttachment(nameText, 8);
		f.right = new FormAttachment(realmCombo, 0, SWT.RIGHT);
		f.width = buttonWidth;
		findImage.setLayoutData(f);

		f = UIUtils.createFormData(nameText, 8 + centering2, null, 0, hardwareLabel, 5, findImage, -10);
		imageText.setLayoutData(f);

		f = UIUtils.createFormData(imageLabel, 8 + centering, null, 0, 0, 0, null, 0);
		archLabel.setLayoutData(f);

		f = UIUtils.createFormData(imageLabel, 8 + centering, null, 0, hardwareLabel, 5, 100, 0);
		arch.setLayoutData(f);

		f = UIUtils.createFormData(arch, 8 + centering, null, 0, 0, 0, null, 0);
		realmLabel.setLayoutData(f);

		f = UIUtils.createFormData(arch, 8, null, 0, hardwareLabel, 5, 100, 0);
		realmCombo.setLayoutData(f);

		Control control = realmCombo;

		Label keyLabel = new Label(container, SWT.NULL);
		keyLabel.setText(WizardMessages.getString(KEY_LABEL));

		keyText = new Text(container, SWT.BORDER | SWT.SINGLE);

		f = UIUtils.createFormData(realmCombo, 8 + centering + centering2, null, 0, 0, 0, null, 0);
		keyLabel.setLayoutData(f);

		f = new FormData();
		f.width = buttonWidth;
		f.top = new FormAttachment(realmCombo, 8);
		f.right = new FormAttachment(realmCombo, 0, SWT.RIGHT);
		keyManage.setLayoutData(f);

		f = UIUtils.createFormData(realmCombo, 8 + centering2, null, 0, hardwareLabel, 5, keyManage, -10);
		keyText.setLayoutData(f);

		control = keyText;

		f = UIUtils.createFormData(control, 8 + centering, null, 0, 0, 0, null, 0);
		hardwareLabel.setLayoutData(f);

		f = UIUtils.createFormData(control, 8, null, 0, hardwareLabel, 5, 100, 0);
		hardware.setLayoutData(f);

		f = UIUtils.createFormData(hardware, 10, 100, 0, 0, 0, 100, 0);
		groupContainer.setLayoutData(f);
		return container;
	}

	private void createProfileComposites() {
		for (DeltaCloudHardwareProfile p : allProfiles) {
			ProfileComposite pc = new ProfileComposite(p, groupContainer);
			profilePages.put(p.getId(), pc);
			pc.setVisible(false);
		}
		groupContainer.layout();
	}

	private void launchFetchRealms() {
		Thread t = new Thread() {
			public void run() {
				realms = getRealms();
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						updateRealmCombo();
					}
				});
			}
		};
		t.start();
	}

	private void launchFetchProfiles() {
		Thread t = new Thread() {
			public void run() {
				allProfiles = getProfiles();
				profilePages = new HashMap<String, ProfileComposite>();
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						createProfileComposites();
						clearProfiles();
						if (allProfiles.length > 0)
							hardware.setEnabled(true);
						filterProfiles();
					}
				});
			}
		};
		t.start();
	}

	/**
	 * Displays the arch in the given label if the given binding is valid.
	 * 
	 * @param archLabel
	 *            the arch label
	 * @return the value change listener
	 */
	private class ArchAdapter implements IValueChangeListener {

		private Label archLabel;
		private NewInstanceModel.ImageContainer container;
		private NewInstancePage page;

		public ArchAdapter(Label archLabel,
				NewInstanceModel.ImageContainer container,
				NewInstancePage page) {
			this.archLabel = archLabel;
			this.page = page;
			this.container = container;
		}

		@Override
		public void handleValueChange(ValueChangeEvent event) {
			IStatus status = (IStatus) event.diff.getNewValue();
			if (status.isOK()) {
				archLabel.setText(model.getArch());
				page.setImage(container.getImage());
				page.filterProfiles();
			} else {
				archLabel.setText("");
				page.clearProfiles();
			}
		}
	}

	/**
	 * Binds the architecture label to the given image id text widget. Attaches
	 * a listener to the image id text widget Adds a validity decorator to the
	 * image text widget.
	 * 
	 * @param dbc
	 *            the databinding context to use
	 * @param imageText
	 *            the image id text widget
	 * @param archLabel
	 *            the label to display the image architecture in
	 * @return
	 * @return the binding that was created
	 */
	private Binding bindArchLabel(DataBindingContext dbc, Text imageText, final Label archLabel,
			final NewInstancePage page) {
		UpdateValueStrategy updateStrategy = new UpdateValueStrategy();
		NewInstanceModel.ImageContainer c = new NewInstanceModel.ImageContainer();
		updateStrategy.setConverter(new NewInstanceModel.ArchConverter(cloud, c, String.class, String.class));
		updateStrategy.setBeforeSetValidator(new NewInstanceModel.ArchValidator());

		Binding binding = dbc.bindValue(
				WidgetProperties.text(SWT.Modify).observeDelayed(100, imageText),
				BeanProperties.value(NewInstanceModel.PROPERTY_ARCH).observe(model),
				updateStrategy,
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER));
		binding.getValidationStatus().addValueChangeListener(new ArchAdapter(archLabel, c, page));
		ControlDecorationSupport.create(binding, SWT.LEFT | SWT.TOP);
		return binding;
	}

	/**
	 * Bind the given text widget to the cloud connection model. Attaches
	 * validator to the binding that enforce a non-empty input.
	 * 
	 * @param dbc
	 *            the databinding context to use
	 * @param text
	 *            the name text widget to bind
	 */
	private void bindText(DataBindingContext dbc, final Text text, String property, String errMsgId) {
		Binding nameTextBinding = dbc.bindValue(
				WidgetProperties.text(SWT.Modify).observe(text),
				BeanProperties.value(NewInstanceModel.class, property)
						.observe(model),
				new UpdateValueStrategy().setBeforeSetValidator(new MandatoryStringValidator(WizardMessages
						.getString(errMsgId))),
				null);
		ControlDecorationSupport.create(nameTextBinding, SWT.LEFT | SWT.TOP);
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
			ErrorUtils.handleErrorAsync("Error",
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
	private void createRealmsControl(final Composite parent) {
		Combo combo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
		realmCombo = combo;
		combo.setEnabled(false);
		combo.setItems(new String[] { WizardMessages.getString(LOADING_VALUE) });
		combo.select(0);
	}

	private void updateRealmCombo() {
		List<String> names = getRealmNames(realms != null ? realms : new ArrayList<DeltaCloudRealm>());
		if (names.size() > 0) {
			realmCombo.setItems(names.toArray(new String[names.size()]));
			realmCombo.setEnabled(true);
			realmCombo.select(0);
		} else {
			realmCombo.setItems(new String[] { WizardMessages.getString(NONE_RESPONSE) });
			realmCombo.select(0);
		}
	}
}
