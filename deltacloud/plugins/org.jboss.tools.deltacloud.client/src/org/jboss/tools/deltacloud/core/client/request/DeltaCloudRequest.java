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
package org.jboss.tools.deltacloud.core.client.request;

import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.tools.deltacloud.core.client.HttpMethod;

/**
 * @author André Dietisheim
 */
public interface DeltaCloudRequest {

	public static final char PATH_SEPARATOR = '/';
	public static final char PARAMETER_SEPARATOR = '?';
	public static final String API_PATH_SEGMENT = "api";

	public URL getUrl() throws MalformedURLException;

	public String getUrlString();
	
	public HttpMethod getHttpMethod();
}
