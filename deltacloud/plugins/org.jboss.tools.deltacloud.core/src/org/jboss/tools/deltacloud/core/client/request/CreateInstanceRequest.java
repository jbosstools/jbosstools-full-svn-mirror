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
 * Creates a new instance
 * 
 * @author André Dietisheim
 */
public class CreateInstanceRequest extends AbstractDeltaCloudRequest {

	private String imageId;
	private String profileId;
	private String realmId ;
	private String name ;
	private String keyName;
	private String memory;
	private String storage;

	public CreateInstanceRequest(URL baseUrl, String imageId) {
		this(baseUrl, imageId, null, null, null, null, null, null);
	}
	
	public CreateInstanceRequest(URL baseUrl, String imageId, String profileId, String realmId, String name,
			String keyname, String memory, String storage) {
		super(baseUrl, HttpMethod.POST);
		this.imageId = imageId;
		this.profileId = profileId;
		this.realmId = realmId;
		this.name = name;
		this.keyName = keyname;
		this.memory = memory;
		this.storage = storage;
	}

	@Override
	protected String doCreateUrl(UrlBuilder urlBuilder) {
		urlBuilder.path("instances").parameter("image_id", imageId);
		appendParameter("hwp_id", profileId, urlBuilder);
		appendParameter("realm_id", realmId, urlBuilder);
		appendParameter("name", name, urlBuilder);
		appendParameter("hwp_memory", memory, urlBuilder);
		appendParameter("hwp_storage", storage, urlBuilder);
		appendParameter("keyname", keyName, urlBuilder);
		appendParameter("commit", "create", urlBuilder);
		return urlBuilder.toString();
	}

	private void appendParameter(String parameterName, String parameterValue, UrlBuilder urlBuilder) {
		if (parameterValue != null) {
			urlBuilder.parameter(parameterName, parameterValue);
		}
	}
}
