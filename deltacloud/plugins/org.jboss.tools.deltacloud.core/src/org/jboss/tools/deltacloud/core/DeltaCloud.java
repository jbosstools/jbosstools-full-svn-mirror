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
package org.jboss.tools.deltacloud.core;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.tools.deltacloud.client.DeltaCloudAuthClientException;
import org.jboss.tools.deltacloud.client.DeltaCloudClient;
import org.jboss.tools.deltacloud.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.client.DeltaCloudClientImpl;
import org.jboss.tools.deltacloud.client.DeltaCloudNotFoundClientException;
import org.jboss.tools.deltacloud.client.HardwareProfile;
import org.jboss.tools.deltacloud.client.Image;
import org.jboss.tools.deltacloud.client.Instance;
import org.jboss.tools.deltacloud.client.Key;
import org.jboss.tools.deltacloud.client.Realm;
import org.jboss.tools.deltacloud.client.API.Driver;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance.State;
import org.jboss.tools.internal.deltacloud.core.observable.ObservablePojo;

/**
 * @author Jeff Jonston
 * @author André Dietisheim
 */
public class DeltaCloud extends ObservablePojo {

	private static final int WAIT_FOR_STATE_DELAY = 4000;

	public static final String PROP_INSTANCES = "instances";
	public static final String PROP_INSTANCES_REMOVED = "instancesRemoved";
	public static final String PROP_IMAGES = "images";
	public static final String PROP_NAME = "name";
	public static final String PROP_URL = "url";
	public static final String PROP_USERNAME = "username";

	private String name;
	private String username;
	private String url;
	private DeltaCloudDriver driver;
	private String lastKeyname = "";
	private String lastImageId = "";
	private String lastRealmName = "";
	private String lastProfileId = "";

	private DeltaCloudClient client;

	private DeltaCloudImagesRepository imagesRepo = new DeltaCloudImagesRepository();
	private boolean areImagesLoaded = false;
	private DeltaCloudInstancesRepository instancesRepo = new DeltaCloudInstancesRepository();
	private boolean areInstancesLoaded = false;

	private IImageFilter imageFilter;
	private IInstanceFilter instanceFilter;

	private SecurePasswordStore passwordStore;
	private Collection<IInstanceAliasMapping> instanceAliasMappings;

	public DeltaCloud(String name, String url, String username, String passwd) throws DeltaCloudException {
		this(name, url, username, passwd, null);
	}

	public DeltaCloud(String name, String url, String username, String password, DeltaCloudDriver driver)
			throws DeltaCloudException {
		this(name, url, username, password, driver, null, null, new ArrayList<IInstanceAliasMapping>());
	}

	public DeltaCloud(String name, String url, String username, DeltaCloudDriver driver, String imageFilterRules,
			String instanceFilterRules, Collection<IInstanceAliasMapping> instanceAliasMappings)
			throws DeltaCloudException {
		this(name, url, username, null, driver, imageFilterRules, instanceFilterRules, instanceAliasMappings);
	}

	public DeltaCloud(String name, String url, String username, String password, DeltaCloudDriver driver,
			String imageFilterRules, String instanceFilterRules, Collection<IInstanceAliasMapping> instanceAliasMappings)
			throws DeltaCloudException {
		this.url = url;
		this.name = name;
		this.username = username;
		this.driver = driver;
		this.passwordStore = createSecurePasswordStore(name, username, password);
		this.client = createClient(url, username, passwordStore.getPassword());
		this.imageFilter = new ImageFilter(imageFilterRules, this);
		this.instanceFilter = new InstanceFilter(instanceFilterRules, this);
		this.instanceAliasMappings = instanceAliasMappings;
	}

	public void update(String name, String url, String username, String password, DeltaCloudDriver driver)
			throws DeltaCloudException {
		this.driver = driver;

		boolean nameChanged = updateName(name);
		boolean connectionPropertiesChanged = updateConnectionProperties(url, username, password);

		if (nameChanged || connectionPropertiesChanged) {
			this.passwordStore.update(new DeltaCloudPasswordStorageKey(name, username), password);
			// TODO: move to notification based approach
			DeltaCloudManager.getDefault().saveClouds();
		}

		if (connectionPropertiesChanged) {
			client = createClient(url, username, password);
			loadChildren();
		}
	}

	private boolean updateName(String name) {
		if (equals(this.name, name)) {
			return false;
		}

		setName(name);
		return true;
	}

	private boolean updateConnectionProperties(String url, String username, String password) throws DeltaCloudException {
		boolean changed = false;
		if (!equals(this.url, url)) {
			setUrl(url);
			changed = true;
		}
		if (!equals(this.username, username)) {
			setUsername(username);
			changed = true;
		}
		if (!equals(this.passwordStore.getPassword(), password)) {
			changed = true;
		}

		return changed;
	}

	private boolean equals(Object thisObject, Object thatObject) {
		return (thisObject != null && thisObject.equals(thatObject))
				|| (thatObject != null && thatObject.equals(thisObject))
				|| (thisObject == null && thatObject == null);
	}

	protected SecurePasswordStore createSecurePasswordStore(String name2, String username2, String password) {
		return new SecurePasswordStore(new DeltaCloudPasswordStorageKey(name, username), password);
	}

	protected DeltaCloudClient createClient(String url, String username, String password)
			throws DeltaCloudException {
		try {
			return new DeltaCloudClientImpl(url, username, password);
		} catch (Exception e) {
			throw new DeltaCloudException(MessageFormat.format("Could not access cloud at {0}", url), e);
		}
	}

	public String getName() {
		return name;
	}

	private void setUrl(String url) {
		firePropertyChange(PROP_URL, this.url, this.url = url);
	}

	public String getURL() {
		return url;
	}

	private void setUsername(String username) {
		firePropertyChange(PROP_USERNAME, this.username, this.username = username);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() throws DeltaCloudException {
		return passwordStore.getPassword();
	}

	public DeltaCloudDriver getDriver() {
		return driver;
	}

	private void updateDriver() {
		try {
			DeltaCloudDriver driver = getServerDriver(url);
			this.driver = driver;
		} catch (DeltaCloudException e) {
			// ignore
		}
	}

	/**
	 * Returns if this cloud points to a known cloud type. The implementation
	 * checks the driver type which will be valid if the url is a valid and
	 * known cloud. The credentials are not checked.
	 * 
	 * @return <code>true</code> if this cloud is a known type
	 */
	public boolean isValid() {
		boolean isValid = isKnownDriver();
		if (!isValid) {
			updateDriver();
			isValid = isKnownDriver();
		}
		return isValid;
	}

	protected boolean isKnownDriver() {
		return driver != null
				&& driver != DeltaCloudDriver.UNKNOWN;
	}

	public String getLastImageId() {
		return lastImageId;
	}

	public DeltaCloudImage getLastImage() throws DeltaCloudException {
		return getImage(lastImageId);
	}

	public void setLastImageId(String lastImageId) {
		this.lastImageId = lastImageId;
	}

	public void setLastRealmName(String lastRealmName) {
		this.lastRealmName = lastRealmName;
	}

	public String getLastRealmName() {
		return lastRealmName;
	}

	public void setLastProfileId(String lastProfileId) {
		this.lastProfileId = lastProfileId;
	}

	public String getLastProfileId() {
		return lastProfileId;
	}

	private void setName(String name) {
		firePropertyChange(PROP_NAME, this.name, this.name = name);
	}

	public String getLastKeyname() {
		return lastKeyname;
	}

	public void setLastKeyname(String lastKeyname) {
		this.lastKeyname = lastKeyname;
	}

	public IInstanceFilter getInstanceFilter() {
		return instanceFilter;
	}

	public void updateInstanceFilter(String nameRule, String idRule, String aliasRule, String imageIdRule,
			String ownerIdRule, String keyNameRule, String realmRule, String profileRule) throws Exception {
		instanceFilter =
				new InstanceFilter(nameRule, idRule, aliasRule, imageIdRule, ownerIdRule, keyNameRule, realmRule,
						profileRule, this);
		firePropertyChange(
					PROP_INSTANCES, instancesRepo.get(), instancesRepo.get());
		DeltaCloudManager.getDefault().saveClouds();
	}

	public IImageFilter getImageFilter() {
		return imageFilter;
	}

	public void updateImageFilter(String nameRule, String idRule, String archRule, String descRule) throws Exception {
		this.imageFilter = new ImageFilter(nameRule, idRule, archRule, descRule, this);
		// TODO: remove notification with all instanceRepo, replace by
		// notifying the changed instance
		firePropertyChange(PROP_IMAGES, imagesRepo.get(), imagesRepo.get());
		// TODO: move to notification based approach
		DeltaCloudManager.getDefault().saveClouds();
	}

	/**
	 * Loads all children of this delta cloud instance (regardless if things
	 * have already been loaded before). Catched and collects individual errors
	 * that may occur and throws a multi exception.
	 * 
	 * @throws DeltaCloudException
	 */
	public void loadChildren() throws DeltaCloudException {
		DeltaCloudMultiException multiException = new DeltaCloudMultiException(
				MessageFormat.format("Could not load children of cloud {0}", getName()));
		clearImages();
		clearInstances();
		try {
			loadImages();
		} catch (DeltaCloudException e) {
			multiException.addError(e);
		}
		try {
			loadInstances();
		} catch (DeltaCloudException e) {
			multiException.addError(e);
		}

		if (!multiException.isEmpty()) {
			throw multiException;
		}
	}

	public DeltaCloudInstance waitForState(String instanceId, DeltaCloudInstance.State expectedState,
			IProgressMonitor pm)
			throws InterruptedException, DeltaCloudException {
		DeltaCloudInstance instance = instancesRepo.getById(instanceId);
		if (instance != null) {
			while (!pm.isCanceled()) {
				State state = instance.getState();
				if (state == expectedState
						|| instance.isInState(DeltaCloudInstance.State.TERMINATED)) {
					return instance;
				}
				Thread.sleep(WAIT_FOR_STATE_DELAY);
				instance = refreshInstance(instance);
			}
		}
		return instance;
	}

	/**
	 * Loads the instanceRepo from the server and stores them in this instance.
	 * Furthermore listeners get informed.
	 * 
	 * @return the instanceRepo
	 * @throws DeltaCloudException
	 * 
	 * @see #notifyInstanceListListeners(DeltaCloudInstance[])
	 */
	public void loadInstances() throws DeltaCloudException {
		try {
			clearInstances();
			DeltaCloudInstance[] oldInstances = instancesRepo.get();
			List<Instance> instances = client.listInstances();
			Collection<DeltaCloudInstance> deltaCloudInstances =
					DeltaCloudInstanceFactory.create(instances, this, instanceAliasMappings);
			instancesRepo.add(deltaCloudInstances);
			areInstancesLoaded = true;
			// TODO: remove notification with all instanceRepo, replace by
			// notifying the changed instance
			firePropertyChange(PROP_INSTANCES, oldInstances, instancesRepo.get());
		} catch (DeltaCloudClientException e) {
			throw new DeltaCloudException(MessageFormat.format(
					"Could not load instances of cloud {0}: {1}", getName(), e.getMessage()), e);
		}
	}

	private void clearImages() {
		areImagesLoaded = false;
		if (imagesRepo != null) {
			// TODO: remove notification with all instanceRepo, replace by
			// notifying the changed instance
			firePropertyChange(PROP_IMAGES, imagesRepo.get(), imagesRepo.clear());
		}
	}

	public boolean imagesLoaded() {
		return imagesRepo == null ? false : true;
	}

	public boolean instancesLoaded() {
		return areInstancesLoaded;
	}

	private void clearInstances() {
		areInstancesLoaded = false;
		// TODO: remove notification with all instanceRepo, replace by
		// notifying the changed instance
		firePropertyChange(PROP_INSTANCES, instancesRepo.get(), instancesRepo.clear());
	}

	/**
	 * Gets the instanceRepo in async manner. The method does not return the
	 * instanceRepo but notifies observers of the instanceRepo.
	 * 
	 * @throws DeltaCloudException
	 */
	public DeltaCloudInstance[] getInstances() throws DeltaCloudException {
		if (!areInstancesLoaded) {
			loadInstances();
		}
		return instancesRepo.get();
	}

	public DeltaCloudImage[] getImages() throws DeltaCloudException {
		if (!areImagesLoaded) {
			loadImages();
		}
		return imagesRepo.get();
	}

	/**
	 * Gets an image for the given image id. In a first step, the local cache is
	 * queried and if no image is found, the server is queried.
	 * 
	 * @param id
	 *            the image id to match
	 * @return the image that has the given id
	 * @throws DeltaCloudException
	 */
	public DeltaCloudImage getImage(String id) throws DeltaCloudException {
		getImages(); // ensure images are loaded
		DeltaCloudImage deltaCloudImage = imagesRepo.getById(id);
		if (deltaCloudImage == null) {
			try {
				Image image = client.listImages(id);
				deltaCloudImage = DeltaCloudImageFactory.create(image, this);
				imagesRepo.add(deltaCloudImage);
			} catch (DeltaCloudClientException e) {
				throw new DeltaCloudException(MessageFormat.format("Cloud not find image with id \"{0}\"", id), e);
			}
		}

		return deltaCloudImage;
	}

	public DeltaCloudKey[] getKeys() throws DeltaCloudException {
		List<DeltaCloudKey> keys = new ArrayList<DeltaCloudKey>();
		try {
			for (Key key : client.listKeys()) {
				DeltaCloudKey deltaCloudKey = new DeltaCloudKey(key, this);
				keys.add(deltaCloudKey);
			}
			return keys.toArray(new DeltaCloudKey[] {});
		} catch (DeltaCloudClientException e) {
			// TODO: internationalize strings
			throw new DeltaCloudException(MessageFormat.format("Cloud not get keys from cloud \"{0}\"", getName()), e);
		}
	}

	public DeltaCloudKey getKey(String keyId) throws DeltaCloudException {
		try {
			Key key = client.listKey(keyId);
			return new DeltaCloudKey(key, this);
		} catch (DeltaCloudClientException e) {
			// TODO: internationalize strings
			throw new DeltaCloudException(MessageFormat.format("Could not get key \"{0}\" from cloud \"{1}\"", keyId,
					getName()), e);
		}
	}

	public DeltaCloudKey createKey(String id) throws DeltaCloudException {
		try {
			Key key = client.createKey(id);
			return new DeltaCloudKey(key, this);
		} catch (DeltaCloudClientException e) {
			// TODO: internationalize strings
			throw new DeltaCloudException(
					MessageFormat.format("Could not create key \"{0}\" on cloud \"{1}\"", id, getName()), e);
		}
	}

	public boolean delete(DeltaCloudKey deltaCloudKey) throws DeltaCloudException {
		Key key = deltaCloudKey.getKey();
		try {
			return key.destroy(client);
		} catch (DeltaCloudClientException e) {
			throw new DeltaCloudException(MessageFormat.format("Could not delete key \"{0}\"", key.getId()), e);
		}
	}

	private DeltaCloudInstance refreshInstance(DeltaCloudInstance deltaCloudInstance) throws DeltaCloudException {
		try {
			DeltaCloudInstance[] instances = instancesRepo.get();
			Instance newInstance = client.listInstances(deltaCloudInstance.getId());
			deltaCloudInstance.setInstance(newInstance);
			firePropertyChange(PROP_INSTANCES, instances, instancesRepo.get());
			return deltaCloudInstance;
		} catch (DeltaCloudClientException e) {
			// TODO : internationalize strings
			throw new DeltaCloudException(MessageFormat.format("Could not refresh instance \"{0}\"",
					deltaCloudInstance.getId()), e);
		}
	}

	public boolean performInstanceAction(String instanceId, DeltaCloudResourceAction action)
			throws DeltaCloudException {
		return performAction(instancesRepo.getById(instanceId), action);
	}

	protected boolean performAction(DeltaCloudInstance instance, DeltaCloudResourceAction action)
			throws DeltaCloudException {
		try {
			if (instance == null) {
				return false;
			}
			DeltaCloudInstancesRepository repo = instancesRepo;
			DeltaCloudInstance[] instances = repo.get();
			boolean result = instance.performAction(action, client);
			if (result) {
				if (DeltaCloudResourceAction.DESTROY.equals(action)) {
					repo.remove(instance);
					firePropertyChange(PROP_INSTANCES_REMOVED, null, instance);
				}
				// TODO: remove notification with all instanceRepo, replace by
				// notifying the changed instance
				firePropertyChange(PROP_INSTANCES, instances, repo.get());
				// int index = repo.indexOf(instance);
				// fireIndexedPropertyChange(PROP_INSTANCES, index, instance,
				// instance);
			}
			return result;
		} catch (DeltaCloudClientException e) {
			throw new DeltaCloudException(e);
		}
	}

	public DeltaCloudHardwareProfile[] getProfiles() throws DeltaCloudException {
		ArrayList<DeltaCloudHardwareProfile> profiles = new ArrayList<DeltaCloudHardwareProfile>();
		try {
			List<HardwareProfile> list = client.listProfiles();
			for (Iterator<HardwareProfile> i = list.iterator(); i.hasNext();) {
				DeltaCloudHardwareProfile profile = new DeltaCloudHardwareProfile(i.next());
				profiles.add(profile);
			}
			return profiles.toArray(new DeltaCloudHardwareProfile[profiles.size()]);
		} catch (DeltaCloudClientException e) {
			throw new DeltaCloudException(MessageFormat.format("Could not list profiles on cloud {0}", name), e);
		}
	}

	/**
	 * Loads the available images from the server and stores them locally.
	 * Furthermore listeners get informed.
	 * 
	 * @return the delta cloud image[]
	 * @throws DeltaCloudClientException
	 * 
	 * @see #notifyImageListListeners(DeltaCloudImage[])
	 */
	public void loadImages() throws DeltaCloudException {
		try {
			clearImages();
			DeltaCloudImage[] oldImages = imagesRepo.get();
			Collection<DeltaCloudImage> deltaCloudImages = DeltaCloudImageFactory.create(client.listImages(), this);
			imagesRepo.add(deltaCloudImages);
			areImagesLoaded = true;
			// TODO: remove notification with all instanceRepo, replace by
			// notifying the changed instance
			firePropertyChange(PROP_IMAGES, oldImages, imagesRepo.get());
		} catch (DeltaCloudClientException e) {
			clearImages();
			throw new DeltaCloudException(
					MessageFormat.format("Could not load images of cloud {0}: {1}", getName(), e.getMessage()), e);
		}

	}

	public DeltaCloudRealm[] getRealms() throws DeltaCloudException {
		ArrayList<DeltaCloudRealm> realms = new ArrayList<DeltaCloudRealm>();
		try {
			List<Realm> list = client.listRealms();
			for (Iterator<Realm> i = list.iterator(); i.hasNext();) {
				DeltaCloudRealm realm = new DeltaCloudRealm(i.next());
				realms.add(realm);
			}
			return realms.toArray(new DeltaCloudRealm[realms.size()]);
		} catch (DeltaCloudClientException e) {
			throw new DeltaCloudException(MessageFormat.format("Could not get realms for cloud {0}", name), e);
		}
	}

	public DeltaCloudInstance createInstance(String name, String imageId, String realmId, String profileId,
			String keyId, String memory, String storage) throws DeltaCloudException {
		try {
			Instance instance = null;
			if (keyId != null) {
				instance = client.createInstance(name, imageId, profileId, realmId, keyId, memory, storage);
			} else {
				instance = client.createInstance(name, imageId, profileId, realmId, memory, storage);
			}
			if (instance != null) {
				DeltaCloudInstance[] instances = instancesRepo.get();
				DeltaCloudInstance deltaCloudInstance = DeltaCloudInstanceFactory.create(instance, this, name);
				instanceAliasMappings.add(new InstanceAliasMapping(instance.getId(), name));
				instancesRepo.add(deltaCloudInstance);
				// TODO: remove notification with all instanceRepo, replace by
				// notifying the changed instance
				firePropertyChange(PROP_INSTANCES, instances, instancesRepo.get());
				// TODO: move to notification based approach
				DeltaCloudManager.getDefault().saveClouds();
				return deltaCloudInstance;
			}
		} catch (DeltaCloudClientException e) {
			throw new DeltaCloudException(e);
		}
		return null;
	}

	public void dispose() throws DeltaCloudException {
		passwordStore.remove();
	}

	public String toString() {
		return name;
	}

	public static DeltaCloudDriver getServerDriver(String url) throws DeltaCloudException {
		try {
			Driver driver = new DeltaCloudClientImpl(url).getServerType();
			return DeltaCloudDriver.valueOf(driver);
		} catch (Exception e) {
			// TODO internationalize strings
			throw new DeltaCloudException(
					"Could not determine the driver of the server on url " + url, e);
		}
	}

	/**
	 * Tests the credentials defined in this DeltaCloud instance by connecting
	 * to the server defined by the url in this instance. Returns
	 * <code>true</code> if the credentials are valid, <code>false</code>
	 * otherwise.
	 * 
	 * @return <code>true</code>, if successful
	 * @throws DeltaCloudClientException
	 *             if any other error occurs while trying to connect to the
	 *             server
	 */
	public boolean testCredentials() throws DeltaCloudException {
		String instanceId = "nonexistingInstance"; //$NON-NLS-1$
		try {
			client.listInstances(instanceId);
			return true;
		} catch (DeltaCloudNotFoundClientException e) {
			return true;
		} catch (DeltaCloudAuthClientException e) {
			return false;
		} catch (DeltaCloudClientException e) {
			throw new DeltaCloudException(MessageFormat.format("Could not connect to cloud \"{0}\" at \"{1}\"",
					name, url), e);
		}

	}
}
