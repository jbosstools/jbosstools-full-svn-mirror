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
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.runtime.Path;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudKey;
import org.jboss.tools.internal.deltacloud.ui.preferences.StringEntriesPreferenceValue;

/**
 * @author André Dietisheim
 */
public class PemFileManager {

	private static final String PEM_FILE_SUFFIX = "pem";
	private static final String PLUGIN_ID = "org.eclipse.jsch.core";
	private static final String KEY_PRIVATEKEY = "PRIVATEKEY";
	
	private static StringEntriesPreferenceValue sshPrivateKeyPreference =
			new StringEntriesPreferenceValue(",", KEY_PRIVATEKEY, PLUGIN_ID);

	public static File create(DeltaCloudKey key) throws DeltaCloudException {
		File file = create(key, getKeyStorePath());
		sshPrivateKeyPreference.add(file.getName());
		return file;
	}

	public static void delete(DeltaCloudKey key) throws DeltaCloudException {
		File file = getFile(key.getId(), getKeyStorePath());
		delete(file);
		sshPrivateKeyPreference.remove(file.getName());
	}

	private static String getKeyStorePath() throws DeltaCloudException {
		// TODO: replace by code that queries the RSE preferences for its key
		// location setting
		String userHomePath = System.getProperty("user.home");
		if (userHomePath == null) {
			throw new DeltaCloudException("Could not determine path to save pem file to");
		}
		return new StringBuilder(userHomePath)
				.append(File.separatorChar).append(".ssh").append(File.separatorChar)
				.toString();
	}

	private static File create(DeltaCloudKey key, String keyStorePath) throws DeltaCloudException {
		try {
			File keyFile = create(getFile(key.getId(), keyStorePath), keyStorePath);
			save(key.getPem(), keyFile);
			keyFile.setWritable(false, false);
			return keyFile;
		} catch (Exception e) {
			throw new DeltaCloudException(e);
		}
	}

	private static void save(String key, File keyFile) throws IOException {
		if (key != null) {
			FileWriter w = new FileWriter(keyFile);
			w.write(key);
			w.close();
		}
	}

	private static File create(File file, String keyStoreLocation)
			throws IOException {
		if (!file.exists()) {
			file.createNewFile();
		}
		file.setReadable(false, false);
		file.setWritable(true, true);
		file.setReadable(true, true);
		return file;
	}

	private static File getFile(String keyId, String keyStoreLocation) {
		File keyFile =
				Path.fromOSString(keyStoreLocation)
						.append(keyId)
						.addFileExtension(PEM_FILE_SUFFIX) //$NON-NLS-1$
						.toFile();
		return keyFile;
	}

	private static void delete(File file) throws DeltaCloudException {
		try {
			if (file == null
					|| !file.exists()) {
				return;
			}
			file.delete();
		} catch (Exception e) {
			throw new DeltaCloudException(e);
		}
	}

}
