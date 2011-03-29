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
import java.text.MessageFormat;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudKey;

/**
 * @author André Dietisheim
 */
public class PemFileManager {

	private static final String PEM_FILE_SUFFIX = "pem";

	public static File delete(DeltaCloudKey key, String keyStorePath) throws DeltaCloudException {
		try {
			Assert.isLegal(key != null);
			Assert.isLegal(keyStorePath != null && keyStorePath.length() > 0);

			File file = getFile(key.getId(), keyStorePath);
			delete(file);
			return file;
		} catch (DeltaCloudException e) {
			throw e;
		} catch (Exception e) {
			throw new DeltaCloudException(MessageFormat.format("Coud not delete key \"{0}\"", key.getName()), e);
		}
	}

	public static File create(DeltaCloudKey key, String keyStorePath) throws DeltaCloudException {
		try {
			Assert.isLegal(key != null);
			Assert.isLegal(keyStorePath != null && keyStorePath.length() > 0, "key store path is not set.");
			File keyFile = create(getFile(key.getId(), keyStorePath));
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

	private static File create(File file) throws IOException {
		if (file.exists()) {
			throw new IllegalStateException(
					MessageFormat.format("File \"{0}\" already exists.", file.getAbsolutePath()));
		}
		file.createNewFile();
		file.setReadable(false, false);
		file.setWritable(true, true);
		file.setReadable(true, true);
		return file;
	}

	public static File getFile(String keyId, String keyStoreLocation) {
		File keyFile =
				Path.fromOSString(keyStoreLocation)
						.append(keyId)
						.addFileExtension(PEM_FILE_SUFFIX)
						.toFile();
		return keyFile;
	}

	public static boolean exists(String keyId, String keyStoreLocation) {
		File file = getFile(keyId, keyStoreLocation);
		return file != null && file.exists();
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
