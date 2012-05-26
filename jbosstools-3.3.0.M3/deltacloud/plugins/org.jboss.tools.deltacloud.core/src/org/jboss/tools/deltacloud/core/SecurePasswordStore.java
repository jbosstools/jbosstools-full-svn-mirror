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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.eclipse.equinox.security.storage.EncodingUtils;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;

/**
 * @author Andre Dietisheim
 * 
 *         TODO: replace DeltaCloudException by SecurePasswordStoreException
 *         (decouple from Deltacloud)
 */
public class SecurePasswordStore {

	private static final String ENCODING = "UTF-8";

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
			} catch (Exception e) {
				// TODO: internationalize strings
				throw new DeltaCloudException("Could get password", e);
			}
		}
	}

	public void setPassword(String password) throws DeltaCloudException {
		update(storageKey, password);
	}

	public void update(IStorageKey key, String password) throws DeltaCloudException {
		if (!storageKey.equals(key)
				|| isPasswordChanged(password)) {
			storeInPreferences(this.password = password, this.storageKey = key);
		}
	}

	private boolean isPasswordChanged(String password) {
		if (this.password == null && password == null) {
			return false;
		} else {
			return (this.password == null && password != null)
					|| (this.password != null && password == null)
					|| !password.equals(this.password);
		}
	}

	public void remove() throws DeltaCloudException {
		try {
			ISecurePreferences node = getNode(storageKey);
			if (node == null) {
				// TODO: internationalize strings
				throw new DeltaCloudException("Could not remove password");
			}
			node.clear();
		} catch (Exception e) {
			throw new DeltaCloudException("Could not remove password", e);
		}
	}

	private String getFromPreferences(IStorageKey key) throws StorageException, UnsupportedEncodingException {
		ISecurePreferences node = getNode(key);
		String password = node.get("password", null); //$NON-NLS-1$
		if (password == null) {
			return null;
		}
		return new String(EncodingUtils.decodeBase64(password));
	}

	private void storeInPreferences(String password, IStorageKey key) throws DeltaCloudException {
		try {
			ISecurePreferences node = getNode(key);
			node.put("password", EncodingUtils.encodeBase64(password.getBytes()), true /* encrypt */); //$NON-NLS-1$
		} catch (Exception e) {
			// TODO: internationalize string
			throw new DeltaCloudException("Could not store password", e);
		}
	}

	private ISecurePreferences getNode(IStorageKey key) throws UnsupportedEncodingException {
		if (key == null) {
			return null;
		}

		ISecurePreferences root = SecurePreferencesFactory.getDefault();
		String keyString = URLEncoder.encode(key.getKey(), ENCODING);
		return root.node(keyString);
	}
}
