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

import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.security.storage.EncodingUtils;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.jboss.tools.deltacloud.core.client.DeltaCloudAuthException;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientImpl;
import org.jboss.tools.deltacloud.core.client.DeltaCloudNotFoundClientException;
import org.jboss.tools.deltacloud.core.client.HardwareProfile;
import org.jboss.tools.deltacloud.core.client.Image;
import org.jboss.tools.deltacloud.core.client.Instance;
import org.jboss.tools.deltacloud.core.client.InternalDeltaCloudClient;
import org.jboss.tools.deltacloud.core.client.Realm;

public class DeltaCloud {

	public final static String MOCK_TYPE = "MOCK"; //$NON-NLS-1$
	public final static String EC2_TYPE = "EC2"; //$NON-NLS-1$

	private String name;
	private String username;
	private String password;
	private String url;
	private String type;
	private String lastKeyname = "";
	private String lastImageId = "";
	private DeltaCloudClientImpl client;
	private ArrayList<DeltaCloudInstance> instances;
	private ArrayList<DeltaCloudImage> images;
	private IImageFilter imageFilter;
	private IInstanceFilter instanceFilter;
	private Map<String, Job> actionJobs;
	private Object imageLock = new Object();
	private Object instanceLock = new Object();
	private Object actionLock = new Object();

	ListenerList instanceListeners = new ListenerList();
	ListenerList imageListeners = new ListenerList();

	public static interface IInstanceStateMatcher {
		public boolean matchesState(DeltaCloudInstance instance, String instanceState);
	}

	public DeltaCloud(String name, String url, String username, String passwd) {
		this(name, url, username, passwd, null);
	}

	public DeltaCloud(String name, String url, String username, String password, String type) {
		this(name, url, username, password, type, IImageFilter.ALL_STRING, IInstanceFilter.ALL_STRING);
	}

	public DeltaCloud(String name, String url, String username, String type, String imageFilterRules,
			String instanceFilterRules) {
		this(name, url, username, null, type, imageFilterRules, instanceFilterRules);
	}

	public DeltaCloud(String name, String url, String username, String passwd,
			String type, String imageFilterRules, String instanceFilterRules) {
		this.url = url;
		this.name = name;
		this.username = username;
		this.type = type;
		imageFilter = createImageFilter(imageFilterRules);
		instanceFilter = createInstanceFilter(instanceFilterRules);
	}

	public void editCloud(String name, String url, String username, String password, String type)
			throws DeltaCloudException {
		this.url = url;
		this.name = name;
		this.username = username;
		this.password = password;
		this.type = type;
		storePassword(url, username, password);
		// save();
		loadChildren();
	}

	private InternalDeltaCloudClient getClient() throws DeltaCloudException {
		try {
			if (client == null) {
				this.client = new DeltaCloudClientImpl(url, username, getPassword(url, username));
			}
			return client;
		} catch (MalformedURLException e) {
			throw new DeltaCloudException(MessageFormat.format("Could not access cloud at {0}", url), e);
		} catch (StorageException e) {
			throw new DeltaCloudException(MessageFormat.format(
					"Could not get password for user {0} on cloud at {1} in the preferences", username, url), e);
		}
	}

	private String getPassword(String url, String username) throws StorageException {
		if (password != null) {
			return password;
		} else {
			this.password = getPasswordFromPreferences(url, username);
			return this.password;
		}
	}

	private String getPasswordFromPreferences(String url, String username) throws StorageException {
		String key = DeltaCloud.getPreferencesKey(url, username); // $NON-NLS-1$
		ISecurePreferences root = SecurePreferencesFactory.getDefault();
		ISecurePreferences node = root.node(key);
		String password = node.get("password", null); //$NON-NLS-1$
		return password;
	}

	private void storePassword(String url, String username, String passwd) throws DeltaCloudException {
		if (passwd != null) {
			ISecurePreferences root = SecurePreferencesFactory.getDefault();
			String key = DeltaCloud.getPreferencesKey(url, username);
			ISecurePreferences node = root.node(key);
			try {
				node.put("password", passwd, true /* encrypt */); //$NON-NLS-1$
			} catch (StorageException e) {
				// TODO: internationalize string
				throw new DeltaCloudException("Could not store password", e);
			}
		}
	}

	public static String getPreferencesKey(String url, String username) {
		String key = "/org/jboss/tools/deltacloud/core/"; //$NON-NLS-1$
		key += url + "/" + username; //$NON-NLS-1$
		return EncodingUtils.encodeSlashes(key);
	}

	public String getName() {
		return name;
	}

	public String getURL() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getType() {
		return type;
	}

	public String getLastImageId() {
		return lastImageId;
	}

	public void setLastImageId(String lastImageId) {
		this.lastImageId = lastImageId;
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

	public void updateInstanceFilter(String ruleString) throws Exception {
		String rules = getInstanceFilter().toString();
		instanceFilter = createInstanceFilter(ruleString);
		if (!rules.equals(ruleString)) {
			// save();
			// TODO: remove notification with all instances, replace by
			// notifying the changed instance
			notifyInstanceListListeners(instances.toArray(instances.toArray(new DeltaCloudInstance[instances.size()])));
		}
	}

	private IInstanceFilter createInstanceFilter(String ruleString) {
		IInstanceFilter instanceFilter = null;
		if (IInstanceFilter.ALL_STRING.equals(ruleString)) {
			instanceFilter = new AllInstanceFilter();
		} else {
			try {
				instanceFilter = new InstanceFilter();
				instanceFilter.setRules(ruleString);
			} catch (PatternSyntaxException e) {
				instanceFilter.setRules(IInstanceFilter.ALL_STRING);
			}
		}
		return instanceFilter;
	}

	public IImageFilter getImageFilter() {
		return imageFilter;
	}

	public void updateImageFilter(String ruleString) throws Exception {
		String rules = getImageFilter().toString();
		this.imageFilter = createImageFilter(ruleString);
		if (!rules.equals(ruleString)) {
			// save();
			notifyImageListListeners(getCurrImages());
		}
	}

	private IImageFilter createImageFilter(String ruleString) {
		IImageFilter imageFilter = null;
		if (IImageFilter.ALL_STRING.equals(ruleString)) {
			imageFilter = new AllImageFilter();
		} else {
			try {
				imageFilter = new ImageFilter();
				imageFilter.setRules(ruleString);
			} catch (PatternSyntaxException e) {
				imageFilter.setRules(IImageFilter.ALL_STRING);
			}
		}
		return imageFilter;
	}

	public void loadChildren() throws DeltaCloudException {
		DeltaCloudMultiException multiException = new DeltaCloudMultiException(MessageFormat.format(
				"Could not load children from cloud {0}", getName()));
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

	// public void save() {
	// // Currently we have to save all clouds instead of just this one
	// DeltaCloudManager.getDefault().saveClouds();
	// }

	public void addInstanceListListener(IInstanceListListener listener) {
		instanceListeners.add(listener);
	}

	public void removeInstanceListListener(IInstanceListListener listener) {
		instanceListeners.remove(listener);
	}

	public void notifyInstanceListListeners(DeltaCloudInstance[] array) {
		Object[] listeners = instanceListeners.getListeners();
		for (int i = 0; i < listeners.length; ++i) {
			((IInstanceListListener) listeners[i]).listChanged(this, array);
		}
	}

	public void addImageListListener(IImageListListener listener) {
		imageListeners.add(listener);
	}

	public void removeImageListListener(IImageListListener listener) {
		imageListeners.remove(listener);
	}

	public DeltaCloudImage[] notifyImageListListeners() {
		DeltaCloudImage[] images = cloneImagesArray();
		notifyImageListListeners(images);
		return images;
	}

	private DeltaCloudImage[] cloneImagesArray() {
		DeltaCloudImage[] imageArray = new DeltaCloudImage[images.size()];
		return images.toArray(imageArray);
	}

	private DeltaCloudInstance[] cloneInstancesArray() {
		DeltaCloudInstance[] instanceArray = new DeltaCloudInstance[instances.size()];
		return instances.toArray(instanceArray);
	}

	public void notifyImageListListeners(DeltaCloudImage[] array) {
		Object[] listeners = imageListeners.getListeners();
		for (int i = 0; i < listeners.length; ++i)
			((IImageListListener) listeners[i]).listChanged(this, array);
	}

	public Job getInstanceJob(String id) {
		synchronized (actionLock) {
			Job j = null;
			if (actionJobs != null) {
				return actionJobs.get(id);
			}
			return j;
		}
	}

	public void registerInstanceJob(String id, Job j) {
		synchronized (actionLock) {
			if (actionJobs == null)
				actionJobs = new HashMap<String, Job>();
			actionJobs.put(id, j);
		}
	}

	public DeltaCloudInstance waitWhilePending(String instanceId, IProgressMonitor pm) throws InterruptedException,
			DeltaCloudException {
		IInstanceStateMatcher differsFromPending = new IInstanceStateMatcher() {

			@Override
			public boolean matchesState(DeltaCloudInstance instance, String instanceState) {
				return !DeltaCloudInstance.PENDING.equals(instanceState);
			}
		};
		return waitForState(instanceId, differsFromPending, pm);
	}

	public DeltaCloudInstance waitForState(String instanceId, final String expectedState, IProgressMonitor pm)
			throws InterruptedException, DeltaCloudException {
		IInstanceStateMatcher stateMatcher = new IInstanceStateMatcher() {

			@Override
			public boolean matchesState(DeltaCloudInstance instance, String instanceState) {
				return expectedState != null && expectedState.equals(instanceState);
			}
		};
		return waitForState(instanceId, stateMatcher, pm);
	}

	public DeltaCloudInstance waitForState(String instanceId, IInstanceStateMatcher stateMatcher, IProgressMonitor pm)
			throws InterruptedException, DeltaCloudException {
		DeltaCloudInstance instance = getInstance(instanceId);
		while (!pm.isCanceled()) {
			if (stateMatcher.matchesState(instance, instance.getState())
					|| instance.getState().equals(DeltaCloudInstance.TERMINATED)) {
				return instance;
			}
			Thread.sleep(400);
			instance = refreshInstance(instance.getId());
		}
		return instance;
	}

	public void removeInstanceJob(String id, Job j) {
		synchronized (actionLock) {
			if (actionJobs != null && actionJobs.get(id) == j)
				actionJobs.remove(id);
		}
	}

	/**
	 * Loads the instances from the server and stores them in this instance.
	 * Furthermore listeners get informed.
	 * 
	 * @return the instances
	 * @throws DeltaCloudException
	 * 
	 * @see #notifyInstanceListListeners(DeltaCloudInstance[])
	 */
	public DeltaCloudInstance[] loadInstances() throws DeltaCloudException {
		synchronized (instanceLock) {
			instances = new ArrayList<DeltaCloudInstance>();
			try {
				List<Instance> list = getClient().listInstances();
				for (Iterator<Instance> i = list.iterator(); i.hasNext();) {
					DeltaCloudInstance instance = new DeltaCloudInstance(this, i.next());
					instances.add(instance);
				}
				// TODO: remove notification with all instances, replace by
				// notifying the changed instance
				DeltaCloudInstance[] instancesArray = instances.toArray(new DeltaCloudInstance[instances.size()]);
				notifyInstanceListListeners(instancesArray);
				return instancesArray;
			} catch (DeltaCloudClientException e) {
				throw new DeltaCloudException(MessageFormat.format("Could not load instances of cloud {0}: {1}",
						getName(), e.getMessage()), e);
			}
		}
	}

	public DeltaCloudInstance[] getCurrInstances() throws DeltaCloudException {
		synchronized (instanceLock) {
			if (instances == null) {
				return loadInstances();
			}
			DeltaCloudInstance[] instanceArray = new DeltaCloudInstance[instances.size()];
			instanceArray = instances.toArray(instanceArray);
			return instanceArray;
		}
	}

	public DeltaCloudInstance[] destroyInstance(String instanceId) {
		try {
			DeltaCloudInstance instance = getInstance(instanceId);
			performInstanceAction(instance, DeltaCloudInstance.DESTROY);
			instances.remove(instance);
		} catch (DeltaCloudException e) {
			return null;
		}
		// TODO: remove notification with all instances, replace by notifying
		// the changed instance
		DeltaCloudInstance[] instancesArray = instances.toArray(instances.toArray(new DeltaCloudInstance[instances
				.size()]));
		notifyInstanceListListeners(instancesArray);
		return instancesArray;
	}

	public void createKey(String keyname, String keystoreLocation) throws DeltaCloudException {
		try {
			getClient().createKey(keyname, keystoreLocation);
		} catch (DeltaCloudClientException e) {
			throw new DeltaCloudException(e);
		}
	}

	public void deleteKey(String keyname) throws DeltaCloudException {
		try {
			getClient().deleteKey(keyname);
		} catch (DeltaCloudClientException e) {
			throw new DeltaCloudException(e);
		}
	}

	public void replaceInstance(DeltaCloudInstance instance) {
		String instanceId = instance.getId();
		if (instance != null) {
			boolean found = false;
			for (int i = 0; i < instances.size(); ++i) {
				DeltaCloudInstance inst = instances.get(i);
				if (inst.getId().equals(instanceId)) {
					found = true;
					instances.set(i, instance);
				}
			}
			if (!found) {
				instances.add(instance);
			}
			// TODO: remove notification with all instances, replace by
			// notifying the changed instance
			notifyInstanceListListeners(instances.toArray(instances.toArray(new DeltaCloudInstance[instances.size()])));
		}
	}

	public DeltaCloudInstance refreshInstance(String instanceId) throws DeltaCloudException {
		DeltaCloudInstance retVal = null;
		try {
			Instance instance = getClient().listInstances(instanceId);
			retVal = new DeltaCloudInstance(this, instance);
			for (int i = 0; i < instances.size(); ++i) {
				DeltaCloudInstance inst = instances.get(i);
				if (inst.getId().equals(instanceId)) {
					// FIXME: remove BOGUS state when server fixes state
					// problems
					if (!(retVal.getState().equals(DeltaCloudInstance.BOGUS))
							&& !(inst.getState().equals(retVal.getState()))) {
						instances.set(i, retVal);
						// TODO: is this correct? getCurrInstances notifies, too
						notifyInstanceListListeners(getCurrInstances());
						return retVal;
					}
				}
			}
		} catch (DeltaCloudClientException e) {
			// will get here when a pending instance is being checked
		}
		return retVal;
	}

	public boolean performInstanceAction(String instanceId, String actionId) throws DeltaCloudException {
		return performInstanceAction(getInstance(instanceId), actionId);
	}

	protected boolean performInstanceAction(DeltaCloudInstance instance, String actionId) throws DeltaCloudException {
		try {
			if (instance == null) {
				return false;
			}

			boolean result = instance.performInstanceAction(actionId, client);
			if (result) {
				// TODO: remove notification with all instances, replace by
				// notifying the changed instance
				notifyInstanceListListeners(instances
						.toArray(instances.toArray(new DeltaCloudInstance[instances.size()])));
			}
			return result;
		} catch (DeltaCloudClientException e) {
			throw new DeltaCloudException(e);
		}
	}

	private DeltaCloudInstance getInstance(String instanceId) {
		for (DeltaCloudInstance instance : instances) {
			if (instance.getId().equals(instanceId)) {
				return instance;
			}
		}
		return null;
	}

	public DeltaCloudHardwareProfile[] getProfiles() throws DeltaCloudException {
		ArrayList<DeltaCloudHardwareProfile> profiles = new ArrayList<DeltaCloudHardwareProfile>();
		try {
			List<HardwareProfile> list = getClient().listProfiles();
			for (Iterator<HardwareProfile> i = list.iterator(); i.hasNext();) {
				DeltaCloudHardwareProfile profile = new DeltaCloudHardwareProfile(i.next());
				profiles.add(profile);
			}
		} catch (DeltaCloudClientException e) {
			throw new DeltaCloudException(MessageFormat.format("Could not list profiles on cloud {0}", name), e);
		}
		DeltaCloudHardwareProfile[] profileArray = new DeltaCloudHardwareProfile[profiles.size()];
		profileArray = profiles.toArray(profileArray);
		return profileArray;
	}

	public DeltaCloudImage loadImage(String imageId) throws DeltaCloudException {
		try {
			Image image = getClient().listImages(imageId);
			DeltaCloudImage deltaCloudImage = addImage(image);
			return deltaCloudImage;
		} catch (DeltaCloudClientException e) {
			throw new DeltaCloudException(e);
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
	public DeltaCloudImage[] loadImages() throws DeltaCloudException {
		synchronized (imageLock) {
			try {
				images = new ArrayList<DeltaCloudImage>();
				List<Image> list = getClient().listImages();
				for (Iterator<Image> i = list.iterator(); i.hasNext();) {
					addImage(i.next());
				}
				return notifyImageListListeners();
			} catch (DeltaCloudClientException e) {
				throw new DeltaCloudException(MessageFormat.format("Could not load images of cloud {0}: {1}",
						getName(), e.getMessage()), e);
			}
		}
	}

	private DeltaCloudImage addImage(Image image) {
		DeltaCloudImage deltaCloudImage = new DeltaCloudImage(image, this);
		images.add(deltaCloudImage);
		return deltaCloudImage;
	}

	public DeltaCloudImage[] getCurrImages() throws DeltaCloudException {
		synchronized (imageLock) {
			if (images == null) {
				return loadImages();
			}
			return cloneImagesArray();
		}
	}

	public DeltaCloudImage getImage(String imageId) {
		DeltaCloudImage retVal = null;
		try {
			Image image = getClient().listImages(imageId);
			retVal = new DeltaCloudImage(image, this);
		} catch (Exception e) {
			e.printStackTrace();
			// do nothing and return null
		}
		return retVal;
	}

	public boolean testConnection() throws DeltaCloudException {
		String instanceId = "nonexistingInstance"; //$NON-NLS-1$
		try {
			getClient().listInstances(instanceId);
			return true;
		} catch (DeltaCloudNotFoundClientException e) {
			return true;
		} catch (DeltaCloudAuthException e) {
			return false;
		} catch (DeltaCloudClientException e) {
			return false;
		}
	}

	public DeltaCloudRealm[] getRealms() throws DeltaCloudException {
		ArrayList<DeltaCloudRealm> realms = new ArrayList<DeltaCloudRealm>();
		try {
			List<Realm> list = getClient().listRealms();
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
			String keyname, String memory, String storage) throws DeltaCloudException {
		try {
			Instance instance = null;
			if (keyname != null) {
				instance = getClient().createInstance(imageId, profileId, realmId, name, keyname, memory, storage);
			} else {
				instance = getClient().createInstance(imageId, profileId, realmId, name, memory, storage);
			}
			if (instance != null) {
				DeltaCloudInstance newInstance = new DeltaCloudInstance(this, instance);
				newInstance.setGivenName(name);
				getCurrInstances(); // make sure instances are initialized
				instances.add(newInstance);
				DeltaCloudInstance[] instanceArray = cloneInstancesArray();
				notifyInstanceListListeners(instanceArray);
				return newInstance;
			}
		} catch (DeltaCloudClientException e) {
			throw new DeltaCloudException(e);
		}
		return null;
	}
}
