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
package org.jboss.tools.internal.deltacloud.ui.wizards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class CloudType {

	private static final String HTTPHEADER_KEY_ACCEPT = "Accept"; //$NON-NLS-1$
	private static final String HTTPHEADER_VALUE_ACCEPTXML = "application/xml;q=1.0"; //$NON-NLS-1$
	private static final String DOCUMENT_ELEMENT_DRIVER = "driver"; //$NON-NLS-1$
	private static final String DOCUMENT_ELEMENT_API = "api"; //$NON-NLS-1$
	private static final String URLCONNECTION_ENCODING = "UTF-8"; //$NON-NLS-1$
	
	public static final String UNKNOWN_TYPE_LABEL = "UnknownType.label"; //$NON-NLS-1$
	private static final String INVALID_URL = "ErrorInvalidURL.text"; //$NON-NLS-1$
	private static final String NONCLOUD_URL = "ErrorNonCloudURL.text"; //$NON-NLS-1$

	private String label = WizardMessages.getString(UNKNOWN_TYPE_LABEL);
	private boolean isValid;
	private String url;

	public CloudType(String url) {
		init(url);
	}

	/**
	 * Initializes this instance, sets the cloud type label and the validity. It
	 * tries to connect to the cloud instance that's supposed at the given url,
	 * requests an API response and parses the xml that's returned.
	 * 
	 * @param url
	 *            the cloud instance url to connect to
	 */
	private void init(String url) {
		this.url = url;
		try {
			Object o = getURLContent(url + "/api?format=xml"); //$NON-NLS-1$ 
			if (o instanceof InputStream) {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document document = db.parse(
						new InputSource(new StringReader(getXML((InputStream) o))));

				NodeList elements = document.getElementsByTagName(DOCUMENT_ELEMENT_API); //$NON-NLS-1$
				if (elements.getLength() > 0) {
					Node n = elements.item(0);
					Node driver = n.getAttributes().getNamedItem(
							DOCUMENT_ELEMENT_DRIVER); //$NON-NLS-1$
					if (driver != null) {
						isValid = true;
						String driverValue = driver.getNodeValue();
						label = driverValue.toUpperCase();
					}
				}
			}
		} catch (MalformedURLException e) {
			label = WizardMessages.getString(INVALID_URL);
		} catch (IOException e) {
			label = WizardMessages.getString(NONCLOUD_URL);
		} catch (ParserConfigurationException e) {
			label = WizardMessages.getString(NONCLOUD_URL);
		} catch (SAXException e) {
			label = WizardMessages.getString(NONCLOUD_URL);
		} catch (Exception e) {
			label = WizardMessages.getString(INVALID_URL);
		}
	}

	private String getXML(InputStream is) throws UnsupportedEncodingException, IOException {
		try {
			if (is == null) {
				return "";
			}
			StringBuilder sb = new StringBuilder();
			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, URLCONNECTION_ENCODING));
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n"); //$NON-NLS-1$
			}
			return sb.toString();
		} finally {
			is.close();
		}
	}

	public boolean isValid() {
		return isValid;
	}

	public String getLabel() {
		return label;
	}

	public String getUrl() {
		return url;
	}
	
	private Object getURLContent(String url) throws IOException {
		URL u = new URL(url);
		URLConnection connection = u.openConnection();
		connection.setRequestProperty(HTTPHEADER_KEY_ACCEPT, HTTPHEADER_VALUE_ACCEPTXML); //$NON-NLS-1$ $NON-NLS-2$
		return connection.getContent();
	}
}
