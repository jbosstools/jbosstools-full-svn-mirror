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
package org.jboss.tools.common.resref.core;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.resref.ui.BaseAddReferenceSupport;
import org.jboss.tools.jst.web.project.WebProject;
import org.jboss.tools.jst.web.tld.TaglibMapping;

public class VpeAddReferenceSupport extends BaseAddReferenceSupport {

	void setURIList() {
		if(file == null) return;
		if(getEntityData()[0].getModelEntity().getName().startsWith("VPETLD")) { //$NON-NLS-1$
			Set set = new TreeSet();
			IModelNature n = EclipseResourceUtil.getModelNature(file.getProject());
			if(n == null) return;
			XModel model = n.getModel();
			TaglibMapping taglibs = WebProject.getInstance(model).getTaglibMapping();
			Map map = taglibs.getTaglibObjects();
			Iterator it = map.keySet().iterator();
			while(it.hasNext()) {
				String s = it.next().toString();
				set.add(taglibs.resolveURI(s));
			}
			String[] uris = (String[])set.toArray(new String[0]);
 			setValueList(0, "location", uris); //$NON-NLS-1$
		}
	}

}
