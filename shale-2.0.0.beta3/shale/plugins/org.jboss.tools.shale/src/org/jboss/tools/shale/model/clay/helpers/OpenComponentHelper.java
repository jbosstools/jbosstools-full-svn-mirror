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
package org.jboss.tools.shale.model.clay.helpers;

import java.util.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.util.FindObjectHelper;
import org.jboss.tools.jsf.model.pv.*;
import org.jboss.tools.shale.model.clay.ShaleClayConstants;
import org.jboss.tools.jst.web.model.pv.WebProjectNode;

public class OpenComponentHelper {

	public String run(XModel model, String componentId) {
		if(model == null || componentId == null) return null;
		if(componentId.length() == 0) return "JSF ID is not specified.";
		XModelObject c = findComponent(model, componentId);
		if(c == null) return "Cannot find component " + componentId + ".";
		FindObjectHelper.findModelObject(c, FindObjectHelper.EVERY_WHERE);
		return null;
	}

	public XModelObject findComponent(XModel model, String componentId) {
		JSFProjectsRoot root = JSFProjectsTree.getProjectsRoot(model);
		if(root == null) return null;
		WebProjectNode n = (WebProjectNode)root.getChildByPath("Configuration/Shale");
		if(n == null) return null;
		XModelObject[] os = n.getTreeChildren();
		for (int i = 0; i < os.length; i++) {
			XModelObject r = os[i].getChildByPath(componentId);
			if(r != null) return r;
		}
		return null;
	}
	
	public static String[] getComponentList(XModelObject context) {
		if(context == null) return new String[0];
		XModelObject r = JSFProjectsTree.getProjectsRoot(context.getModel());
		if(r == null) return new String[0];
		XModelObject shale = r.getChildByPath("Configuration/Shale");
		if(!(shale instanceof WebProjectNode)) return new String[0];
		XModelObject[] os = ((WebProjectNode)shale).getTreeChildren();
		XModelObject f = context;
		while(f != null && f.getFileType() != XModelObject.FILE) f = f.getParent();
		boolean addThis = f != null;
		Set<String> set = new TreeSet<String>();
		for (int i = 0; i < os.length; i++) {
			if(!ShaleClayConstants.ENT_SHALE_CLAY.equals(os[i].getModelEntity().getName())) continue;
			appendFile(os[i], set);
			if(os[i] == f) addThis = false;
		}
		if(addThis) appendFile(f, set);
		return set.toArray(new String[0]);
	}
	
	private static void appendFile(XModelObject f, Set<String> set) {
		XModelObject[] cs = f.getChildren();
		for (int i = 0; i < cs.length; i++) {
			String name = cs[i].getAttributeValue("jsf id");
			if(name != null && !set.contains(name)) set.add(name);
		}
	}

}
