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

import java.util.List;

import org.jboss.tools.deltacloud.core.client.Key;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author André Dietisheim
 */
@SuppressWarnings("rawtypes")
public class KeysUnmarshaller extends AbstractDOMUnmarshaller<List> {

	public KeysUnmarshaller() {
		super("keys", List.class);
	}

	@SuppressWarnings("unchecked")
	protected List doUnmarshall(Element element, List keys) throws Exception {
		if (element != null) {
			NodeList nodeList = element.getElementsByTagName("key");
			if (nodeList != null
					&& nodeList.getLength() > 0) {
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					if (node instanceof Element) {
						Key key = new KeyUnmarshaller().unmarshall((Element) node, new Key());
						keys.add(key);
					}
				}
			}
		}
		return keys;
	}
}
