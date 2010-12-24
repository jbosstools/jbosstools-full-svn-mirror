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

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Andre Dietisheim
 */
public class Key extends AbstractDeltaCloudObject {

	private static final long serialVersionUID = 1L;

	private URL url;
	private String pem;
	private String fingerprint;
	private String state;
	private List<KeyAction> actions;

	public Key() {
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public void setUrl(String url) throws MalformedURLException {
		this.url = new URL(url);
	}

	public void setPem(String pem) {
		this.pem = pem;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setActions(List<KeyAction> actions) {
		this.actions = actions;
	}

	public URL getUrl() {
		return url;
	}

	public String getFingerprint() {
		return fingerprint;
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
