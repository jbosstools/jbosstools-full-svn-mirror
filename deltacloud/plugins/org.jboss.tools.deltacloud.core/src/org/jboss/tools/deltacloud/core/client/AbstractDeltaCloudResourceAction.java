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


/**
 * An action that is executable on a deltacloud resource
 * 
 * @author André Dietisheim
 */
public abstract class AbstractDeltaCloudResourceAction<OWNER> implements IDeltaCloudResourceAction<OWNER> {

	private String name;
	private String url;
	private HttpMethod method;
	private OWNER owner;

	protected AbstractDeltaCloudResourceAction() {
	}

	@Override
	public HttpMethod getMethod() {
		return method;
	}

	public void setMethodString(String method) {
		this.method = HttpMethod.valueOf(method.toUpperCase());
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public void setMethod(String method) {
		this.method = HttpMethod.valueOf(method.toUpperCase());
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getUrl() {
		return url;
	}

	public void setOwner(OWNER owner) {
		this.owner = owner;
	}

	public OWNER getOwner() {
		return owner;
	}
}
