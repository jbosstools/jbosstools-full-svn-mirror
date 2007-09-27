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

import java.util.*;
import org.jboss.tools.common.model.XModelObject;

public class ListObjectImpl extends SpringBeansAnyObjectImpl {
	private static final long serialVersionUID = 1L;
	
	public ListObjectImpl() {}
	
	public boolean addChild_0(XModelObject o) {
		o.setAttributeValue("index", "" + getChildren().length);
		return super.addChild_0(o);
	}
	
	public void removeChild_0(XModelObject o) {
		super.removeChild_0(o);
		updateIndices();
	}
	
	public void updateIndices() {
		XModelObject[] os = getChildren();
		Map<String,String> temp = null;
		for (int i = 0; i < os.length; i++) {
			String ni = "" + i;
			String oi = os[i].getAttributeValue("index");
			if(oi == null || ni.equals(oi)) continue;
			if(getChildByPath(ni) != null) {
				if(temp == null) temp = new HashMap<String,String>();
				String random = "" + Math.random();
				os[i].setAttributeValue("index", random);
				temp.put(random, ni);
			} else {
				os[i].setAttributeValue("index", ni);
			}
		}
		if(temp != null) {
			Iterator it = temp.keySet().iterator();
			while(it.hasNext()) {
				String ci = it.next().toString();
				String ni = temp.get(ci).toString();
				XModelObject o = getChildByPath(ci);
				if(o != null && ni != null) o.setAttributeValue("index", ni);
			}
		}
	}

    public boolean move(int from, int to, boolean firechange) {
    	boolean b = super.move(from, to, firechange);
    	if(b) updateIndices();
    	return b;
    }
}
