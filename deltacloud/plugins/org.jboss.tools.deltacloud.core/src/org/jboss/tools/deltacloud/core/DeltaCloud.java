package org.jboss.tools.deltacloud.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.ListenerList;
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
	private URL url;
	private DeltaCloudClient client;
	private ArrayList<DeltaCloudInstance> instances;
	
	ListenerList instanceListeners = new ListenerList();
	ListenerList imageListeners = new ListenerList();
	
	public DeltaCloud(String name, URL url, String username, String passwd) throws MalformedURLException {
		this.client = new DeltaCloudClient(url, username, passwd);
		this.url = url;
		this.name = name;
		this.username = username;
	}

	public String getName() {
		return name;
	}
	
	public String getURL() {
		return url.toString();
	}
	
	public String getUsername() {
		return username;
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
			((IInstanceListListener)listeners[i]).listChanged(array);
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
			((IImageListListener)listeners[i]).listChanged(array);
	}

	public DeltaCloudInstance[] getInstances() {
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
	
	public DeltaCloudInstance[] getCurrInstances() {
		DeltaCloudInstance[] instanceArray = new DeltaCloudInstance[instances.size()];
		instanceArray = instances.toArray(instanceArray);
		return instanceArray;
	}
	
	public DeltaCloudInstance refreshInstance(String instanceId) {
		DeltaCloudInstance retVal = null;
		try {
			Instance instance = client.listInstances(instanceId);
			retVal = new DeltaCloudInstance(instance);
			for (int i = 0; i < instances.size(); ++i) {
				DeltaCloudInstance inst = instances.get(i);
				if (inst.getId().equals(instanceId)) {
					if (!inst.getState().equals(instance.getState())) {
						instances.set(i, retVal);
						DeltaCloudInstance[] instanceArray = new DeltaCloudInstance[instances.size()];
						instanceArray = instances.toArray(instanceArray);
						notifyInstanceListListeners(instanceArray);
						return retVal;
					}
				}
			}
		} catch (DeltaCloudClientException e) {
			// do nothing and return null
		}
		return retVal;
	}
	
	public boolean performInstanceAction(String instanceId, String action) {
		try {
			return client.performInstanceAction(instanceId, action);
		} catch (DeltaCloudClientException e) {
			return false;
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
		ArrayList<DeltaCloudImage> images = new ArrayList<DeltaCloudImage>();
		try {
			List<Image> list = client.listImages();
			for (Iterator<Image> i = list.iterator(); i.hasNext();) {
				DeltaCloudImage image = new DeltaCloudImage(i.next());
				images.add(image);
			}
		} catch (DeltaCloudClientException e) {
			Activator.log(e);
		}
		return images.toArray(new DeltaCloudImage[images.size()]);
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

	public DeltaCloudInstance createInstance(String name, String imageId, String realmId, String profileId) throws DeltaCloudException {
		try {
			Instance instance = client.createInstance(imageId, profileId, realmId, name);
			if (instance != null) {
				DeltaCloudInstance newInstance = new DeltaCloudInstance(instance);
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
