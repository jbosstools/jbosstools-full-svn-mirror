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
package org.jboss.tools.deltacloud.core.client;

import org.jboss.tools.deltacloud.core.client.request.DeltaCloudRequest.HttpMethod;

/**
 * An action that is executable on a deltacloud resource
 * 
 * @author André Dietisheim
 *
 */
public abstract class AbstractDeltaCloudResourceAction implements IDeltaCloudResourceAction {
	
	private String name;
	private String url;
	private HttpMethod method;

	protected AbstractDeltaCloudResourceAction() {
	}
	
	protected AbstractDeltaCloudResourceAction(String name, String url, String method) {
		this.url = url;
		this.method = HttpMethod.valueOf(method.toUpperCase());
		this.name = name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	protected void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getUrl() {
		return url;
	}

	protected void setMethod(HttpMethod method) {
		this.method = method;
	}

	protected void setMethod(String method) {
		this.method = HttpMethod.valueOf(method.toUpperCase());
	}
	
	
	@Override
	public HttpMethod getMethod() {
		return method;
	}
}
