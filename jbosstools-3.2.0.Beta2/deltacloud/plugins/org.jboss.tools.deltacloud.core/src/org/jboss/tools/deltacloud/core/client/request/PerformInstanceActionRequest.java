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

import java.net.URL;

import org.jboss.tools.deltacloud.core.client.utils.UrlBuilder;


/**
 * Performs an action on a instance on the deltacloud server
 */
public class PerformInstanceActionRequest extends AbstractDeltaCloudRequest {
	
	public PerformInstanceActionRequest(URL url, HttpMethod httpMethod) {
		super(url, httpMethod);
	}

	@Override
	protected String doCreateUrl(UrlBuilder urlBuilder) {
		return urlBuilder.toString();
	}
	
	@Override
	protected UrlBuilder createUrlBuilder(URL baseURL) {
		return new UrlBuilder(baseURL);
	}

}
