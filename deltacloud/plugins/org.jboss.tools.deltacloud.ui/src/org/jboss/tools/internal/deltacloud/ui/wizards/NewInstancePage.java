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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudHardwareProfile;
import org.jboss.tools.deltacloud.core.DeltaCloudHardwareProperty;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;

public class NewInstancePage extends WizardPage {

	private final static String NAME = "NewInstance.name"; //$NON-NLS-1$
	private final static String DESCRIPTION = "NewInstance.desc"; //$NON-NLS-1$
	private final static String TITLE = "NewInstance.title"; //$NON-NLS-1$
	
	private static final String NAME_LABEL = "Name.label"; //$NON-NLS-1$
	private static final String IMAGE_LABEL = "Image.label"; //$NON-NLS-1$
	private static final String ARCH_LABEL = "Arch.label"; //$NON-NLS-1$
	private static final String HARDWARE_LABEL = "Profiles.label"; //$NON-NLS-1$
	private static final String NAME_ALREADY_IN_USE = "ErrorNameInUse.text"; //$NON-NLS-1$


	private DeltaCloud cloud;
	private DeltaCloudImage image;
	private ArrayList<DeltaCloudHardwareProfile> profiles;
	
	private Text nameText;
	private Combo hardware;
	private String[] profileIds;
	private ProfileComposite currPage;
	private ProfileComposite[] profilePages;


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
			profilePages[index].setVisible(true);
		}
	};
	
	public NewInstancePage(DeltaCloud cloud, DeltaCloudImage image) {
		super(WizardMessages.getString(NAME));
		this.cloud = cloud;
		this.image = image;
		getPossibleProfiles();
		setDescription(WizardMessages.getString(DESCRIPTION));
		setTitle(WizardMessages.getString(TITLE));
		setPageComplete(false);
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
			if (image.getArchitecture().equals(p.getArchitecture())) {
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
		}
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
		
		nameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		nameText.addModifyListener(textListener);

		Label hardwareLabel = new Label(container, SWT.NULL);
		hardwareLabel.setText(WizardMessages.getString(HARDWARE_LABEL));
		
		hardware = new Combo(container, SWT.READ_ONLY);
		Composite groupContainer = new Composite(container, SWT.NULL);
		FormLayout groupLayout = new FormLayout();
		groupLayout.marginHeight = 0;
		groupLayout.marginWidth = 0;
		groupContainer.setLayout(groupLayout);		
		
		profileIds = getProfileIds(groupContainer);
		
		if (profileIds.length > 0) {
			hardware.setItems(profileIds);
			hardware.setText(profileIds[0]);
//			hardware.addModifyListener(comboListener);
		}
		
		FormData f = new FormData();
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(100, 0);
		dummyLabel.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(dummyLabel, 8);
		f.left = new FormAttachment(0, 0);
		imageLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(dummyLabel, 8);
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
		f.top = new FormAttachment(archLabel, 8);
		f.left = new FormAttachment(0, 0);
		nameLabel.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(arch, 5);
		f.left = new FormAttachment(hardwareLabel, 5);
		f.right = new FormAttachment(100, 0);
		nameText.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(nameText, 8);
		f.left = new FormAttachment(0, 0);
		hardwareLabel.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(nameText, 5);
		f.left = new FormAttachment(hardwareLabel, 5);
		f.right = new FormAttachment(100, 0);
		hardware.setLayoutData(f);
		
		setControl(container);
	}

}
