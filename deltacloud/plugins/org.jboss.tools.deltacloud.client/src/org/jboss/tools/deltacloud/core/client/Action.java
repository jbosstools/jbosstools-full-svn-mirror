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
 * An action that may be performed on a resource
 * 
 * @author André Dietisheim
 */
public class Action<OWNER> {

	public static final String START_NAME = "start";
	public static final String STOP_NAME = "stop";
	public static final String REBOOT_NAME = "reboot";
	public static final String DESTROY_NAME = "destroy";

	private String name;
	private String url;
	private HttpMethod method;
	private OWNER owner;

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

	public String getName() {
		return name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setOwner(OWNER owner) {
		this.owner = owner;
	}

	public OWNER getOwner() {
		return owner;
	}

	public boolean isStart() {
		return START_NAME.equals(getName());
	}

	public boolean isStop() {
		return STOP_NAME.equals(getName());
	}

	public boolean isReboot() {
		return REBOOT_NAME.equals(getName());
	}

	public boolean isDestroy() {
		return DESTROY_NAME.equals(getName());
	}
}
