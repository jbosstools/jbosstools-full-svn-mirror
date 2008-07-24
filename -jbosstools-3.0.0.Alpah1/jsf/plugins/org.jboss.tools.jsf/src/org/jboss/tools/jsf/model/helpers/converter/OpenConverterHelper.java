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
package org.jboss.tools.jsf.model.helpers.converter;

import java.util.Properties;

import org.eclipse.osgi.util.NLS;

import org.jboss.tools.common.meta.action.XAction;
import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.util.FindObjectHelper;
import org.jboss.tools.jsf.messages.JSFUIMessages;
import org.jboss.tools.jsf.model.pv.*;
import org.jboss.tools.jst.web.model.pv.WebProjectNode;

public class OpenConverterHelper {
	
	public String run(XModel model, String converterId) {
		if(model == null || converterId == null) return null;
		if(converterId.length() == 0) return JSFUIMessages.CONVERTER_ID_IS_NOT_SPECIFIED;
		XModelObject c = findConverter(model, converterId);
		if(c == null) return NLS.bind(JSFUIMessages.CANNOT_FIND_CONVERTER, converterId);
		FindObjectHelper.findModelObject(c, FindObjectHelper.EVERY_WHERE);
		return null;
	}
	
	String openClass(XModelObject c, String converterId) {
		String className = c.getAttributeValue("converter-class");
		if(className == null || className.length() == 0) return NLS.bind(JSFUIMessages.ATTRIBUTE_CONVERTER_CLASS_FOR_CONVERTER_ISNOT_SPECIFIED, converterId);
		
		XAction xaction = XActionInvoker.getAction("OpenSource", c);
		if(xaction != null && xaction.isEnabled(c)) {
			Properties p = new Properties();
			p.setProperty("ignoreWarning", "true");
			XActionInvoker.invoke("OpenSource", c, p);
			return p.getProperty("error");
		}
		return null;
	}
	
	public XModelObject findConverter(XModel model, String converterId) {
		JSFProjectsRoot root = JSFProjectsTree.getProjectsRoot(model);
		if(root == null) return null;
		WebProjectNode n = (WebProjectNode)root.getChildByPath("Configuration");
		if(n == null) return null;
		XModelObject[] os = n.getTreeChildren();
		for (int i = 0; i < os.length; i++) {
			XModelObject r = os[i].getChildByPath("Converters/" + converterId);
			if(r != null) return r;
		}
		return null;
	}

}
