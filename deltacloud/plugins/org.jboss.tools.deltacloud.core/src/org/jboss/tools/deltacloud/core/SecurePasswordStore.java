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
package org.jboss.tools.deltacloud.core;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;

/**
 * @author Andre Dietisheim
 * 
 *         TODO: remove DeltaCloudException
 */
public class SecurePasswordStore {

	public static interface IStorageKey {
		public String getKey();

		public boolean equals(IStorageKey key);
	}

	private String password;
	private boolean stored;
	private IStorageKey storageKey;

	public SecurePasswordStore(IStorageKey key, String password) {
		this.storageKey = key;
		this.password = password;
	}

	public String getPassword() throws DeltaCloudException {
		if (password != null) {
			if (!stored) {
				storeInPreferences(password, storageKey);
				stored = true;
			}
			return password;
		} else {
			try {
				return this.password = getFromPreferences(storageKey);
			} catch (StorageException e) {
				throw new DeltaCloudException("Could get password", e);
			}
		}
	}
	
	public void setPassword(String password) throws DeltaCloudException {
		update(storageKey, password);
	}

	public void update(IStorageKey key, String password) throws DeltaCloudException {
		if (!storageKey.equals(key)
				|| hasPasswordChanged(password)) {
			storeInPreferences(this.password = password, this.storageKey = key);
		}
	}

	private boolean hasPasswordChanged(String password) {
		if (this.password == null && password == null) {
			return false;
		} else {
			return (this.password == null && password != null)
					|| (this.password != null && password == null)
					|| !password.equals(this.password);
		}
	}

	public void remove() throws DeltaCloudException {
		ISecurePreferences node = getNode(storageKey);
		if (node == null) {
			throw new DeltaCloudException("Could not remove password");
		}
		node.clear();
	}

	private String getFromPreferences(IStorageKey key) throws StorageException {
		ISecurePreferences node = getNode(key);
		String password = node.get("password", null); //$NON-NLS-1$
		return password;
	}

	private void storeInPreferences(String password, IStorageKey key) throws DeltaCloudException {
		try {
			ISecurePreferences node = getNode(key);
			node.put("password", password, true /* encrypt */); //$NON-NLS-1$
		} catch (StorageException e) {
			// TODO: internationalize string
			throw new DeltaCloudException("Could not store password", e);
		}
	}

	private ISecurePreferences getNode(IStorageKey key) {
		ISecurePreferences root = SecurePreferencesFactory.getDefault();
		return root.node(key.getKey());
	}
}
