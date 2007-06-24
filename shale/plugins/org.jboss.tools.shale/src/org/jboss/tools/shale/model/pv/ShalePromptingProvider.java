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
package org.jboss.tools.shale.model.pv;

import java.util.*;

import org.jboss.tools.common.model.XModel;
import org.jboss.tools.shale.model.clay.helpers.OpenComponentHelper;
import org.jboss.tools.shale.model.dialog.helpers.OpenDialogHelper;
import org.jboss.tools.jst.web.project.list.IWebPromptingProvider;

public class ShalePromptingProvider implements IWebPromptingProvider {

	public boolean isSupporting(String id) {
		return id != null && id.startsWith("shale");
	}

	public List<Object> getList(XModel model, String id, String prefix, Properties properties) {
		try {
			return getListInternal(model, id, prefix, properties);
		} catch (Exception e) {
			if(properties != null) {
				String message = e.getMessage();
				if (message == null) {
					message = e.getClass().getName();
				}
				properties.setProperty(ERROR, message);
			}
			return EMPTY_LIST;
		}
	}
	
	private List<Object> getListInternal(XModel model, String id, String prefix, Properties properties) throws Exception {
		String error = null;
		if(SHALE_OPEN_COMPONENT.equals(id)) {
			String converterId = prefix;
			OpenComponentHelper h = new OpenComponentHelper();
			error = h.run(model, converterId);
		} else if(SHALE_OPEN_DIALOG.equals(id)) {
			String dialogName = prefix;
			OpenDialogHelper h = new OpenDialogHelper();
			error = h.run(model, dialogName);
		} else if(SHALE_VIEW_DIALOGS.equals(id)) {
			OpenDialogHelper h = new OpenDialogHelper();
			String viewPath = properties.getProperty(VIEW_PATH);
			return h.getViewDialogs(model, viewPath);
		} else if(SHALE_COMPONENTS.equals(id)) {
			String[] components = OpenComponentHelper.getComponentList(model.getRoot());
			List<Object> list = new ArrayList<Object>();
			for (int i = 0; i < components.length; i++) list.add(components[i]);
			return list;
		}
		if(error != null) throw new Exception(error);
		return EMPTY_LIST;
	}

}
