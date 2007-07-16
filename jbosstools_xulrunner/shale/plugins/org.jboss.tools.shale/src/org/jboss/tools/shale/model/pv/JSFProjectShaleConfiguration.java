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
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.event.XModelTreeEvent;
import org.jboss.tools.jsf.model.pv.JSFProjectFolder;
import org.jboss.tools.jsf.web.JSFWebHelper;
import org.jboss.tools.shale.model.clay.ShaleClayConstants;
import org.jboss.tools.shale.model.dialog.ShaleDialogConstants;
import org.jboss.tools.shale.model.spring.SpringBeansConstants;

public class JSFProjectShaleConfiguration extends JSFProjectFolder {
	private static final long serialVersionUID = 1L;

	public void invalidate() {
		if(!valid || isLoading) return;
		valid = false;
		fireStructureChanged(XModelTreeEvent.STRUCTURE_CHANGED, this);
	}

	public XModelObject[] getTreeChildren() {
		if(valid || isLoading) return treeChildren;
		isLoading = true;
		valid = true;
		try {
			List<XModelObject> list = getConfiguration(getModel());
			treeChildren = (XModelObject[])list.toArray(new XModelObject[0]);
		} finally {
			isLoading = false;
		}
		return treeChildren;
	}

	String ENTITIES = ".FileShaleDialog.FileSpringBeans.FileShaleClay.";

	public XModelObject getTreeParent(XModelObject object) {
		String entity = object.getModelEntity().getName();
		if(ENTITIES.indexOf("." + entity + ".") < 0) return null;
		XModelObject[] cs = getTreeChildren();
		for (int i = 0; i < cs.length; i++) {
			if(cs[i] == object) return this;
		}
		return null;
	}

	static List<XModelObject> getConfiguration(XModel model) {
		List<XModelObject> list = new ArrayList<XModelObject>();
		Set<XModelObject> set = new HashSet<XModelObject>();

		JSFWebHelper.getConfigFiles(list, set, model, ShaleDialogConstants.REGISTRATION_DATA);

		XModelObject o = model.getByPath("FileSystems/lib-shale-clay.jar/META-INF/clay-config.xml");
		if(o != null) list.add(o);
		JSFWebHelper.getConfigFiles(list, set, model, ShaleClayConstants.REGISTRATION_DATA);

		JSFWebHelper.getConfigFiles(list, set, model, SpringBeansConstants.REGISTRATION_DATA);

		return list;
	}
	
}
