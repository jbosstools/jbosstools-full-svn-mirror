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

import org.jboss.tools.common.model.impl.OrderedObjectImpl;

public class SpringBeansBeanObjectImpl extends OrderedObjectImpl {
	private static final long serialVersionUID = 1L;

	public SpringBeansBeanObjectImpl() {}
	
	public String getPresentationString() {
		String name = getAttributeValue("name");
		if(name != null && name.length() > 0) return name;
		name = getAttributeValue("class");
		if(name != null && name.length() > 0) return name;
		name = getAttributeValue("id");
		if(name == null) name = "bean"; else name = "bean " + name;
		return name;
	}
	
	public String getPathPart() {
		String name = getAttributeValue("name");
		String cn = getAttributeValue("class");
		String id = getAttributeValue("id");
		return "" + cn + ":" + name + ":" + id;		
	}

}
