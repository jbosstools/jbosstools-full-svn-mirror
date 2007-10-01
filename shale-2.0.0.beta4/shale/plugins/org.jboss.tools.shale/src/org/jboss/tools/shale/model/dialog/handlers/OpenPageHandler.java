/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.shale.model.dialog.handlers;

import org.jboss.tools.common.meta.action.impl.handlers.*;
import org.jboss.tools.common.model.XModelObject;

public class OpenPageHandler extends DefaultRedirectHandler {

	protected XModelObject getTrueSource(XModelObject source) {
		String attr = "view id";
		String path = source.getAttributeValue(attr);
		if(path == null || path.length() == 0 || path.indexOf('*') >= 0) return null;
		path = path.replace('\\', '/');
		return (path.startsWith("/")) ? source.getModel().getByPath(path) : null;
	}

}
