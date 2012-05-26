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
package org.jboss.tools.internal.deltacloud.ui.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author André Dietisheim
 *
 */
public class URIUtils {

	private URIUtils() {
		// inhibit instantiation
	}
	
	private static final String HTTP_PREFIX = "http://"; //$NON-NLS-1$
	private static final Pattern URI_REGEXP = Pattern.compile("(.+://)*(.+)(:[0-9]){0,1}"); //$NON-NLS-1$
	
	public static String prependHttp(String url) {
		if (!startsWithScheme(url)) {
			return HTTP_PREFIX + url;
		} else {
			return url;
		}
	}

	public static boolean startsWithScheme(String url) {
		if (url == null || url.length() == 0) {
			return false;
		} else {
			try {
				Matcher matcher = URI_REGEXP.matcher(url);
				if (matcher.matches()) {
					return matcher.group(1) != null;
				}
			} catch (Exception e ) {
				// ignore
			}
			return false;
		}
	}

}
