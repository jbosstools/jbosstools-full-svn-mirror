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

import org.eclipse.equinox.security.storage.EncodingUtils;
import org.jboss.tools.deltacloud.core.SecurePasswordStore.IStorageKey;

/**
 * Implements a key to be used to store values in the preferences store.
 * 
 * @author Andre Dietisheim
 *
 */
public class DeltaCloudPasswordStorageKey implements IStorageKey {

	private static final String PREFERNCES_BASEKEY = Activator.PLUGIN_ID.replace('.', '/');
	private String cloudName;
	private String userName;

	public DeltaCloudPasswordStorageKey(String cloudName, String userName) {
		this.userName = EncodingUtils.encodeBase64(userName.getBytes());
		this.cloudName = EncodingUtils.encodeBase64(cloudName.getBytes());
	}

	@Override
	public String getKey() {
		String key = new StringBuilder(PREFERNCES_BASEKEY)
				.append(cloudName)
				.append('/') //$NON-NLS-1$
				.append(userName)
				.toString();
		return EncodingUtils.encodeSlashes(key);
	}

	@Override
	public boolean equals(IStorageKey key) {
		if (!key.getClass().isAssignableFrom(DeltaCloudPasswordStorageKey.class)) {
			return false;
		}
		DeltaCloudPasswordStorageKey deltaCloudKey = (DeltaCloudPasswordStorageKey) key;
		return userName.equals(deltaCloudKey.userName)
				&& cloudName.equals(deltaCloudKey.cloudName);
	}
}
