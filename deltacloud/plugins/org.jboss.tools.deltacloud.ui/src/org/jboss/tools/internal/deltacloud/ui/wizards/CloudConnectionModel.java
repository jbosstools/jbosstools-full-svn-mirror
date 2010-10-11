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

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.jboss.tools.deltacloud.ui.common.databinding.ObservablePojo;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Andre Dietisheim
 */
public class CloudConnectionModel extends ObservablePojo {

	public static final String PROPERTY_URL = "url";
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_PASSWORD = "password";
	public static final String PROPERTY_USERNAME = "username";
	public static final String PROPERTY_TYPE = "type";

	public static final String UNKNOWN_TYPE_LABEL = "UnknownType.label"; //$NON-NLS-1$
	public static final String INVALID_URL = "ErrorInvalidURL.text"; //$NON-NLS-1$
	public static final String NONCLOUD_URL = "ErrorNonCloudURL.text"; //$NON-NLS-1$

	private String name;
	private String url;
	private String username;

	private String password;
	private String cloudType;

	public static class CloudTypeConverter implements IConverter {

		private static final String HTTPHEADER_KEY_ACCEPT = "Accept"; //$NON-NLS-1$
		private static final String HTTPHEADER_VALUE_ACCEPTXML = "application/xml;q=1.0"; //$NON-NLS-1$
		private static final String DOCUMENT_ELEMENT_DRIVER = "driver"; //$NON-NLS-1$
		private static final String DOCUMENT_ELEMENT_API = "api"; //$NON-NLS-1$
		private static final String URLCONNECTION_ENCODING = "UTF-8"; //$NON-NLS-1$

		@Override
		public Object getFromType() {
			return String.class;
		}

		@Override
		public Object getToType() {
			return String.class;
		}

		@Override
		public Object convert(final Object fromObject) {
			return getCloudType((String) fromObject);
		}

		private String getCloudType(String url) {
			String cloudType = UNKNOWN_TYPE_LABEL;
			try {
				Object o = getURLContent(url + "/api?format=xml"); //$NON-NLS-1$ 
				if (o instanceof InputStream) {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					Document document = db.parse(
							new InputSource(new StringReader(getXML((InputStream) o))));

					NodeList elements = document.getElementsByTagName(DOCUMENT_ELEMENT_API);
					if (elements.getLength() > 0) {
						Node n = elements.item(0);
						Node driver = n.getAttributes().getNamedItem(DOCUMENT_ELEMENT_DRIVER);
						if (driver != null) {
							String driverValue = driver.getNodeValue();
							cloudType = driverValue.toUpperCase();
						}
					}
				}
			} catch (MalformedURLException e) {
				cloudType = WizardMessages.getString(INVALID_URL);
			} catch (IOException e) {
				cloudType = WizardMessages.getString(NONCLOUD_URL);
			} catch (ParserConfigurationException e) {
				cloudType = WizardMessages.getString(NONCLOUD_URL);
			} catch (SAXException e) {
				cloudType = WizardMessages.getString(NONCLOUD_URL);
			} catch (Exception e) {
				cloudType = WizardMessages.getString(INVALID_URL);
			}
			return cloudType;
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

		private Object getURLContent(String url) throws IOException {
			URL u = new URL(url);
			URLConnection connection = u.openConnection();
			connection.setRequestProperty(HTTPHEADER_KEY_ACCEPT, HTTPHEADER_VALUE_ACCEPTXML); 
			return connection.getContent();
		}
	}

	public static class CloudTypeValidator implements IValidator {

		@Override
		public IStatus validate(Object value) {
			Assert.isTrue(value instanceof String);
			if (value != null
					&& !WizardMessages.getString(UNKNOWN_TYPE_LABEL).equals(value)
					&& !WizardMessages.getString(INVALID_URL).equals(value)
					&& !WizardMessages.getString(NONCLOUD_URL).equals(value)) {
				return ValidationStatus.ok();
			} else {
				return ValidationStatus.error((String) value);
			}
		}
	}

	
	public CloudConnectionModel(String name, String url, String username, String password) {
		this.name = name;
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_USERNAME, this.username, this.username = username);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_PASSWORD, this.password, this.password = password);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_NAME, this.name, this.name = name);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_URL, this.url, this.url = url);
	}

	public String getType() {
		return cloudType;
	}

	public void setType(String cloudType) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_TYPE, this.cloudType, this.cloudType = cloudType);
	}
}
