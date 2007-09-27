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
package org.jboss.tools.struts.model.helpers.open;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.impl.XModelImpl;
import org.jboss.tools.common.model.util.FindObjectHelper;
import org.jboss.tools.struts.messages.StrutsUIMessages;

public class OpenFileInWebRoot {

	public String run(XModel model, String path) {
		XModelObject f = XModelImpl.getByRelativePath(model, path);
		if(f == null) return StrutsUIMessages.CANNOT_FIND_RESOURCE + path;
		FindObjectHelper.findModelObject(f, FindObjectHelper.EVERY_WHERE);
		return null;
	}

}
