/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.gwt.core.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.core.runtime.Assert;

/**
 * @author Andre Dietisheim
 */
public class ZipUtils {

	/** Signals the end of a stream. */
	private static final int EOS = -1;


	/**
	 * Unzips the given ZipInputStream to the given folder.
	 * 
	 * @param zipInputStream
	 *            the zip input stream
	 * @param dirToExtractTo
	 *            the dir to extract to
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * 
	 * @see ZipInputStream
	 * @see File
	 */
	public static final void unzipToFolder(ZipInputStream zipInputStream, File dirToExtractTo) throws IOException {
		Assert.isLegal(zipInputStream != null);
		Assert.isLegal(!dirToExtractTo.exists() || dirToExtractTo.isDirectory());
		try {
			for (ZipEntry zipEntry = null; (zipEntry = zipInputStream.getNextEntry()) != null;) {
				File file = new File(dirToExtractTo, zipEntry.getName());
				if (zipEntry.isDirectory()) {
					if (!file.exists()) {
						file.mkdirs();
					}
				} else {
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					writeTo(zipInputStream, file);
				}
			}
		} finally {
			zipInputStream.close();
		}
	}
	
	private static void writeTo(InputStream inputStream, File file) throws IOException {
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
		byte[] buffer = new byte[8192];
		try {
			for (int read = 0; (read = inputStream.read(buffer)) != EOS;) {
				outputStream.write(buffer, 0, read);
			}
		} finally {
			outputStream.close();
		}
	}
}
