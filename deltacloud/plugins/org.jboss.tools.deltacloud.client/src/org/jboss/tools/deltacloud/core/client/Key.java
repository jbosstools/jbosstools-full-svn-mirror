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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.tools.deltacloud.core.client.unmarshal.KeyUnmarshaller;

/**
 * @author Andre Dietisheim
 */
public class Key extends StateAware<Key> {

	private static final long serialVersionUID = 1L;

	private URL url;
	private String pem;
	private String fingerprint;
	private String state;

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

	public URL getUrl() {
		return url;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public String getPem() {
		return pem;
	}

	@Override
	protected void doUpdate(InputStream in) throws DeltaCloudClientException {
		new KeyUnmarshaller().unmarshall(in, this);
	}

	@Override
	public String toString() {
		return "Key [url=" + url + ", pem=" + pem + ", fingerprint=" + fingerprint + ", state=" + state + ", actions="
				+ getActions() + ", toString()=" + super.toString() + "]";
	}
}
