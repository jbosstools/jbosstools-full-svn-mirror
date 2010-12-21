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

import org.jboss.tools.deltacloud.core.client.HttpMethod;
import org.jboss.tools.deltacloud.core.client.utils.UrlBuilder;


/**
 * Lists keys available on the deltacloud server
 * 
 * @author André Dietisheim
 */
public class ListKeysRequest extends AbstractDeltaCloudRequest {
	
	public ListKeysRequest(URL baseUrl) {
		super(baseUrl, HttpMethod.GET);
	}

	@Override
	protected String doCreateUrl(UrlBuilder urlBuilder) {
		return urlBuilder.path("keys").toString();
	}
}
