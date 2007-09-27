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
package org.jboss.tools.shale.ui.attribute.adapter;

import org.jboss.tools.common.model.ui.attribute.IListContentProvider;
import org.jboss.tools.common.model.ui.attribute.adapter.*;

import org.jboss.tools.common.meta.XAttribute;
import org.jboss.tools.common.model.XModelObject;

public class ShaleStateListAdapter extends DefaultComboBoxValueAdapter {

	protected IListContentProvider createListContentProvider(XAttribute attribute) {
		ShaleStateListContentProvider p = new ShaleStateListContentProvider();
		p.setContext(modelObject);
		p.setAttribute(attribute);
		return p;	
	}

}

class ShaleStateListContentProvider extends DefaultXAttributeListContentProvider {
	private XModelObject context;
	
	public void setContext(XModelObject context) {
		this.context = context;
	}

	protected void loadTags() {
		if(context == null) return;
		XModelObject o = context;
		String entity = context.getModelEntity().getName();
		while(entity != null && !"ShaleDialog".equals(entity) && entity.startsWith("Shale")) {
			o = o.getParent();
			entity = (o == null) ? null : o.getModelEntity().getName();
		}
		if(entity == null || !"ShaleDialog".equals(entity)) return;
		XModelObject[] os = o.getChildren();
		tags = new String[os.length];
		for (int i = 0; i < tags.length; i++) tags[i] = os[i].getAttributeValue("name");
	}

}
