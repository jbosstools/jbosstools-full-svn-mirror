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
package org.jboss.tools.common.model.ui.action.sample;

import java.util.Properties;

import org.jboss.tools.common.model.ui.dnd.DnDUtil;
import org.eclipse.jface.action.Action;

import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.model.XModelObject;

public class XActionWrapper extends Action {
	
	protected XModelObject xmo;
	protected String path;

	public XActionWrapper() {}
	public XActionWrapper(String label) {
		super(label);
	}
	
	public void setXModelObject(XModelObject xmo) {
		this.xmo = xmo;
		updateState();
	}
	
	public void setActionPath(String path) {
		this.path = path;
		updateState();
	}
	
	public void updateState() {
		if ((this.xmo==null)||(path==null)) {
			setEnabled(Boolean.FALSE.booleanValue());
		} else {
			setEnabled(DnDUtil.getEnabledAction(xmo, null, path) != null);
		}
	}
	
	public void run() {
		if (xmo == null || path == null) return;
		Properties p = new Properties();
		p.setProperty("actionSourceGUIComponentID", "editor"); //$NON-NLS-1$ //$NON-NLS-2$
		initRunningProperties(p);
		XActionInvoker.invoke(path, xmo, null, p);
	}
	
	protected void initRunningProperties(Properties p) {
	}

}
