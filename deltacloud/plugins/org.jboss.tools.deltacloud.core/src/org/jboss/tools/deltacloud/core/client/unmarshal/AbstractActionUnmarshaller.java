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

import org.jboss.tools.deltacloud.core.client.AbstractDeltaCloudResourceAction;
import org.w3c.dom.Element;

/**
 * @author André Dietisheim
 */
public class AbstractActionUnmarshaller <ACTION extends AbstractDeltaCloudResourceAction<OWNER>, OWNER> extends AbstractDOMUnmarshaller<ACTION> {

	public AbstractActionUnmarshaller(Class<ACTION> actionClass) {
		super("link", actionClass);
	}

	@Override
	protected ACTION doUnmarshall(Element element, ACTION action) throws Exception {
		if (element != null) {
			action.setMethod(getAttributeText("method", element));
			action.setName(getAttributeText("rel", element));
			action.setUrl(getAttributeText("href", element));
		}
		return action;
	}
}
