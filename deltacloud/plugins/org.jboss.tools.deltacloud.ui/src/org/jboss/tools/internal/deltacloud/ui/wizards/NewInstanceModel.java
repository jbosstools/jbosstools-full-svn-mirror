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

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.internal.deltacloud.ui.common.databinding.ObservablePojo;

/**
 * @author Jeff Jonhston
 */
public class NewInstanceModel extends ObservablePojo {

	public static final String PROPERTY_URL = "url"; //$NON-NLS-1$
	public static final String PROPERTY_NAME = "name"; //$NON-NLS-1$
	public static final String PROPERTY_IMAGE_ID = "imageId"; //$NON-NLS-1$
	public static final String PROPERTY_ARCH = "arch"; //$NON-NLS-1$
	public static final String PROPERTY_REALM = "realm"; //$NON-NLS-1$
	public static final String PROPERTY_KEYNAME = "keyname"; //$NON-NLS-1$
	public static final String PROPERTY_PROFILE = "profile"; //$NON-NLS-1$

	private static final String IMAGE_ID_NOT_FOUND = "ErrorImageIdNotFound.text"; //$NON-NLS-1$

	private String name;
	private String imageId;
	private String arch;
	private String realm;
	private String keyname;
	private String profile;

	public static class ImageContainer {
		private DeltaCloudImage image;

		public DeltaCloudImage getImage() {
			return image;
		}

		public void setImage(DeltaCloudImage image) {
			this.image = image;
		}
	}

	public static class ArchConverter extends Converter {

		private DeltaCloud cloud;
		private ImageContainer imageContainer;

		public ArchConverter(DeltaCloud cloud, ImageContainer imageContainer,
				Object fromType, Object toType) {
			super(fromType, toType);
			this.cloud = cloud;
			this.imageContainer = imageContainer;
		}

		@Override
		public Object convert(final Object fromObject) {
			try {
				return getArch((String) fromObject);
			} catch (DeltaCloudException e) {
				return ValidationStatus.error("Could find image on the server", e);
			}
		}

		private String getArch(String imageId) throws DeltaCloudException {
			String arch = "";
			if (imageId != null && imageId.length() > 0) {
				DeltaCloudImage image = getImage(imageId, arch);
				if (image != null) {
					imageContainer.setImage(image);
					arch = image.getArchitecture();
				}
			}
			return arch;
		}

		private DeltaCloudImage getImage(String imageId, String arch) throws DeltaCloudException {
			DeltaCloudImage image = getFromcachedImage(imageId, cloud.getImages());
			if (image == null) {
				image = cloud.loadImage(imageId);
			}
			return image;
		}

		private DeltaCloudImage getFromcachedImage(String id, DeltaCloudImage[] images) {
			for (int i = 0; i < images.length; ++i) {
				DeltaCloudImage image = images[i];
				if (image.getId().equals(id)) {
					return image;
				}
			}
			return null;
		}

	}

	public static class ArchValidator implements IValidator {

		@Override
		public IStatus validate(Object value) {
			if (value instanceof String
					&& ((String) value).length() > 0) {
				return ValidationStatus.ok();
			} else {
				return ValidationStatus.error(WizardMessages.getString(IMAGE_ID_NOT_FOUND));
			}
		}
	}

	public NewInstanceModel(String name, String imageId, String arch, String realm,
			String keyname, String profile) {
		this.name = name;
		this.imageId = imageId;
		this.realm = realm;
		this.keyname = keyname;
		this.profile = profile;
		this.arch = arch;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_NAME, this.name, this.name = name);
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_IMAGE_ID, this.imageId, this.imageId = imageId);
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_REALM, this.realm, this.realm = realm);
	}

	public String getKeyname() {
		return keyname;
	}

	public void setKeyname(String keyname) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_KEYNAME, this.keyname, this.keyname = keyname);
	}

	public String getArch() {
		return arch;
	}

	public void setArch(String arch) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_ARCH, this.arch, this.arch = arch);
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_PROFILE, this.profile, this.profile = profile);
	}
}
