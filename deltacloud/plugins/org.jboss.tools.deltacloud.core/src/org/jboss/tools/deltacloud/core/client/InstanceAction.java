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
 * An action that is executable on an instance
 * 
 * @author André Dietisheim
 * @see Instance
 * @see DeltaCloudClient#performInstanceAction(String, String);
 *
 */
public class InstanceAction {

	public static final String START = "start";
	public static final String STOP = "stop";
	public static final String DESTROY = "destroy";
	public static final String REBOOT = "reboot";

	private String name;
	private String url;
	private HttpMethod method;

	public InstanceAction(String name, String url, String method) {
		this(name, url, HttpMethod.valueOf(method.toUpperCase()));
	}

	public InstanceAction(String name, String url, HttpMethod method) {
		this.name = name;
		this.url = url;
		this.method = method;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public HttpMethod getMethod() {
		return method;
	}

	@Override
	public String toString() {
		return "InstanceAction [name=" + name + ", url=" + url + ", method=" + method + "]";
	}
}
