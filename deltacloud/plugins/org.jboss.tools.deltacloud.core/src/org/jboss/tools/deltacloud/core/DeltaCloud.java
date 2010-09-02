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
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.equinox.security.storage.EncodingUtils;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.jboss.tools.deltacloud.core.client.DeltaCloudAuthException;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClient;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.core.client.HardwareProfile;
import org.jboss.tools.deltacloud.core.client.Image;
import org.jboss.tools.deltacloud.core.client.Instance;
import org.jboss.tools.deltacloud.core.client.Realm;

public class DeltaCloud {
	
	private String name;
	private String username;
	private String url;
	private String type;
	private DeltaCloudClient client;
	private ArrayList<DeltaCloudInstance> instances;
	private ArrayList<DeltaCloudImage> images;
	private Object imageLock = new Object();
	private Object instanceLock = new Object();
	
	ListenerList instanceListeners = new ListenerList();
	ListenerList imageListeners = new ListenerList();
	
	public DeltaCloud(String name, String url, String username, String passwd) throws MalformedURLException {
		this(name, url, username, passwd, null, false);
	}

	public DeltaCloud(String name, String url, String username, String passwd, String type, boolean persistent) throws MalformedURLException {
		this.client = new DeltaCloudClient(new URL(url + "/api"), username, passwd); //$NON-NLS-1$
		this.url = url;
		this.name = name;
		this.username = username;
		this.type = type;
		if (persistent) {
			ISecurePreferences root = SecurePreferencesFactory.getDefault();
			String key = DeltaCloud.getPreferencesKey(url, username);
			ISecurePreferences node = root.node(key);
			try {
				node.put("password", passwd, true /*encrypt*/);
			} catch (StorageException e) {
				e.printStackTrace();
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
	
	public void loadChildren() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				getImages();
				getInstances();
			}
			
		});
		t.start();
	}
	
	public void addInstanceListListener(IInstanceListListener listener) {
		instanceListeners.add(listener);
	}
	
	public void removeInstanceListListener(IInstanceListListener listener) {
		instanceListeners.remove(listener);
	}

	public void notifyInstanceListListeners(DeltaCloudInstance[] array) {
		Object[] listeners = instanceListeners.getListeners();
		for (int i = 0; i < listeners.length; ++i)
			((IInstanceListListener)listeners[i]).listChanged(this, array);
	}
	
	public void addImageListListener(IImageListListener listener) {
		imageListeners.add(listener);
	}
	
	public void removeImageListListener(IImageListListener listener) {
		imageListeners.remove(listener);
	}
	
	public void notifyImageListListeners(DeltaCloudImage[] array) {
		Object[] listeners = imageListeners.getListeners();
		for (int i = 0; i < listeners.length; ++i)
			((IImageListListener)listeners[i]).listChanged(this, array);
	}

	public DeltaCloudInstance[] getInstances() {
		synchronized (instanceLock) {
			instances = new ArrayList<DeltaCloudInstance>();
			try {
				List<Instance> list = client.listInstances();
				for (Iterator<Instance> i = list.iterator(); i.hasNext();) {
					DeltaCloudInstance instance = new DeltaCloudInstance(i.next());
					instances.add(instance);
				}
			} catch (DeltaCloudClientException e) {
				Activator.log(e);
			}
			DeltaCloudInstance[] instanceArray = new DeltaCloudInstance[instances.size()];
			instanceArray = instances.toArray(instanceArray);
			notifyInstanceListListeners(instanceArray);
			return instanceArray;
		}
	}
	
	public DeltaCloudInstance[] getCurrInstances() {
		synchronized (instanceLock) {
			if (instances == null)
				return getInstances();
			DeltaCloudInstance[] instanceArray = new DeltaCloudInstance[instances.size()];
			instanceArray = instances.toArray(instanceArray);
			return instanceArray;
		}
	}
	
	public DeltaCloudInstance[] destroyInstance(String instanceId) {
		try {
			client.destroyInstance(instanceId);
			for (int i = 0; i < instances.size(); ++i) {
				DeltaCloudInstance instance = instances.get(i);
				if (instance.getId().equals(instanceId)) {
					instances.remove(i);
					break;
				}
			}
		} catch (DeltaCloudClientException e) {
			return null;
		}
		DeltaCloudInstance[] instanceArray = new DeltaCloudInstance[instances.size()];
		instanceArray = instances.toArray(instanceArray);
		notifyInstanceListListeners(instanceArray);
		return instanceArray;
	}

	public void createKey(String keyname, String keystoreLocation) throws DeltaCloudException {
		try {
			client.createKey(keyname, keystoreLocation);
		} catch (DeltaCloudClientException e) {
			throw new DeltaCloudException(e);
		}
	}
	
	public void deleteKey(String keyname) throws DeltaCloudException {
		try {
			client.deleteKey(keyname);
		} catch (DeltaCloudClientException e) {
			throw new DeltaCloudException(e);
		}
	}
	
	public DeltaCloudInstance refreshInstance(String instanceId) {
		DeltaCloudInstance retVal = null;
		try {
			Instance instance = client.listInstances(instanceId);
			retVal = new DeltaCloudInstance(instance);
			for (int i = 0; i < instances.size(); ++i) {
				DeltaCloudInstance inst = instances.get(i);
				if (inst.getId().equals(instanceId)) {
					// FIXME: remove BOGUS state when server fixes state problems
					if (!(retVal.getState().equals(DeltaCloudInstance.BOGUS)) && !(inst.getState().equals(retVal.getState()))) {
						instances.set(i, retVal);
						DeltaCloudInstance[] instanceArray = new DeltaCloudInstance[instances.size()];
						instanceArray = instances.toArray(instanceArray);
						notifyInstanceListListeners(instanceArray);
						return retVal;
					}
				}
			}
		} catch (DeltaCloudClientException e) {
			// will get here when a pending instance is being checked
		}
		return retVal;
	}
	
	public boolean performInstanceAction(String instanceId, String action) throws DeltaCloudException {
		try {
			return client.performInstanceAction(instanceId, action);
		} catch (DeltaCloudClientException e) {
			throw new DeltaCloudException(e);
		}
	}

	public DeltaCloudHardwareProfile[] getProfiles() {
		ArrayList<DeltaCloudHardwareProfile> profiles = new ArrayList<DeltaCloudHardwareProfile>();
		try {
			List<HardwareProfile> list = client.listProfiles();
			for (Iterator<HardwareProfile> i = list.iterator(); i.hasNext();) {
				DeltaCloudHardwareProfile profile = new DeltaCloudHardwareProfile(i.next());
				profiles.add(profile);
			}
		} catch (DeltaCloudClientException e) {
			Activator.log(e);
		}
		DeltaCloudHardwareProfile[] profileArray = new DeltaCloudHardwareProfile[profiles.size()];
		profileArray = profiles.toArray(profileArray);
		return profileArray;
	}
	
	public DeltaCloudImage[] getImages() {
		synchronized (imageLock) {
			images = new ArrayList<DeltaCloudImage>();
			try {
				List<Image> list = client.listImages();
				for (Iterator<Image> i = list.iterator(); i.hasNext();) {
					DeltaCloudImage image = new DeltaCloudImage(i.next());
					images.add(image);
				}
			} catch (DeltaCloudClientException e) {
				Activator.log(e);
			}
			DeltaCloudImage[] imageArray = new DeltaCloudImage[images.size()];
			imageArray = images.toArray(imageArray);
			notifyImageListListeners(imageArray);
			return imageArray;
		}
	}

	public DeltaCloudImage[] getCurrImages() {
		synchronized(imageLock) {
			if (images == null)
				return getImages();
			DeltaCloudImage[] imageArray = new DeltaCloudImage[images.size()];
			imageArray = images.toArray(imageArray);
			return imageArray;
		}
	}
	
	public boolean testConnection() {
		String instanceId = "madeupValue"; //$NON-NLS-1$
		try {
			client.listInstances(instanceId);
			return true;
		} catch (DeltaCloudAuthException e) {
			return false;
		} catch (DeltaCloudClientException e) {
			return true;
		}
	}

	public DeltaCloudRealm[] getRealms() {
		ArrayList<DeltaCloudRealm> realms = new ArrayList<DeltaCloudRealm>();
		try {
			List<Realm> list = client.listRealms();
			for (Iterator<Realm> i = list.iterator(); i.hasNext();) {
				DeltaCloudRealm realm = new DeltaCloudRealm(i.next());
				realms.add(realm);
			}
		} catch (DeltaCloudClientException e) {
			Activator.log(e);
		}
		return realms.toArray(new DeltaCloudRealm[realms.size()]);
	}

	public DeltaCloudInstance createInstance(String name, String imageId, String realmId, String profileId,
			String keyname, String memory, String storage) throws DeltaCloudException {
		try {
			Instance instance = null;
			if (keyname != null) {
				instance = client.createInstance(imageId, profileId, realmId, name, keyname, memory, storage);
			} else {
				instance = client.createInstance(imageId, profileId, realmId, name, memory, storage);
			}
			if (instance != null) {
				DeltaCloudInstance newInstance = new DeltaCloudInstance(instance);
				newInstance.setGivenName(name);
				instances.add(newInstance);
				DeltaCloudInstance[] instanceArray = new DeltaCloudInstance[instances.size()];
				instanceArray = instances.toArray(instanceArray);
				notifyInstanceListListeners(instanceArray);
				return newInstance;
			}
		} catch (DeltaCloudClientException e) {
			throw new DeltaCloudException(e);
		}
		return null;
	}
}
