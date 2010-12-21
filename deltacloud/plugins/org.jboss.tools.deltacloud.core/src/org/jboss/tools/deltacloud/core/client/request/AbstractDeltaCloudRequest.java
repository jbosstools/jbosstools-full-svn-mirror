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
import org.jboss.tools.deltacloud.core.client.utils.UrlBuilder;

/**
 * @author André Dietisheim
 */
public abstract class AbstractDeltaCloudRequest implements DeltaCloudRequest {
		
	private URL url;
	private HttpMethod httpMethod;
	private UrlBuilder urlBuilder;

	protected AbstractDeltaCloudRequest(URL baseURL, HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
		this.urlBuilder = createUrlBuilder(baseURL);
	}

	protected abstract String doCreateUrl(UrlBuilder urlBuilder);

	protected UrlBuilder createUrlBuilder(URL baseURL) {
		return new UrlBuilder(baseURL).path(API_PATH_SEGMENT);
	}
	
	public URL getUrl() throws MalformedURLException {
		if (url == null) {
			this.url = new URL(doCreateUrl(urlBuilder));
		}
		return url;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}
}
