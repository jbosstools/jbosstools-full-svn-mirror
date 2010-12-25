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

/**
 * @author André Dietisheim
 */
public class PemFileFactory {

	private static final String PEM_FILE_SUFFIX = "pem";

	public static File create(DeltaCloudKey key) throws DeltaCloudException {
		return create(key, getKeyStorePath());
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
			File keyFile = createFile(key.getId(), keyStorePath);
			save(key.getPem(), keyFile);
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

	private static File createFile(String keyname, String keyStoreLocation)
			throws IOException {
		File keyFile =
				Path.fromOSString(keyStoreLocation)
						.append(keyname)
						.addFileExtension(PEM_FILE_SUFFIX) //$NON-NLS-1$
						.toFile();
		if (!keyFile.exists()) {
			keyFile.createNewFile();
		}
		keyFile.setReadable(false, false);
		keyFile.setWritable(true, true);
		keyFile.setReadable(true, true);
		return keyFile;
	}

}
