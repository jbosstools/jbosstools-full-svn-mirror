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
package org.jboss.tools.vpe.resref.core;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.common.resref.core.ResourceReferenceList;
import org.jboss.tools.common.resref.ui.AbstractResourceReferencesComposite;
import org.jboss.tools.common.resref.ui.ResourceReferencesTableProvider;

public abstract class VpeResourceReferencesComposite extends AbstractResourceReferencesComposite {
	
	public VpeResourceReferencesComposite() {
		super();
	}

	
	protected abstract ResourceReferencesTableProvider createTableProvider(List dataList);
	protected abstract ResourceReferenceList getReferenceList();
	
	/**
	 * Returned the label that will display in group.
	 *
	 * @return label displayed in group
	 * @see #createControl(Composite)
	 */
	protected abstract String createGroupLabel();
	   

	protected void add(int index) {
		ResourceReference css = getDefaultResourceReference();
		
		initFilterInFileChooser();
		boolean ok = VpeAddReferenceSupport.add(file, css, getReferenceArray(), getEntity());
		if(!ok) return;
		dataList.add(css);
		update();
		table.setSelection(dataList.size() - 1);
	}
	
	protected void edit(int index) {
		if(index < 0) return;
		ResourceReference css = getReferenceArray()[index];
		initFilterInFileChooser();
		boolean ok = VpeAddReferenceSupport.edit(file, css, getReferenceArray(), getEntity());
		if(!ok) return;
		update();
	}
	
}
