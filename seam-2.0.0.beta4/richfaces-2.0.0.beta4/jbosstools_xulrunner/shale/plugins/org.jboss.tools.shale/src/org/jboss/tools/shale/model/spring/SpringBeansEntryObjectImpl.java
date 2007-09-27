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

import org.jboss.tools.common.model.XModelObject;

public class SpringBeansEntryObjectImpl extends SpringBeansAnyObjectImpl {
	private static final long serialVersionUID = 1L;

	public String getPresentationString() {
		String s = getAttributeValue("key");
		if(s != null && s.length() > 0) return s;
		s = getAttributeValue("key-ref");
		if(s != null && s.length() > 0) return s;
		XModelObject o = getChildByPath("key");
		if(o != null) {
			o = o.getChildByPath("");
			if(o != null) return o.getPresentationString();
		}
		return "entry";
	}
	
	public String getAttributeValue(String name) {
		if("key presentation".equals(name)) {
			return getPresentationString();
		} else {
			return super.getAttributeValue(name);
		}
	}
	

}
