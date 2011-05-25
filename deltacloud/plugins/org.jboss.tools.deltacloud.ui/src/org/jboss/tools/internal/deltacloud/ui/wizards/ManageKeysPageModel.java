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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudKey;
import org.jboss.tools.internal.deltacloud.ui.common.databinding.validator.ObservableUIPojo;

/**
 * @author André Dietisheim
 */
public class ManageKeysPageModel extends ObservableUIPojo {

	public static final String PROP_SELECTED_KEY = "selectedKey";
	public static final String PROP_KEYS = "keys";
	public static final String PROP_KEYS_ADDED = "keyAdded";
	public static final String PROP_KEYS_REMOVED = "keyRemoved";
	public static final String PROP_KEY_STORE_PATH = "keyStorePath";

	private List<DeltaCloudKey> keys = new ArrayList<DeltaCloudKey>();
	private DeltaCloud cloud;
	private DeltaCloudKey selectedKey;
	private String keyStorePath;

	public ManageKeysPageModel(DeltaCloud cloud) {
		this.cloud = cloud;
		this.keyStorePath = getInitialKeyStorePath();
	}

	public String getInitialKeyStorePath() {
		try {
			return SshPrivateKeysPreferences.getSshKeyDirectory();
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	public DeltaCloudKey deleteSelectedKey() throws DeltaCloudException {
		if (selectedKey == null) {
			return null;
		}
		DeltaCloudKey keyToDelete = selectedKey;
		cloud.deleteKey(keyToDelete.getId());
		int index = keys.indexOf(keyToDelete);
		keys.remove(keyToDelete);
		fireIndexedPropertyChange(PROP_KEYS, index, keyToDelete, null);
		setSelectedKey(index - 1);
		return keyToDelete;
	}

	public void removeKeyLocally(DeltaCloudKey key) throws DeltaCloudException, FileNotFoundException {
		if (key == null) {
			return;
		}

		String keyStorePath = getKeyStorePath();
		if (keyStorePath != null && keyStorePath.length() > 0) {
			File pemFile = PemFileManager.delete(key, keyStorePath);
			SshPrivateKeysPreferences.remove(pemFile.getAbsolutePath());
		}
	}

	public DeltaCloudKey createKey(String keyId) throws DeltaCloudException {
		DeltaCloudKey key = cloud.createKey(keyId);
		keys.add(key);
		int index = keys.indexOf(key);
		fireIndexedPropertyChange(PROP_KEYS, index, null, key);
		setSelectedKey(key);
		return key;
	}

	public void storeKeyLocally(DeltaCloudKey key, String pemFolder) throws DeltaCloudException, FileNotFoundException {
		File pemFile = PemFileManager.create(key, pemFolder);
		SshPrivateKeysPreferences.add(pemFile.getAbsolutePath());
	}

	public void refreshKeys() throws DeltaCloudException {
		java.util.List<DeltaCloudKey> newKeys = new ArrayList<DeltaCloudKey>();
		newKeys.addAll(Arrays.asList(cloud.getKeys()));
		setKeys(newKeys);
	}

	public DeltaCloudKey getSelectedKey() {
		return selectedKey;
	}

	public void setSelectedKey(DeltaCloudKey selectedKey) {
		firePropertyChange(PROP_SELECTED_KEY, this.selectedKey, this.selectedKey = selectedKey);
	}

	/**
	 * Sets the selected key by the given index in the list of keys that are
	 * currently available to this model. If the index's larger than the number
	 * of keys, the selection is reseted. If the index's is below 0 and there
	 * are still keys available, the key at index 0 is selected.
	 * 
	 * @param index
	 *            the new selected key
	 */
	public void setSelectedKey(int index) {
		DeltaCloudKey key = null;
		if (index < 0) {
			index = 0;
		}
		if (index < keys.size()) {
			key = keys.get(index);
		}
		setSelectedKey(key);
	}

	/**
	 * Sets the currently selected key or the the first key (in the available
	 * keys) as selected key.
	 */
	public void setSelectedKey() {
		DeltaCloudKey key = getSelectedKey();
		if (key == null) {
			setSelectedKey(0);
		} else {
			setSelectedKey(key);
		}
	}

	public List<DeltaCloudKey> getKeys() {
		return keys;
	}

	public void setKeys(List<DeltaCloudKey> newKeys) {
		firePropertyChange(PROP_KEYS, this.keys, this.keys = newKeys);
		/*
		 * need to reset selection since widget looses selection (when items are
		 * set) and property may not fire if no change in selection
		 */
		DeltaCloudKey key = getSelectedKey();
		setSelectedKey(null);
		setSelectedKey(key);
	}

	public DeltaCloudKey getKey(String keyId) {
		if (keys == null
				|| keyId == null) {
			return null;
		}
		DeltaCloudKey matchingKey = null;
		for (DeltaCloudKey key : keys) {
			if (keyId.equals(key.getId())) {
				matchingKey = key;
				break;
			}
		}
		return matchingKey;
	}

	public String getKeyStorePath() {
		return keyStorePath;
	}

	public void setKeyStorePath(String keyStorePath) {
		System.err.println(keyStorePath);
		firePropertyChange(PROP_KEY_STORE_PATH, this.keyStorePath, this.keyStorePath = keyStorePath);
	}

	public DeltaCloud getCloud() {
		return cloud;
	}

}
