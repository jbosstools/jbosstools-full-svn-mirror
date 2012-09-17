/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.express.internal.ui.utils;

import org.eclipse.jsch.internal.core.IConstants;
import org.eclipse.jsch.internal.core.JSchCorePlugin;

/**
 * @author Andre Dietisheim
 */
public class SSHUtils {

	public static String getSSH2Home() {
		return JSchCorePlugin.getPlugin().getPluginPreferences().getString(IConstants.KEY_SSH2HOME);
	}
}
