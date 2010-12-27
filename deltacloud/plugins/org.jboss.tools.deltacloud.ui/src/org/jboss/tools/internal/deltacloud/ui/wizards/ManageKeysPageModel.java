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
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jboss.tools.common.log.StatusFactory;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudKey;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob.CLOUDELEMENT;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.internal.deltacloud.ui.common.databinding.validator.ObservableUIPojo;

/**
 * @author André Dietisheim
 */
public class ManageKeysPageModel extends ObservableUIPojo {

	public static final String PROP_SELECTED_KEY = "selectedKey";
	public static final String PROP_KEYS = "keys";
	public static final String PROP_KEYS_ADDED = "keyAdded";
	public static final String PROP_KEYS_REMOVED = "keyRemoved";

	private List<DeltaCloudKey> keys = new ArrayList<DeltaCloudKey>();
	private DeltaCloud cloud;
	private DeltaCloudKey selectedKey;
	
	public ManageKeysPageModel(DeltaCloud cloud) {
		this.cloud = cloud;
		asyncGetKeys(cloud);
	}

	public void refreshKeys() {
		asyncGetKeys(cloud);
	}

	public void deleteSelectedKey() throws DeltaCloudException {
		if (selectedKey == null) {
			return;
		}
		cloud.deleteKey(selectedKey.getId());
		int index = keys.indexOf(selectedKey);
		keys.remove(selectedKey);
		fireIndexedPropertyChange(PROP_KEYS, index, selectedKey, null);
		PemFileManager.delete(selectedKey);
		setSelectedKey(index - 1);
	}

	public void createKey(String keyId) throws DeltaCloudException {
		DeltaCloudKey key = cloud.createKey(keyId);
		keys.add(key);
		int index = keys.indexOf(key);
		fireIndexedPropertyChange(PROP_KEYS, index, null, key);
		setSelectedKey(key);
		PemFileManager.create(key);
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

	public List<DeltaCloudKey> getKeys() {
		return keys;
	}

	public void setKeys(List<DeltaCloudKey> newKeys) {
		firePropertyChange(PROP_KEYS, keys, keys = newKeys);
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

	private void asyncGetKeys(final DeltaCloud cloud) {
		// TODO: internationalize strings
		new AbstractCloudElementJob("get keys", cloud, CLOUDELEMENT.KEYS) {

			protected IStatus doRun(IProgressMonitor monitor) throws Exception {
				try {
					List<DeltaCloudKey> newKeys = new ArrayList<DeltaCloudKey>();
					newKeys.addAll(Arrays.asList(cloud.getKeys()));
					setKeys(newKeys);
					setSelectedKey(null);
					return Status.OK_STATUS;
				} catch (DeltaCloudException e) {
					// TODO: internationalize strings
					return StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID,
							MessageFormat.format("Could not get keys from cloud {0}", cloud.getName()), e);
				}
			}

		}.schedule();
	}

}
