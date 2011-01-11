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

import org.jboss.tools.deltacloud.core.client.Realm;
import org.w3c.dom.Element;

/**
 * @author André Dietisheim
 */
public class RealmUnmarshaller extends AbstractDOMUnmarshaller<Realm> {

	public RealmUnmarshaller() {
		super("realm", Realm.class);
	}

	protected Realm doUnmarshall(Element element, Realm realm) throws Exception {
		realm.setId(getAttributeText("id", element));
		realm.setName(getFirstElementText("name", element));
		realm.setLimit(getFirstElementText("limit", element));
		realm.setState(getFirstElementText("state", element));
		return realm;
	}
}
