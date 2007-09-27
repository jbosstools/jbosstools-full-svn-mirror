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
package org.jboss.tools.shale.model.spring;

import org.jboss.tools.common.meta.XAttribute;
import org.jboss.tools.common.model.impl.CustomizedObjectImpl;

public class SpringBeansAnyObjectImpl extends CustomizedObjectImpl {
	private static final long serialVersionUID = 1L;
	String attribute2 = null;

	public String getPresentationString() {
		String i = getAttributeValue("index");
		String et = "<" + getModelEntity().getXMLSubPath() + ">";
		String pa = getAttribute2();
		String sa = (pa != null) ? getAttributeValue(pa) : null;
		StringBuffer sb = new StringBuffer();
		if(i != null && i.length() > 0) sb.append('[').append(i).append(']').append(' ');
		sb.append(et);
		if(sa != null && sa.length() > 0) sb.append(" ").append(sa);
		return sb.toString();
	}
	
	public String getPathPart() {
		return "" + getAttributeValue("index");
	}

	public String getAttribute2() {
		if(attribute2 != null) return attribute2;
		XAttribute[] as = getModelEntity().getAttributes();
		for (int i = 0; i < as.length; i++) {
			if("true".equals(as[i].getProperty("presentation"))) {
				attribute2 = as[i].getName();
				return attribute2;
			}
		}
		attribute2 = "";
		return attribute2;
	}
	
	public String getAttributeValue(String name) {
		if("presentation".equals(name)) {
			return getPresentationString();
		} else {
			return super.getAttributeValue(name);
		}
	}

}
