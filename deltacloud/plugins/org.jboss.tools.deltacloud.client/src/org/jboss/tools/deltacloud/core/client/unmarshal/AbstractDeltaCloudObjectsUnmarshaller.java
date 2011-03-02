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

import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author André Dietisheim
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractDeltaCloudObjectsUnmarshaller<CHILD> extends AbstractDOMUnmarshaller<List> {

	private String childTag;

	public AbstractDeltaCloudObjectsUnmarshaller(String parentTag, String childTag) {
		super(parentTag, List.class);
		this.childTag = childTag;
	}

	@SuppressWarnings("unchecked")
	protected List doUnmarshall(Element element, List children) throws Exception {
		if (element != null) {
			NodeList nodeList = element.getElementsByTagName(childTag);
			if (nodeList != null
					&& nodeList.getLength() > 0) {
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					if (node instanceof Element) {
						CHILD child = unmarshallChild(node);
						children.add(child);
					}
				}
			}
		}
		return children;
	}

	protected abstract CHILD unmarshallChild(Node node) throws DeltaCloudClientException;
}
