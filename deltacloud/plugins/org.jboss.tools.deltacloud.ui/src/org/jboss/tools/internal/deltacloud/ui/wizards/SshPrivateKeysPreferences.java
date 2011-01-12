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

import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.internal.deltacloud.ui.preferences.StringEntriesPreferenceValue;
import org.jboss.tools.internal.deltacloud.ui.preferences.StringPreferenceValue;

/**
 * @author André Dietisheim
 */
public class SshPrivateKeysPreferences {

	private static final String PLUGIN_ID = "org.eclipse.jsch.core";
	/**
	 * Preference keys defined by org.eclipse.jsch.
	 * 
	 * these keys are replicates from org.eclipse.jsch.internal.core.IConstants 
	 */
	private static final String PRIVATEKEY = "PRIVATEKEY";
	private static final String SSH2HOME = "SSH2HOME";

	private static StringEntriesPreferenceValue sshPrivateKeyPreference =
			new StringEntriesPreferenceValue(",", PRIVATEKEY, PLUGIN_ID);
	private static StringPreferenceValue sshHome = new StringPreferenceValue(SSH2HOME, PLUGIN_ID);

	/**
	 * Adds the given keyName to the ssh-preferences
	 * 
	 * @param keyName
	 *            the name of the key to add
	 */
	public static void add(String keyName) {
		sshPrivateKeyPreference.add(keyName);
	}

	/**
	 * Removes the given keyName from the ssh-preferences
	 * 
	 * @param keyName
	 *            the name of the key to remove
	 */
	public static void remove(String keyName) {
		sshPrivateKeyPreference.remove(keyName);
	}

	public static String getKeyStorePath() throws DeltaCloudException {
		// TODO: replace by code that queries the RSE preferences for its key
		// location setting
//		String userHomePath = System.getProperty("user.home");
//		if (userHomePath == null) {
//			throw new DeltaCloudException("Could not determine path to save pem file to");
//		}
//		return new StringBuilder(userHomePath)
//				.append(File.separatorChar).append(".ssh").append(File.separatorChar)
//				.toString();
		return sshHome.get();
	}

}
