/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.core.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author Andre Dietisheim
 */
public class Key extends DeltaCloudObject {

	private static final long serialVersionUID = 1L;

	private URL url;
	private String id;
	private String pem;
	private String fingerPrint;
	private String state;
	private List<KeyAction> actions;

	public String getId() {
		return id;
	}

	protected void setUrl(String url) throws MalformedURLException {
		this.url = new URL(url);
	}

	protected void setUrl(URL url) {
		this.url = url;
	}

	protected void setId(String id) {
		this.id = id;
	}

	protected void setPem(String pem) {
		this.pem = pem;
	}

	protected void setFingerPrint(String fingerPrint) {
		this.fingerPrint = fingerPrint;
	}

	protected void setState(String state) {
		this.state = state;
	}

	protected void setActions(List<KeyAction> actions) {
		this.actions = actions;
	}

	public URL getUrl() {
		return url;
	}

	public String getFingerPrint() {
		return fingerPrint;
	}

	public String getPem() {
		return pem;
	}

	public String getState() {
		return state;
	}

	public List<KeyAction> getActions() {
		return actions;
	}

}
