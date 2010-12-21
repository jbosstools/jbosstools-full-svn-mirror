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

import org.jboss.tools.deltacloud.core.client.KeyAction;
import org.w3c.dom.Element;

/**
 * @author André Dietisheim
 */
public class KeyActionUnmarshaller extends AbstractDOMUnmarshaller<KeyAction> {

	public KeyActionUnmarshaller() {
		super("link", KeyAction.class);
	}

	@Override
	protected KeyAction doUnmarshall(Element element, KeyAction keyAction) throws Exception {
		if (element != null) {
			keyAction.setMethod(getAttributeText("method", element));
			keyAction.setName(getAttributeText("rel", element));
			keyAction.setUrl(getAttributeText("href", element));
		}
		return keyAction;
	}
}
