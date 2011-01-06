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
package org.jboss.tools.internal.deltacloud.test.fakes;

/**
 * @author André Dietisheim
 */
public class ServerResponseFakes {

	public static final String getActionXML(String url, String method, String name) {
		return "<link "
				+ "method='" + method + "' "
				+ "href='" + url + "' "
				+ "rel='" + name + "' />";
	}
}
