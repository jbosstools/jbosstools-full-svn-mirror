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

import java.util.Properties;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.filesystems.impl.*;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.jsf.web.*;

public abstract class AbstractDeleteConfigFileHandler extends DeleteFileHandler {

	public void executeHandler(XModelObject object, Properties p) throws Exception {
		String path = XModelObjectLoaderUtil.getResourcePath(object);
		if(object.getModel().getByPath("FileSystems/WEB-INF" + path) == object) {
			path = "/WEB-INF" + path;
		}
		boolean unregister = false;
		if(JSFWebHelper.isConfigFileRegistered(object.getModel(), path, getConfigFilesData())) {
			ServiceDialog d = object.getModel().getService();
			Properties pd = new Properties();
			String message = "Delete faces-config " + FileAnyImpl.toFileName(object);
			pd.setProperty(ServiceDialog.DIALOG_MESSAGE, message);
			pd.setProperty(ServiceDialog.CHECKBOX_MESSAGE, "Delete reference from web.xml");
			pd.put(ServiceDialog.CHECKED, new Boolean(true));
			if(!d.openConfirm(pd)) return;
			Boolean b = (Boolean)pd.get(ServiceDialog.CHECKED);
			unregister = b.booleanValue();
			super.executeHandler(object, p);
			if(object.isActive()) return;
			if(unregister) {
				JSFWebHelper.unregisterConfigFile(object.getModel(), path, getConfigFilesData());
			}
		}
	}		

	public boolean getSignificantFlag(XModelObject object) {
		String path = XModelObjectLoaderUtil.getResourcePath(object);
		return !JSFWebHelper.isConfigFileRegistered(object.getModel(), path, getConfigFilesData());
	}

	protected abstract ConfigFilesData getConfigFilesData();

}
