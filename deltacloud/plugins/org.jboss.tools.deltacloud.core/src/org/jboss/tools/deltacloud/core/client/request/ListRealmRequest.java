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
 * Lists a realm on the deltacloud server
 */
public class ListRealmRequest extends AbstractDeltaCloudRequest {
	
	private String realmId;

	public ListRealmRequest(URL baseUrl, String realmId) {
		super(baseUrl, HttpMethod.GET);
		this.realmId = realmId;
	}

	@Override
	protected String doCreateUrl(UrlBuilder urlBuilder) {
		return urlBuilder.path("realms").path(realmId).toString();
	}
}
