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
package org.jboss.tools.jst.web.tiles.model.handlers;

import java.util.Properties;
import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.meta.action.impl.handlers.DefaultEditHandler;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.impl.FileAnyImpl;
import org.jboss.tools.jst.web.tiles.model.helpers.TilesRegistrationHelper;

public class RenameTilesHandler extends DefaultEditHandler {

	public void executeHandler(XModelObject object, Properties prop) throws XModelException {
		// prompt file object to build body if it has not been done yet.
		((FileAnyImpl)object).getAsText();
    	String oldPath = ((FileAnyImpl)object).getAbsolutePath();
		super.executeHandler(object, prop);
		XActionInvoker.invoke("SaveActions.Save", object, prop); //$NON-NLS-1$
		TilesRegistrationHelper.update(object.getModel(), object, oldPath);
	}

}