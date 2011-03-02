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

import org.jboss.tools.deltacloud.core.client.HttpMethod;
import org.jboss.tools.deltacloud.core.client.utils.UrlBuilder;

/**
 * Lists an instance on the deltacloud server
 * 
 * @author André Dietisheim
 */
public class ListInstanceRequest extends AbstractDeltaCloudRequest {
	
	private String instanceId;

	public ListInstanceRequest(String baseUrl, String instanceId) {
		super(baseUrl, HttpMethod.GET);
		this.instanceId = instanceId;
	}

	@Override
	protected String doCreateUrl(UrlBuilder urlBuilder) {
		return urlBuilder.path("instances").path(instanceId).toString();
	}
}
