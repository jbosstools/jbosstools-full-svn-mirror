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
package org.jboss.tools.jsf.model.handlers;

import java.util.*;
import org.jboss.tools.common.meta.XAdoptManager;
import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.meta.action.impl.handlers.DefaultCreateHandler;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.jsf.JSFPreference;
import org.jboss.tools.jsf.model.*;
import org.jboss.tools.jsf.model.helpers.JSFProcessHelper;

public class ProcessAdopt implements XAdoptManager, JSFConstants {

	public void adopt(XModelObject target, XModelObject object, java.util.Properties p) {
		if(isAdoptableJSP(target, object)) {
			adoptJSP(target, object, p);
		} else if(isAdoptableItem(target, object)) {
			adoptItem(target, object, p);
		}
	}

	public boolean isAdoptable(XModelObject target, XModelObject object) {
		if(isAdoptableJSP(target, object)) return true;
		if(isAdoptableItem(target, object)) return true;
		return false;
	}
	
	static String ADOPTABLE_JSP = "." + ENT_FILEJSP + "." 
	                                  + ENT_FILEHTML + "." 
	                                  + ENT_FILEXHTML + "."
	                                  + "FileXML" + ".";

	private boolean isAdoptableJSP(XModelObject target, XModelObject object) {
		String entity = object.getModelEntity().getName();
		if (ADOPTABLE_JSP.indexOf("." + entity + ".") >= 0) {
			String path = XModelObjectLoaderUtil.getResourcePath(object);
			if (target.getModelEntity().getName().startsWith(ENT_FACESCONFIG)) {
				target = target.getChildByPath(ELM_PROCESS);
			}
			return JSFProcessHelper.getHelper(target).getPage(path) == null;
		}
		return false;
	}

	private void adoptJSP(XModelObject target, XModelObject object, Properties p) {
		if (target.getModelEntity().getName().startsWith(ENT_FACESCONFIG)) {
			target = target.getChildByPath(ELM_PROCESS);
		}
		addRuleByPageAdopt(target, object, p);
/*		
		Properties runningProperties = new Properties();
		runningProperties.put("preselectedObject", object);
		if(p != null) runningProperties.putAll(p);
		XActionInvoker.invoke("CreateActions.CreatePage", target, runningProperties);
*/
	}

	private void addRuleByPageAdopt(XModelObject process, XModelObject page, Properties p) {
		String path = XModelObjectLoaderUtil.getResourcePath(page);
		XModelObject group = JSFProcessHelper.getHelper(process).getPage(path);
		if(group != null) return;
		boolean doNotCreateEmptyRule = "yes".equals(JSFPreference.DO_NOT_CREATE_EMPTY_RULE.getValue());
		group = JSFProcessHelper.getHelper(process).findOrCreateGroup(path, null);
		setShape(group, p);
		if(doNotCreateEmptyRule) {
			group.setAttributeValue("persistent", "true");
			group.setModified(true);
		} else {
			XModelObject rules = process.getParent().getChildByPath(FOLDER_NAVIGATION_RULES);
			XModelObject rule = rules.getModel().createModelObject(ENT_NAVIGATION_RULE, null);
			rule.setAttributeValue(ATT_FROM_VIEW_ID, path);
			DefaultCreateHandler.addCreatedObject(rules, rule, p);
		}
	}
	
	public static void setShape(XModelObject group, Properties p) {
		String x = (p == null) ? null : p.getProperty("process.mouse.x");
		String y = (p == null) ? null : p.getProperty("process.mouse.y");
		if(x != null && y != null) {
			group.setAttributeValue("shape", "" + x + "," + y + ",0,0");
		}
	}
	
	private boolean isAdoptableItem(XModelObject target, XModelObject object) {
		return ENT_PROCESS_GROUP.equals(object.getModelEntity().getName());
	}
	
	private void adoptItem(XModelObject target, XModelObject object, Properties p) {
//		String path = object.getAttributeValue(ATT_PATH);
		p.put("sample", object);
		XActionInvoker.invoke("CreateActions.AddRule", target, p);
	}

}
