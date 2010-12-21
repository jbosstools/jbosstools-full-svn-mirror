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
 * Lists images, instances, realms or profiles on the deltacloud server. 
 * 
 * @see ListRealmRequest
 * @see ListHardwareProfilesRequest
 * @see ListHardwareProfilesRequest
 * @see ListInstancesRequest
 * 
 * @author André Dietisheim
 */
public abstract class AbstractListObjectsRequest extends AbstractDeltaCloudRequest {
	
	private String objectType;

	public AbstractListObjectsRequest(URL baseUrl, String objectType) {
		super(baseUrl, HttpMethod.GET);
		this.objectType = objectType;
	}

	@Override
	protected String doCreateUrl(UrlBuilder urlBuilder) {
		return urlBuilder.path(objectType).toString();
	}
}
