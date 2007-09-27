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
package org.jboss.tools.shale.model.handlers;

import java.util.*;
import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.meta.action.impl.handlers.DefaultEditHandler;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.impl.FileAnyImpl;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.jsf.web.ConfigFilesData;
import org.jboss.tools.jsf.web.JSFWebHelper;
import org.jboss.tools.jst.web.project.WebProject;

public abstract class AbstractRenameConfigFileHandler extends DefaultEditHandler {
	
	public void executeHandler(XModelObject object, Properties prop) throws Exception {
		//prompt file object to compute body.
		((FileAnyImpl)object).getAsText();
		String oldConfigName = FileAnyImpl.toFileName(object);
		super.executeHandler(object, prop);
		String newConfigName = FileAnyImpl.toFileName(object);
		XActionInvoker.invoke("SaveActions.Save", object, prop);
		String path = WebProject.getInstance(object.getModel()).getPathInWebRoot(object);
		if(path == null) path = "" + XModelObjectLoaderUtil.getResourcePath(object);
		JSFWebHelper.registerConfigFileRename(object.getModel(), oldConfigName, newConfigName, path, getConfigFilesData());
	}
	
	protected abstract ConfigFilesData getConfigFilesData();

}
