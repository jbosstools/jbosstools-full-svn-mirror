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
package org.jboss.tools.shale.model.dialog.helpers;

import java.util.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.util.FindObjectHelper;
import org.jboss.tools.jsf.model.pv.*;
import org.jboss.tools.shale.model.dialog.ShaleDialogConstants;
import org.jboss.tools.jst.web.model.pv.WebProjectNode;
import org.jboss.tools.jst.web.project.list.IWebPromptingProvider;

public class OpenDialogHelper implements ShaleDialogConstants {

	public String run(XModel model, String dialogName) {
		if(model == null || dialogName == null) return null;
		if(dialogName.startsWith("dialog:")) dialogName = dialogName.substring("dialog:".length());
		if(dialogName.length() == 0) return "JSF ID is not specified.";
		XModelObject c = findDialog(model, dialogName);
		if(c == null) return "Cannot find dialog " + dialogName + ".";
		FindObjectHelper.findModelObject(c, FindObjectHelper.EVERY_WHERE);
		return null;
	}

	public XModelObject findDialog(XModel model, String dialogName) {
		JSFProjectsRoot root = JSFProjectsTree.getProjectsRoot(model);
		if(root == null) return null;
		WebProjectNode n = (WebProjectNode)root.getChildByPath("Configuration/Shale");
		if(n == null) return null;
		XModelObject[] os = n.getTreeChildren();
		for (int i = 0; i < os.length; i++) {
			if(!ENT_SHALE_DIALOG.equals(os[i].getModelEntity().getName())) continue;
			XModelObject r = os[i].getChildByPath(dialogName);
			if(r != null) return r;
		}
		return null;
	}

	public List<Object> getViewDialogs(XModel model, String viewPath) {
		if(viewPath == null) return IWebPromptingProvider.EMPTY_LIST;
		JSFProjectsRoot root = JSFProjectsTree.getProjectsRoot(model);
		if(root == null) return IWebPromptingProvider.EMPTY_LIST;
		WebProjectNode n = (WebProjectNode)root.getChildByPath("Configuration/Shale");
		if(n == null) return IWebPromptingProvider.EMPTY_LIST;
		XModelObject[] os = n.getTreeChildren();
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < os.length; i++) {
			if(!ENT_SHALE_DIALOG.equals(os[i].getModelEntity().getName())) continue;
			XModelObject[] rs = os[i].getChildren(DIALOG_ENTITY);
			for (int j = 0; j < rs.length; j++) {
				String q = "dialog:" + rs[j].getAttributeValue("name");
//				if(!OpenCaseHelper.isPatternMatches(rs[j].getAttributeValue(JSFConstants.ATT_FROM_VIEW_ID), viewPath)) continue;
				if(!list.contains(q)/* && q.startsWith(viewPath)*/) list.add(q); 
			}			
		}
		return list;
	}
}
