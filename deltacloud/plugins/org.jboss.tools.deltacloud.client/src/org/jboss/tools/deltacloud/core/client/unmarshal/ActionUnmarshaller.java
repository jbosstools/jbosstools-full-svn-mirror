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

import org.jboss.tools.deltacloud.core.client.Action;
import org.w3c.dom.Element;

/**
 * @author André Dietisheim
 */
public class ActionUnmarshaller<OWNER> extends AbstractDOMUnmarshaller<Action<OWNER>> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ActionUnmarshaller() {
		super("link", (Class) Action.class);
	}

	@Override
	protected Action<OWNER> doUnmarshall(Element element, Action<OWNER> action) throws Exception {
		if (element != null) {
			action.setMethod(getAttributeText("method", element));
			action.setName(getAttributeText("rel", element));
			action.setUrl(getAttributeText("href", element));
		}
		return action;
	}
}
