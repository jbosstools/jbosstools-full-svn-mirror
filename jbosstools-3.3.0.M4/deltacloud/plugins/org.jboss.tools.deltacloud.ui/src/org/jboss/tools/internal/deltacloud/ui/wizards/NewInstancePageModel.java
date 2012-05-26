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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.common.ui.databinding.ObservableUIPojo;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudHardwareProfile;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.DeltaCloudRealm;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob.CLOUDELEMENT;

/**
 * @author Jeff Jonhston
 * @author André Dietisheim
 */
public class NewInstancePageModel extends ObservableUIPojo {

	public static final String PROPERTY_URL = "url"; //$NON-NLS-1$
	public static final String PROPERTY_NAME = "name"; //$NON-NLS-1$
	public static final String PROPERTY_IMAGE = "image"; //$NON-NLS-1$
	public static final String PROPERTY_IMAGE_ID = "imageId"; //$NON-NLS-1$
	public static final String PROPERTY_ARCH = "arch"; //$NON-NLS-1$
	public static final String PROPERTY_REALMS = "realms"; //$NON-NLS-1$
	public static final String PROPERTY_SELECTED_REALM_INDEX = "selectedRealmIndex"; //$NON-NLS-1$
	public static final String PROPERTY_KEYID = "keyId"; //$NON-NLS-1$
	public static final String PROP_PROFILE = "profile"; //$NON-NLS-1$
	public static final String PROP_ALL_PROFILES = "allProfiles"; //$NON-NLS-1$
	public static final String PROP_FILTERED_PROFILES = "filteredProfiles"; //$NON-NLS-1$
	public static final String PROP_SELECTED_PROFILE_INDEX = "selectedProfileIndex"; //$NON-NLS-1$

	private String name;
	private DeltaCloudImage image;
	private String arch;
	private String keyId;
	private DeltaCloudRealm selectedRealm;
	private List<DeltaCloudRealm> realms = new ArrayList<DeltaCloudRealm>();
	private DeltaCloudHardwareProfile selectedProfile;
	private List<DeltaCloudHardwareProfile> allProfiles = new ArrayList<DeltaCloudHardwareProfile>();
	private List<DeltaCloudHardwareProfile> filteredProfiles = new ArrayList<DeltaCloudHardwareProfile>();
	private String cpu;
	private String storage;
	private String memory;
	private DeltaCloud cloud;
	private String imageId;

	protected NewInstancePageModel(String keyId, String imageId, DeltaCloud cloud) {
		this.cloud = cloud;
		this.keyId = keyId;
		initImageId(imageId, cloud);
	}

	private void initImageId(String imageId, DeltaCloud cloud) {
		if (imageId == null) {
			imageId = cloud.getLastImageId();
		}
		setImageId(imageId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange(PROPERTY_NAME, this.name, this.name = name);
	}

	public DeltaCloudImage getImage() {
		return image;
	}

	public void setImage(DeltaCloudImage image) {
		firePropertyChange(PROPERTY_IMAGE, this.image, this.image = image);
		List<DeltaCloudHardwareProfile> filteredProfiles = filterProfiles(image, allProfiles);
		setFilteredProfiles(filteredProfiles);
		if (image != null) {
			setArch(image.getArchitecture());
		}
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(final String imageId) {
		firePropertyChange(PROPERTY_IMAGE_ID, this.imageId, this.imageId = imageId);
		if (imageId == null || imageId.length() == 0) {
			return;
		}
		
		new AbstractCloudElementJob(
				MessageFormat.format("Getting image {0} from cloud {1}", imageId, cloud.getName()),
				cloud, CLOUDELEMENT.IMAGES) {

			@Override
			protected IStatus doRun(IProgressMonitor monitor) throws Exception {
				try {
					final DeltaCloudImage image = cloud.getImage(imageId);
					Display.getDefault().syncExec(new Runnable() {
						
						@Override
						public void run() {
							setImage(image);
						}
					});
				} catch (Exception e) {
					setImage(null);
					// do not re-throw exception. DC server 0.1.2 return internal
					// error 500 when requesting unknown image
				}
				return Status.OK_STATUS;
			}

		}.schedule();
	}

	public int getSelectedRealmIndex() {
		return realms.indexOf(selectedRealm);
	}

	public void setSelectedRealmIndex(int index) {
		if (realms.size() > index) {
			int oldIndex = -1;
			if (selectedRealm != null
					&& realms.size() > 0) {
				oldIndex = realms.indexOf(selectedRealm);
			}
			DeltaCloudRealm deltaCloudRealm = realms.get(index);
			selectedRealm = deltaCloudRealm;
			cloud.setLastRealmName(selectedRealm.getName());
			firePropertyChange(PROPERTY_SELECTED_REALM_INDEX, oldIndex, index);
		}
	}

	private void setSelectedRealm(String name) {
		if (realms.size() > 0) {
			int index = getRealmIndex(name);
			if (index >= 0) {
				setSelectedRealmIndex(index);
			}
		}
	}

	private int getRealmIndex(String name) {
		int index = 0;
		if (name != null
				&& realms != null
				&& realms.size() > 0) {
			for (int i = 0; i < realms.size(); i++) {
				if (name.equals(realms.get(i).getName())) {
					index = i;
					break;
				}
			}
		}
		return index;
	}

	public String getRealmId() {
		if (selectedRealm == null) {
			return null;
		}
		return selectedRealm.getId();
	}

	protected void setRealms(List<DeltaCloudRealm> realms) {
		firePropertyChange(PROPERTY_REALMS, this.realms, this.realms = realms);
		setSelectedRealm(cloud.getLastRealmName());
	}

	public List<DeltaCloudRealm> getRealms() {
		return realms;
	}

	public void setAllProfiles(List<DeltaCloudHardwareProfile> profiles) {
		firePropertyChange(PROP_ALL_PROFILES, this.allProfiles, this.allProfiles = profiles);
		setFilteredProfiles(filterProfiles(image, profiles));
	}

	public List<DeltaCloudHardwareProfile> getAllProfiles() {
		return allProfiles;
	}

	private void setFilteredProfiles(List<DeltaCloudHardwareProfile> profiles) {
		firePropertyChange(PROP_FILTERED_PROFILES, this.filteredProfiles, this.filteredProfiles = profiles);
		setSelectedProfileIndex();
	}

	public List<DeltaCloudHardwareProfile> getFilteredProfiles() {
		return filteredProfiles;
	}

	private List<DeltaCloudHardwareProfile> filterProfiles(DeltaCloudImage image,
			Collection<DeltaCloudHardwareProfile> profiles) {
		List<DeltaCloudHardwareProfile> filteredProfiles = new ArrayList<DeltaCloudHardwareProfile>();
		for (DeltaCloudHardwareProfile p : profiles) {
			if (p.getArchitecture() == null
					|| image == null
					|| image.getArchitecture().equals(p.getArchitecture())) {
				filteredProfiles.add(p);
			}
		}

		return filteredProfiles;
	}

	private void setSelectedProfileIndex() {
		setSelectedProfileIndex(getProfileIndex(cloud.getLastProfileId()));
	}

	private int getProfileIndex(String selectedProfileId) {
		int index = 0;
		if (selectedProfileId != null
				&& filteredProfiles != null && filteredProfiles.size() > 0) {
			for (int i = 0; i < filteredProfiles.size(); i++) {
				if (selectedProfileId.equals(filteredProfiles.get(i).getId())) {
					index = i;
					break;
				}
			}
		}
		return index;
	}

	public void setSelectedProfileIndex(int index) {
		if (filteredProfiles.size() > index) {
			int oldIndex = -1;
			if (selectedProfile != null
					&& filteredProfiles.size() > 0) {
				oldIndex = filteredProfiles.indexOf(selectedProfile);
			}
			DeltaCloudHardwareProfile hardwareProfile = filteredProfiles.get(index);
			selectedProfile = hardwareProfile;
			firePropertyChange(PROP_SELECTED_PROFILE_INDEX, oldIndex, index);
			cloud.setLastProfileId(hardwareProfile.getId());
		}
	}

	public int getSelectedProfileIndex() {
		if (filteredProfiles == null ||
				filteredProfiles.size() <= 0) {
			return -1;
		}
		return filteredProfiles.indexOf(selectedProfile);
	}

	public String getProfileId() {
		if (selectedProfile == null) {
			return null;
		}
		return selectedProfile.getId();
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_KEYID, this.keyId, this.keyId = keyId);
	}

	public String getArch() {
		return arch;
	}

	public void setArch(String arch) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_ARCH, this.arch, this.arch = arch);
	}

	public int getSelectedProfile() {
		return allProfiles.indexOf(selectedProfile);
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getCpu() {
		return this.cpu;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getMemory() {
		return this.memory;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getStorage() {
		return this.storage;
	}
}
