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
package org.jboss.tools.shale.model.clay.handlers;

import java.util.*;
import org.jboss.tools.common.meta.action.impl.handlers.*;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.shale.model.clay.ShaleClayConstants;
import org.jboss.tools.shale.model.clay.helpers.OpenComponentHelper;

public class CreateAttributeSupport extends DefaultCreateSupport implements ShaleClayConstants {

	public void reset() {
		super.reset();
		initExtendedAttributes();
	}
	
	void initExtendedAttributes() {
		Set<String> set = new TreeSet<String>();
		collectExtendedAttributes(getTarget(), set);
		removeOverrided(getTarget(), set);
		String[] vs = set.toArray(new String[0]);
		setValueList(0, "name", vs);
		if(vs.length > 0) {
			setAttributeValue(0, "name", vs[0]);
		}
	}
	
	void collectExtendedAttributes(XModelObject o, Set<String> set) {
		Set<String> checked = new HashSet<String>();
		OpenComponentHelper h = new OpenComponentHelper();
		while(o != null) {
			String jsfid = getSuperId(o);
			if(jsfid == null || jsfid.length() == 0 || checked.contains(jsfid)) return;
			checked.add(jsfid);
			XModelObject c = h.findComponent(o.getModel(), jsfid);
			if(c == null) return;
			XModelObject[] cs = c.getChildByPath("Attributes").getChildren();
			for (int i = 0; i < cs.length; i++) {
				set.add(cs[i].getAttributeValue("name"));
			}
			o = c;
		}
	}
	
	String getSuperId(XModelObject o) {
		if(o == null) return null;
		String entity = o.getModelEntity().getName();
		if(ATTRIBUTES_ENTITY.equals(entity)) return getSuperId(o.getParent());
		if(COMPONENT_ENTITY.equals(entity)) return o.getAttributeValue("extends");
		if(ELEMENT_ENTITY.equals(entity)) return o.getAttributeValue("jsf id");
		return null;
	}
	
	void removeOverrided(XModelObject o, Set set) {
		String entity = o.getModelEntity().getName();
		XModelObject c = (ATTRIBUTES_ENTITY.equals(entity)) ? o
			: (COMPONENT_ENTITY.equals(entity)) ? o.getChildByPath("Attributes") 
			: (ELEMENT_ENTITY.equals(entity)) ? o.getChildByPath("Attributes")
			: null;
		if(c == null) return;
		XModelObject[] cs = c.getChildren();
		for (int i = 0; i < cs.length; i++) {
			set.remove(cs[i].getAttributeValue("name"));
		}
	}

}
