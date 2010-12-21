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
import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.core.client.Key;
import org.jboss.tools.deltacloud.core.client.KeyAction;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author André Dietisheim
 */
public class KeyUnmarshaller extends AbstractDOMUnmarshaller<Key> {

	public KeyUnmarshaller() {
		super("key", Key.class);
	}

	protected Key doUnmarshall(Element element, Key key) throws Exception {
		if (element != null) {
			key.setId(getAttributeText("id", element));
			key.setUrl(getAttributeText("href", element));
			key.setState(getFirstElementText("state", element));
			key.setFingerprint(getFirstElementText("fingerprint", element));
			key.setPem(trimPem(getPem(element))); //$NON-NLS-1$
			key.setActions(getKeyActions(element));
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


	private List<KeyAction> getKeyActions(Element keyElement) throws DeltaCloudClientException {
		if (keyElement == null) {
			return null;
		}
		List<KeyAction> actions = new ArrayList<KeyAction>();
		NodeList nodeList = keyElement.getElementsByTagName("link");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node linkNode = nodeList.item(i);
			KeyAction keyAction = createKeyAction(linkNode);
			if (keyAction != null) {
				actions.add(keyAction);
			}
		}
		return actions;
	}

	private KeyAction createKeyAction(Node node) throws DeltaCloudClientException {
		if (!(node instanceof Element)) {
			return null;
		}
		KeyAction keyAction = new KeyAction();
		new KeyActionUnmarshaller().unmarshall((Element) node, keyAction);
		return keyAction;
	}

}
