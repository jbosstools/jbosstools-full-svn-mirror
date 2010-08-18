package org.jboss.tools.internal.deltacloud.ui.wizards;

import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudHardwareProfile;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.DeltaCloudRealm;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;

public class NewInstancePage extends WizardPage {

	private final static String NAME = "NewInstance.name"; //$NON-NLS-1$
	private final static String DESCRIPTION = "NewInstance.desc"; //$NON-NLS-1$
	private final static String TITLE = "NewInstance.title"; //$NON-NLS-1$
	
	private static final String NAME_LABEL = "Name.label"; //$NON-NLS-1$
	private static final String IMAGE_LABEL = "Image.label"; //$NON-NLS-1$
	private static final String ARCH_LABEL = "Arch.label"; //$NON-NLS-1$
	private static final String HARDWARE_LABEL = "Profile.label"; //$NON-NLS-1$
	private static final String REALM_LABEL = "Realm.label"; //$NON-NLS-1$
	private static final String PROPERTIES_LABEL = "Properties.label"; //$NON-NLS-1$
	private static final String NONE_RESPONSE = "None.response"; //$NON-NLS-1$
	@SuppressWarnings("unused")
	private static final String NAME_ALREADY_IN_USE = "ErrorNameInUse.text"; //$NON-NLS-1$


	private DeltaCloud cloud;
	private DeltaCloudImage image;
	private ArrayList<DeltaCloudHardwareProfile> profiles;
	
	private Text nameText;
	private Combo hardware;
	private Control realm;
	private String[] profileIds;
	private ProfileComposite currPage;
	private ProfileComposite[] profilePages;
	private ArrayList<String> realmIds;


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
		if (realm instanceof Combo) {
			int index = ((Combo)realm).getSelectionIndex();
			return realmIds.get(index);
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
	
	private void validate() {
		boolean complete = true;
		boolean errorFree = true;
		
		setMessage(null);
		
		String name = nameText.getText();
		if (name.length() == 0) {
			complete = false;
		}
		
		if (errorFree)
			setErrorMessage(null);
		setPageComplete(complete & errorFree);
	}
	
	private void getPossibleProfiles() {
		profiles = new ArrayList<DeltaCloudHardwareProfile>();
		DeltaCloudHardwareProfile[] allProfiles = cloud.getProfiles();
		for (DeltaCloudHardwareProfile p : allProfiles) {
			if (p.getArchitecture() == null || image.getArchitecture().equals(p.getArchitecture())) {
				profiles.add(p);
			}
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

		DeltaCloudRealm[] realms = cloud.getRealms();
		realmIds = new ArrayList<String>();
		ArrayList<String> realmNames = new ArrayList<String>();
		for (int i = 0; i < realms.length; ++i) {
			DeltaCloudRealm r = realms[i];
			if (r.getState() == null || r.getState().equals(DeltaCloudRealm.AVAILABLE)) {
				realmNames.add(r.getId() + "   [" + r.getName() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
				realmIds.add(r.getId());
			}
		}
		if (realmIds.size() > 0) {
			Combo combo = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
			combo.setItems(realmNames.toArray(new String[realmNames.size()]));
			combo.setText(realmNames.get(0));
			realm = combo;
		} else {
			Label label = new Label(container, SWT.NULL);
			label.setText(WizardMessages.getString(NONE_RESPONSE));
			realm = label;
		}
		
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
		
		FormData f = new FormData();
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(100, 0);
		dummyLabel.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(dummyLabel, 8);
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
		f.top = new FormAttachment(archLabel, 11);
		f.left = new FormAttachment(0, 0);
		realmLabel.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(arch, 8);
		f.left = new FormAttachment(hardwareLabel, 5);
		f.right = new FormAttachment(100, 0);
		realm.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(realm, 11);
		f.left = new FormAttachment(0, 0);
		hardwareLabel.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(realm, 8);
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

}
