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

import org.jboss.tools.deltacloud.core.client.API;
import org.w3c.dom.Element;

/**
 * @author André Dietisheim
 */
public class APIUnmarshaller extends AbstractDOMUnmarshaller<API> {

	public APIUnmarshaller() {
		super("api", API.class);
	}

	protected API doUnmarshall(Element element, API server) throws Exception {
		if (element != null) {
			server.setDriver(getAttributeText("driver", element));
		}
		return server;
	}
}
