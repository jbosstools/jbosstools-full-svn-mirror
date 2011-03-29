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
 * A request for a hardware profile on a deltacloud server.
 * 
 * @author André Dietisheim
 */
public class ListHardwareProfileRequest extends AbstractDeltaCloudRequest {

	private String profileId;

	/**
	 * Instantiates a new type request.
	 *
	 * @param baseUrl the base url
	 * @param imageId 
	 */
	public ListHardwareProfileRequest(String baseUrl, String profileId) {
		super(baseUrl, HttpMethod.GET);
		this.profileId = profileId;
	}

	@Override
	protected String doCreateUrl(UrlBuilder urlBuilder) {
		return urlBuilder.path("hardware_profiles").path(profileId).toString();
	}
}
