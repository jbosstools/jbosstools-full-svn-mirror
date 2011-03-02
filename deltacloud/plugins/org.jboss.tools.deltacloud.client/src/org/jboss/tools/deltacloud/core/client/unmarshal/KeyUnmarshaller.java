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
package org.jboss.tools.deltacloud.core.client.unmarshal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.jboss.tools.deltacloud.core.client.Action;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.core.client.Key;
import org.w3c.dom.Element;

/**
 * @author André Dietisheim
 */
public class KeyUnmarshaller extends AbstractActionAwareUnmarshaller<Key> {

	public KeyUnmarshaller() {
		super("key", Key.class, "link");
	}

	protected Key doUnmarshall(Element element, Key key) throws Exception {
		if (element != null) {
			key.setId(getAttributeText("id", element));
			key.setUrl(getAttributeText("href", element));
			key.setState(getFirstElementText("state", element));
			key.setFingerprint(getFirstElementText("fingerprint", element));
			key.setPem(trimPem(getPem(element))); //$NON-NLS-1$
			key.setActions(getActions(element, key));
		}
		return key;
	}

	private String getPem(Element element) {
		Element pemElement = getFirstElement("pem", element);
		if (pemElement != null) {
			return getFirstElementText("pem", pemElement);
		}
		return null;
	}
	
	private String trimPem(String pem) throws IOException {
		if (pem == null
				|| pem.length() <= 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader reader = new BufferedReader(new StringReader(pem));
		while ((line = reader.readLine()) != null) {
			// We must trim off the white-space from the xml
			// Complete white-space lines are to be ignored.
			String trimmedLine = line.trim();
			if (trimmedLine.length() > 0) {
				sb.append(trimmedLine).append('\n');
			}
		}
		return sb.toString();
	}

	@Override
	protected Action<Key> unmarshallAction(Element element) throws DeltaCloudClientException {
		Action<Key> keyAction = new Action<Key>();
		new ActionUnmarshaller<Key>().unmarshall(element, keyAction);
		return keyAction;
	}
}
